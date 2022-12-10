package com.example.ezchores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class Personal_Info_Activity extends AppCompatActivity implements View.OnClickListener {
    // Buttons
    Button log_out, apply;
    // Firebase
    private FirebaseAuth mAuth;
    TextView user_name,new_mail,total_points_num;
    String UserId,userName,current_points,mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        // Init buttons
        log_out = (Button) findViewById(R.id.log_out);

        // Listeners
        log_out.setOnClickListener(this);

        String[] Uid_name_email_currpoints = getIntent().getSerializableExtra("Uid_name_email_currpoints").toString().split(",");
        UserId = Uid_name_email_currpoints[0];
        userName = Uid_name_email_currpoints[1];
        mail = Uid_name_email_currpoints[2];
        current_points = Uid_name_email_currpoints[3];

        user_name = (TextView) findViewById(R.id.user_name);
        user_name.setText(userName);

        new_mail = (TextView) findViewById(R.id.new_mail);
        new_mail.setText("Email: "+mail);

        total_points_num = (TextView) findViewById(R.id.total_points_num);
        total_points_num.setText(current_points);

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

            default:
                break;
        }

    }
}