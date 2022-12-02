package com.example.ezchores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Group_Info_Activity extends AppCompatActivity implements View.OnClickListener {
    private Button back,apply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        back=(Button)findViewById(R.id.to_gr);
        apply=(Button)findViewById(R.id.apply_group_changes);
        back.setOnClickListener(this);
        apply.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.to_gr:
                Intent BTGR = new Intent(this,Group_User_Activity.class);
                startActivity(BTGR);
                break;

            case R.id.apply_group_changes:
                Intent CGRC = new Intent(this,Group_Admin_Activity.class);
                startActivity(CGRC);
                break;
            default:
                break;
        }

    }
    public void TGRS(){Intent TGS = new Intent(this,My_Groups_Activity.class); startActivity(TGS);}
}