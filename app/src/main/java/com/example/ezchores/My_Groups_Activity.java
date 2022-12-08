package com.example.ezchores;


import androidx.annotation.NonNull;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class My_Groups_Activity extends Activity implements View.OnClickListener  {

    // Buttons & TextViews
    Button add_group, personal_info;
    private TextView gr_1, gr_2;
  private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);

        // Init Buttons & TextViews

        add_group = (Button) findViewById(R.id.add_group);
        personal_info = (Button) findViewById(R.id.personal_info);
//        gr_1 = (TextView) findViewById(R.id.group_1);
//        gr_2 = (TextView) findViewById(R.id.group_2);

        // Listeners
//        gr_1.setOnTouchListener(this);
//        gr_2.setOnTouchListener(this);
        add_group.setOnClickListener((View.OnClickListener) this);
        personal_info.setOnClickListener((View.OnClickListener) this);
        firebaseAuth = FirebaseAuth.getInstance();
        ListView listview = findViewById(R.id.listview);
        String UserId = firebaseAuth.getCurrentUser().getUid();

        List<String> listGroupid = new ArrayList<>();
        List<String> listGroupname = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference refUSer = FirebaseDatabase.getInstance().getReference("Users").child(UserId).child("Groups");
        refUSer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                HashMap<String, Object> list = snapshot.getValue(new GenericTypeIndicator<HashMap<String, Object>>() {
                });
                for (String name : list.keySet()) {
                    String key = name.toString();
                    String value = list.get(key).toString();

                    listGroupid.add(value);

                }
                ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, listGroupid);
                listview.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        DatabaseReference refgroup = FirebaseDatabase.getInstance().getReference("Groups");
//        refgroup.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                HashMap<String, Object> list = snapshot.getValue(new GenericTypeIndicator<HashMap<String, Object>>() {
//                });
//                for (String name : list.keySet()) {
//                    String key = name.toString();
//                    if (listGroupid.contains(key)) {
//                        listGroupname.add(snapshot.child(key).child("name").getValue().toString());
//                    }
//
//                }
//                ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, listGroupname);
//                listview.setAdapter(arrayAdapter);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

//

        // Override the 'onClick' method, divided by button id


        // Override the 'onTouch' method, divided by TextView id
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
////        switch (v.getId()) {
////            case R.id.group_1:
////                Intent GR1U = new Intent(this, Group_User_Activity.class);
////                startActivity(GR1U);
////                break;
////
////            case R.id.group_2:
////                Intent GR2A = new Intent(this, Group_Admin_Activity.class);
////                startActivity(GR2A);
////                break;
////        }
//        return true;
//    }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_group:
                Intent i = new Intent(this, Create_Group_Activity.class);
                startActivity(i);
                break;

            case R.id.personal_info:
                Intent j = new Intent(this, Personal_Info_Activity.class);
                startActivity(j);
                break;

            default:
                break;
        }
    }
}