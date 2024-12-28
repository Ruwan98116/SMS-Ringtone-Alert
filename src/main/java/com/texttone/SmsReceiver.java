package com.texttone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {

    private static final String TAG = "SmsReceiver";
    private static final String PREFERENCES_FILE = "com.texttone.preferences";
    private static final String KEY_SAVED_WORD = "saved_word";

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                Log.e(TAG, "Bundle is null, no SMS received.");
                return;
            }

            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus == null || pdus.length == 0) {
                Log.e(TAG, "No PDUs found, no SMS received.");
                return;
            }

            StringBuilder messageBody = new StringBuilder();
            String format = bundle.getString("format"); // SMS format for decoding
            for (Object pdu : pdus) {
                try {
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu, format);
                    if (sms != null) {
                        messageBody.append(sms.getMessageBody());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error processing SMS PDU", e);
                }
            }

            String receivedMessage = messageBody.toString().trim();
            if (receivedMessage.isEmpty()) {
                Log.d(TAG, "Received SMS is empty. Ignoring.");
                return;
            }

            // Retrieve the saved keyword
            SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
            String savedKeyword = preferences.getString(KEY_SAVED_WORD, "ring");
            Log.d(TAG, "Saved keyword: " + savedKeyword);

            // Normalize strings for comparison
            String normalizedMessage = normalizeString(receivedMessage);
            String normalizedKeyword = normalizeString(savedKeyword);

            if (normalizedMessage.equals(normalizedKeyword)) {
                Log.d(TAG, "Keyword matched. Starting SmsService to play ringtone.");
                startSmsService(context);
            } else {
                Log.d(TAG, "Keyword not matched. Ignoring SMS.");
            }
        } else {
            Log.e(TAG, "Received unexpected intent action: " + intent.getAction());
        }
    }

    private String normalizeString(String input) {
        return input.replaceAll("\\W", "").toLowerCase(); // Use \W to match non-alphanumeric characters
    }

    private void startSmsService(Context context) {
        Intent serviceIntent = new Intent(context, SmsService.class);
        serviceIntent.putExtra("action", "play_ringtone");
        context.startService(serviceIntent);
    }
}
