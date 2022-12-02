package com.example.ezchores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Group_Info_Activity extends AppCompatActivity implements View.OnClickListener {

    // Declaration of .xml file widgets
    Button back, apply;
    EditText new_name, mem_email;
    FloatingActionButton icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

        //Init widgets
        back = (Button) findViewById(R.id.to_gr);
        apply = (Button) findViewById(R.id.apply_group_changes);
        new_name = (EditText) findViewById(R.id.new_group_name_field);
        mem_email = (EditText) findViewById(R.id.new_member_mail);
        icon = (FloatingActionButton) findViewById(R.id.group_photo);

        //Listeners
        back.setOnClickListener(this);
        apply.setOnClickListener(this);
        icon.setOnClickListener(this);
    }

    // Override the 'onClick' method, divided by button id
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.to_gr:
                finish();
                break;

            case R.id.apply_group_changes:
                // Needs to save the new group info [name, icon,
                Intent i = new Intent(this, Group_Admin_Activity.class);
                startActivity(i);
                break;

            case R.id.group_photo:
                //Opens logo options
                break;
            default:
                break;
        }

    }
}