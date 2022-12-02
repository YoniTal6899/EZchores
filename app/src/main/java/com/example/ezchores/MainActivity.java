package com.example.ezchores;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
  //Buttons
  private Button login, signup;

  // Firebase
  private FirebaseAuth mAuth;
  FirebaseDatabase database = FirebaseDatabase.getInstance();


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    //Buttons init
    login = (Button)findViewById(R.id.log_in_button);
    signup = (Button)findViewById(R.id.sign_up_button);
    mAuth = FirebaseAuth.getInstance();

    // login listener
    login.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        open_LogIn_Activity();
      }
    });


    // sign listener
    signup.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        open_SignUp_Activity();
      }
    });
  }

  public void open_LogIn_Activity() {
    Intent home_to_login = new Intent(this, LogIn_Activity.class);
    startActivity(home_to_login);
  }

  public void open_SignUp_Activity() {
    Intent home_to_signup = new Intent(this, SignUp_Activity.class);
    startActivity(home_to_signup);
  }






}
