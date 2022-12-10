package com.example.ezchores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Group_Info_Activity extends AppCompatActivity implements View.OnClickListener {

    // Declaration of .xml file widgets
    Button back, apply;
    EditText new_name, mem_email;
    FloatingActionButton icon, add_mem;

    // DB
    DatabaseReference ref;

    String groupID;
    String myUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        ref = FirebaseDatabase.getInstance().getReference();
        groupID = (String) getIntent().getSerializableExtra("group_id");

        //Init widgets
        back = (Button) findViewById(R.id.to_gr);
        apply = (Button) findViewById(R.id.apply_group_changes);
        new_name = (EditText) findViewById(R.id.new_group_name_field);
        mem_email = (EditText) findViewById(R.id.new_member_mail);
        icon = (FloatingActionButton) findViewById(R.id.group_photo);
        add_mem= (FloatingActionButton) findViewById(R.id.add_member);
        //Listeners
        back.setOnClickListener(this);
        apply.setOnClickListener(this);
        icon.setOnClickListener(this);
        add_mem.setOnClickListener(this);
        myUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
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

            case R.id.add_member:
                String new_mem_email= mem_email.getText().toString();
                addFriend(new_mem_email);
                break;
            default:
                break;
        }

    }

    private void addFriend(String mail) {
        ref.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String,Object> list = snapshot.getValue(new GenericTypeIndicator<HashMap<String, Object>>() {});
                try {
                    boolean found = false;
                    for (String userId : list.keySet() ){
                        String checkMail = snapshot.child(userId).child("email").getValue().toString();
                        if (checkMail.equals( mail)){
                            String name = snapshot.child(userId).child("name").getValue().toString();

                            String groupName = snapshot.child(myUserID).child("Groups").child(groupID).getValue().toString();
                            ref.child("Groups").child(groupID).child("Friends").child(userId).setValue(name);
                            ref.child("Users").child(userId).child("Groups").child(groupID).setValue(groupName);
                            Toast.makeText(Group_Info_Activity.this, mail+" was added to the group", Toast.LENGTH_SHORT).show();
                            found = true;
                            break;
                        }
                    }
                    if(!found){
                        Toast.makeText(Group_Info_Activity.this, "Member doesn't exist :(", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e){
                    Toast.makeText(Group_Info_Activity.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Group_Info_Activity.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();

            }
        });

    }
}