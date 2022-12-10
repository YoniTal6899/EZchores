package com.example.ezchores;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;




//import com.google.android.material.textfield.TextInputEditText;


import com.google.firebase.auth.FirebaseUser;


public class SignUp_Activity extends AppCompatActivity implements View.OnClickListener {

    //Buttons & EditTexts
    private Button back, commit_login;
    private AppCompatEditText mail_field, password_field, full_name_field;

    // Firebase
    private FirebaseAuth mAuth;

    private String UserID;
    DatabaseReference database;


    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Firebase init
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_sign_up);

        // Buttons & EditTexts init
        back = findViewById(R.id.back_home1);
        commit_login = findViewById(R.id.commit_login);
        mail_field = findViewById(R.id.Email_field);
        password_field = findViewById(R.id.password);
        full_name_field = findViewById(R.id.full_name);

        //Listeners
        back.setOnClickListener(this);
        commit_login.setOnClickListener(this);
    }

    // Override the 'onClick' method, divided by button id
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_home1:
                finish(); // Finish current activity
                break;

            case R.id.commit_login:

                //LOGIN();
                createUser();

                break;

            default:
                break;
        }
    }

    private void LOGIN(){
        Intent i= new Intent(this,My_Groups_Activity.class);
        startActivity(i);
    }
    // create user function
    private void createUser() {

        // Get the input from the fields
        String full_name = full_name_field.getText().toString();
        String email = mail_field.getText().toString();
        String password = password_field.getText().toString();

        // Initialize validator
        Auth_utils checker = new Auth_utils();

        // Validate that the user args [full name, email address, password] are valid
        if (checker.password_validity(password) == false) {
            password_field.setError("The password need to contain at least 8 characters including  : one digit , one Uppercase , one low case , one special character ");
            password_field.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            password_field.setError("You need to enter a password between 8-32 character with at least : one digit , one uppercase , one lowercase , one special character ");
            password_field.requestFocus();
        } else if (TextUtils.isEmpty(full_name) || checker.full_name_validity(full_name) == false) {
            full_name_field.setError("You need to enter your surname and name ");
            full_name_field.requestFocus();
        } else if (TextUtils.isEmpty(email) || checker.email_validity(email) == false) {
            mail_field.setError("you need to enter a valid email");
            mail_field.requestFocus();
        } else { // Valid args

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUp_Activity.this, new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUp_Activity.this, "User registered successfully", Toast.LENGTH_SHORT).show();


                        UserID=mAuth.getCurrentUser().getUid();
                        User user=new User(full_name,email,password);


                        database= FirebaseDatabase.getInstance().getReference();
                        database.child("Users").child(UserID).setValue(user);


                        startActivity(new Intent(SignUp_Activity.this, LogIn_Activity.class));
                    } else {
                        Toast.makeText(SignUp_Activity.this, "The error: " +
                                        task.getException().getMessage() +
                                        "occur during the registration",
                                Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
}