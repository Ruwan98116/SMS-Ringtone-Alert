package com.texttone;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyForegroundService extends Service {

    private static final String CHANNEL_ID = "ForegroundServiceChannel";
    private static final String TAG = "MyForegroundService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service created");

        // Create a notification channel for Android 8.0+ (API level 26)
        NotificationChannel serviceChannel = new NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_LOW // Minimize user disruption
        );
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service started");

        // Create the notification
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("App Running")
                .setContentText("Your app is running in the background.")
                .setSmallIcon(R.drawable.texttone) // Replace with your actual icon resource
                .setPriority(NotificationCompat.PRIORITY_MIN) // Minimize user disruption
                .setContentIntent(getPendingIntent()) // Open MainActivity when tapped
                .build();

        // Specify the foreground service type for Android 12+ (API 31+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK);
        } else {
            startForeground(1, notification);
        }

        return START_STICKY; // Keep the service running until explicitly stopped
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null; // Not a bound service
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Service destroyed");
        super.onDestroy();
    }

    private PendingIntent getPendingIntent() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        return PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE // Ensure compatibility with modern APIs
        );
    }

}
