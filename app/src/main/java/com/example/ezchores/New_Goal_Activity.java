package com.example.ezchores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class New_Goal_Activity extends AppCompatActivity implements View.OnClickListener {

    //Declaration of .xml file widgets
    // Declaration of .xml file widgets
    EditText name, val;
    AppCompatButton back, create;
    ImageView refreshUsers;

    String groupID,groupName;

    //DB
    DatabaseReference ref;
    String[] listOfusers;
    String[] listOfuserIDs;

    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goal);
        String[] groupInfo = getIntent().getSerializableExtra("ID_name").toString().split(",");
        groupID = groupInfo[0];
        groupName = groupInfo[1];
        // Init .xml widgets
        back = (AppCompatButton) findViewById(R.id.back_to_groups);
        create = (AppCompatButton) findViewById(R.id.create_goal);
        name = (EditText) findViewById(R.id.Goal_name);
        val = (EditText) findViewById(R.id.Goal_val);
        refreshUsers = (ImageView) findViewById(R.id.refreshUsers);


        // Listeners
        back.setOnClickListener(this);
        create.setOnClickListener(this);
        refreshUsers.setOnClickListener(this);

        // DB
        ref = FirebaseDatabase.getInstance().getReference();
        ;
        GetUsersFromDB();
        Dropdown();
    }

    private void GetUsersFromDB() {
        ref.child("Groups").child(groupID).child("Friends").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> list = snapshot.getValue(new GenericTypeIndicator<HashMap<String, Object>>() {
                });
                int numOfUsers = list.keySet().size();
                listOfusers = new String[numOfUsers];
                listOfuserIDs = new String[numOfUsers];
                int i = 0;
                for (String userId : list.keySet()) {
                    listOfusers[i] = i+") "+list.get(userId).toString();
                    listOfuserIDs[i] = userId;
                    i++;
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
            case R.id.back_to_groups:
                Intent i = new Intent(this, Group_Admin_Activity.class);
                i.putExtra("ID_name",groupID+","+groupName);
                startActivity(i);
                break;

            case R.id.create_goal:
                // Needs to save goal info [name, points, icon, members] and save in DB
                createGoal();
                break;

            case R.id.refreshUsers:
                GetUsersFromDB();
                Dropdown();
                break;

            default:
                break;
        }

    }

    private void Dropdown() {
        autoCompleteTextView = findViewById(R.id.auto_complete_txt);
        if (listOfusers != null) {
            adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, listOfusers);
            System.out.println("Users: " + listOfusers.toString());
        } else {
            adapterItems = new ArrayAdapter<String>(this, R.layout.list_item);
            System.out.println("list is null!");
        }
        autoCompleteTextView.setAdapter(adapterItems);
    }

    private void createGoal() {
        String taskName = name.getText().toString();
        String value = val.getText().toString();
        String[] assign = autoCompleteTextView.getText().toString().split("\\)");
        int assignIndex;
        try {
            assignIndex = Integer.parseInt(assign[0]);
        } catch (Exception e){
            Toast.makeText(New_Goal_Activity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        String assignID = listOfuserIDs[assignIndex];
        if (value.equals("")||taskName.equals("")||assignID==null){
            Toast.makeText(New_Goal_Activity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        int numVal = 0;
        try {
            numVal = Integer.parseInt(value);
            if (numVal < 0) {
                Toast.makeText(New_Goal_Activity.this, "Value should be a whole, non-negative number", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            Toast.makeText(New_Goal_Activity.this, "Value should be a whole, non-negative number", Toast.LENGTH_SHORT).show();
            return;
        }

        myGoal goal = new myGoal(numVal, taskName, assignID,false);
        String goalID = ref.child("Groups").child(groupID).child("Goals").push().getKey();
        ref.child("Groups").child(groupID).child("Goals").child(goalID).setValue(goal);
        ref.child("Users").child(assignID).child("MyGoals").child(goalID).child("groupID").setValue(groupID);
        ref.child("Users").child(assignID).child("MyGoals").child(goalID).child("isComplete").setValue(false);
        Toast.makeText(New_Goal_Activity.this, "Goal added successfully", Toast.LENGTH_SHORT).show();
        Intent CRG = new Intent(this, Group_Admin_Activity.class);
        CRG.putExtra("ID_name",groupID+","+groupName);
        startActivity(CRG);
    }

}
