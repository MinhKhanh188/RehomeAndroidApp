package com.example.rehomemobileapp.ui.fragment.market;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rehomemobileapp.R;
import com.example.rehomemobileapp.activities.PostDetailActivity;
import com.example.rehomemobileapp.adapter.PostAdapter;
import com.example.rehomemobileapp.model.Post;

import java.util.ArrayList;

public class MarketFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private ProgressBar loadingIndicator;
    private MarketViewModel viewModel;

    public MarketFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market, container, false);

        recyclerView = view.findViewById(R.id.recyclerMarket);
        loadingIndicator = view.findViewById(R.id.marketLoading);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));


        postAdapter = new PostAdapter(new ArrayList<>(), post -> {
            Intent intent = new Intent(getContext(), PostDetailActivity.class);
            intent.putExtra("POST_ID", post.getId());
            startActivity(intent);
        });

        recyclerView.setAdapter(postAdapter);

        viewModel = new ViewModelProvider(this).get(MarketViewModel.class);

        viewModel.getPosts().observe(getViewLifecycleOwner(), posts -> {
            postAdapter.setPosts(posts); // you may need to create setPosts() in your adapter
        });

        viewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            loadingIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        // Fetch data
        viewModel.fetchPosts("Hà Nội"); // Later: SessionManager.getProvince(getContext())

        return view;
    }
}
