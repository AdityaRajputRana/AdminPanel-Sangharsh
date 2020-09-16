package com.sangharsh.adminpanel_sangharsh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.sangharsh.adminpanel_sangharsh.Model.Category;
import com.sangharsh.adminpanel_sangharsh.Model.HomeCategory;
import com.sangharsh.adminpanel_sangharsh.Model.HomeDocument;
import com.sangharsh.adminpanel_sangharsh.Model.SubCategory;
import com.sangharsh.adminpanel_sangharsh.Utils.Tools;

import java.io.File;
import java.util.ArrayList;

public class VideoActivity extends AppCompatActivity {

    private final int PIC_VID_CODE = 1;

    EditText catEditText;
    EditText subCatEditText;
    TextView detailsTxt;
    Button addVidBtn;
    Button pickCat;
    Button pickSubCat;
    HomeDocument homeDocument;
    HomeCategory homeCategory;
    int index = 0;
    Category category;
    SubCategory subCategory;

    Button pickVidBtn;
    Uri vidUri;
    String vidName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        pickVidBtn = findViewById(R.id.pickVideo);
        picVidCode();

        catEditText = findViewById(R.id.categoryEt);
        subCatEditText = findViewById(R.id.subCatET);
        addVidBtn = findViewById(R.id.addBtn);
        addVidCose();
        detailsTxt = findViewById(R.id.detailsText);
        pickSubCat = findViewById(R.id.pickSubCatBtn);
        pickCat = findViewById(R.id.pickCatBtn);
        pickCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (homeDocument != null) {
                    showDialogList();
                } else {
                    detailsTxt.setText("PLEASE WAIT");
                    addVidBtn.setEnabled(false);
                    FirebaseFirestore.getInstance()
                            .collection("app")
                            .document("Home")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        homeDocument = task.getResult().toObject(HomeDocument.class);
                                        showDialogList();
                                    } else {
                                        detailsTxt.setText("SOME ERROR OCCURED: \n\n" + task
                                                .getException());
                                        addVidBtn.setEnabled(true);
                                    }
                                }
                            });
                }
            }
        });


        pickSubCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String categoryID = catEditText.getText().toString();
                if (!categoryID.isEmpty()){
                    if(category != null && categoryID.equals(category.getId())){
                        showSubCatPickList();
                    } else {
                        pickSubCat.setEnabled(false);
                        detailsTxt.setText("PLEASE WAIT");
                        FirebaseFirestore.getInstance()
                                .collection("Categories")
                                .document(categoryID)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        pickSubCat.setEnabled(true);
                                        if (task.isSuccessful()) {
                                            category = task.getResult().toObject(Category.class);
                                            showSubCatPickList();
                                        } else {
                                            detailsTxt.setText("SOME ERROR OCCURED: \n\n" + task
                                                    .getException());
                                            addVidBtn.setEnabled(true);
                                        }
                                    }
                                });
                    }
                } else {
                    detailsTxt.setText("Please Pick the Category");
                }
            }
        });

    }

    private void picVidCode() {
        pickVidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Video"),PIC_VID_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PIC_VID_CODE:
                if (resultCode == RESULT_OK && data != null){
                    vidUri = data.getData();
                    File file = new File(String.valueOf(data.getData()));
                    detailsTxt.setText("video Picked: " + file.getName()+ "\nFrom Location: " + data.getData().toString());
                    vidName= new Tools().getTimeStamp(file.getName());
                }
                break;
            default:
                break;
        }
    }

    private void showSubCatPickList() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(VideoActivity.this);
        builderSingle.setIcon(R.mipmap.ic_launcher);
        builderSingle.setTitle("Select Any One SubCategory:-");

        final ArrayList<SubCategory> categories = category.getSubcategories();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(VideoActivity.this, android.R.layout.select_dialog_singlechoice);

        for (SubCategory category: categories) {
            arrayAdapter.add(category.getName()+"\n(" + category.getId()+")");
        }

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                subCatEditText.setText(categories.get(which).getId());
                subCategory = categories.get(which);
                index = which;
                dialog.dismiss();
            }
        });
        builderSingle.show();
        addVidBtn.setEnabled(true);
        detailsTxt.setText("");
}

    private void showDialogList() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(VideoActivity.this);
        builderSingle.setIcon(R.mipmap.ic_launcher);
        builderSingle.setTitle("Select Any One Category:-");

        final ArrayList<HomeCategory> categories = homeDocument.getCourses();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(VideoActivity.this, android.R.layout.select_dialog_singlechoice);

        for (HomeCategory category: categories) {
            arrayAdapter.add(category.getName()+"\n(" + category.getId()+")");
        }

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                catEditText.setText(categories.get(which).getId());
                homeCategory = categories.get(which);
                index = which;
                pickSubCat.setEnabled(true);
                dialog.dismiss();
            }
        });
        builderSingle.show();
        addVidBtn.setEnabled(true);
        detailsTxt.setText("");
    }

    private void addVidCose() {
        addVidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!catEditText.getText().toString().isEmpty()
                        && !subCatEditText.getText().toString().isEmpty()) {
                    addVidBtn.setEnabled(false);
                    pickVidBtn.setEnabled(false);
                    pickSubCat.setEnabled(false);
                    pickCat.setEnabled(false);
                    FirebaseStorage.getInstance()
                            .getReference()
                            .child("content")
                            .child("categories")
                            .child(catEditText.getText().toString())
                            .child("subcat_" + subCatEditText.getText().toString())
                            .child(vidName + ".mp4")
                            .putFile(vidUri)
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                    detailsTxt.setText("Uploading Video: " +
                                            String.valueOf(snapshot.getBytesTransferred() * 100 / snapshot.getTotalByteCount())
                                            + "%");
                                }
                            })
                            .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        detailsTxt.setText("Video Uploaded Successfully! Now Updating details");
                                    } else {
                                        detailsTxt.setText("Upload Failed: "
                                                + task.getException());
                                        addVidBtn.setEnabled(true);
                                        pickVidBtn.setEnabled(true);
                                        pickSubCat.setEnabled(true);
                                        pickCat.setEnabled(true);
                                    }
                                }
                            });
                } else {
                    detailsTxt.setText("All Fields are Maindatory");
                }
            }
        });
    }

}