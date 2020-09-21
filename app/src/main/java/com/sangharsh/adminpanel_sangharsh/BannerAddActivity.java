package com.sangharsh.adminpanel_sangharsh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.sangharsh.adminpanel_sangharsh.Model.Banner;
import com.sangharsh.adminpanel_sangharsh.Model.Category;
import com.sangharsh.adminpanel_sangharsh.Model.HomeCategory;
import com.sangharsh.adminpanel_sangharsh.Model.HomeDocument;
import com.sangharsh.adminpanel_sangharsh.Model.SubCategory;
import com.sangharsh.adminpanel_sangharsh.Utils.Tools;

import java.io.File;
import java.util.ArrayList;

public class BannerAddActivity extends AppCompatActivity {

    private static final int PIC_VID_CODE = 101;
    Button pickCat;
    Button pickSubCat;
    EditText catEditText;
    EditText urlEt;
    EditText subCatEditText;
    
    HomeDocument homeDocument;
    HomeCategory homeCategory;
    Category category;
    SubCategory subCategory;
    int index = 0;
    int subCatIndex = 0;


    TextView detailsTxt;
    Button addBtn;
    Button picImgBtn;

    Uri imgUri;
    String imgName;
    int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_add);

        detailsTxt = findViewById(R.id.detailsText);
        addBtn = findViewById(R.id.addBtn);
        addBannerCode();

        picImgBtn = findViewById(R.id.pickBannerBtn);
        imgCode();

        catEditText = findViewById(R.id.categoryEt);
        subCatEditText = findViewById(R.id.subCatET);

        pickSubCat = findViewById(R.id.pickSubCatBtn);
        pickCat = findViewById(R.id.pickCatBtn);

        urlEt = findViewById(R.id.urlET);

        urlEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (urlEt.getText()!=null && !urlEt.getText().toString().isEmpty()){
                    pickCat.setEnabled(false);
                } else {
                    pickCat.setEnabled(true);
                }
            }
        });

        pickCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlEt.setText("");
                urlEt.setEnabled(false);
                if (homeDocument != null) {
                    showDialogList();
                } else {
                    detailsTxt.setText("PLEASE WAIT");
                    addBtn.setEnabled(false);
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
                                        addBtn.setEnabled(true);
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
                                            addBtn.setEnabled(true);
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

    private void imgCode() {
        picImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
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
                    imgUri = data.getData();
                    File file = new File(String.valueOf(data.getData()));
                    detailsTxt.setText("Image Picked: " + file.getName()+ "\nFrom Location: " + data.getData().toString());
                    imgName= new Tools().getTimeStamp(file.getName());
                    addBtn.setEnabled(true);
                }
                break;
            default:
                break;
        }
    }

    private void showSubCatPickList() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(BannerAddActivity.this);
        builderSingle.setIcon(R.mipmap.ic_launcher);
        builderSingle.setTitle("Select Any One SubCategory:-");

        final ArrayList<SubCategory> categories = category.getSubcategories();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(BannerAddActivity.this, android.R.layout.select_dialog_singlechoice);

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
                subCatIndex = which;
                dialog.dismiss();
            }
        });
        builderSingle.show();
        addBtn.setEnabled(true);
        detailsTxt.setText("");
    }

    private void showDialogList() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(BannerAddActivity.this);
        builderSingle.setIcon(R.mipmap.ic_launcher);
        builderSingle.setTitle("Select Any One Category:-");

        final ArrayList<HomeCategory> categories = homeDocument.getCourses();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(BannerAddActivity.this, android.R.layout.select_dialog_singlechoice);

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
        addBtn.setEnabled(true);
        detailsTxt.setText("");
    }


    private void addBannerCode() {
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgUri!=null) {
                    addBtn.setEnabled(false);
                    detailsTxt.setText("PLEASE WAIT");
                    picImgBtn.setEnabled(false);
                    pickSubCat.setEnabled(false);
                    pickCat.setEnabled(false);
                    urlEt.setEnabled(false);
                    FirebaseStorage.getInstance()
                            .getReference()
                            .child("content")
                            .child("banners")
                            .child(imgName + ".png")
                            .putFile(imgUri)
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                    detailsTxt.setText("Uploading Image: " +
                                            String.valueOf(snapshot.getBytesTransferred() * 100 / snapshot.getTotalByteCount())
                                            + "%");
                                }
                            })
                            .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        detailsTxt.setText("Video Uploaded Successfully! Now Updating details");
                                        task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                uplaodBanner(uri.toString());
                                            }
                                        });
                                    } else {
                                        detailsTxt.setText("Upload Failed: "
                                                + task.getException());
                                        addBtn.setEnabled(true);
                                        picImgBtn.setEnabled(true);
                                        pickSubCat.setEnabled(true);
                                        urlEt.setEnabled(true);
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

    private void uplaodBanner(String url) {
        final Banner banner = new Banner();
        banner.setImageUrl(url);
        if (urlEt.getText()!=null && !urlEt.getText().toString().isEmpty()){
            banner.setRedirectUrl(urlEt.getText().toString());
        }

        if (catEditText.getText()!=null && !catEditText.getText().toString().isEmpty()){
            banner.setRedirectUrl(catEditText.getText().toString());
        }

        if (subCatEditText.getText()!=null && !subCatEditText.getText().toString().isEmpty()){
            banner.setRedirectUrl(subCatEditText.getText().toString());
        }

        banner.setId(imgName.substring(0, 3) + (int) Math.random()*1000);

        if (homeDocument != null){
            homeDocument.getBanners().add(banner);
        }

        FirebaseFirestore.getInstance()
                .collection("app")
                .document("Home")
                .update("banners", FieldValue.arrayUnion(banner))
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    detailsTxt.setText("Banner Add Success");
                } else {
                    detailsTxt.setText("Banner Add Failed" + task.getException());
                }
                picImgBtn.setEnabled(true);
                urlEt.setEnabled(true);
                pickCat.setEnabled(true);
                imgUri = null;
                urlEt.setText("");
                catEditText.setText("");
                subCatEditText.setText("");
            }
        });
    }
}