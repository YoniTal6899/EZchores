package com.example.ezchores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
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

public class Group_User_Activity extends AppCompatActivity implements View.OnClickListener {
    // Declaration of the .xml file
    Button to_gr;
    ListView task_list, goals_list;
    FloatingActionButton shop;
    int counter = 0;
    String groupId;
    String groupName;
    TextView groupn;
    CustomAdapter goals_adp, task_adp;
    ListView task, goals;
    DatabaseReference ref;
    ArrayList<String> points = new ArrayList<>();
    ArrayList<String> tasks_names = new ArrayList<>();
    ArrayList<String> goals_names = new ArrayList<>();
    ArrayList<ProgressBar> bars = new ArrayList<>();
    String userId;
    ArrayList<String> taskId = new ArrayList<>();
    ArrayList<String> goalID= new ArrayList<>();
    ArrayList<Integer> goal_prog= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_user);
        String id_name = (String) getIntent().getSerializableExtra("ID_name");
        groupName = id_name.split(",")[1];
        groupId = id_name.split(",")[0];
        groupn = (TextView) findViewById(R.id.group_name);
        groupn.setText(groupName);

        // Init of the .xml file
        to_gr = (Button) findViewById(R.id.back_to_groups);
        shop = (FloatingActionButton) findViewById(R.id.shopping_list);
        task_list = (ListView) findViewById(R.id.tasks_list);
        goals_list = (ListView) findViewById(R.id.goals_list);
        task = (ListView) findViewById(R.id.tasks_list);
        // Listeners
        to_gr.setOnClickListener(this);
        shop.setOnClickListener(this);
        ref = FirebaseDatabase.getInstance().getReference();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        ref.child("Users").child(userId).child("MyTasks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> tasklist = snapshot.getValue(new GenericTypeIndicator<HashMap<String, Object>>() {
                });
                try {
                    for (String id : tasklist.keySet()) {
                        String tempgroupId = snapshot.child(id).getValue().toString();
                        if (groupId.equals(tempgroupId)) {
                            taskId.add(id);
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(Group_User_Activity.this, "No tasks found for you :)", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        ref.child("Users").child(userId).child("MyGoals").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> g_id_list = snapshot.getValue(new GenericTypeIndicator<HashMap<String, Object>>() {
                });
                try {
                    for (String id : g_id_list.keySet()) {
                        String tempgroupId = snapshot.child(id).getValue().toString();
                        if (groupId.equals(tempgroupId)) {
                            goalID.add(id);
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(Group_User_Activity.this, "No goals found for you :(", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        ref.child("Groups").child(groupId).child("Tasks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> listtask = snapshot.getValue(new GenericTypeIndicator<HashMap<String, Object>>() {
                });
                try {
                    for (String tempid : listtask.keySet()) {
                        if (taskId.contains(tempid)) {
                            tasks_names.add(snapshot.child(tempid).child("name").getValue().toString());
                            points.add(snapshot.child(tempid).child("point").getValue().toString());
                        }
                    }

                    task_adp = new CustomAdapter(getApplicationContext(), tasks_names, points, null, 't', null);
                    task.setAdapter(task_adp);
                    task.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String taskID = taskId.get(i);
                            ref.child("Users").child(userId).child("MyTasks").child(taskID).removeValue();
                            ref.child("Groups").child(groupId).child("Tasks").child(taskID).removeValue();
                            Toast.makeText(Group_User_Activity.this, "Successfully completed task:" + tasks_names.get(i), Toast.LENGTH_SHORT).show();
                            Intent user2user = new Intent(Group_User_Activity.this, Group_User_Activity.class);
                            user2user.putExtra("ID_name", groupId + "," + groupName);
                            startActivity(user2user);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Group_User_Activity.this, "No tasks", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        ref.child("Groups").child(groupId).child("Goals").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> g_id_list = snapshot.getValue(new GenericTypeIndicator<HashMap<String, Object>>() {
                });
                try {
                    for (String id : g_id_list.keySet()) {
                        String userID = snapshot.child(id).child("assignID").getValue().toString();
                        if (userId.equals(userID)) {
                            goals_names.add(snapshot.child(id).child("name").getValue().toString());
                            double curr= Double.parseDouble(snapshot.child(id).child("currentPoints").getValue().toString());
                            double tot= Double.parseDouble(snapshot.child(id).child("value").getValue().toString());
                            double percent= (curr/tot);
                            percent=percent*100;
                            bars.add(new ProgressBar(getApplicationContext()));
                            goal_prog.add((int)percent);
                        }
                    }
                    goals_adp = new CustomAdapter(getApplicationContext(), goals_names, null, bars, 'g',goal_prog);
                    goals_list.setAdapter(goals_adp);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Group_User_Activity.this, "No goals", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


        // Override the 'onClick' method, divided by button id
        @Override
        public void onClick (View v){
            switch (v.getId()) {
                case R.id.back_to_groups:
                    Intent i = new Intent(this, My_Groups_Activity.class);
                    i.putExtra("ID_name", groupId + "," + groupName);
                    startActivity(i);
                    break;

                case R.id.shopping_list:
                    Intent j = new Intent(this, Shopping_List_Activity.class);
                    startActivity(j);
                    break;
            }
        }}