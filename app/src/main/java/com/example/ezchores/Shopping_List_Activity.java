package com.example.ezchores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Shopping_List_Activity extends AppCompatActivity implements View.OnClickListener {
    private Button to_user_screen;
    private FloatingActionButton pending_items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        to_user_screen=findViewById(R.id.back_to_user_screen);
        pending_items=(FloatingActionButton)findViewById(R.id.pending_items);
        to_user_screen.setOnClickListener(this);
        pending_items.setOnClickListener(this);
    }

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
            default:
                break;
        }
    }
}