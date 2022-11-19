package com.example.ezchores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button login,signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login=(Button) findViewById(R.id.log_in_button);
        signup=(Button) findViewById(R.id.sign_up_button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_LogIn_Activity();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_SignUp_Activity();
            }
        });
    }
    public void open_LogIn_Activity(){
        Intent home_to_login= new Intent(this,LogIn_Activity.class);
        startActivity(home_to_login);
    }
    public void open_SignUp_Activity(){
        Intent home_to_signup= new Intent(this,SignUp_Activity.class);
        startActivity(home_to_signup);
    }
}