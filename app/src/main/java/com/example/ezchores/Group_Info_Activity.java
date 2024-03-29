package com.example.ezchores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Group_Info_Activity extends AppCompatActivity implements View.OnClickListener {

    // Declaration of .xml file widgets
    AppCompatButton back, apply;
    ImageView group_photo;
    EditText new_name, mem_email;

    FloatingActionButton add_mem;
    TextView group_name;
    ListView memberList;
    CustomAdapter FriendsDisplayArr;
    int curr_userPoints;
    // DB
    DatabaseReference ref;
    // Display the friends in the group
    ArrayList<String> FriendsName = new ArrayList<>();
    ArrayList<String> KeyList = new ArrayList<>();
    private ArrayList<Integer> checkboxStates = new ArrayList<>();



    // handle change of pic initialisation
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_SELECT_PICTURE = 2;

    String groupID;
    String myUserID;
    String groupName;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_group_info);

        String args = (String) getIntent().getSerializableExtra("ARGS");
        System.out.println("GIA- CONTENT OF ARGS : "+ args);
        ref = FirebaseDatabase.getInstance().getReference();
        groupID = args.split(",")[0];
        group_name = (TextView) findViewById(R.id.group_name);
        curr_userPoints = Integer.parseInt(args.split(",")[1]);
        groupName=args.split(",")[2];
        group_name.setText(groupName);

        //Init widgets

        back = (AppCompatButton) findViewById(R.id.to_gr);
        apply = (AppCompatButton) findViewById(R.id.apply_group_changes);
        new_name = (EditText) findViewById(R.id.new_group_name_field);
        mem_email = (EditText) findViewById(R.id.new_member_mail);
        group_photo = (ImageView) findViewById(R.id.group_photo);
        add_mem = (FloatingActionButton) findViewById(R.id.add_member);
        memberList = (ListView) findViewById(R.id.view_memberAsList);
        memberList.setVerticalScrollBarEnabled(true);
        //Listeners
        back.setOnClickListener(this);
        apply.setOnClickListener(this);
        add_mem.setOnClickListener(this);
        myUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Object> data = new HashMap<>();
        data.put("groupId", groupID);
        FirebaseFunctions.getInstance().getHttpsCallable("getGroupInfo").call(data).addOnCompleteListener(new OnCompleteListener<HttpsCallableResult>() {
            @Override
            public void onComplete(@NonNull Task<HttpsCallableResult> task) {
                if (task.isSuccessful()) {
                    if (task.isComplete()) {
                        String groupInfo = (String) task.getResult().getData();
                        FriendsName = updatenlists(groupInfo, 'n');
                        KeyList = updatenlists(groupInfo, 'k');
                        for(int i=0;i<FriendsName.size();i++){checkboxStates.add(0);}

                        System.out.println("*************************************");
                        System.out.println("Friends names " + FriendsName);
                        System.out.println("Friends ID " + KeyList);
                        System.out.println("Group name " + groupName);
                        System.out.println("*************************************");


                        // init the DataBase reference
                        DatabaseReference refFriends = FirebaseDatabase
                                .getInstance()
                                .getReference("Groups")
                                .child(groupID)
                                .child("Friends");

                        DatabaseReference refImage = FirebaseDatabase
                                .getInstance()
                                .getReference()
                                .child("Groups")
                                .child(groupID)
                                .child("image");

                        FriendsDisplayArr = new CustomAdapter(
                                getApplicationContext(),
                                FriendsName,
                                null,
                                null,
                                'u',
                                checkboxStates
                        );

                        memberList.setAdapter(FriendsDisplayArr);



                        refImage.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String imageString = snapshot.getValue(String.class);
                                if (imageString == null) {
                                    System.out.println("imageString is null");

                                } else {
                                    byte[] data = Base64.decode(imageString, Base64.DEFAULT);
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    group_photo.setImageBitmap(bitmap);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                System.out.println(error);
                            }
                        });

                        group_photo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Group_Info_Activity.this);

                                builder.setTitle("Add a Photo")
                                        .setItems(new CharSequence[]{"Use the Camera", "Import from Gallery"}, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int choice) {
                                                switch (choice) {
                                                    case 0:
                                                        takePicture();
                                                        break;
                                                    case 1:
                                                        selectPicture();
                                                        break;
                                                }
                                            }
                                        });
                                builder.create().show();
                            }
                        });
                    }
                }
            }
        });
    }


    public static ArrayList<String> updatenlists(String jsonString, char c) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<String> ret = new ArrayList<String>();
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            System.out.println(jsonNode);
            String gname=jsonNode.get("groupName").toString();
            String[] users = jsonNode.get("userList").toString().split(",");
            if (c == 'n') {
                for (int i = 0; i < users.length; i++) {
                    String memberName = users[i].split(":")[1];
                    memberName = memberName.split("\"")[1];
                    ret.add(memberName);
                }
            } else {
                for (int i = 0; i < users.length; i++) {
                    String MemID = users[i].split(":")[0];
                    MemID = MemID.split("\"")[1];
                    ret.add(MemID);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
              

    // Override the 'onClick' method, divided by button id
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.to_gr:
                Intent myGroups = new Intent(this, Group_Admin_Activity.class);
                myGroups.putExtra("ARGS", groupID + "," + curr_userPoints+ "," + groupName );
                startActivity(myGroups);
                break;

            case R.id.apply_group_changes:
                // Needs to save the new group info [name, icon
                Intent i = new Intent(this, Group_Info_Activity.class);

                String N = new_name.getText().toString();
                if (!N.equals("")) {
                    updateName(N);
                    i.putExtra("ARGS", groupID + "," + curr_userPoints+","+N);
                } else {
                    i.putExtra("ARGS", groupID + "," +curr_userPoints+","+groupName);
                }
                // there was adding into the remove list
                if (!isFilledZero(FriendsDisplayArr.getCheckBox())) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
                            // Code for creating and showing the AlertDialog
                            AlertDialog.Builder builder = new AlertDialog.Builder(Group_Info_Activity.this);
                            builder.setTitle("Remove");
                            builder.setMessage("you have selected users to delete from the group , are you sure to remove them ?");
                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    DatabaseReference refFriends = FirebaseDatabase
                                            .getInstance()
                                            .getReference("Groups")
                                            .child(groupID)
                                            .child("Friends");
                                    for (int j = 0; j < FriendsDisplayArr.getCheckBox().length ; j++) {
                                        if (FriendsDisplayArr.getCheckBox()[j] == 1){
                                            // if we want to remove the current user (admin)
                                            if (myUserID.equals(KeyList.get(j))){
                                                Toast.makeText(Group_Info_Activity.this,"You try to remove an administrator, this cannot be done",Toast.LENGTH_SHORT ).show();
                                            }else {
                                                refFriends.child(KeyList.get(j)).removeValue();
                                                System.out.println("the user in place " + j + "was removed ");
                                            }
                                        }
                                    }
                                    startActivity(i);

                                }
                            });
                            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    System.out.println("The user choose to not remove the specified users");
                                    startActivity(i);
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
//                        }
//                    });
                }
