package com.example.rehomemobileapp.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rehomemobileapp.R;
import com.example.rehomemobileapp.model.Message;
import com.example.rehomemobileapp.model.Post;

public class PostMessageViewHolder extends RecyclerView.ViewHolder {
    ImageView postImage;
    TextView postName, postDescription;

    public PostMessageViewHolder(View itemView) {
        super(itemView);
        postImage = itemView.findViewById(R.id.imageViewPostImage);
        postName = itemView.findViewById(R.id.textViewPostName);
        postDescription = itemView.findViewById(R.id.textViewPostDescription);
    }

    public void bind(Message message) {
        Post post = message.getPost();
        if (post != null) {
            postName.setText(post.getName());
            postDescription.setText(post.getDescription());
            if (post.getImages() != null && !post.getImages().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(post.getImages().get(0))
                        .into(postImage);
            }
        }
    }
}

