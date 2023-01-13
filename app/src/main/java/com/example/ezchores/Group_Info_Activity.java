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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class Group_Info_Activity extends AppCompatActivity implements View.OnClickListener {

    // Declaration of .xml file widgets
    AppCompatButton back, apply;
    ImageView group_photo;
    EditText new_name, mem_email;
    FloatingActionButton  add_mem;
    TextView group_name;
    ListView memberList;
    CustomAdapter FriendsDisplayArr;

    // DB
    private DatabaseReference ref;

    // handle change of pic initialisation
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_SELECT_PICTURE = 2;

    // parameters
    private String groupID, myUserID,groupName;
    private ArrayList<String> FriendsName = new ArrayList<>();
    private ArrayList<String> KeyList = new ArrayList<>();
    private ArrayList<Integer> checkboxStates = new ArrayList<>();




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_group_info);

        String id_name = (String) getIntent().getSerializableExtra("ID_name");
        groupName=id_name.split(",")[1];
        ref = FirebaseDatabase.getInstance().getReference();
        groupID = id_name.split(",")[0];
        group_name=(TextView) findViewById(R.id.group_name);
//        checkBox = (CheckBox) findViewById(R.id.);
        //Init widgets

        back = (AppCompatButton) findViewById(R.id.to_gr);
        apply = (AppCompatButton) findViewById(R.id.apply_group_changes);
        new_name = (EditText) findViewById(R.id.new_group_name_field);
        mem_email = (EditText) findViewById(R.id.new_member_mail);
        group_photo = (ImageView) findViewById(R.id.group_photo);
        add_mem= (FloatingActionButton) findViewById(R.id.add_member);
        memberList = (ListView) findViewById(R.id.view_memberAsList);
        //Listeners
        back.setOnClickListener(this);
        apply.setOnClickListener(this);
        add_mem.setOnClickListener(this);
        group_name.setText(groupName);
        myUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();


// DB references
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

        refFriends.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                HashMap<String , Object> friendMap = snapshot
                        .getValue(new GenericTypeIndicator<HashMap<String, Object>>() {
                        });
                try {
                    for (String UID : friendMap.keySet()){
                        String key = UID.toString();
                        String name = friendMap.get(UID).toString();
                        System.out.println("TEST ->" + name );
                        FriendsName.add(name);
                        checkboxStates.add(0);
                        KeyList.add(UID);

                    }
                    FriendsDisplayArr = new CustomAdapter(
                            getApplicationContext(),
                            FriendsName,
                            null,
                            null,
                            'u',
                            checkboxStates
                    );


                    memberList.setAdapter(FriendsDisplayArr);
//
                } catch (Exception e) {
                    System.out.println("error :"+ e );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        refImage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String imageString = snapshot.getValue(String.class);
                if (imageString == null){
                    System.out.println("imageString is null");

                }else {
                    byte[] data = Base64.decode(imageString, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    group_photo.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println( error);
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

    // Override the 'onClick' method, divided by button id
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.to_gr:
                Intent myGroups = new Intent(this, My_Groups_Activity.class);
                myGroups.putExtra("ID_name",groupID+"," + groupName);
                startActivity(myGroups);
                break;

            case R.id.apply_group_changes:
                // Needs to save the new group info [name, icon
                String N=new_name.getText().toString();
                Intent i = new Intent(this, Group_Admin_Activity.class);
                if(!N.equals("")) {
                    updateName(N);
                    i.putExtra("ID_name",groupID+"," + N);
                }else{
                    i.putExtra("ID_name",groupID+"," + groupName);
//                    i.putExtra("group_name",groupName);
                    Toast.makeText(Group_Info_Activity.this, "on click Something went wrong :(", Toast.LENGTH_SHORT).show();
                }
                // there was adding into the remove list
                if (!isFilledZero(FriendsDisplayArr.getCheckBox())) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
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
                                    updateUI();
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
                        }
                    });
                }
                break;

            case R.id.add_member:
                String new_mem_email= mem_email.getText().toString();
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
                HashMap<String,Object> list = snapshot.getValue(new GenericTypeIndicator<HashMap<String, Object>>() {});

                for(String friendId: list.keySet()){
                    ref.child("Users").child(friendId).child("Groups").child(groupID).setValue(newname);
                    Toast.makeText(Group_Info_Activity.this, "group name has changed...", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Group_Info_Activity.this, "something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Update the ListView with the changed data

    private void updateUI(){
        Intent intent = new Intent(this,Group_Info_Activity.class);
        intent.putExtra("ID_name", groupID+","+groupName);
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
                HashMap<String,Object> list = snapshot.getValue(new GenericTypeIndicator<HashMap<String, Object>>() {});
                try {
                    boolean found = false;
                    for (String userId : list.keySet() ){
                        String checkMail = snapshot.child(userId).child("email").getValue().toString();
                        if (checkMail.equals( mail)){
                            String name = snapshot.child(userId).child("name").getValue().toString();

                            groupName = snapshot.child(myUserID).child("Groups").child(groupID).getValue().toString();
                            ref.child("Groups").child(groupID).child("Friends").child(userId).setValue(name);
                            ref.child("Users").child(userId).child("Groups").child(groupID).setValue(groupName);
                            Toast.makeText(Group_Info_Activity.this, mail+" was added to the group", Toast.LENGTH_SHORT).show();
                            found = true;
                            updateUI();
                            break;
                        }
                    }
                    if(!found){
                        Toast.makeText(Group_Info_Activity.this, "Member doesn't exist :(", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e){
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