package com.sangharsh.adminpanel_sangharsh;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
                if (categoryName != null && !categoryName.isEmpty()){

                } else {
                    detailsTxt.setText("Please Enter the Name");
                }
            }
        });

    }
}