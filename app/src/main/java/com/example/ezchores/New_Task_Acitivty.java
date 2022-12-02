package com.example.ezchores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class New_Task_Acitivty extends AppCompatActivity implements View.OnClickListener {
    Button back,create;
    FloatingActionButton icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task_acitivty);
        back=(Button) findViewById(R.id.back_to_groups);
        create=(Button) findViewById(R.id.add_goal);
        icon=(FloatingActionButton) findViewById(R.id.task_icn);
        back.setOnClickListener(this);
        create.setOnClickListener(this);
        icon.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.back_to_groups:
                Intent BTGR = new Intent(this,My_Groups_Activity.class);
                startActivity(BTGR);
                break;

            case R.id.add_goal:
                Intent CRG = new Intent(this,Group_Admin_Activity.class);
                startActivity(CRG);
                break;
            default:
                break;
        }

    }
}