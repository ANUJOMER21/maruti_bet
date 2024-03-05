package com.example.betapp.misc;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.example.betapp.R;

public class NotificationHelper {

    private static final String CHANNEL_ID = "MyChannelID";
    private static final String CHANNEL_NAME = "MyChannel";
    private static final int NOTIFICATION_ID = 1;

    public static void showNotification(Context context, String title, String message) {

        // Create Notification Manager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create Notification Channel (required for Android Oreo and higher)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);

            notificationManager.createNotificationChannel(channel);
        }
        String packageName = context.getApplicationContext().getPackageName();

        RemoteViews remoteViews = new RemoteViews( packageName, R.layout.custom_notification);

        // Set custom content for the notification
        remoteViews.setTextViewText(R.id.title, title);
        remoteViews.setTextViewText(R.id.text, message);
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.bell)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(remoteViews)
                .build();

        // Show the notification

        notificationManager.notify(NOTIFICATION_ID, notification);
        //

    }
}
