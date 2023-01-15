package com.example.ezchores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Personal_Info_Activity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    // Buttons
    AppCompatButton log_out;
    // Firebase
    private FirebaseAuth mAuth;
    // Visual component
    ImageView user_photo;
    TextView user_name, new_mail, total_points_num;
    String UserId,sendToIntent;
    DatabaseReference ref;


    // handle change of pic initialisation
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_SELECT_PICTURE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        //init Database reference
        ref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        UserId = mAuth.getInstance().getUid();
        // Init buttons
        log_out = (AppCompatButton) findViewById(R.id.log_out);
        user_photo = (ImageView) findViewById(R.id.user_photo);
        user_name = (TextView) findViewById(R.id.user_name);
        new_mail = (TextView) findViewById(R.id.new_mail);
        total_points_num = (TextView) findViewById(R.id.total_points_num);

        Map<String, Object> data = new HashMap<>();
        data.put("userId", UserId);
        FirebaseFunctions.getInstance().getHttpsCallable("getUserInfo").call(data).addOnCompleteListener(new OnCompleteListener<HttpsCallableResult>() {
            @Override
            public void onComplete(@NonNull Task<HttpsCallableResult> task) {
                if (task.isSuccessful()) {
                    if (task.isComplete()) {
                        String userInfo = (String) task.getResult().getData();
                        HashMap<String, String> data = jsonListToHashMap(userInfo);
                        System.out.println("*************************************************************************");
                        System.out.println("userInfo is: " + data.toString());
                        System.out.println("*************************************************************************");
                        user_name.setText(data.get("name"));
                        new_mail.setText(data.get("email"));
                        total_points_num.setText(data.get("curr_points"));
                    }
                }
            }
        });


        // Listeners
        log_out.setOnClickListener(this);
        total_points_num.setOnTouchListener(this);

        DatabaseReference refImage = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Users")
                .child(UserId)
                .child("image");

        user_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Personal_Info_Activity.this);

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

        refImage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String imageString = snapshot.getValue(String.class);
                if (imageString == null) {
                    System.out.println("ImageString is null");
                } else {
                    byte[] data = Base64.decode(imageString, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    user_photo.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error);
            }
        });
    }

    // Override the 'onClick' method, divided by button id
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.log_out:
                mAuth.getInstance().signOut();
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }

    }

    // Help function which takes a String representing Json file, and transform it to hashmap<String,JsonNode>
    public static HashMap<String, String> jsonListToHashMap(String jsonList) {
        HashMap<String, String> map = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonList);
            map.put("curr_points", jsonNode.get("curr_points").asText());
            map.put("email", jsonNode.get("email").asText());
            map.put("name", jsonNode.get("name").asText());
            map.put("image", jsonNode.get("image").toString());
        } catch (IOException e) {
            e.printStackTrace();
            return map;
        }
        return map;
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        switch (v.getId()) {
            case R.id.total_points_num:
                Intent j = new Intent(this, Update_Goals_Activity.class);
                j.putExtra("Uid_name_email_currpoints", sendToIntent);
                startActivity(j);
                break;
            default:
                break;
        }
        return true;
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
        Drawable drawable = user_photo.getDrawable();
        // Convert the drawable to a bitmap
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        return bitmap;
    }


    private void storeImage(String UserId, Bitmap image) {
        DatabaseReference userKey = ref.child("Users").child(UserId);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        String ImageString = Base64.encodeToString(data, Base64.DEFAULT);
        userKey.child("image").setValue(ImageString).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
//                Toast.makeText(Personal_Info_Activity.this, "Image Stored", Toast.LENGTH_SHORT).show();
                System.out.println("The new Image was Stored");
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
                user_photo.setImageBitmap(bitmap);
                storeImage(UserId, getImageFromImageView());

            } else if (resultCode == REQUEST_SELECT_PICTURE) {
                Uri selectedImage = data.getData();
                if (selectedImage != null) {
                    user_photo.setImageURI(selectedImage);
                }
            }
        }
    }


}