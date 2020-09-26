package com.sangharsh.adminpanel_sangharsh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sangharsh.adminpanel_sangharsh.Model.Category;
import com.sangharsh.adminpanel_sangharsh.Model.HomeCategory;
import com.sangharsh.adminpanel_sangharsh.Model.HomeDocument;
import com.sangharsh.adminpanel_sangharsh.Model.SubCategory;
import com.sangharsh.adminpanel_sangharsh.Model.Topic;
import com.sangharsh.adminpanel_sangharsh.Model.Video;

import java.util.ArrayList;

public class SubCategoryActivity extends AppCompatActivity {

    EditText catEditText;
    EditText subCatEditText;
    TextView detailsTxt;
    Button addCatBtn;
    HomeDocument homeDocument;
    HomeCategory homeCategory;
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);

        catEditText = findViewById(R.id.categoryEt);
        subCatEditText = findViewById(R.id.subCatET);
        addCatBtn = findViewById(R.id.addBtn);
        detailsTxt = findViewById(R.id.detailsText);

        Button pickBtn = findViewById(R.id.pickCatBtn);
        pickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (homeDocument != null) {
                    showDialogList();
                } else {
                    detailsTxt.setText("PLEASE WAIT");
                    addCatBtn.setEnabled(false);
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
                                        addCatBtn.setEnabled(true);
                                    }
                                }
                            });
                }
            }
        });

        addCatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String categoryName = catEditText.getText().toString();
                String subCategoryName = subCatEditText.getText().toString();
                detailsTxt.setText("PLEASE WAIT");
                addCatBtn.setEnabled(false);
                if (categoryName != null && !categoryName.isEmpty()
                && subCategoryName != null && !subCategoryName.isEmpty()){
                    String id = FirebaseFirestore.getInstance()
                            .collection("app")
                            .document()
                            .getId();
                    id = id.substring(0, (id.length()-1-subCategoryName.length())) + subCategoryName;
                    final SubCategory subCategory = new SubCategory(id, subCategoryName, 0, new ArrayList<Video>(), categoryName, 0, new ArrayList<Topic>());
                    FirebaseFirestore.getInstance()
                            .collection("Categories")
                            .document(categoryName)
                            .update("subcategories", FieldValue.arrayUnion(subCategory), "subcat", FieldValue.increment(1))
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        incrementSubCatNumbers(categoryName);
                                    } else {
                                        detailsTxt.setText("SOME ERROR OCCURED: \n\n" + task
                                                .getException());
                                        addCatBtn.setEnabled(true);
                                    }
                                }
                            });
                } else {
                    detailsTxt.setText("Please Enter the Names");
                    addCatBtn.setEnabled(true);
                }
            }
        });
    }

    private void incrementSubCatNumbers(String categoryName) {
        if (homeDocument != null){
            homeCategory.setSubcat(homeCategory.getSubcat()+1);
            homeDocument.getCourses().remove(index);
            homeDocument.getCourses().add(index, homeCategory);
            FirebaseFirestore.getInstance().collection("app")
                    .document("Home")
                    .set(homeDocument)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        detailsTxt.setText("SUCCESSFULLY ADDED SUBCATEGORY\n\n");
                        subCatEditText.setText("");
                        addCatBtn.setEnabled(true);
                    } else {
                        detailsTxt.setText("SOME ERROR OCCURED: \n\n" + task
                                .getException());
                        addCatBtn.setEnabled(true);
                    }
                }
            });
        }

    }

    private void showDialogList() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(SubCategoryActivity.this);
        builderSingle.setIcon(R.mipmap.ic_launcher);
        builderSingle.setTitle("Select Any One Category:-");

        final ArrayList<HomeCategory> categories = homeDocument.getCourses();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SubCategoryActivity.this, android.R.layout.select_dialog_singlechoice);

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
                dialog.dismiss();
            }
        });
        builderSingle.show();
        addCatBtn.setEnabled(true);
        detailsTxt.setText("");
    }
}