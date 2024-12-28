package com.texttone;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class SmsService extends Service {

    private static final String TAG = "SmsService";
    private static final String CHANNEL_ID = "default";
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "SmsService created.");

        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "SMS Service",
                NotificationManager.IMPORTANCE_LOW
        );
        channel.setDescription("Channel for SMS foreground service");
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.createNotificationChannel(channel);
        }

        // Display the notification
        showNotification("Listening for SMS...", false);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getStringExtra("action");
            Log.d(TAG, "Service received action: " + action);
            if ("play_ringtone".equals(action)) {
                Log.d(TAG, "Starting to play ringtone.");
                handlePlayRingtone();
            } else if ("stop_ringtone".equals(action)) {
                Log.d(TAG, "Stopping ringtone.");
                stopRingtone();
            }
        } else {
            Log.d(TAG, "Intent is null in onStartCommand.");
        }
        return START_STICKY;
    }


    private void showNotification(String contentText, boolean isPlaying) {
        Intent stopIntent = new Intent(this, SmsService.class);
        stopIntent.putExtra("action", "stop_ringtone");
        PendingIntent stopPendingIntent = PendingIntent.getService(
                this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("SMS Service Active")
                .setContentText(contentText)
                .setSmallIcon(R.drawable.texttone) // Ensure this icon exists in res/drawable
                .setPriority(NotificationCompat.PRIORITY_LOW);

        if (isPlaying) {
            builder.addAction(android.R.drawable.ic_media_pause, "Stop Ringtone", stopPendingIntent);
        }

        Notification notification = builder.build();
        startForeground(1, notification);
    }

    private void handlePlayRingtone() {
        Log.d(TAG, "Checking permissions before playing ringtone.");
        if (checkModifySettingsPermission() && checkDNDPermission()) {
            Log.d(TAG, "Permissions granted. Adjusting volume and playing ringtone.");
            adjustVolumeToMax();
            playRingtone(); // This should start the ringtone
            showNotification("Playing ringtone...", true);
        } else {
            Log.d(TAG, "Permissions not granted. Cannot play ringtone.");
            explainPermissions();
        }
    }


    private boolean checkModifySettingsPermission() {
        boolean hasPermission = Settings.System.canWrite(this);
        Log.d(TAG, "Modify Settings Permission: " + hasPermission);
        return hasPermission;
    }

    private boolean checkDNDPermission() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        boolean hasPermission = notificationManager != null && notificationManager.isNotificationPolicyAccessGranted();
        Log.d(TAG, "DND Permission: " + hasPermission);
        return hasPermission;
    }


    private void explainPermissions() {
        Toast.makeText(this, "Please grant the Modify Settings and DND permissions to play the ringtone.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void adjustVolumeToMax() {
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioManager != null) {
            // Get the current ringer mode
            int ringerMode = audioManager.getRingerMode();

            // If the phone is in silent or vibrate mode, switch it to normal
            if (ringerMode == AudioManager.RINGER_MODE_SILENT || ringerMode == AudioManager.RINGER_MODE_VIBRATE) {
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);  // Set to normal mode
                Log.d(TAG, "Phone was in silent/vibrate mode. Set to normal.");
            }

            // Set the ringtone volume to max
            int maxRingtoneVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);  // Use STREAM_RING for ringtone volume
            audioManager.setStreamVolume(AudioManager.STREAM_RING, maxRingtoneVolume, AudioManager.FLAG_SHOW_UI);
            Log.d(TAG, "Ringtone volume set to max: " + maxRingtoneVolume);

            // Set the media volume to max
            int maxMediaVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);  // Use STREAM_MUSIC for media volume
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxMediaVolume, AudioManager.FLAG_SHOW_UI);
            Log.d(TAG, "Media volume set to max: " + maxMediaVolume);
        }
    }





    private void playRingtone() {
        try {
            if (mediaPlayer == null) {
                Log.d(TAG, "Initializing MediaPlayer.");
                mediaPlayer = MediaPlayer.create(this, R.raw.ringtone);
                if (mediaPlayer == null) {
                    Log.e(TAG, "Failed to initialize MediaPlayer. File may be missing.");
                    return;
                }
                mediaPlayer.setLooping(true);
            }

            // Ensure MediaPlayer is not playing already
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                Log.d(TAG, "Ringtone started playing.");
                vibrate();
            } else {
                Log.d(TAG, "MediaPlayer is already playing.");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error playing ringtone", e);
        }
    }


    private void stopRingtone() {
        try {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.release();
                mediaPlayer = null;
                Log.d(TAG, "Ringtone stopped.");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error stopping MediaPlayer", e);
        }

        try {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            if (vibrator != null) {
                vibrator.cancel();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error stopping vibration", e);
        }

        showNotification("Listening for SMS...", false);
    }

    public void vibrate() {
        // Get the Vibrator system service
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (vibrator != null) {
            // Check Android version
            // For devices with Android 8.0 (API 26) and higher
            VibrationEffect effect = VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE);
            vibrator.vibrate(effect);
        } else {
            Log.e(TAG, "Vibrator service is not available on this device.");
        }
    }


    @Override
    public void onDestroy() {
        stopRingtone(); // Ensures MediaPlayer and Vibrator resources are released
        stopForeground(true);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}