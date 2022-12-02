package com.example.ezchores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Create_Group_Activity extends AppCompatActivity implements View.OnClickListener {

    // Buttons & EditTexts
    Button create_group;
    FloatingActionButton icon, add_member;
    EditText group_name, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        // Init Buttons & EditTexts
        create_group = (Button) findViewById(R.id.create_group_btn);
        icon = (FloatingActionButton) findViewById(R.id.group_photo);
        add_member = (FloatingActionButton) findViewById(R.id.add_friend);
        group_name = (EditText) findViewById(R.id.group_name);
        email = (EditText) findViewById(R.id.Email_field);

        // Listeners
        create_group.setOnClickListener(this);
        icon.setOnClickListener(this);
        add_member.setOnClickListener(this);

    }

    // Override the 'onClick' method, divided by button id
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_group_btn:
                // Needs to save the new group properties [icon, name]
                Intent i = new Intent(this, My_Groups_Activity.class);
                startActivity(i);
                break;

            case R.id.add_friend:
                // Needs to send an invitation to the email in the 'Email_field' field
                String mail = email.getText().toString();
                break;

            case R.id.group_photo:
                // Needs to open phone's gallery to select photo
                break;

            default:
                break;
        }
    }

}