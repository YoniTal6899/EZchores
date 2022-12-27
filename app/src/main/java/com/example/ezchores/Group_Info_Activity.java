package com.example.ezchores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;

public class Group_Info_Activity extends AppCompatActivity implements View.OnClickListener {

    // Declaration of .xml file widgets
    AppCompatButton back, apply;
    Button trash;
    EditText new_name, mem_email;
    FloatingActionButton icon, add_mem;
    TextView group_name;
    ListView memberList;
    CustomAdapter FriendsDisplay;
    ArrayAdapter FriendsDisplayArr;
    // DB
    DatabaseReference ref;

    String groupID;
    String myUserID;
    String groupName;
    @SuppressLint("MissingInflatedId")
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

        back = (AppCompatButton) findViewById(R.id.to_gr);
        trash = (Button) findViewById(R.id.trash_icon);
        apply = (AppCompatButton) findViewById(R.id.apply_group_changes);
        new_name = (EditText) findViewById(R.id.new_group_name_field);
        mem_email = (EditText) findViewById(R.id.new_member_mail);
        icon = (FloatingActionButton) findViewById(R.id.group_photo);
        add_mem= (FloatingActionButton) findViewById(R.id.add_member);
        memberList = (ListView) findViewById(R.id.view_memberAsList);
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

       // Display the friends in the group
        ArrayList<String> FriendsName = new ArrayList<>();
        ArrayList<String> KeyList = new ArrayList<>();


        // init the DataBase reference
       DatabaseReference refFriends = FirebaseDatabase
               .getInstance()
               .getReference("Groups")
               .child(groupID)
               .child("Friends");

       refFriends.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               HashMap<String , Object> friendMap = snapshot
                       .getValue(new GenericTypeIndicator<HashMap<String, Object>>() {
               });
               try {
                   for (String UID : friendMap.keySet()){
                       String key = UID.toString();
                       String name = friendMap.get(UID).toString();
                       System.out.println("TEST ->" + name );
                       FriendsName.add(name);
                       KeyList.add(UID);

                   }
                   FriendsDisplayArr = new ArrayAdapter(
                           getApplicationContext(),
                           android.R.layout.simple_list_item_1,
                           FriendsName);


                  FriendsDisplay = new CustomAdapter(
                          getApplicationContext(),
                          FriendsName,
                          null,
                          null,
                          'm',
                          null);

                   memberList.setAdapter(FriendsDisplayArr);
                   memberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                       @Override
                       public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                           int itemPosition = memberList.getPositionForView(view);
                           System.out.println("We want to delete :"+FriendsName.get(itemPosition)+"at place "+ itemPosition);
                           refFriends.child(KeyList.get(itemPosition)).removeValue();
                           updateUI();

                       }
                   });
               } catch (Exception e) {
                   System.out.println("error :"+ e );
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

                case R.id.trash_icon:

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

   // Update the ListView with the changed data

    private void updateUI(){
        Intent intent = new Intent(this,Group_Info_Activity.class);
        intent.putExtra("ID_name", groupID+","+groupName);
        startActivity(intent);
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
                            updateUI();
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