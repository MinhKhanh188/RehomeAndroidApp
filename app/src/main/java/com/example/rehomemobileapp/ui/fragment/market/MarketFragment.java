package com.example.rehomemobileapp.ui.fragment.market;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
import com.example.rehomemobileapp.model.Category;
import com.example.rehomemobileapp.utils.FilterHelper;
import com.example.rehomemobileapp.utils.SortType;
import com.example.rehomemobileapp.data.SessionManager;
import java.util.List;
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

        // Lưu trữ danh sách gốc để filter
        final List<Post>[] originalPosts = new List[]{new ArrayList<>()};

        viewModel.getPosts().observe(getViewLifecycleOwner(), posts -> {
            postAdapter.setPosts(posts);
            originalPosts[0] = new ArrayList<>(posts);
        });

        viewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            loadingIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        // Fetch data
        viewModel.fetchPosts("Hà Nội"); // Later: SessionManager.getProvince(getContext())

        // Xử lý filter
        View fabFilter = view.findViewById(R.id.fabFilter);
        fabFilter.setOnClickListener(v -> showFilterDialog(originalPosts[0]));

        return view;
    }

    private void showFilterDialog(List<Post> originalPosts) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_filter, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        Spinner spinnerCategory = dialogView.findViewById(R.id.spinnerCategory);
        EditText etMinPrice = dialogView.findViewById(R.id.etMinPrice);
        EditText etMaxPrice = dialogView.findViewById(R.id.etMaxPrice);
        RadioGroup radioGroupSort = dialogView.findViewById(R.id.radioGroupSort);
        RadioButton rbPriceLowToHigh = dialogView.findViewById(R.id.rbPriceLowToHigh);
        RadioButton rbPriceHighToLow = dialogView.findViewById(R.id.rbPriceHighToLow);
        View btnApply = dialogView.findViewById(R.id.btnApply);
        View btnReset = dialogView.findViewById(R.id.btnReset);

        // Load categories
        List<Category> categories = SessionManager.getCategories(getContext());
        List<String> categoryNames = new ArrayList<>();
        categoryNames.add("Tất cả");
        for (Category c : categories) categoryNames.add(c.getName());
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categoryNames);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        btnApply.setOnClickListener(v -> {
            String selectedCategory = spinnerCategory.getSelectedItemPosition() == 0 ? null : categories.get(spinnerCategory.getSelectedItemPosition() - 1).getName();
            double minPrice = 0, maxPrice = 0;
            try { minPrice = Double.parseDouble(etMinPrice.getText().toString()); } catch (Exception ignored) {}
            try { maxPrice = Double.parseDouble(etMaxPrice.getText().toString()); } catch (Exception ignored) {}
            SortType sortType = SortType.PRICE_LOW_TO_HIGH;
            if (rbPriceLowToHigh.isChecked()) sortType = SortType.PRICE_LOW_TO_HIGH;
            else if (rbPriceHighToLow.isChecked()) sortType = SortType.PRICE_HIGH_TO_LOW;
            List<Post> filtered = FilterHelper.filterAndSortPosts(originalPosts, selectedCategory, sortType, minPrice, maxPrice);
            postAdapter.setPosts(filtered);
            dialog.dismiss();
        });
        btnReset.setOnClickListener(v -> {
            spinnerCategory.setSelection(0);
            etMinPrice.setText("");
            etMaxPrice.setText("");
            rbPriceLowToHigh.setChecked(true);
        });
        dialog.show();
    }
}
