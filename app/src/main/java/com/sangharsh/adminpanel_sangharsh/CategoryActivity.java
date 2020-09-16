package com.sangharsh.adminpanel_sangharsh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sangharsh.adminpanel_sangharsh.Model.Category;
import com.sangharsh.adminpanel_sangharsh.Model.HomeCategory;
import com.sangharsh.adminpanel_sangharsh.Model.SubCategory;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {
    EditText editText;
    TextView detailsTxt;
    Button addCatBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        editText = findViewById(R.id.editText);
        addCatBtn = findViewById(R.id.addBtn);
        detailsTxt = findViewById(R.id.detailsText);

        addCatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String categoryName = editText.getText().toString();
                detailsTxt.setText("PLEASE WAIT");
                addCatBtn.setEnabled(false);
                if (categoryName != null && !categoryName.isEmpty()){
                    String id = FirebaseFirestore.getInstance()
                            .collection("Categories")
                            .document()
                            .getId();
                    final Category category = new Category(id, categoryName, 0, new ArrayList<SubCategory>());
                    FirebaseFirestore.getInstance()
                            .collection("Categories")
                            .document(id)
                            .set(category)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        addHomeCat(category);
                                    } else {
                                        detailsTxt.setText("SOME ERROR OCCURED: \n\n" + task
                                        .getException());
                                        addCatBtn.setEnabled(true);
                                    }
                                }
                            });
                } else {
                    detailsTxt.setText("Please Enter the Name");
                    addCatBtn.setEnabled(true);
                }
            }
        });

    }

    private void addHomeCat(final Category category) {
        final HomeCategory homeCategory = new HomeCategory(category.getId(),
                category.getName(),
                category.getSubcat());
        FirebaseFirestore.getInstance()
                .collection("app")
                .document("Home")
                .update("courses", FieldValue.arrayUnion(homeCategory))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            detailsTxt.setText("SUCCESSFULL \n\nID:  "
                                    + category.getId()
                            );
                            editText.setText("");
                            addCatBtn.setEnabled(true);
                        } else {
                            detailsTxt.setText("SOME ERROR OCCURRED: \n\n" + task.getException());
                            addCatBtn.setEnabled(true);
                        }
                    }
                });
    }
}