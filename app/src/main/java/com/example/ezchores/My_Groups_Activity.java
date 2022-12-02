package com.example.ezchores;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class My_Groups_Activity extends Activity implements View.OnTouchListener, View.OnClickListener {

    // Buttons & TextViews
    FloatingActionButton add_group, personal_info;
    private TextView gr_1, gr_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);

        // Init Buttons & TextViews
        add_group = (FloatingActionButton) findViewById(R.id.add_group);
        personal_info = (FloatingActionButton) findViewById(R.id.personal_info);
        gr_1 = (TextView) findViewById(R.id.group_1);
        gr_2 = (TextView) findViewById(R.id.group_2);

        // Listeners
        gr_1.setOnTouchListener(this);
        gr_2.setOnTouchListener(this);
        add_group.setOnClickListener(this);
        personal_info.setOnClickListener(this);

    }

    // Override the 'onClick' method, divided by button id
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_group:
                Intent i = new Intent(this, Create_Group_Activity.class);
                startActivity(i);
                break;

            case R.id.personal_info:
                Intent j = new Intent(this, Personal_Info_Activity.class);
                startActivity(j);
                break;

            default:
                break;
        }
    }

    // Override the 'onTouch' method, divided by TextView id
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.group_1:
                Intent GR1U = new Intent(this, Group_User_Activity.class);
                startActivity(GR1U);
                break;

            case R.id.group_2:
                Intent GR2A = new Intent(this, Group_Admin_Activity.class);
                startActivity(GR2A);
                break;
        }
        return true;
    }


}