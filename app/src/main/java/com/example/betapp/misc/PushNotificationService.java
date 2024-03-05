package com.example.betapp.misc;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class PushNotificationService extends FirebaseMessagingService {
    private String tag="PushNotify";

    @Override
    public void onNewToken(@NonNull String token)
    {
        Log.d(tag, "Refreshed token: " + token);
    }
    @Override
    public void
    onMessageReceived(RemoteMessage remoteMessage)
    {        Log.d(tag,remoteMessage.getNotification().getBody());

        // First case when notifications are received via
        // data event
        // Here, 'title' and 'message' are the assumed names
        // of JSON
        // attributes. Since here we do not have any data
        // payload, This section is commented out. It is
        // here only for reference purposes.
        /*if(remoteMessage.getData().size()>0){
            showNotification(remoteMessage.getData().get("title"),
                          remoteMessage.getData().get("message"));
        }*/

        // Second case when notification payload is
        // received.
        if (remoteMessage.getNotification() != null) {
            // Since the notification is received directly
            // from FCM, the title and the body can be
            // fetched directly as below.
            showNotification(
                    remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody());

        }
    }

    private void showNotification(String title, String body) {
        Log.d(tag,title+"|"+body);
        NotificationHelper.showNotification(this, title, body);
    }

    // Override onMessageReceived() method to extract the
    // title and
    // body from the message passed in FCM
}