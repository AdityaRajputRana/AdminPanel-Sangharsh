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
import com.google.firebase.firestore.FirebaseFirestore;
import com.sangharsh.adminpanel_sangharsh.Model.Category;
import com.sangharsh.adminpanel_sangharsh.Model.HomeCategory;
import com.sangharsh.adminpanel_sangharsh.Model.HomeDocument;
import com.sangharsh.adminpanel_sangharsh.Model.SubCategory;
import com.sangharsh.adminpanel_sangharsh.Model.Topic;
import com.sangharsh.adminpanel_sangharsh.Model.Video;

import java.util.ArrayList;
import java.util.Random;

public class TopicActivity extends AppCompatActivity {

    EditText catEditText;
    EditText subCatEditText;
    EditText topicEt;
    TextView detailsTxt;
    Button addTopicBtn;
    HomeDocument homeDocument;
    HomeCategory homeCategory;

    Category category;
    SubCategory subCategory;

    int catIndex = 0;
    int subCatIndex = 0;

    Button pickSubCat;
    Button pickCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        topicEt = findViewById(R.id.topicET);
        catEditText = findViewById(R.id.categoryEt);
        subCatEditText = findViewById(R.id.subCatET);
        addTopicBtn = findViewById(R.id.addBtn);
        addTopicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTopicCode();
                detailsTxt.setText("PLEASE WAIT");
            }
        });


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
                    addTopicBtn.setEnabled(false);
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
                                        addTopicBtn.setEnabled(true);
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
                                            addTopicBtn.setEnabled(true);
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

    private void showSubCatPickList() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(TopicActivity.this);
        builderSingle.setIcon(R.mipmap.ic_launcher);
        builderSingle.setTitle("Select Any One SubCategory:-");

        final ArrayList<SubCategory> categories = category.getSubcategories();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(TopicActivity.this, android.R.layout.select_dialog_singlechoice);

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
        addTopicBtn.setEnabled(true);
        detailsTxt.setText("");
    }

    private void showDialogList() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(TopicActivity.this);
        builderSingle.setIcon(R.mipmap.ic_launcher);
        builderSingle.setTitle("Select Any One Category:-");

        final ArrayList<HomeCategory> categories = homeDocument.getCourses();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(TopicActivity.this, android.R.layout.select_dialog_singlechoice);

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
                catIndex = which;
                pickSubCat.setEnabled(true);
                dialog.dismiss();
            }
        });
        builderSingle.show();
        addTopicBtn.setEnabled(true);
        detailsTxt.setText("");
    }

    private void addTopicCode() {
        if (catEditText.getText() != null && !catEditText.getText().toString().isEmpty()
        && subCatEditText.getText() != null && !subCatEditText.getText().toString().isEmpty()
        && topicEt.getText() != null && !topicEt.getText().toString().isEmpty()){
            addTopic();
        } else {
            detailsTxt.setText("Please fill all details");
            detailsTxt.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    private void addTopic() {
        String id = "topic_" + String.valueOf(new Random().nextInt(999)  + "_&HU"  + topicEt.getText().toString()
                + "_&HU" + String.valueOf(new Random().nextInt(999)));
        Topic topic = new Topic(id, topicEt.getText().toString(), 0, new ArrayList<Video>(), category.getId(), subCategory.getId());

        ArrayList<Topic> topics = subCategory.getTopics();
        if (topic == null){
            topics = new ArrayList<Topic>();
        }
        topics.add(topic);
        subCategory.setTopics(topics);
        subCategory.setTop(subCategory.getTop() + 1);

        category.getSubcategories().get(subCatIndex).setTopics(topics);
        category.getSubcategories().get(subCatIndex).setTop(subCategory.getTop());

        changeEnableState(false);
        uploadData();
    }

    private void uploadData() {
        FirebaseFirestore.getInstance()
                .collection("Categories")
                .document(category.getId())
                .set(category)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        changeEnableState(true);
                        if (task.isSuccessful()){
                            topicEt.setText("");
                            detailsTxt.setText("UPLOAD SUCCESSFULL");
                        } else {
                            detailsTxt.setText("FAILED \n "  +  task.getException().getMessage());
                        }
                    }
                });
    }

    private void changeEnableState(boolean state) {
        pickCat.setEnabled(state);
        pickSubCat.setEnabled(state);
        topicEt.setEnabled(state);
        addTopicBtn.setEnabled(state);
    }

}