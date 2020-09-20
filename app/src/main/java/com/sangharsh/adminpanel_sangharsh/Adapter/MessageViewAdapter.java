package com.sangharsh.adminpanel_sangharsh.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.sangharsh.adminpanel_sangharsh.Model.Message;
import com.sangharsh.adminpanel_sangharsh.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageViewAdapter extends RecyclerView.Adapter<MessageViewAdapter.MyViewHolder> {

    private ArrayList<Message> messages;
    private Listener listener;
    private Context context;

    public MessageViewAdapter(ArrayList<Message> messages, Listener listener, Context context) {
        this.messages = messages;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case 0:
                MyViewHolder holder = new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_left_item, parent, false));
                return holder;
            case 1:
                MyViewHolder holder1 = new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.message_right_item, parent, false));
                return holder1;
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.textView.setText(messages.get(position).getContent());
        if (messages.get(position).getAttachments() != null && !messages.get(position).getAttachments().isEmpty()) {
            Log.i("Image", String.valueOf(position));
//            Glide.with(context)
//                    .load(messages.get(position).getAttachments())
//                    .into(holder.imageView);
            Picasso.get()
                    .load(messages.get(position).getAttachments())
                    .into(holder.imageView);
            holder.imageView.setVisibility(View.VISIBLE);
            holder.textView.setVisibility(View.GONE);
            holder.mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.enlarge(messages.get(position).getAttachments());
                }
            });
        } else {
            holder.textView.setText(messages.get(position).getContent());
            holder.imageView.setVisibility(View.GONE);
            holder.textView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        ImageView imageView;
        View mItemView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);
            mItemView = itemView;
        }
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
        String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String senderUid = messages.get(position).getSender();
        if (senderUid == "admin" ||myUid == senderUid || myUid.equals(senderUid)){
            viewType = 1;
        }
        return viewType;
    }

    public interface Listener{
        void enlarge(String url);
    }
}