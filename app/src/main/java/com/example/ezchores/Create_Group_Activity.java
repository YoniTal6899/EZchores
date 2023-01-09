package com.example.ezchores;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

public class Create_Group_Activity extends AppCompatActivity implements View.OnClickListener {

    // Buttons & EditTexts
    ImageView group_photo;
    AppCompatButton create_group;
    FloatingActionButton  add_member;
    EditText group_name, email;
    String username, groupkey,UserId;

// handle change of pic initialisation
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_SELECT_PICTURE = 2;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        // Init Buttons & EditTexts
        create_group = (AppCompatButton) findViewById(R.id.create_group_btn);
        group_photo = (ImageView) findViewById(R.id.group_photo);
        group_name = (EditText) findViewById(R.id.group_name);


        // Listeners
        create_group.setOnClickListener(this);
        group_photo.setOnClickListener(this);



        firebaseAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();

        UserId = firebaseAuth.getCurrentUser().getUid();
        ref.child("Users").child(UserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    username = snapshot.child("name").getValue().toString();
                } catch (Exception e) {
                    Toast.makeText(Create_Group_Activity.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        group_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               AlertDialog.Builder builder = new AlertDialog.Builder(Create_Group_Activity.this);
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

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }catch (ActivityNotFoundException e){
            System.out.println(e);
        }
    }


    private void selectPicture(){
        Intent selectPicture = new Intent();
        selectPicture.setType("image/*");
        selectPicture.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(selectPicture,"Select Picture"), REQUEST_SELECT_PICTURE);
    }

    private Bitmap getImageFromImageView() {
        // Get the drawable of the image view
        Drawable drawable = group_photo.getDrawable();
        // Convert the drawable to a bitmap
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        return bitmap;
    }
// Method to upload the choosen image to the database
   private void storeImage(String groupkey, Bitmap image){
        DatabaseReference groupKey = ref.child("Groups").child(groupkey);
       ByteArrayOutputStream baos = new ByteArrayOutputStream();
       image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
       byte[] data = baos.toByteArray();
       String ImageString = Base64.encodeToString(data, Base64.DEFAULT);
       groupKey.child("image").setValue(ImageString).addOnSuccessListener(new OnSuccessListener<Void>() {
           @Override
           public void onSuccess(Void unused) {
              Toast.makeText(Create_Group_Activity.this, "Image Stored", Toast.LENGTH_SHORT).show();
           }
       });
   }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                Bundle extras = data.getExtras();
                Bitmap bitmap = (Bitmap) extras.get("data");
                group_photo.setImageBitmap(bitmap);
            }
            else if (resultCode == REQUEST_SELECT_PICTURE){
                Uri selectedImage = data.getData();
                if (selectedImage != null){
                    group_photo.setImageURI(selectedImage);
                }
            }
        }
    }





    // Override the 'onClick' method, divided by button id
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_group_btn:
                // Needs to save the new group properties [icon, name]
                CreateGroup();
                Intent i = new Intent(this, My_Groups_Activity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }

    public void CreateGroup() {
        String group_n = group_name.getText().toString();
        if (TextUtils.isEmpty(group_n)) {
            Toast.makeText(this, "Please enter group name...", Toast.LENGTH_SHORT).show();
            return;
        }
        Group group = new Group(group_n);
        groupkey = ref.child("Users").child(UserId).child("Groups").push().getKey();//
        ref.child("Users").child(UserId).child("Groups").child(groupkey).setValue(group.getName());//push to user -> groups->  key of new group
        ref.child("Groups").child(groupkey).setValue(group);// groups-> new group key->set data
        storeImage(groupkey, getImageFromImageView());
        ref.child("Groups").child(groupkey).child("admins").child(UserId).setValue(username);
        ref.child("Groups").child(groupkey).child("Friends").child(UserId).setValue(username);
    }






}