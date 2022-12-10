package com.example.ezchores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.auth.api.identity.GetSignInIntentRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Timer;
import java.util.TimerTask;

public class Group_User_Activity extends AppCompatActivity implements View.OnClickListener {
    // Declaration of the .xml file
    Button to_gr;
    ListView task_list, goals_list;
    FloatingActionButton shop;
    int counter = 0;
    String groupId;
    String groupName;
    TextView groupn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_user);
        String id_name = (String) getIntent().getSerializableExtra("ID_name");
        groupName = id_name.split(",")[1];
        groupId = id_name.split(",")[0];
        groupn = (TextView) findViewById(R.id.group_name);
        groupn.setText(groupName);



        String[] tasks = {"task1", "task2", "task3"};
        String[] goals = {"goal1", "goal2", "goal3"};
        String[] points = {"10", "20", "30"};
        ProgressBar[] bars = {new ProgressBar(this), new ProgressBar(this), new ProgressBar(this)};
        CustomAdapter task_adp, goals_adp;

        // Init of the .xml file
        to_gr = (Button) findViewById(R.id.back_to_groups);
        shop = (FloatingActionButton) findViewById(R.id.shopping_list);
        task_list = (ListView) findViewById(R.id.tasks_list);
        goals_list = (ListView) findViewById(R.id.goals_list);
        task_adp = new CustomAdapter(getApplicationContext(), tasks, points, null, 't');
        goals_adp = new CustomAdapter(getApplicationContext(), goals, null, bars, 'g');
        task_list.setAdapter(task_adp);
        goals_list.setAdapter(goals_adp);

        // Listeners
        to_gr.setOnClickListener(this);
        shop.setOnClickListener(this);

    }

    // Override the 'onClick' method, divided by button id
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_to_groups:
                Intent i = new Intent(this, My_Groups_Activity.class);
                startActivity(i);
                break;

            case R.id.shopping_list:
                Intent j = new Intent(this, Shopping_List_Activity.class);
                startActivity(j);
                break;
        }
    }


}