package com.example.rehomemobileapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rehomemobileapp.R;
import com.example.rehomemobileapp.model.Conversation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ConversationListAdapter extends RecyclerView.Adapter<ConversationListAdapter.ConversationViewHolder> {

    private List<Conversation> conversationList;
    private OnConversationClickListener listener;
    private SimpleDateFormat timeFormat;

    public interface OnConversationClickListener {
        void onConversationClick(Conversation conversation);
    }

    public ConversationListAdapter(List<Conversation> conversationList, OnConversationClickListener listener) {
        this.conversationList = conversationList;
        this.listener = listener;
        this.timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_list, parent, false);
        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        Conversation conversation = conversationList.get(position);

        holder.textViewAvatar.setText(conversation.getAvatar());
        holder.textViewUserName.setText(conversation.getUserName());
        holder.textViewLastMessage.setText(conversation.getLastMessage() != null ? conversation.getLastMessage().getText() : "");

        if (conversation.getLastMessageTime() > 0) {
            holder.textViewTime.setText(timeFormat.format(new Date(conversation.getLastMessageTime())));
            holder.textViewTime.setVisibility(View.VISIBLE);
        } else {
            holder.textViewTime.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onConversationClick(conversation);
            }
        });
    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }

    static class ConversationViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAvatar;
        TextView textViewUserName;
        TextView textViewLastMessage;
        TextView textViewTime;

        public ConversationViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAvatar = itemView.findViewById(R.id.textViewAvatar);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            textViewLastMessage = itemView.findViewById(R.id.textViewLastMessage);
            textViewTime = itemView.findViewById(R.id.textViewTime);
        }
    }
}



