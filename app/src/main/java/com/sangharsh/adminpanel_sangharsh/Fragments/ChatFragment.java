package com.sangharsh.adminpanel_sangharsh.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sangharsh.adminpanel_sangharsh.Adapter.ChatsAdapter;
import com.sangharsh.adminpanel_sangharsh.MessageActivity;
import com.sangharsh.adminpanel_sangharsh.Model.Chat;
import com.sangharsh.adminpanel_sangharsh.R;

import java.util.ArrayList;

public class ChatFragment extends Fragment implements ChatsAdapter.ChatsInteractionListener {


    private OnFragmentInteractionListener mListener;
    private Context context;

    public ChatFragment() {
        // Required empty public constructor
    }



    public static ChatFragment newInstance() {
        ChatFragment fragment = new ChatFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 1:
                if (resultCode == 2){
                    getActivity().finish();
                }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_chat, container, false);
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String myUid = user.getUid();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final ArrayList<Chat> chats = new ArrayList<Chat>();
        final ChatsAdapter chatsAdapter = new ChatsAdapter(chats, this, getActivity());
        recyclerView.setAdapter(chatsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        DatabaseReference ref = db.getReference();
        DatabaseReference reference = ref.child("admin")
                .child("chats");
        final LinearLayout noMessage = view.findViewById(R.id.noMessageLayout);
        final ProgressBar progressBar = view.findViewById(R.id.progressBar);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    noMessage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final TextView unreadChat = view.findViewById(R.id.chatCount);
        unreadChat.setText("0");

        reference.orderByChild("time/time")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if (progressBar.isShown()){
                            progressBar.setVisibility(View.GONE);
                        }
                        if (!recyclerView.isShown()){
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                        if (noMessage.isShown()){
                            noMessage.setVisibility(View.GONE);
                        }
                        Chat mChat = dataSnapshot.getValue(Chat.class);
                        if ((mChat.getStatus() == 1)){
                            int count = Integer.parseInt(unreadChat.getText().toString());
                            if (count < 1){
                                unreadChat.setVisibility(View.VISIBLE);
                            }
                            unreadChat.setText(Integer.toString(count+1));
                        }
                        chats.add(mChat);
                        chatsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Chat oldChat = null;
                        Chat newChat = dataSnapshot.getValue(Chat.class);
                        for (Chat chat:chats){
                            if (chat.getChatId().equals(newChat.getChatId())){
                                oldChat = chat;
                            }
                        }
                        if (oldChat.getStatus() == 2 && newChat.getStatus() == 1){
                            int count = Integer.parseInt(unreadChat.getText().toString());
                            if (count < 1){
                                unreadChat.setVisibility(View.VISIBLE);
                            }
                            unreadChat.setText(Integer.toString(count+1));
                        }
                        if (oldChat.getStatus() == 1 && newChat.getStatus() == 2){
                            int count = Integer.parseInt(unreadChat.getText().toString());
                            if (count == 1){
                                unreadChat.setVisibility(View.GONE);
                            }
                            unreadChat.setText(Integer.toString(count-1));
                        }
                        if (newChat.getLastMessage() == oldChat.getLastMessage()){
                            chats.set(chats.indexOf(oldChat), newChat);
                        } else {
                            chats.remove(oldChat);
                            chats.add(dataSnapshot.getValue(Chat.class));
                            chatsAdapter.notifyDataSetChanged();
                        }
                        chatsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Chat oldChat = null;
                        Chat newChat = dataSnapshot.getValue(Chat.class);
                        for (Chat chat:chats){
                            if (chat.getChatId().equals(newChat.getChatId())){
                                oldChat = chat;
                            }
                        }
                        if (oldChat.getStatus() == 2 && newChat.getStatus() == 1){
                            int count = Integer.parseInt(unreadChat.getText().toString());
                            if (count < 1){
                                unreadChat.setVisibility(View.VISIBLE);
                            }
                            unreadChat.setText(Integer.toString(count+1));
                        }

                        if (oldChat.getStatus() == 1 && newChat.getStatus() == 2){
                            int count = Integer.parseInt(unreadChat.getText().toString());
                            if (count == 1){
                                unreadChat.setVisibility(View.GONE);
                            }
                            unreadChat.setText(Integer.toString(count-1));
                        }

                        if (oldChat.getStatus() < 2 && newChat.getStatus() == 3){
                            int count = Integer.parseInt(unreadChat.getText().toString());
                            if (count == 1){
                                unreadChat.setVisibility(View.GONE);
                            }
                            unreadChat.setText(Integer.toString(count-1));
                        }
                        chats.remove(oldChat);
                        chats.add(dataSnapshot.getValue(Chat.class));
                        chatsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void startMessgaeActivity(String chatId, String name, String photoUrl) {
        Intent intent = new Intent(getContext(), MessageActivity.class);
        intent.putExtra("CHAT_ID", chatId);
        intent.putExtra("NAME", name);
        intent.putExtra("PHOTO", photoUrl);
        startActivity(intent);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}