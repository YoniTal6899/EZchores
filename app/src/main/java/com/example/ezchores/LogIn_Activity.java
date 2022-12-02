package com.example.ezchores;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
//import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn_Activity extends AppCompatActivity {

    // Buttons
    Button back, commit_login;

    //fields
    private AppCompatEditText mail_field , password_field ;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // Buttons inits
        mail_field = findViewById(R.id.Email_field);
        password_field = findViewById(R.id.Password_field);
        back=findViewById(R.id.back_home);
        commit_login=findViewById(R.id.commit_login);

        // Firebase init
        mAuth = FirebaseAuth.getInstance();

        // back button listener
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back_home();
            }
        });

        //  commmit button listener
        commit_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }

    // login function
    private void loginUser() {

        //get the text from the fields
        String email = mail_field.getText().toString();
        String password = password_field.getText().toString();

        // checker object for validate infos
        Auth_utils checker = new Auth_utils();


        if (checker.password_validity(password) == false) {
            password_field.setError("The password need to contain at least 8 characters including  : one digit , one Uppercase , one low case , one special character ");
            password_field.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            password_field.setError("You need to enter a password between 8-32 character with at least : one digit , one uppercase , one lowercase , one special character ");
            password_field.requestFocus();
        } else if (TextUtils.isEmpty(email) || checker.email_validity(email) == false) {
            mail_field.setError("you need to enter a valid email");
            mail_field.requestFocus();
        } else {
            mAuth.signInWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(LogIn_Activity.this , "User logged in successfully" , Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LogIn_Activity.this , My_Groups_Activity.class));
                    }else {
                        Toast.makeText(LogIn_Activity.this ,
                                task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
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