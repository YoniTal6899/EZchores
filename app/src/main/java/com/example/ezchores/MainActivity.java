package com.example.ezchores;


import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class MainActivity extends AppCompatActivity implements View.OnClickListener{

  //Buttons
  private Button login, signup;
  SignInButton signInButton;

  // Firebase
  private FirebaseAuth mAuth;


  //finals
  final private int SIGN_IN = 5555;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mAuth = FirebaseAuth.getInstance();
    //Buttons init
    login = (Button)findViewById(R.id.log_in_button);
    signup = (Button)findViewById(R.id.sign_up_button);
    
    // Firbase init
    mAuth = FirebaseAuth.getInstance();

    // Listeners
    login.setOnClickListener(this);
    signup.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.log_in_button:
        open_LogIn_Activity();
        break;
      case R.id.sign_up_button:
        open_SignUp_Activity();
        break;
      default:
        break;
    }
  }

  public void open_LogIn_Activity() {
    Intent home_to_login = new Intent(this, LogIn_Activity.class);
    startActivity(home_to_login);
  }

  private void open_SignUp_Activity() {
    Intent home_to_signup = new Intent(this, SignUp_Activity.class);
    startActivity(home_to_signup);
  }

}
