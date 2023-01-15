package com.example.ezchores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;



public class LogIn_Activity extends AppCompatActivity implements View.OnClickListener {

    // Buttons
    AppCompatButton back, commit_login;
    //fields
    private AppCompatEditText mail_field, password_field;
    // Firebase
    private FirebaseAuth mAuth;
    String regTK;
    DatabaseReference database;
    String UserID;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        regTK= (String)getIntent().getSerializableExtra("Registration Token");
        // Buttons inits
        mail_field = findViewById(R.id.Email_field);
        password_field = findViewById(R.id.Password_field);
        back = findViewById(R.id.back_home);
        commit_login = findViewById(R.id.commit_login);
        // Firebase init
        mAuth = FirebaseAuth.getInstance();
        // Listeners
        back.setOnClickListener(this);
        commit_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_home:
                back_home();
                break;
            case R.id.commit_login:
                loginUser();
                break;
            default:
                break;
        }
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
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
//                        Toast.makeText(LogIn_Activity.this, "User logged in successfully", Toast.LENGTH_SHORT).show();
                        System.out.println("The user Logged-In successfully");
                        update_regTK();
                        Intent i = new Intent(LogIn_Activity.this,My_Groups_Activity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(LogIn_Activity.this,
                                task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void update_regTK() {
        UserID = mAuth.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance().getReference();
        database.child("Users").child(UserID).child("regTK").setValue(regTK);
    }

    public void back_home() {
        Intent backHome = new Intent(this, MainActivity.class);
        startActivity(backHome);
    }
}