package com.example.rehomemobileapp.ui.fragment.mylistings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rehomemobileapp.R;
import com.example.rehomemobileapp.adapter.MyListingsAdapter;
import com.example.rehomemobileapp.data.SessionManager;
import com.example.rehomemobileapp.model.Post;
import com.example.rehomemobileapp.network.ApiClient;
import com.example.rehomemobileapp.network.request.StatusUpdateRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyListingsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private MyListingsAdapter adapter;

    public MyListingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_listings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recyclerViewMyListings);
        progressBar = view.findViewById(R.id.progressBarMyListings);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewMyListings);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));


        fetchListings();
    }

    private void fetchListings() {
        progressBar.setVisibility(View.VISIBLE);

        String token = SessionManager.getAuthToken(requireContext());
        if (token == null) {
            Toast.makeText(getContext(), "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }

        ApiClient.getApiService().getUserPosts("Bearer " + token)
                .enqueue(new Callback<List<Post>>() {
                    @Override
                    public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            adapter = new MyListingsAdapter(response.body(), new MyListingsAdapter.OnItemActionListener() {
                                @Override
                                public void onEdit(Post post) {
                                    // No used for it, for now
                                }

                                @Override
                                public void onDelete(Post post) {
                                    deletePost(post);
                                }
                                @Override
                                public void onChangeStatus(Post post) {
                                    showStatusDialog(post);
                                }
                            });
                            recyclerView.setAdapter(adapter);
                        } else {
                            Toast.makeText(getContext(), "Không thể tải danh sách", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Post>> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deletePost(Post post) {
        String token = SessionManager.getAuthToken(requireContext());
        if (token == null) return;

        ApiClient.getApiService().deletePost(post.getId(), "Bearer " + token)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            adapter.removePost(post);
                            Toast.makeText(getContext(), "Đã xóa bài đăng", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showStatusDialog(Post post) {
        String[] statuses = {"available", "sold", "hidden"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Chọn trạng thái mới");

        builder.setItems(statuses, (dialog, which) -> {
            String selectedStatus = statuses[which];
            updateStatus(post, selectedStatus);
        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    private void updateStatus(Post post, String newStatus) {
        String token = SessionManager.getAuthToken(requireContext());
        if (token == null) return;

        ApiClient.getApiService()
                .updatePostStatus(post.getId(), new StatusUpdateRequest(newStatus), "Bearer " + token)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Đã cập nhật trạng thái", Toast.LENGTH_SHORT).show();
                            // Optional: reload list or update adapter
                            fetchListings();
                        } else {
                            Toast.makeText(getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
