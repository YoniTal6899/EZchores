package com.example.ezchores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class Personal_Info_Activity extends AppCompatActivity implements View.OnClickListener {
    // Buttons
    Button log_out, apply;
    FloatingActionButton icon;

    // Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        // Init buttons
        log_out = (Button) findViewById(R.id.log_out);
        apply = (Button) findViewById(R.id.apply_changes);
        icon = (FloatingActionButton) findViewById(R.id.person_photo);

        // Listeners
        log_out.setOnClickListener(this);
        apply.setOnClickListener(this);
        icon.setOnClickListener(this);
    }

    // Override the 'onClick' method, divided by button id
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.log_out:
                mAuth.getInstance().signOut();
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                break;

            case R.id.apply_changes:
                // Needs to save the new data in the DB as well
                Intent j = new Intent(this, My_Groups_Activity.class);
                startActivity(j);
                break;

            case R.id.person_photo:
                // Needs to open phone gallery
                break;

            default:
                break;
        }

    }
}