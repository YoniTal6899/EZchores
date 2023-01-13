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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Update_Goals_Activity extends AppCompatActivity implements View.OnClickListener {

    String currpoints;
    // Declaration of .xml file widgets
    EditText name, val;
    AppCompatButton back, create;
    ImageView refreshUsers;
    TextView available_points_Text;

    String groupID, groupName, UserID, sendToIntent;
    int counter = 0;
    int available_points;
    //DB
    DatabaseReference ref;
    ArrayList<String> listOfGoalsGroupIds = new ArrayList<String>();
    ArrayList<String> listOfGoalsGroupNames = new ArrayList<String>();
    ArrayList<String> listOfGoalIDs = new ArrayList<String>();
    ArrayList<String> listOfGoalNames= new ArrayList<String>();
    ArrayList<String> DropdownList = new ArrayList<String>();
    ArrayList<Integer> listOfGoalVals= new ArrayList<Integer>();
    ArrayList<Integer> listOfGoalcurrPoints= new ArrayList<Integer>();
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_goals);
        sendToIntent = getIntent().getSerializableExtra("Uid_name_email_currpoints").toString();
        String[] Uid_name_email_currpoints = sendToIntent.split(",");
        UserID = Uid_name_email_currpoints[0];
        available_points = Integer.parseInt(Uid_name_email_currpoints[3]);

        // Init .xml widgets
        back = (AppCompatButton) findViewById(R.id.back_to_groups);
        create = (AppCompatButton) findViewById(R.id.update_goal);
        val = (EditText) findViewById(R.id.Val);
        refreshUsers = (ImageView) findViewById(R.id.refreshUsers);
        available_points_Text = (TextView) findViewById(R.id.available_points);
        available_points_Text.setText("Available points: " + available_points);


        // Listeners
        back.setOnClickListener(this);
        create.setOnClickListener(this);
        refreshUsers.setOnClickListener(this);

        // DB
        ref = FirebaseDatabase.getInstance().getReference();
        ;
        GetGoalsFromDB();
        DropDown();

    }
    public String ExtractGroupID(String s){
        String ans=s.split(",")[0];
        ans=ans.split("=")[1];
        return ans;
    }


    private void GetGoalsFromDB() {
        ref.child("Users").child(UserID).child("MyGoals").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> list = snapshot.getValue(new GenericTypeIndicator<HashMap<String, Object>>() {
                });

                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                System.out.println();
                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

                if(list!=null) {
                    for (String goalId : list.keySet()) {
                        if (!listOfGoalIDs.contains(goalId)) {
                            listOfGoalsGroupIds.add(ExtractGroupID(list.get(goalId).toString()));
                            listOfGoalIDs.add(goalId);
                            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                            System.out.println("To the listOfGoalsGroupIds " + list.get(goalId).toString());
                            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                            System.out.println("To the listOfGoalIDs " + goalId);
                            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        if (listOfGoalsGroupIds.size()!= 0 && listOfGoalIDs.size()!= 0) {
            for (counter = 0; counter < listOfGoalsGroupIds.size(); counter++) {
                ref.child("Groups").child(listOfGoalsGroupIds.get(counter)).child("Goals").child(listOfGoalIDs.get(counter)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name = snapshot.child("name").getValue().toString();
                        int currPoints = Integer.parseInt(snapshot.child("currentPoints").getValue().toString());
                        int val = Integer.parseInt(snapshot.child("value").getValue().toString());
                        if (listOfGoalNames.size()!= listOfGoalIDs.size()) {
                            listOfGoalNames.add(name);
                            listOfGoalcurrPoints.add(currPoints);
                            listOfGoalVals.add(val);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
            if (listOfGoalNames.size()!= 0 && listOfGoalVals.size()!= 0 && listOfGoalcurrPoints.size()!= 0) {

                for (counter = 0; counter < listOfGoalsGroupIds.size(); counter++) {
                    ref.child("Groups").child(listOfGoalsGroupIds.get(counter)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(listOfGoalsGroupNames.size()!= listOfGoalIDs.size()) {
                                listOfGoalsGroupNames.add(snapshot.child("name").getValue().toString());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }

            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_to_groups:
                Intent BTGR = new Intent(this, My_Groups_Activity.class);
                BTGR.putExtra("Uid_name_email_currpoints", sendToIntent);

                startActivity(BTGR);
                break;

            case R.id.update_goal:
                // Needs to save into the DB the values in the EditText's [name, val, comments]
                updateGoal();
                break;
            case R.id.refreshUsers:
                GetGoalsFromDB();
                DropDown();

            default:
                break;
        }
    }

    private void updateGoal() {
        String value = val.getText().toString();
        String[] index = autoCompleteTextView.getText().toString().split("\\)");
        int numIndex = -1;
        try {
            numIndex = Integer.parseInt(index[0]);
        } catch (Exception e) {
            Toast.makeText(Update_Goals_Activity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (value.equals("") || numIndex == -1) {
            Toast.makeText(Update_Goals_Activity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        int numVal = 0;
        int remaing_points = (listOfGoalVals.get(numIndex) - listOfGoalcurrPoints.get(numIndex));
        try {
            numVal = Integer.parseInt(value);
        } catch (Exception e) {
            Toast.makeText(Update_Goals_Activity.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
            return;
        }

        // check value is legal
        if (numVal < 0) {
            Toast.makeText(Update_Goals_Activity.this, "Points should be a whole, non-negative number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (numVal > available_points) {
            Toast.makeText(Update_Goals_Activity.this, "You don't have enough points :(", Toast.LENGTH_SHORT).show();
            return;
        }
        if (numVal > remaing_points) {
            Toast.makeText(Update_Goals_Activity.this, "Points entered is more than remaining for that goal", Toast.LENGTH_SHORT).show();
            return;
        }

        // Selection is good! Update DB accordingly

        // Goal completed!
        remaing_points = (remaing_points - numVal);
        available_points = available_points - numVal;
        if (remaing_points == 0) {
            int updated_currPoints = (listOfGoalcurrPoints.get(numIndex)+numVal);
            ref.child("Groups").child(listOfGoalsGroupIds.get(numIndex)).child("Goals").child(listOfGoalIDs.get(numIndex)).child("currentPoints").setValue(updated_currPoints);
            ref.child("Groups").child(listOfGoalsGroupIds.get(numIndex)).child("Goals").child(listOfGoalIDs.get(numIndex)).child("isComplete").setValue(true);
            ref.child("Users").child(UserID).child("MyGoals").child(listOfGoalIDs.get(numIndex)).child("isComplete").setValue(true);
            Toast.makeText(Update_Goals_Activity.this, "Goal " + listOfGoalNames.get(numIndex) + " completed successfully! :)", Toast.LENGTH_SHORT).show();
            Intent BTGR = new Intent(this, Personal_Info_Activity.class);
            String[] toSend = sendToIntent.split(",");
            BTGR.putExtra("Uid_name_email_currpoints", toSend[0]+","+toSend[1]+","+toSend[2]+","+available_points);
            startActivity(BTGR);
        } else { // Goal not yet completed - update currpoints for user and goal.
            int updated_currPoints = (listOfGoalcurrPoints.get(numIndex)+numVal);
            ref.child("Groups").child(listOfGoalsGroupIds.get(numIndex)).child("Goals").child(listOfGoalIDs.get(numIndex)).child("currentPoints").setValue(updated_currPoints);
            ref.child("Users").child(UserID).child("curr_points").setValue(available_points);
            Toast.makeText(Update_Goals_Activity.this, "Goal " + listOfGoalNames.get(numIndex) + " updated successfully! :)", Toast.LENGTH_SHORT).show();
            Intent BTINF = new Intent(this, Personal_Info_Activity.class);
            Intent BTGR = new Intent(this, Personal_Info_Activity.class);
            String[] toSend = sendToIntent.split(",");
            BTGR.putExtra("Uid_name_email_currpoints", toSend[0]+","+toSend[1]+","+toSend[2]+","+available_points);
            startActivity(BTGR);
        }
    }

    private void DropDown() {
        autoCompleteTextView = findViewById(R.id.auto_complete_txt);
        int size = listOfGoalVals.size();

        if (listOfGoalIDs.size()==size && listOfGoalcurrPoints.size()==size&&listOfGoalNames.size()==size&& listOfGoalsGroupIds.size()==size&& listOfGoalsGroupNames.size()==size) {
            for (int i = 0; i < listOfGoalcurrPoints.size(); i++) {
                String toAdd = i + ") Group: " + listOfGoalsGroupNames.get(i) + " | Goal: " + listOfGoalNames.get(i) + " " + listOfGoalcurrPoints.get(i) + "/" + listOfGoalVals.get(i);
                if (!DropdownList.contains(toAdd)) {
                    DropdownList.add(toAdd);
                }
            }


            adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, DropdownList);
            System.out.println("Goal dropdown: " + DropdownList.toString());
        } else {
            adapterItems = new ArrayAdapter<String>(this, R.layout.list_item);
            System.out.println("list is null!");
        }
        autoCompleteTextView.setAdapter(adapterItems);
    }
}