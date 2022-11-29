package com.example.ezchores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Pending_Items_Activity extends AppCompatActivity {
    private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_items);
        back=(Button) findViewById(R.id.back_to_shoplst);
        back.setOnClickListener(new View.OnClickListener(){@Override public void onClick(View view){TOSL();}});
    }

    public void TOSL(){Intent TSL= new Intent(this,Shopping_List_Activity.class); startActivity(TSL);}
}