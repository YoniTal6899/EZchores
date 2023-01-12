package com.example.ezchores;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class PushNotificationService extends FirebaseMessagingService {
    final String CHANNEL_ID= "Notification Channel";
    NotificationChannel channel;
    String regToken=null;
    MainActivity main_activity;

    public PushNotificationService(){
        if (Build.VERSION.SDK_INT >= 26) {
            this.channel = new NotificationChannel(CHANNEL_ID,"My_Channel", NotificationManager.IMPORTANCE_HIGH);
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        this.regToken=token;
        SharedPreferences sharedPreferences = getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
        PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token",token);
        editor.apply();
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("this.token changed to:"+ this.regToken);
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        Map<String, Object> tokendata= new HashMap<>();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        String title= message.getNotification().getTitle();
        String body= message.getNotification().getBody();
        if (Build.VERSION.SDK_INT >= 26) {
            getSystemService(NotificationManager.class).createNotificationChannel(this.channel);
            Notification.Builder notification= new Notification.Builder(this,CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(R.drawable.ezchores_logo).setAutoCancel(true);
            NotificationManagerCompat.from(this).notify(1,notification.build());
        }



    }
}
