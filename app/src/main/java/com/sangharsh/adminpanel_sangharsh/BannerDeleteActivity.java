package com.sangharsh.adminpanel_sangharsh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sangharsh.adminpanel_sangharsh.Adapter.BannerAdapter;
import com.sangharsh.adminpanel_sangharsh.Model.Banner;
import com.sangharsh.adminpanel_sangharsh.Model.HomeDocument;
import com.squareup.picasso.Picasso;

public class BannerDeleteActivity extends AppCompatActivity implements BannerAdapter.Listener {
    
    HomeDocument document;
    TextView textView;
    RecyclerView recyclerView;
    BannerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_delete);
        
        recyclerView = findViewById(R.id.recyclerView);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        textView = findViewById(R.id.textView);
        textView.setText("Please Wait");
        
        FirebaseFirestore.getInstance()
                .collection("app")
                .document("Home")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        textView.setText("Select Any One to Delete");
                        if (task.isSuccessful()){
                            document = task.getResult().toObject(HomeDocument.class);
                            adapter = new BannerAdapter(document.getBanners(), BannerDeleteActivity.this);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(manager);
                        } else {
                            textView.setText("Some error occured" + task.getException());
                        }
                    }
                });
    }


    @Override
    public void delete(final Banner banner) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Banner")
                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        permaDelete(banner);
                    }
                })
                .setMessage("This action cannot be UnDone")
                .setCancelable(true)
        .show();
    }

    private void permaDelete(final Banner banner) {
        textView.setText("Please Wait");
        FirebaseFirestore.getInstance()
                .collection("app")
                .document("Home")
                .update("banners", FieldValue.arrayRemove(banner))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            textView.setText("Successfull");
                            document.getBanners().remove(document.getBanners().indexOf(banner));
                            adapter.notifyDataSetChanged();
                        } else {
                            textView.setText("Failed" + task.getException());
                        }
                    }
                });
    }
}