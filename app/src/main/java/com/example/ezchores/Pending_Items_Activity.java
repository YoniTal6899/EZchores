package com.example.ezchores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Pending_Items_Activity extends AppCompatActivity implements View.OnClickListener {
    //Buttons
    private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_items);
        //Init buttons
        back=(Button) findViewById(R.id.back_to_shoplst);

        //Listeners
        back.setOnClickListener(this);
    }

    // Override the 'onClick' method, divided by button id
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.back_to_shoplst:
                finish();
                break;

            default:
                break;
        }
    }
}