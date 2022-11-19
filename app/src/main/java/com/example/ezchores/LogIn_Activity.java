package com.example.ezchores;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class LogIn_Activity extends AppCompatActivity {
    Button back, commit_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        back=findViewById(R.id.back_home);
        commit_login=findViewById(R.id.commit_login);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back_home();
            }
        });
        commit_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }
    public void back_home(){
        Intent backHome= new Intent(this,MainActivity.class);
        startActivity(backHome);
    }
    public void login(){
        Intent Login= new Intent(this,My_Groups_Activity.class);
        startActivity(Login);
    }



}