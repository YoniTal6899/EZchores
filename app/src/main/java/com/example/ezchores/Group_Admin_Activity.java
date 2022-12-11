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

public class Group_Admin_Activity extends AppCompatActivity implements View.OnClickListener {
    // Declaration of .xml widgets
    Button back_to_groups;

    FloatingActionButton shop, group_info, add_goal, add_task;
    String groupID;
    String groupName;
    String userID;
    TextView groupn;
    CustomAdapter goals_adp, task_adp;
    ListView task, goals;
    DatabaseReference ref;
    ArrayList<String> points = new ArrayList<>();
    ArrayList<String> tasks_names = new ArrayList<>();
    ArrayList<String> taskId = new ArrayList<>();
    int curr_userPoints;

    ArrayList<String> goals_names = new ArrayList<>();
    ArrayList<ProgressBar> bars = new ArrayList<>();
    ArrayList<Integer> goal_prog= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_admin);
        String id_name = (String) getIntent().getSerializableExtra("ID_name");
        groupName = id_name.split(",")[1];
        groupn = (TextView) findViewById(R.id.group_name);
        groupn.setText(groupName);
        groupID = id_name.split(",")[0];

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        shop = (FloatingActionButton) findViewById(R.id.shopping_list);
        group_info = (FloatingActionButton) findViewById(R.id.group_info);
        add_goal = (FloatingActionButton) findViewById(R.id.new_goal);
        add_task = (FloatingActionButton) findViewById(R.id.new_task);

        // Init buttons
        ref = FirebaseDatabase.getInstance().getReference();
        back_to_groups = (Button) findViewById(R.id.back_to_groups);
        // Listeners
        back_to_groups.setOnClickListener(this);
        shop.setOnClickListener(this);
        group_info.setOnClickListener(this);
        add_task.setOnClickListener(this);
        add_goal.setOnClickListener(this);
        task = (ListView) findViewById(R.id.tasks_list);
        goals=(ListView)findViewById(R.id.goals_list);
        ref.child("Groups").child(groupID).child("Tasks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> listtask = snapshot.getValue(new GenericTypeIndicator<HashMap<String, Object>>() {
                });
                try {
                    for (String id : listtask.keySet()) {
                        taskId.add(id);
                        tasks_names.add(snapshot.child(id).child("name").getValue().toString());
                        points.add(snapshot.child(id).child("point").getValue().toString());

                    }
                    task_adp = new CustomAdapter(getApplicationContext(), tasks_names, points, null, 't',null);
                    task.setAdapter(task_adp);
                    task.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String taskID = taskId.get(i);
                            ref.child("Users").child(userID).child("MyTasks").child(taskID).removeValue();
                            ref.child("Groups").child(groupID).child("Tasks").child(taskID).removeValue();
                            ref.child("Users").child(userID).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    curr_userPoints = Integer.parseInt(snapshot.child("curr_points").getValue().toString());
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {}
                            });

                            // Update points
                            ref.child("Groups").child(groupID).child("Goals").child("-NIyf6z6Vdch8-Ww6HIS").child("currentPoints").setValue(20);
                            ref.child("Users").child(userID).child("curr_points").setValue(curr_userPoints+Integer.parseInt(points.get(i)));


                            Toast.makeText(Group_Admin_Activity.this, "Successfully completed task:" + tasks_names.get(i), Toast.LENGTH_SHORT).show();
                            Intent user2user = new Intent(Group_Admin_Activity.this, Group_Admin_Activity.class);
                            user2user.putExtra("ID_name", groupID+","+groupName);
                            startActivity(user2user);

                        }
                    });

                } catch (Exception e) {
                
                    e.printStackTrace();
                    Toast.makeText(Group_Admin_Activity.this, "No Tasks...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ref.child("Groups").child(groupID).child("Goals").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> goal_list = snapshot.getValue(new GenericTypeIndicator<HashMap<String, Object>>() {
                });
                try {
                    for (String id : goal_list.keySet()) {
                        double val= (Double.parseDouble( snapshot.child(id).child("value").getValue().toString()));
                        double curr= (Double.parseDouble(snapshot.child(id).child("currentPoints").getValue().toString()));
                        goals_names.add(snapshot.child(id).child("name").getValue().toString());
                        double percent= (curr/val);
                        percent=percent*100;
                        bars.add(new ProgressBar(getApplicationContext()));
                        goal_prog.add((int)percent);

                    }
                    goals_adp = new CustomAdapter(getApplicationContext(), goals_names, null, bars, 'g',goal_prog);
                    goals.setAdapter(goals_adp);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Group_Admin_Activity.this, "No Goals...", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //goals_adp= new CustomAdapter(getApplicationContext(),goals_names,null,bars,'g');
        //goals.setAdapter(goals_adp);




    }

    // Override the 'onClick' method, divided by button id
    @Override
    public void onClick(View v) {
        String id_name = groupID + "," + groupName;
        switch (v.getId()) {
            case R.id.back_to_groups:
                Intent i = new Intent(this, My_Groups_Activity.class);
                startActivity(i);
                break;

            case R.id.shopping_list:
                Intent j = new Intent(this, Shopping_List_Activity.class);
                j.putExtra("ID_name", id_name);
                startActivity(j);
                break;

            case R.id.group_info:
                Intent k = new Intent(this, Group_Info_Activity.class);
                k.putExtra("ID_name", id_name);
                startActivity(k);
                break;

            case R.id.new_goal:
                Intent r = new Intent(this, New_Goal_Activity.class);
                r.putExtra("ID_name", id_name);
                startActivity(r);
                break;

            case R.id.new_task:
                Intent m = new Intent(this, New_Task_Acitivty.class);
                m.putExtra("ID_name", id_name);
                startActivity(m);
                break;

            default:
                break;
        }
    }
}