//                startActivity(i);
                break;

            case R.id.add_member:
                String new_mem_email = mem_email.getText().toString();
                addFriend(new_mem_email);
                break;

            default:
                break;
        }

    }

    private void updateName(String newname) {
        group_name.setText(newname);
        ref.child("Groups").child(groupID).child("name").setValue(newname);
        ref.child("Groups").child(groupID).child("Friends").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> list = snapshot.getValue(new GenericTypeIndicator<HashMap<String, Object>>() {
                });

                for (String friendId : list.keySet()) {
                    ref.child("Users").child(friendId).child("Groups").child(groupID).setValue(newname);
                    Toast.makeText(Group_Info_Activity.this, "Changing name succeed", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Group_Info_Activity.this, "something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Update the ListView with the changed data

    private void updateUI() {
        Intent intent = new Intent(this, Group_Info_Activity.class);
        intent.putExtra("ARGS", groupID + "," + curr_userPoints +"," + groupName);
        startActivity(intent);
    }


    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        } catch (ActivityNotFoundException e) {
            System.out.println(e);
        }
    }

    private void selectPicture() {
        Intent selectPicture = new Intent();
        selectPicture.setType("image/*");
        selectPicture.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(selectPicture, "Select Picture"), REQUEST_SELECT_PICTURE);
    }

    private Bitmap getImageFromImageView() {
        // Get the drawable of the image view
        Drawable drawable = group_photo.getDrawable();
        // Convert the drawable to a bitmap
        return ((BitmapDrawable)drawable).getBitmap();
    }

    private void storeImage(String UserId, Bitmap image) {
        DatabaseReference GroupKey = ref.child("Groups").child(groupID);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        String ImageString = Base64.encodeToString(data, Base64.DEFAULT);
        GroupKey.child("image")
                .setValue(ImageString)
                .addOnSuccessListener(unused -> Toast.makeText(Group_Info_Activity.this
                        , "Image Stored"
                        , Toast.LENGTH_SHORT).show());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                Bundle extras = data.getExtras();
                Bitmap bitmap = (Bitmap) extras.get("data");
                group_photo.setImageBitmap(bitmap);
                storeImage(groupID, getImageFromImageView());
            }
            else if (resultCode == REQUEST_SELECT_PICTURE){
                Uri selectedImage = data.getData();
                if (selectedImage != null){
                    group_photo.setImageURI(selectedImage);
                }
            }
        }
    }

    private boolean isFilledZero(int[] arr) {
        for (int j : arr) {
            if (j == 1) {
                return false;
            }
        }
        return true;
    }

    private void addFriend(String mail) {
        ref.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> list = snapshot.getValue(new GenericTypeIndicator<HashMap<String, Object>>() {
                });
                try {
                    boolean found = false;
                    for (String userId : list.keySet()) {
                        String checkMail = snapshot.child(userId).child("email").getValue().toString();
                        if (checkMail.equals(mail)) {
                            String name = snapshot.child(userId).child("name").getValue().toString();

                            groupName = snapshot.child(myUserID).child("Groups").child(groupID).getValue().toString();
                            ref.child("Groups").child(groupID).child("Friends").child(userId).setValue(name);
                            ref.child("Users").child(userId).child("Groups").child(groupID).setValue(groupName);
                            Toast.makeText(Group_Info_Activity.this, mail + " was added to the group", Toast.LENGTH_SHORT).show();
                            found = true;
                            updateUI();
                            break;
                        }
                    }
                    if (!found) {
                        Toast.makeText(Group_Info_Activity.this, "Member doesn't exist :(", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    System.out.println(e);
                    Toast.makeText(Group_Info_Activity.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Group_Info_Activity.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();

            }
        });

    }
}