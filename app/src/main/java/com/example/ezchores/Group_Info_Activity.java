package com.example.ezchores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    TextView group_name;
    // DB
    DatabaseReference ref;

    String groupID;
    String myUserID;
    String groupName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_group_info);

                String id_name = (String) getIntent().getSerializableExtra("ID_name");
        groupName=id_name.split(",")[1];
        ref = FirebaseDatabase.getInstance().getReference();
        groupID = id_name.split(",")[0];
        group_name=(TextView) findViewById(R.id.group_name);
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
        group_name.setText(groupName);
        myUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        try {
//            ref.child("Groups").child(groupID).child("name").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    group_name.setText(snapshot.getValue().toString());
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }catch (Exception e){
//            Toast.makeText(Group_Info_Activity.this, "on create Something went wrong :(", Toast.LENGTH_SHORT).show();
//
//        }


    }


    // Override the 'onClick' method, divided by button id
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.to_gr:
                Intent myGroups = new Intent(this, My_Groups_Activity.class);
                myGroups.putExtra("ID_name",groupID+"," + groupName);
                startActivity(myGroups);
                break;

            case R.id.apply_group_changes:
                // Needs to save the new group info [name, icon
                String N=new_name.getText().toString();
                if(N!=null) {
                    updateName(N);
                    Intent i = new Intent(this, Group_Admin_Activity.class);
                    i.putExtra("ID_name",groupID+"," + N);
                    startActivity(i);
                }else{
                    Intent i = new Intent(this, Group_Admin_Activity.class);

                    i.putExtra("group_Id",groupID);
                    i.putExtra("group_name",groupName);
                    startActivity(i);
                    Toast.makeText(Group_Info_Activity.this, "on click Something went wrong :(", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.add_member:
                String new_mem_email= mem_email.getText().toString();
                addFriend(new_mem_email);
                break;
            default:
                break;
        }

    }

    private void updateName(String newname) {
        group_name.setText(newname);
        ref.child("Groups").child(groupID).child("name").setValue(newname);

       ref.child("Groups").child(groupID).child("Friends").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               HashMap<String,Object> list = snapshot.getValue(new GenericTypeIndicator<HashMap<String, Object>>() {});

               for(String friendId: list.keySet()){
                   ref.child("Users").child(friendId).child("Groups").child(groupID).setValue(newname);
                   Toast.makeText(Group_Info_Activity.this, "group name has changed...", Toast.LENGTH_SHORT).show();
               }

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {
               Toast.makeText(Group_Info_Activity.this, "something went wrong...", Toast.LENGTH_SHORT).show();
           }
       });
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

                            groupName = snapshot.child(myUserID).child("Groups").child(groupID).getValue().toString();
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
                Toast.makeText(Group_Info_Activity.this, "Member doesn't exist :(", Toast.LENGTH_SHORT).show();

            }
        });

    }
}