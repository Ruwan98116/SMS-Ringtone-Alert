A mobile Android application that listens for incoming SMS messages and triggers a ringtone alert when a predefined keyword is detected. This app is useful for scenarios where you need immediate attention from specific SMS messages, such as for emergency alerts, important reminders, or notifications.

Features:
SMS Keyword Detection: The app listens for incoming SMS messages and compares the content with a saved keyword.
Ringtone Alerts: When the keyword is matched, the app plays a loud ringtone, ensuring you don't miss important messages.
Foreground Service: Runs as a foreground service to ensure continuous operation, even when the app is in the background.
Notification Management: Displays persistent notifications to indicate the status of the service (e.g., "Listening for SMS...").
Volume Control: Automatically adjusts ringtone and media volumes to maximum to ensure the alert is heard.
Permissions: Requests necessary permissions for reading SMS, modifying system settings, and managing Do Not Disturb (DND) mode.
Permissions Required:
SMS Permission: To read incoming SMS messages.
Modify System Settings: To adjust the phone’s ringer mode and volume.
DND Permission: To manage the Do Not Disturb settings to ensure alerts are audible.
Installation:
Clone the repository or download the source code.
Open the project in Android Studio.
Build and run the app on a physical device (SMS permissions cannot be tested on an emulator).
Ensure all required permissions are granted, including SMS, system settings modification, and Do Not Disturb access.
Contributing:
Feel free to fork the repository and submit pull requests. Contributions are welcome!
