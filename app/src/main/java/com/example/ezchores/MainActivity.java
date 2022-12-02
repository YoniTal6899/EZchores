package com.example.ezchores;

import android.app.Activity;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  //Buttons
  private Button login, signup;

  // Firebase
  private FirebaseAuth mAuth;
  FirebaseDatabase database = FirebaseDatabase.getInstance();


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mAuth = FirebaseAuth.getInstance();
    //Buttons init
    login = (Button)findViewById(R.id.log_in_button);
    signup = (Button)findViewById(R.id.sign_up_button);

    //Listeners
    login.setOnClickListener(this);
    signup.setOnClickListener(this);

  }

  // Override the 'onClick' method, divided by button id
  @Override
  public void onClick(View v)
  {
    switch (v.getId())
    {
      case R.id.log_in_button:
        Intent i = new Intent(this,LogIn_Activity.class);
        startActivity(i);
        break;

      case R.id.sign_up_button:
        Intent j = new Intent(this,SignUp_Activity.class);
        startActivity(j);
        break;

      default:
        break;
    }
  }







}
