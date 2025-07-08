package com.example.rehomemobileapp.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rehomemobileapp.R;
import com.example.rehomemobileapp.model.Post;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;

public class MyListingsAdapter extends RecyclerView.Adapter<MyListingsAdapter.PostViewHolder> {

    public interface OnItemActionListener {
        void onEdit(Post post);
        void onDelete(Post post);
        void onChangeStatus(Post post);
    }

    private List<Post> posts;
    private OnItemActionListener listener;

    public MyListingsAdapter(List<Post> posts, OnItemActionListener listener) {
        this.posts = posts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_listing, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);

        // Load image
        if (post.getImages() != null && !post.getImages().isEmpty()) {
            Picasso.get()
                    .load(post.getImages().get(0))
                    .placeholder(R.drawable.broken_image_1)
                    .into(holder.image);
        }

        holder.title.setText(post.getName());
        holder.price.setText(String.format("Price: %,.0f₫", post.getPrice()));

        String status = post.getStatus();
        holder.status.setText("Status: " + status);

        // Status color
        switch (status.toLowerCase()) {
            case "pending":
                holder.status.setTextColor(Color.parseColor("#FFC107")); break;
            case "approved":
                holder.status.setTextColor(Color.parseColor("#4CAF50")); break;
            case "rejected":
            case "banned":
                holder.status.setTextColor(Color.parseColor("#F44336")); break;
            default:
                holder.status.setTextColor(Color.parseColor("#757575")); break;
        }

        // Popup menu
        // Inside your onBindViewHolder method, within the setOnMenuItemClickListener:
        holder.moreOptions.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(v.getContext(), holder.moreOptions);
            popup.inflate(R.menu.my_listing_post_options_menu);
            popup.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId(); // Get the item ID once
                if (itemId == R.id.menu_edit) {
                    listener.onEdit(post);
                    return true;
                } else if (itemId == R.id.menu_delete) {
                    listener.onDelete(post);
                    return true;
                } else if (itemId == R.id.menu_change_status) {
                    listener.onChangeStatus(post);
                    return true;
                } else {
                    return false;
                }
            });
            popup.show();
        });

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void removePost(Post post) {
        int index = posts.indexOf(post);
        if (index != -1) {
            posts.remove(index);
            notifyItemRemoved(index);
        }
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView image, moreOptions;
        TextView title, price, status;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imagePostThumbnail);
            title = itemView.findViewById(R.id.textPostTitle);
            price = itemView.findViewById(R.id.textPostPrice);
            status = itemView.findViewById(R.id.textPostStatus);
            moreOptions = itemView.findViewById(R.id.imageMoreOptions); // ✅ Correct place
        }
    }
}
