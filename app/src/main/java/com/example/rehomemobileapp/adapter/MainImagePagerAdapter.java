package com.example.rehomemobileapp.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MainImagePagerAdapter extends RecyclerView.Adapter<MainImagePagerAdapter.ImageViewHolder> {

    private List<String> imageUrls;

    public MainImagePagerAdapter(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new ImageViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Glide.with(holder.imageView.getContext())
                .load(imageUrls.get(position))
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull ImageView itemView) {
            super(itemView);
            this.imageView = itemView;
        }
    }
}
