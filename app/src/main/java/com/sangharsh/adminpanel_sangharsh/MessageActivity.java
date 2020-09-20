package com.sangharsh.adminpanel_sangharsh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.sangharsh.adminpanel_sangharsh.Adapter.MessageViewAdapter;
import com.sangharsh.adminpanel_sangharsh.Model.Chat;
import com.sangharsh.adminpanel_sangharsh.Model.Message;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MessageActivity extends AppCompatActivity implements MessageViewAdapter.Listener {

    private EditText editText;
    private FirebaseUser user;
    private DatabaseReference reference;
    private ImageView profileImg;

    private ImageButton attachmentBtn;
    private ConstraintLayout constraintLayout;
    private ImageView imageView;
    private ProgressBar progressBar;

    private LinearLayout enlargeLayout;
    private ImageView enlargeImage;
    private int screenState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        TextView nameTextView = (TextView) findViewById(R.id.nameTextView);
        nameTextView.setText(getIntent().getStringExtra("NAME"));

        FirebaseDatabase.getInstance()
                .getReference()
                .child("admin")
                .child("chats")
                .child(getIntent().getStringExtra("CHAT_ID"))
                .child("status")
                .setValue(2);

        editText = findViewById(R.id.editText);
        user = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference("chats")
                .child(getIntent().getStringExtra("CHAT_ID"));

        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        final ArrayList<Message> messages = new ArrayList<Message>();

        final MessageViewAdapter adapter = new MessageViewAdapter(messages, this, this);
        recyclerView.setAdapter(adapter);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message message = dataSnapshot.getValue(Message.class);
                messages.add(message);
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messages.size() - 1);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        attachmentBtn = (ImageButton) findViewById(R.id.attBtn);
        constraintLayout = (ConstraintLayout) findViewById(R.id.attachmentLayout);
        imageView = (ImageView) findViewById(R.id.attachmentsView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        screenState = 0;
        enlargeLayout = (LinearLayout) findViewById(R.id.enlargeLayout);
        enlargeImage = (ImageView) findViewById(R.id.enlargeImage);

        attachmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setDarkBar();
        }

        profileImg = findViewById(R.id.profileImage);

        Picasso.get()
                .load(getIntent().getStringExtra("PHOTO"))
        .into(profileImg);

        nameTextView.setText(getIntent().getStringExtra("NAME"));
        Log.i("MessageAct", "SetUp");
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @TargetApi(21)
    private void setDarkBar() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    public void collapse(View view){
        screenState = 0;
        enlargeLayout.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        switch (screenState){
            case 1:
                collapse(editText);
                break;
            default:
                super.onBackPressed();
        }
    }

    private void sendAttachment(Uri uri) {
        constraintLayout.setVisibility(View.GONE);
        editText.setVisibility(View.VISIBLE);
        attachmentBtn.setVisibility(View.VISIBLE);
        Message message = new Message();
        message.setChatId(getIntent().getStringExtra("CHAT_ID"));
        message.setSender(user.getUid());
        message.setAttachments(uri.toString());
        message.setContent("");

        reference.push().setValue(message)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                addAdminMsg("Photo");
            }
        });
    }

    private void addAdminMsg(String lastMsg){
        Chat chat = new Chat();
        Map map = new HashMap<String, Object>();
        map.put("time", ServerValue.TIMESTAMP);
        chat.setTime(map);
        chat.setLastMessage(lastMsg);
        chat.setChatId(getIntent().getStringExtra("CHAT_ID"));

        chat.setChaterPic(user.getPhotoUrl() + "?height=256");
        chat.setChaterUid(user.getUid());
        chat.setChaterName(user.getDisplayName());
        chat.setStatus(2);

        FirebaseDatabase.getInstance()
                .getReference()
                .child("admin")
                .child("chats")
                .child(getIntent().getStringExtra("CHAT_ID")).setValue(chat);
    }

    private void changeProgress(Long trans, Long total) {
        progressBar.setProgress((int) (trans * 100 / total));
    }

    public void sendMessage(View view){
        if (!editText.getText().toString().isEmpty()){
            final Message message = new Message();
            message.setChatId(getIntent().getStringExtra("CHAT_ID"));
            message.setSender(user.getUid());
            message.setAttachments(null);
            final String mes = editText.getText().toString();
            message.setContent(mes);

            reference.push().setValue(message)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        addAdminMsg(message.getContent());
                    }
                }
            });
            editText.setText("");
        }
    }

    public void goBack(View view){
        finish();
    }

    @Override
    public void enlarge(final String url) {

        enlargeLayout.setVisibility(View.VISIBLE);


        ViewTreeObserver vto = enlargeImage.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                enlargeImage.getViewTreeObserver().removeOnPreDrawListener(this);
                Picasso.get()
                        .load(url)
                        .resize(enlargeImage.getMeasuredWidth(), enlargeImage.getMeasuredHeight())
                        .centerInside()
                        .into(enlargeImage);

                return true;
            }
        });
        screenState =1;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1: // Image Picker
                if (resultCode == RESULT_OK){
                    if (data != null) {
                        editText.setVisibility(View.GONE);
                        attachmentBtn.setVisibility(View.GONE);
                        constraintLayout.setVisibility(View.VISIBLE);
                        Picasso.get()
                                .load(data.getData())
                                .into(imageView);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
                        final String format = "image-" +  simpleDateFormat.format(new Date());

                        FirebaseStorage.getInstance().getReference()
                                .child(getIntent().getStringExtra("CHAT_ID"))
                                .child(format)
                                .putFile(data.getData())
                                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            Log.i("Info",  "task succesfull");
                                            FirebaseStorage.getInstance().getReference()
                                                    .child(getIntent().getStringExtra("CHAT_ID"))
                                                    .child(format)
                                                    .getDownloadUrl()
                                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            sendAttachment(uri);
                                                        }
                                                    });
                                        } else {
                                            Log.i("Error", "Uploading");
                                        }
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        Log.i("Info",  "task progress changed");
                                        changeProgress(taskSnapshot.getBytesTransferred(), taskSnapshot.getTotalByteCount());
                                    }
                                });
                    }
                }
                break;
            default:
                break;
        }
    }
}