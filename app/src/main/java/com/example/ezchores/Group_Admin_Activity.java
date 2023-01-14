package com.example.ezchores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Group_Admin_Activity extends AppCompatActivity implements View.OnClickListener {
    // Declaration of .xml widgets
    AppCompatButton back_to_groups, group_info;
    ImageView add_goal, add_task;
    String GroupID, groupName, userID, args;
    TextView groupn;
    CustomAdapter goals_adp, task_adp;
    ListView tasks, goals;
    DatabaseReference ref;
    int curr_userPoints;
    ArrayList<String> points = new ArrayList<>();
    ArrayList<String> tasks_names = new ArrayList<>();
    ArrayList<String> taskId = new ArrayList<>();
    ArrayList<String> goals_names = new ArrayList<>();
    ArrayList<ProgressBar> bars = new ArrayList<>();
    ArrayList<Integer> goal_prog = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_admin);
        groupn = (TextView) findViewById(R.id.group_name);
        groupn.setText(groupName);
        args = (String) getIntent().getSerializableExtra("ARGS");
        curr_userPoints = Integer.parseInt(args.split(",")[1]);
        GroupID = args.split(",")[0];

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        group_info = (AppCompatButton) findViewById(R.id.group_info);
        add_goal = (ImageView) findViewById(R.id.new_goal);
        add_task = (ImageView) findViewById(R.id.new_task);

        // Init buttons
        ref = FirebaseDatabase.getInstance().getReference();
        back_to_groups = (AppCompatButton) findViewById(R.id.back_to_groups);
        // Listeners
        back_to_groups.setOnClickListener(this);
        group_info.setOnClickListener(this);
        add_task.setOnClickListener(this);
        add_goal.setOnClickListener(this);
        tasks = (ListView) findViewById(R.id.tasks_list);
        goals = (ListView) findViewById(R.id.goals_list);
        Map<String, Object> data = new HashMap<>();
        data.put("groupId", GroupID);
        FirebaseFunctions.getInstance().getHttpsCallable("getAllTasksInGroup").call(data).addOnCompleteListener(new OnCompleteListener<HttpsCallableResult>() {
            @Override
            public void onComplete(@NonNull Task<HttpsCallableResult> task) {
                if (task.isSuccessful()) {
                    if (task.isComplete()) {
                        String userTasks = (String) task.getResult().getData();
                        HashMap<String, JsonNode> data = jsonListToHashMap(userTasks, 't');
                        System.out.println("*******************************************************");
                        System.out.println(data.toString());
                        System.out.println("*******************************************************");
                        try {
                            for (String TaskID : data.keySet()) {
                                String TaskName = data.get(TaskID).get("taskName").asText();
                                String TaskPoints = data.get(TaskID).get("taskPoints").asText();
                                tasks_names.add(TaskName);
                                points.add(TaskPoints);
                                taskId.add(TaskID);
                            }
                            task_adp = new CustomAdapter(getApplicationContext(), tasks_names, points, null, 't', null);
                            tasks.setAdapter(task_adp);
                            tasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String taskID = taskId.get(i);
                                    ref.child("Users").child(userID).child("MyTasks").child(taskID).child("isComplete").setValue(true);
                                    ref.child("Groups").child(GroupID).child("Tasks").child(taskID).child("isComplete").setValue(true);
                                    // Update points
                                    int new_points = (curr_userPoints + Integer.parseInt(points.get(i)));
                                    ref.child("Users").child(userID).child("curr_points").setValue(new_points);
                                    Toast.makeText(Group_Admin_Activity.this, "Successfully completed task:" + tasks_names.get(i), Toast.LENGTH_SHORT).show();
                                    Intent user2user = new Intent(Group_Admin_Activity.this, Group_User_Activity.class);
                                    user2user.putExtra("ARGS", GroupID + "," + new_points);
                                    startActivity(user2user);
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(Group_Admin_Activity.this, "No tasks", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        FirebaseFunctions.getInstance().getHttpsCallable("getAllGoalsInGroup").call(data).addOnCompleteListener(new OnCompleteListener<HttpsCallableResult>() {
            @Override
            public void onComplete(@NonNull Task<HttpsCallableResult> task) {
                if (task.isSuccessful()) {
                    if (task.isComplete()) {
                        String userGoals = (String) task.getResult().getData();
                        HashMap<String, JsonNode> data = jsonListToHashMap(userGoals, 'g');
                        System.out.println("*******************************************************");
                        System.out.println(data.toString());
                        System.out.println("*******************************************************");
                        try {
                            for (String GoalID : data.keySet()) {
                                String GoalName = data.get(GoalID).get("goalName").asText();
                                double value = Double.parseDouble(data.get(GoalID).get("goalVal").toString());
                                double curr = Double.parseDouble(data.get(GoalID).get("currPoints").toString());
                                double percent = (curr / value);
                                percent = percent * 100;
                                goals_names.add(GoalName);
                                bars.add(new ProgressBar(getApplicationContext()));
                                goal_prog.add((int) percent);
                            }
                            goals_adp = new CustomAdapter(getApplicationContext(), goals_names, null, bars, 'g', goal_prog);
                            goals.setAdapter(goals_adp);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(Group_Admin_Activity.this, "No tasks", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    // Override the 'onClick' method, divided by button id
    @Override
    public void onClick(View v) {
        String id_name = GroupID + "," + groupName;
        switch (v.getId()) {
            case R.id.back_to_groups:
                Intent i = new Intent(this, My_Groups_Activity.class);
                startActivity(i);
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

    public static HashMap<String, JsonNode> jsonListToHashMap(String jsonList, char classification) {
        HashMap<String, JsonNode> map = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonList);
            for (JsonNode node : jsonNode) {
                String key = "";
                if (classification == 't') {
                    key = node.get("taskId").asText();
                } else {
                    key = node.get("goalId").asText();
                }
                map.put(key, node);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return map;
        }
        return map;
    }
}
