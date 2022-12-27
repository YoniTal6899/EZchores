package com.example.ezchores;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class Personal_Info_Activity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    // Buttons
    AppCompatButton log_out, apply;
    // Firebase
    private FirebaseAuth mAuth;
    TextView user_name, new_mail, total_points_num;
    String UserId, userName, current_points, mail, sendToIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        // Init buttons
        log_out = (AppCompatButton) findViewById(R.id.log_out);

        // Listeners
        log_out.setOnClickListener(this);
        sendToIntent = getIntent().getSerializableExtra("Uid_name_email_currpoints").toString();
        String[] Uid_name_email_currpoints = sendToIntent.split(",");
        UserId = Uid_name_email_currpoints[0];
        userName = Uid_name_email_currpoints[1];
        mail = Uid_name_email_currpoints[2];
        current_points = Uid_name_email_currpoints[3];

        user_name = (TextView) findViewById(R.id.user_name);
        user_name.setText(userName);

        new_mail = (TextView) findViewById(R.id.new_mail);
        new_mail.setText("Email: " + mail);

        total_points_num = (TextView) findViewById(R.id.total_points_num);
        total_points_num.setText(current_points);
        total_points_num.setOnTouchListener(this);

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

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        switch (v.getId()) {
            case R.id.total_points_num:
                Intent j = new Intent(this, Update_Goals_Activity.class);
                j.putExtra("Uid_name_email_currpoints", sendToIntent);
                startActivity(j);
                break;
            default:
                break;
        }
        return true;
    }
}