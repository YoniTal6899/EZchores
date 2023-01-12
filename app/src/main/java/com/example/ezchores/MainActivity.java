package com.example.ezchores;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.auth.FirebaseAuth;




public class MainActivity extends AppCompatActivity implements View.OnClickListener{

  //Buttons
  private AppCompatButton login, signup;

  // Firebase
  private FirebaseAuth mAuth;

  // Yoni's Tests:
  PushNotificationService NotificationsService;
  String Token;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    NotificationsService= new PushNotificationService();

    mAuth = FirebaseAuth.getInstance();
    //Buttons init
    login = (AppCompatButton) findViewById(R.id.log_in_button);
    signup = (AppCompatButton) findViewById(R.id.sign_up_button);
    
    // Firbase init
    mAuth = FirebaseAuth.getInstance();

    // Listeners
    login.setOnClickListener(this);
    signup.setOnClickListener(this);

  }

  public void updateToken(){
    SharedPreferences sharedPreferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
    this.Token=  sharedPreferences.getString("token", "");
  }

  @Override
  public void onClick(View view) {
    this.updateToken();
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
    home_to_login.putExtra("Registration Token", Token);
    startActivity(home_to_login);
  }

  private void open_SignUp_Activity() {
    Intent home_to_signup = new Intent(this,SignUp_Activity.class);
    home_to_signup.putExtra("Registration Token", Token);
    startActivity(home_to_signup);
  }

}
