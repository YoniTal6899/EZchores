package com.example.ezchores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class Group_User_Activity extends AppCompatActivity implements View.OnClickListener {
    // Declaration of the .xml file
    AppCompatButton to_gr;
    ListView task_list, goals_list;
    String groupName,GroupID,args,userId;
    TextView groupn;
    CustomAdapter goals_adp, task_adp;
    DatabaseReference ref;
    ArrayList<String> points = new ArrayList<>();
    ArrayList<String> tasks_names = new ArrayList<>();
    ArrayList<String> goals_names = new ArrayList<>();
    ArrayList<ProgressBar> bars = new ArrayList<>();
    ArrayList<String> taskId = new ArrayList<>();
    int curr_userPoints;
    ArrayList<Integer> goal_prog = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_user);
        args = (String) getIntent().getSerializableExtra("ARGS");
        curr_userPoints = Integer.parseInt(args.split(",")[1]);
        GroupID = args.split(",")[0];
        groupName=args.split(",")[2];
        groupn = (TextView) findViewById(R.id.group_name);

        groupn.setText(groupName);

        // Init of the .xml file
        to_gr = (AppCompatButton) findViewById(R.id.back_to_groups);
        task_list = (ListView) findViewById(R.id.tasks_list);
        goals_list = (ListView) findViewById(R.id.goals_list);
        // Listeners
        to_gr.setOnClickListener(this);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference();

        Map<String, Object> data = new HashMap<>();
        data.put("groupId", GroupID);
        data.put("userId", userId);
        FirebaseFunctions.getInstance().getHttpsCallable("getUserTasksInGroup").call(data).addOnCompleteListener(new OnCompleteListener<HttpsCallableResult>() {
            @Override
            public void onComplete(@NonNull Task<HttpsCallableResult> task) {
                if (task.isSuccessful()) {
                    if (task.isComplete()) {
                        String userTasks = (String) task.getResult().getData();
                        HashMap<String, JsonNode> data = jsonListToHashMap(userTasks,'t');
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
                            task_list.setAdapter(task_adp);
                            task_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String taskID = taskId.get(i);
                                    ref.child("Users").child(userId).child("MyTasks").child(taskID).child("isComplete").setValue(true);
                                    ref.child("Groups").child(GroupID).child("Tasks").child(taskID).child("isComplete").setValue(true);
                                    // Update points
                                    int new_points = (curr_userPoints + Integer.parseInt(points.get(i)));
                                    ref.child("Users").child(userId).child("curr_points").setValue(new_points);
                                    Toast.makeText(Group_User_Activity.this, "Successfully completed task:" + tasks_names.get(i), Toast.LENGTH_SHORT).show();
                                    Intent user2user = new Intent(Group_User_Activity.this, Group_User_Activity.class);
                                    user2user.putExtra("ARGS", GroupID + "," + new_points);
                                    startActivity(user2user);
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(Group_User_Activity.this, "No tasks", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        FirebaseFunctions.getInstance().getHttpsCallable("getUserGoalsInGroup").call(data).addOnCompleteListener(new OnCompleteListener<HttpsCallableResult>() {
            @Override
            public void onComplete(@NonNull Task<HttpsCallableResult> task) {
                if (task.isSuccessful()) {
                    if (task.isComplete()) {

                        String userGoals = (String) task.getResult().getData();
                        HashMap<String, JsonNode> data = jsonListToHashMap(userGoals,'g');
                        System.out.println("*******************************************************");
                        System.out.println(data.toString());
                        System.out.println("*******************************************************");
                        try {
                            for (String GoalID : data.keySet()) {
                                String GoalName = data.get(GoalID).get("goalName").asText();
                                double value = Double.parseDouble(data.get(GoalID).get("goalVal").toString());
                                double curr = Double.parseDouble(data.get(GoalID).get("currPoints").toString());
                                double percent= (curr/value);
                                percent=percent*100;
                                goals_names.add(GoalName);
                                bars.add(new ProgressBar(getApplicationContext()));
                                goal_prog.add((int)percent);
                            }
                            goals_adp = new CustomAdapter(getApplicationContext(), goals_names, null, bars, 'g',goal_prog);
                            goals_list.setAdapter(goals_adp);

                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(Group_User_Activity.this, "No tasks", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    // Help function which takes a String representing Json file, and transform it to hashmap<String,JsonNode>
    public static HashMap<String, JsonNode> jsonListToHashMap(String jsonList,char classification) {
        HashMap<String, JsonNode> map = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonList);
            for (JsonNode node : jsonNode) {
                String key="";
                if(classification=='t'){key = node.get("taskId").asText();}
                else {key = node.get("goalId").asText();}
                map.put(key, node);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return map;
        }
        return map;
    }

    // Override the 'onClick' method, divided by button id
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_to_groups:
                Intent i = new Intent(this, My_Groups_Activity.class);
                i.putExtra("ID_name", GroupID + "," + groupName);
                startActivity(i);
                break;
            default:
                break;
        }
    }
}