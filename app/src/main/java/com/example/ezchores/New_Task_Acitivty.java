package com.example.ezchores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class New_Task_Acitivty extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    // Declaration of .xml file widgets
    EditText name, val, comments;
    Button back, create;
    FloatingActionButton icon;
    Spinner s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task_acitivty);

        // Init .xml widgets
        back = (Button) findViewById(R.id.back_to_groups);
        create = (Button) findViewById(R.id.add_goal);
        icon = (FloatingActionButton) findViewById(R.id.task_icn);
        name = (EditText) findViewById(R.id.Task_name);
        val = (EditText) findViewById(R.id.Task_val);
        comments = (EditText) findViewById(R.id.comments);
        s=(Spinner)findViewById(R.id.members_assign);

        // Listeners
        back.setOnClickListener(this);
        create.setOnClickListener(this);
        icon.setOnClickListener(this);

        DropDown(); // DropDown example

    }

    // Override the 'onClick' method, divided by button id
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_to_groups:
                Intent BTGR = new Intent(this, My_Groups_Activity.class);
                startActivity(BTGR);
                break;

            case R.id.add_goal:
                // Needs to save into the DB the values in the EditText's [name, val, comments]
                Intent CRG = new Intent(this, Group_Admin_Activity.class);
                startActivity(CRG);
                break;

            case R.id.task_icn:
                // Needs to open logo options
                break;

            default:
                break;
        }

    }

    private void DropDown(){
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this,R.array.task_assignee, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text=adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(),text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}