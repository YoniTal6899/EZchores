package com.example.ezchores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Group_Admin_Activity extends AppCompatActivity implements View.OnClickListener {
    // Declaration of .xml widgets
    Button back_to_groups;
    FloatingActionButton shop,group_info, add_goal, add_task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_admin);

        // Init buttons
        back_to_groups=(Button)findViewById(R.id.back_to_groups);
        shop=(FloatingActionButton) findViewById(R.id.shopping_list);
        group_info=(FloatingActionButton)findViewById(R.id.group_info);
        add_goal=(FloatingActionButton)findViewById(R.id.new_goal);
        add_task=(FloatingActionButton)findViewById(R.id.new_task);

        // Listeners
        back_to_groups.setOnClickListener(this);
        shop.setOnClickListener(this);
        group_info.setOnClickListener(this);
        add_task.setOnClickListener(this);
        add_goal.setOnClickListener(this);
    }

    // Override the 'onClick' method, divided by button id
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.back_to_groups:
                Intent i = new Intent(this,My_Groups_Activity.class);
                startActivity(i);
                break;

            case R.id.shopping_list:
                Intent j = new Intent(this,Shopping_List_Activity.class);
                startActivity(j);
                break;

            case R.id.group_info:
                Intent k = new Intent(this,Group_Info_Activity.class);
                startActivity(k);
                break;

            case R.id.new_goal:
                Intent r = new Intent(this,New_Goal_Activity.class);
                startActivity(r);
                break;

            case R.id.new_task:
                Intent m = new Intent(this,New_Task_Acitivty.class);
                startActivity(m);
                break;

            default:
                break;
        }
    }
}