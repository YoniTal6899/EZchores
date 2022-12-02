package com.example.ezchores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Group_User_Activity extends AppCompatActivity {
    private Button to_gr;
    FloatingActionButton shop,group_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_user);
        to_gr=(Button) findViewById(R.id.back_to_groups);
        shop=(FloatingActionButton) findViewById(R.id.shopping_list);
        group_info=(FloatingActionButton)findViewById(R.id.group_info);
        to_gr.setOnClickListener(new View.OnClickListener(){@Override public void onClick(View view) {TOGROUPS();}});
        shop.setOnClickListener(new View.OnClickListener(){@Override public void onClick(View view) {TOSHOPLST();}});
        group_info.setOnClickListener(new View.OnClickListener(){@Override public void onClick(View view) {TOGRINFO();}});
    }

    public void TOGROUPS(){Intent TGR = new Intent(this,My_Groups_Activity.class); startActivity(TGR);}
    public void TOSHOPLST(){Intent TSL = new Intent(this,Shopping_List_Activity.class); startActivity(TSL);}
    public void TOGRINFO(){Intent TGRI = new Intent(this,Group_Info_Activity.class); startActivity(TGRI);}
}