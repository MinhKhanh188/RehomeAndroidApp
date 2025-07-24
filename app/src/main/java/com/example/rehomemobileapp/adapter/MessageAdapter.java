package com.example.rehomemobileapp.adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rehomemobileapp.R;
import com.example.rehomemobileapp.model.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_MESSAGE_SENT = 1;
    private static final int TYPE_MESSAGE_RECEIVED = 2;
    private static final int TYPE_TEXT_SENT = 0;
    private static final int TYPE_TEXT_RECEIVED = 1;
    private static final int TYPE_POST_SENT = 2;
    private static final int TYPE_POST_RECEIVED = 3;


    private List<Message> messageList;
    private SimpleDateFormat timeFormat;

    public MessageAdapter(List<Message> messageList) {
        this.messageList = messageList;
        this.timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    }

    static class PostMessageViewHolder extends RecyclerView.ViewHolder {
        TextView textViewPostName;
        TextView textViewPostDescription;
        ImageView imageViewPostImage;

        public PostMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPostName = itemView.findViewById(R.id.textViewPostName);
            textViewPostDescription = itemView.findViewById(R.id.textViewPostDescription);
            imageViewPostImage = itemView.findViewById(R.id.imageViewPostImage);
        }

        public void bind(Message message) {
            if (message.getPost() != null) {
                textViewPostName.setText(message.getPost().getName());
                textViewPostDescription.setText(message.getPost().getDescription());
                if (message.getPost().getImages() != null && !message.getPost().getImages().isEmpty()) {
                    Glide.with(itemView.getContext())
                            .load(message.getPost().getImages().get(0))
                            .into(imageViewPostImage);
                    Log.d("PostMessageViewHolder", "Binding post: " + message.getPost().getName());

                }
            }
        }
    }




    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        boolean isSent = message.isSent();

        if (message.getPost() != null) {
            Log.d("MessageAdapter", "Post message detected: " + message.getPost().getName());
            return isSent ? TYPE_POST_SENT : TYPE_POST_RECEIVED;
        } else {
            Log.d("MessageAdapter", "This is a text message. Post is null.");
            return isSent ? TYPE_TEXT_SENT : TYPE_TEXT_RECEIVED;
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case TYPE_TEXT_SENT:
                return new SentMessageViewHolder(inflater.inflate(R.layout.item_message_sent, parent, false));
            case TYPE_TEXT_RECEIVED:
                return new ReceivedMessageViewHolder(inflater.inflate(R.layout.item_message_received, parent, false));
            case TYPE_POST_SENT:
                return new PostMessageViewHolder(inflater.inflate(R.layout.item_message_post, parent, false));
            case TYPE_POST_RECEIVED:
                return new PostMessageViewHolder(inflater.inflate(R.layout.item_message_post_receive, parent, false));
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);

        if (holder instanceof SentMessageViewHolder) {
            SentMessageViewHolder sentHolder = (SentMessageViewHolder) holder;
            sentHolder.textViewMessage.setText(message.getContent());
            sentHolder.textViewTime.setText(timeFormat.format(new Date(message.getTimestamp())));
        } else if (holder instanceof ReceivedMessageViewHolder) {
            ReceivedMessageViewHolder receivedHolder = (ReceivedMessageViewHolder) holder;
            receivedHolder.textViewMessage.setText(message.getContent());
            receivedHolder.textViewTime.setText(timeFormat.format(new Date(message.getTimestamp())));
            receivedHolder.textViewAvatar.setText("PT");
        } else if (holder instanceof PostMessageViewHolder) {
            ((PostMessageViewHolder) holder).bind(message);
        }
    }



    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMessage;
        TextView textViewTime;

        public SentMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
            textViewTime = itemView.findViewById(R.id.textViewTime);
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAvatar;
        TextView textViewMessage;
        TextView textViewTime;

        public ReceivedMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAvatar = itemView.findViewById(R.id.textViewAvatar);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
            textViewTime = itemView.findViewById(R.id.textViewTime);
        }
    }
}
