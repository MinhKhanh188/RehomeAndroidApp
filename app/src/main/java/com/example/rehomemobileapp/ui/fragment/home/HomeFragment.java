package com.example.rehomemobileapp.ui.fragment.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rehomemobileapp.R;
import com.example.rehomemobileapp.adapter.PostAdapter;
import com.example.rehomemobileapp.model.Post;
import com.example.rehomemobileapp.network.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList = new ArrayList<>();
    private ProgressBar loadingIndicator;

    public HomeFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerRecentProducts);
        loadingIndicator = new ProgressBar(getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        postAdapter = new PostAdapter(postList, post -> {
            Bundle bundle = new Bundle();
            bundle.putString("postId", post.getId());
           // NavHostFragment.findNavController(this).navigate(R.id.productDetailsFragment, bundle);
        });
        recyclerView.setAdapter(postAdapter);

        fetchRecentPosts();

        return view;
    }

    private void fetchRecentPosts() {
        loadingIndicator.setVisibility(View.VISIBLE);

        ApiClient.getApiService()
                .getPostsByProvince("Hà Nội") // Replace later with SessionManager.getProvince(getContext())
                .enqueue(new Callback<List<Post>>() {
                    @Override
                    public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                        loadingIndicator.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            postList.clear();
                            postList.addAll(response.body());
                            postAdapter.notifyDataSetChanged();
                        } else {
                            Log.e("API_ERROR", "Response error: " + response.code());
                            Toast.makeText(getContext(), "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Post>> call, Throwable t) {
                        loadingIndicator.setVisibility(View.GONE);
                        Log.e("API_FAILURE", t.getMessage(), t);
                        Toast.makeText(getContext(), "Không kết nối được tới server", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
