package com.example.ezchores;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Create_Group_Activity extends AppCompatActivity implements View.OnClickListener {

    // Buttons & EditTexts
    Button create_group;
    FloatingActionButton icon, add_member;
    EditText group_name, email;
    String username, groupkey,UserId;


    private FirebaseAuth firebaseAuth;
    private DatabaseReference ref;

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


        firebaseAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();

        UserId = firebaseAuth.getCurrentUser().getUid();
        ref.child("Users").child(UserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    username = snapshot.child("name").getValue().toString();
                } catch (Exception e) {
                    Toast.makeText(Create_Group_Activity.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }


    // Override the 'onClick' method, divided by button id
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_group_btn:
                // Needs to save the new group properties [icon, name]

                CreateGroup();

                Intent i = new Intent(this, My_Groups_Activity.class);
                startActivity(i);
                break;

            case R.id.add_friend:
                // Needs to send an invitation to the email in the 'Email_field' field
                String mail = email.getText().toString();
                break;

            default:
                break;
        }
    }

    public void CreateGroup() {


        String group_n = group_name.getText().toString();
        if (TextUtils.isEmpty(group_n)) {
            Toast.makeText(this, "Please enter group name...", Toast.LENGTH_SHORT).show();
            return;
        }

        Group group = new Group(group_n);
        groupkey = ref.child("Users").child(UserId).child("Groups").push().getKey();//

        ref.child("Users").child(UserId).child("Groups").child(groupkey).setValue(group.getName());//push to user -> groups->  key of new group

        ref.child("Groups").child(groupkey).setValue(group);// groups-> new group key->set data
        ref.child("Groups").child(groupkey).child("admins").child(UserId).setValue(username);
        ref.child("Groups").child(groupkey).child("Friends").child(UserId).setValue(username);
    }
}