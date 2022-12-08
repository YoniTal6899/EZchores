package com.example.ezchores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Timer;
import java.util.TimerTask;

public class Group_User_Activity extends AppCompatActivity implements View.OnClickListener {
    // Declaration of the .xml file
    Button to_gr;
    FloatingActionButton shop, group_info;
    ProgressBar pb;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_user);

        // Init of the .xml file
        to_gr = (Button) findViewById(R.id.back_to_groups);
        shop = (FloatingActionButton) findViewById(R.id.shopping_list);
        group_info = (FloatingActionButton) findViewById(R.id.group_info);
        pb = (ProgressBar) findViewById(R.id.goal_1);

        // Listeners
        to_gr.setOnClickListener(this);
        shop.setOnClickListener(this);
        group_info.setOnClickListener(this);

        pb_example(); // Example to fill progress bar using timer

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

    public void pb_example() {
        Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                counter++;
                pb.setProgress(counter);
                if (counter == 5000) {
                    t.cancel();
                }
            }
        };
        t.schedule(tt, 100, 100);
    }

}