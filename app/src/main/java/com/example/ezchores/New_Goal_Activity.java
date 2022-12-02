package com.example.ezchores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class New_Goal_Activity extends AppCompatActivity implements View.OnClickListener {

    //Declaration of .xml file widgets
    Button back, create;
    FloatingActionButton icon;
    EditText name, val;
    Spinner s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goal);

        //Init widgets
        back = (Button) findViewById(R.id.back_to_groups);
        create = (Button) findViewById(R.id.add_goal);
        icon = (FloatingActionButton) findViewById(R.id.goal_icn);
        name = (EditText) findViewById(R.id.Task_name);
        val = (EditText) findViewById(R.id.Task_val);
        s = (Spinner) findViewById(R.id.members_assign);

        //Listeners
        back.setOnClickListener(this);
        create.setOnClickListener(this);
        icon.setOnClickListener(this);
    }

    // Override the 'onClick' method, divided by button id
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_to_groups:
                Intent i = new Intent(this, Group_Admin_Activity.class);
                startActivity(i);
                break;

            case R.id.add_goal:
                // Needs to save goal info [name, points, icon, members] and save in DB
                Intent j = new Intent(this, Group_Admin_Activity.class);
                startActivity(j);
                break;

            case R.id.goal_icn:
                //Opens logo options
                break;

            default:
                break;
        }

    }
}