package com.example.ezchores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Group_User_Activity extends AppCompatActivity implements View.OnClickListener {
    // Declaration of the .xml file
    Button to_gr;
    ListView task_list,goals_list;
    FloatingActionButton shop, group_info;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_user);

        String[] tasks={"task1","task2","task3"};
        String[] goals={"goal1","goal2","goal3"};
        String[] points ={"10","20","30"};
        ProgressBar[] bars={new ProgressBar(this),new ProgressBar(this), new ProgressBar(this)};
        CustomAdapter task_adp,goals_adp;

        // Init of the .xml file
        to_gr = (Button) findViewById(R.id.back_to_groups);
        shop = (FloatingActionButton) findViewById(R.id.shopping_list);
        group_info = (FloatingActionButton) findViewById(R.id.group_info);
        task_list=(ListView)findViewById(R.id.tasks_list);
        goals_list=(ListView)findViewById(R.id.goals_list);
        task_adp= new CustomAdapter(getApplicationContext(),tasks,points,null,'t');
        goals_adp= new CustomAdapter(getApplicationContext(),goals,null,bars,'g');
        task_list.setAdapter(task_adp);
        goals_list.setAdapter(goals_adp);

        // Listeners
        to_gr.setOnClickListener(this);
        shop.setOnClickListener(this);
        group_info.setOnClickListener(this);

        //pb_example(); // Example to fill progress bar using timer

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

            case R.id.group_info:
                Intent k = new Intent(this, Group_Info_Activity.class);
                startActivity(k);
        }
    }



}