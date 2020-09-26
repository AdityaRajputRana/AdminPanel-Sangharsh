package com.sangharsh.adminpanel_sangharsh.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sangharsh.adminpanel_sangharsh.Model.Chat;
import com.sangharsh.adminpanel_sangharsh.R;
import com.sangharsh.adminpanel_sangharsh.Utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.MyViewHolder> {

    private ArrayList<Chat> chats;
    private ChatsInteractionListener listener;
    private Context context;

    public interface ChatsInteractionListener {
        void startMessgaeActivity(String chatId, String name, String photoUrl);
    }

    public ChatsAdapter(ArrayList<Chat> chats, ChatsInteractionListener context, Context activity) {
        this.chats = chats;
        this.listener = context;
        this.context = activity;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolder viewHolder = new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_chat_item, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        if (chats.get(position).getChaterPic() == null || chats.get(position).getChaterPic().isEmpty()){
            holder.profileImage.setImageResource(R.mipmap.ic_launcher_round);
        }
        {
            Picasso.get()
                    .load(chats.get(position).getChaterPic())
                    .transform(new CircleTransform())
                    .into(holder.profileImage);
        }

        holder.nameTextView.setText(chats.get(position).getChaterName());
        if (chats.get(position).getStatus() == 1){
            holder.nameTextView.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            holder.lastMessage.setTextColor(context.getResources().getColor(R.color.colorBlack));
//            FirebaseDatabase.getInstance().getReference("status")
//                    .child(chats.get(position).getChaterUid())
//                    .child("isOnline")
//                    .addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            if (dataSnapshot.getValue() != null) {
//                                if (dataSnapshot.getValue(Boolean.class)) {
//                                    Drawable drawable = context.getResources().getDrawable(R.drawable.ic_online_dot);
//                                    holder.onlineDot.setImageDrawable(drawable);
//                                } else {
//                                    Drawable drawable = context.getResources().getDrawable(R.drawable.ic_offline_dot);
//                                    holder.onlineDot.setImageDrawable(drawable);
//                                }
//                            } else {
//                                Log.i("Value", "NUll");
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
        } else if (chats.get(position).getStatus() == 2){
            holder.nameTextView.setTextColor(context.getResources().getColor(R.color.grayDark));
            holder.lastMessage.setTextColor(context.getResources().getColor(R.color.grayDark));
//            FirebaseDatabase.getInstance().getReference("status")
//                    .child(chats.get(position).getChaterUid())
//                    .child("isOnline")
//                    .addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            if (dataSnapshot.getValue() != null) {
//                                if (dataSnapshot.getValue(Boolean.class)) {
//                                    Drawable drawable = context.getResources().getDrawable(R.drawable.ic_online_dot);
//                                    holder.onlineDot.setImageDrawable(drawable);
//                                } else {
//                                    Drawable drawable = context.getResources().getDrawable(R.drawable.ic_offline_dot);
//                                    holder.onlineDot.setImageDrawable(drawable);
//                                }
//                            } else {
//                                Log.i("Value", "NUll");
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
        } else {
            holder.nameTextView.setTextColor(context.getResources().getColor(R.color.grayDark));
            holder.lastMessage.setTextColor(context.getResources().getColor(R.color.grayDark));
        }



            holder.lastMessage.setText(chats.get(position).getLastMessage());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.startMessgaeActivity(chats.get(position).getChatId(),
                            chats.get(position).getChaterName(), chats.get(position).getChaterPic());
                }
            });
        }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nameTextView;
        TextView lastMessage;
        TextView time;
        ImageView profileImage;
//        ImageView onlineDot;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.nameText);
            lastMessage = (TextView) itemView.findViewById(R.id.message);
            time = (TextView) itemView.findViewById(R.id.timeTextView);
            profileImage = (ImageView) itemView.findViewById(R.id.imageView);
//            onlineDot = (ImageView) itemView.findViewById(R.id.dotView);
        }


    }
}