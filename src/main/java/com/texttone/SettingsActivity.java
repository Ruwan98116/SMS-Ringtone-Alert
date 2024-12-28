package com.texttone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private EditText wordInput;

    private static final String PREFERENCES_NAME = "com.texttone.preferences";
    private static final String KEY_SAVED_WORD = "saved_word";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize UI elements
        wordInput = findViewById(R.id.editTextText);
        Button saveButton = findViewById(R.id.button2);

        // Load the previously saved word (if any)
        loadSavedWord();

        // Set up save button logic
        saveButton.setOnClickListener(v -> {
            String enteredWord = wordInput.getText().toString().trim();

            if (enteredWord.isEmpty()) {
                Toast.makeText(this, "Please enter a word before saving.", Toast.LENGTH_SHORT).show();
            } else if (enteredWord.length() > 30) {
                Toast.makeText(this, "Word is too long. Please use up to 30 characters.", Toast.LENGTH_SHORT).show();
            } else {
                saveWord(enteredWord);
                loadSavedWord(); // Refresh the input field with the saved word
            }
        });

        // Disabling Save Button on Empty Input
        wordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveButton.setEnabled(!s.toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Redirect to Modify System Settings
        findViewById(R.id.button4).setOnClickListener(v -> {
            if (Settings.System.canWrite(SettingsActivity.this)) {
                Toast.makeText(this, "You already have the permission.", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this, "Unable to open settings.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Redirect to DND Access Settings
        findViewById(R.id.button5).setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
        });

        // Redirect to App's SMS Permission Settings
        findViewById(R.id.button3).setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        });
    }

    private void saveWord(String word) {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_SAVED_WORD, word);
        editor.apply();
        Toast.makeText(this, "\"" + word + "\" saved successfully!", Toast.LENGTH_SHORT).show();
    }

    private void loadSavedWord() {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        String savedWord = preferences.getString(KEY_SAVED_WORD, "");

        if (!savedWord.isEmpty()) {
            wordInput.setText(savedWord); // Populate the text box with the saved word
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check permissions and update UI if necessary
    }
}
