package com.example.ezchores;


import androidx.annotation.NonNull;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
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

public class My_Groups_Activity extends Activity implements View.OnClickListener {

    // Buttons & TextViews
    Button add_group, personal_info;
    private FirebaseAuth firebaseAuth;
    ListView listview;
    String UserId,userName,current_points,mail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);


        add_group = (Button) findViewById(R.id.add_group);
        personal_info = (Button) findViewById(R.id.personal_info);

        add_group.setOnClickListener((View.OnClickListener) this);
        personal_info.setOnClickListener((View.OnClickListener) this);
        firebaseAuth = FirebaseAuth.getInstance();
        listview = findViewById(R.id.listview);
        UserId = firebaseAuth.getCurrentUser().getUid();




        List<String> listGroupid = new ArrayList<>();
        List<String> listGroupname = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Users").child(UserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userName = snapshot.child("name").getValue().toString();
                current_points = snapshot.child("curr_points").getValue().toString();
                mail = snapshot.child("email").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        DatabaseReference refUSer = FirebaseDatabase.getInstance().getReference("Users").child(UserId).child("Groups");
        refUSer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                HashMap<String, Object> list = snapshot.getValue(new GenericTypeIndicator<HashMap<String, Object>>() {
                });
                try {
                    for (String name : list.keySet()) {
                        String key = name.toString();
                        String value = list.get(key).toString();
                        listGroupid.add(key);
                        listGroupname.add(value);

                    }
                    ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, listGroupname);
                    listview.setAdapter(arrayAdapter);
                } catch (Exception e) {
                    System.out.println("error " + e);
                }

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        Toast.makeText(My_Groups_Activity.this, "you click group name:" + listGroupname.get(position), Toast.LENGTH_SHORT).show();
                        String UserId = firebaseAuth.getCurrentUser().getUid();

                        DatabaseReference refGroup = FirebaseDatabase.getInstance().getReference("Groups").child(listGroupid.get(position)).child("admins");
                        refGroup.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                HashMap<String, Object> admins = snapshot.getValue(new GenericTypeIndicator<HashMap<String, Object>>() {
                                });
                                System.out.println("Userid: " + UserId);
                                System.out.println("admins: " + snapshot);
                                if (!admins.containsKey(UserId)) {
                                    Intent groups2user = new Intent(My_Groups_Activity.this, Group_User_Activity.class);
                                    groups2user.putExtra("ID_name", listGroupid.get(position)+","+listGroupname.get(position));
                                    Toast.makeText(My_Groups_Activity.this, "user permission", Toast.LENGTH_SHORT).show();
                                    startActivity(groups2user);
                                } else {

                                    Intent groups2admin = new Intent(My_Groups_Activity.this, Group_Admin_Activity.class);
                                    groups2admin.putExtra("ID_name", listGroupid.get(position)+","+listGroupname.get(position));
                                    Toast.makeText(My_Groups_Activity.this, "admin permission", Toast.LENGTH_SHORT).show();
                                    startActivity(groups2admin);
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(My_Groups_Activity.this, "something went wrong try again...   ):", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(My_Groups_Activity.this, My_Groups_Activity.class));
                            }
                        });
                    }
                });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_group:
                Intent i = new Intent(this, Create_Group_Activity.class);
                startActivity(i);
                break;

            case R.id.personal_info:
                Intent j = new Intent(this, Personal_Info_Activity.class);;
                j.putExtra("Uid_name_email_currpoints",UserId+","+userName+","+mail+","+current_points);
                startActivity(j);
                break;

            default:
                break;
        }

    }
}
