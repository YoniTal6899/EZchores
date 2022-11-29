package com.example.ezchores;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LogIn_Activity extends AppCompatActivity {
    Button back, commit_login;
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        back = findViewById(R.id.back_home);
        commit_login = findViewById(R.id.commit_login);
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
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        final GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton signInBtn = findViewById(R.id.google_signin_button);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            ActivityResultLauncher<Intent> googleSignInResultLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                // The Task returned from this call is always completed, no need to attach
                                // a listener.
                                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                                handleSignInResult(task);
                            }
                        }
                    });
            private void signIn() {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                activityResultLaunch.launch(signInIntent);

            }


            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.google_signin_button:
                        signIn();
                        break;
                    // ...

                }
            }

        });

    }

    ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    handleSignInResult(task);
                }
            });

    // [START handleSignInResult]
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.i("Connected","googleSignInSuccess: \nID:"+account.getId()+"\nDisplay name: " +account.getDisplayName()+"\nEmail: " + account.getEmail()+"\n");
            Intent Login= new Intent(this,My_Groups_Activity.class);
            startActivity(Login);
            // Signed in successfully, show authenticated UI.
            ;
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("GoogleFails", "signInResult:failed code=" + e.getStatusCode());
            Intent backHome= new Intent(this,MainActivity.class);
            startActivity(backHome);
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