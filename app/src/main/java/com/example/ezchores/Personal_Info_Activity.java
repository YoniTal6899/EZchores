package com.example.ezchores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Personal_Info_Activity extends AppCompatActivity {
    Button log_out,apply;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        log_out=findViewById(R.id.log_out);
        apply = findViewById(R.id.apply_changes);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogOut();
            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Apply();
            }
        });
    }

    public void LogOut(){
        Intent LO = new Intent(this, MainActivity.class);
        startActivity(LO);
    }

    public void Apply(){
        Intent AP = new Intent(this, My_Groups_Activity.class);
        startActivity(AP);
    }


}