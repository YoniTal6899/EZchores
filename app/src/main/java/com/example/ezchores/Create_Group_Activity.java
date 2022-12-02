package com.example.ezchores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Create_Group_Activity extends AppCompatActivity {
    Button create_group;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        create_group=findViewById(R.id.create_group_btn);
        create_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateGroup();
            }
        });
    }
    public void CreateGroup(){
        Intent CG= new Intent(this,My_Groups_Activity.class);
        startActivity(CG);
    }
}