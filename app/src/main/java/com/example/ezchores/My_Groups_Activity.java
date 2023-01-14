package com.example.ezchores;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.ArrayList;
import android.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class My_Groups_Activity extends Activity implements View.OnClickListener {

    // Buttons & TextViews
    AppCompatButton add_group, personal_info;
    private FirebaseAuth firebaseAuth;
    ListView listview;
    String UserId;
    int current_points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);
        // Init variables
        add_group = (AppCompatButton) findViewById(R.id.add_group);
        personal_info = (AppCompatButton) findViewById(R.id.personal_info);
        firebaseAuth = FirebaseAuth.getInstance();
        listview = findViewById(R.id.listview);
        UserId = firebaseAuth.getCurrentUser().getUid();
        List<String> listGroupid = new ArrayList<>();
        List<String> listGroupname = new ArrayList<>();
        // Listeners
        add_group.setOnClickListener((View.OnClickListener) this);
        personal_info.setOnClickListener((View.OnClickListener) this);
        // Args to Firebase function
        Map<String, Object> data = new HashMap<>();
        data.put("userId", UserId);
        //call Firebase "getUserGroups(UserID)" function in order to get all the user's groups relevant info (ID,name,IsAdmin)
        FirebaseFunctions.getInstance().getHttpsCallable("getUserGroups").call(data).addOnCompleteListener(new OnCompleteListener<HttpsCallableResult>() {
            @Override
            public void onComplete(@NonNull Task<HttpsCallableResult> task) {
                if (task.isSuccessful()) {
                    if (task.isComplete()) {
                        String userGroups = (String) task.getResult().getData();
                        HashMap<String,JsonNode> data=jsonListToHashMap(userGroups);
                        try {
                            for (String name : data.keySet()) {
                                String key = name.toString();
                                String value = data.get(key).get("groupName").toString();
                                listGroupid.add(key);
                                listGroupname.add(value.substring(1,value.length()-1));
                            }
                            ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, listGroupname);
                            listview.setAdapter(arrayAdapter);
                        }
                        catch (Exception e) {
                            System.out.println("error " + e);
                        }
                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                                Toast.makeText(My_Groups_Activity.this, "you click group name:" + listGroupname.get(position), Toast.LENGTH_SHORT).show();
                                String GroupId= listGroupid.get(position);
                                String isAdmin= data.get(GroupId).get("isAdmin").toString();

                                if(isAdmin.equals("true")){
                                    Intent groups2admin = new Intent(My_Groups_Activity.this, Group_Admin_Activity.class);
                                    groups2admin.putExtra("ARGS", listGroupid.get(position)+","+current_points+","+listGroupname.get(position));
                                    Toast.makeText(My_Groups_Activity.this, "admin permission", Toast.LENGTH_SHORT).show();
                                    startActivity(groups2admin);
                                }
                                else{
                                    Intent groups2user = new Intent(My_Groups_Activity.this, Group_User_Activity.class);
                                    groups2user.putExtra("ARGS", listGroupid.get(position)+","+current_points+","+listGroupname.get(position));
                                    Toast.makeText(My_Groups_Activity.this, "user permission", Toast.LENGTH_SHORT).show();
                                    startActivity(groups2user);
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    // Help function which takes a String representing Json file, and transform it to hashmap<String,JsonNode>
    public static HashMap<String, JsonNode> jsonListToHashMap(String jsonList) {
        HashMap<String, JsonNode> map = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonList);
            for (JsonNode node : jsonNode) {
                String key = node.get("groupId").asText();
                map.put(key, node);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return map;
        }
        return map;
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
                startActivity(j);
                break;

            default:
                break;
        }

    }

}
