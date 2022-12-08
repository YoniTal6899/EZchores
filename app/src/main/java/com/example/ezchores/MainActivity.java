package com.example.ezchores;

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
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

  //Buttons
  private Button login, signup;
  SignInButton signInButton;

  // Google sign in
  private GoogleSignInClient client;

  // Firebase
  private FirebaseAuth mAuth;
  private DatabaseReference database ;

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
    signInButton = findViewById(R.id.google_signin_button);
    
    // Firbase init


    mAuth = FirebaseAuth.getInstance();


    // Google sign in init
    GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build();
    client = GoogleSignIn.getClient(this,options);

    // Listeners
    login.setOnClickListener(this);
    signup.setOnClickListener(this);
    signInButton.setOnClickListener(this);
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
      case R.id.google_signin_button:
        loginGoogle();
        break;
      default:
        break;
    }
  }



  // Google sign in
  private void loginGoogle(){
    Intent i = client.getSignInIntent();
    startActivityForResult(i,SIGN_IN);
  }

  // Google sign in result handling
  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(requestCode == SIGN_IN){
      com.google.android.gms.tasks.Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
      try {
        GoogleSignInAccount account = task.getResult(ApiException.class);

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                      Toast.makeText(MainActivity.this , "User logged in successfully" , Toast.LENGTH_SHORT).show();
                      startActivity(new Intent(MainActivity.this , My_Groups_Activity.class));

                    }else{
                      Toast.makeText(MainActivity.this , "Error logging in" , Toast.LENGTH_SHORT).show();
                      startActivity(new Intent(MainActivity.this , MainActivity.class));
                    }
                  }
                });


      } catch (ApiException e) {
        e.printStackTrace();
      }
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
