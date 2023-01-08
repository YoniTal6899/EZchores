package com.example.ezchores;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Shopping_List_Activity extends AppCompatActivity implements View.OnClickListener {

    // Declaration of .xml file widgets
    private AppCompatButton to_user_screen,confirm;
    EditText add_item;
    private FloatingActionButton pending_items, submit_add_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        //Init widgets
        to_user_screen=findViewById(R.id.back_to_user_screen);
        pending_items=(FloatingActionButton)findViewById(R.id.pending_items);
        confirm=(AppCompatButton) findViewById(R.id.confirm);
        add_item = (EditText) findViewById(R.id.add_item);
        submit_add_item = (FloatingActionButton) findViewById(R.id.submit_add_item);

        //Listeners
        to_user_screen.setOnClickListener(this);
        pending_items.setOnClickListener(this);
        confirm.setOnClickListener(this);
        submit_add_item.setOnClickListener(this);
    }

    // Override the 'onClick' method, divided by button id
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.back_to_user_screen:
                Intent BTUS = new Intent(this,Group_User_Activity.class);
                startActivity(BTUS);
                break;

            case R.id.pending_items:
                Intent PENIT = new Intent(this,Pending_Items_Activity.class);
                startActivity(PENIT);
                break;

            case R.id.confirm:
                // Needs to delete the checked items, and refresh the page
                break;

            case R.id.submit_add_item:
                // Need to take text from EditText add_item and add to shopping list

                break;
            default:
                break;
        }
    }
}