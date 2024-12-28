package com.texttone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Start the foreground service
        startForegroundServiceCompat();

        // Set up the button to stop the ringtone
        Button stopRingtoneButton = findViewById(R.id.button); // Ensure the ID matches your layout XML
        stopRingtoneButton.setOnClickListener(v -> stopRingtone());

        // Set up the ImageButton to open the settings page
        ImageButton settingsButton = findViewById(R.id.imageButton); // Ensure the ID matches your layout XML
        settingsButton.setOnClickListener(v -> openSettingsPage());
    }

    private void startForegroundServiceCompat() {
        Intent serviceIntent = new Intent(this, SmsService.class); // Update service name to SmsService
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    private void stopRingtone() {
        Intent stopServiceIntent = new Intent(this, SmsService.class);
        stopServiceIntent.putExtra("action", "stop_ringtone");
        startService(stopServiceIntent);
        Toast.makeText(this, "Ringtone stopped.", Toast.LENGTH_SHORT).show();
    }

    private void openSettingsPage() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent); // Launch the SettingsActivity
    }

}
