# SMS-Ringtone-Alert

A mobile Android application that listens for incoming SMS messages and triggers a ringtone alert when a predefined keyword is detected. This app is useful for scenarios where you need immediate attention from specific SMS messages, such as for emergency alerts, important reminders, or notifications.

## Features
- **SMS Keyword Detection**: The app listens for incoming SMS messages and compares the content with a saved keyword.
- **Ringtone Alerts**: When the keyword is matched, the app plays a loud ringtone, ensuring you don't miss important messages.
- **Foreground Service**: Runs as a foreground service to ensure continuous operation, even when the app is in the background.
- **Notification Management**: Displays persistent notifications to indicate the status of the service (e.g., "Listening for SMS...").
- **Volume Control**: Automatically adjusts ringtone and media volumes to maximum to ensure the alert is heard.

## Permissions Required
- **SMS Permission**: To read incoming SMS messages.
- **Modify System Settings**: To adjust the phone’s ringer mode and volume.
- **DND Permission**: To manage the Do Not Disturb settings to ensure alerts are audible.

## Installation
To set up the project locally, follow these steps:

1. Clone the repository or download the source code.
   ```bash
   git clone https://github.com/your-username/SMS-Ringtone-Alert.git
2. Open the project in Android Studio.

3. Build and run the app on a physical device (SMS permissions cannot be tested on an emulator).

4. Ensure all required permissions are granted:

##
SMS permission: For reading incoming SMS.
Modify System Settings permission: For adjusting ringer mode and volume.
Do Not Disturb access: For managing DND settings.

## Usage
Once the app is installed and the necessary permissions are granted, the app will start listening for incoming SMS messages. If a message containing the predefined keyword is received, the app will trigger a loud ringtone to alert you.

## Contributing
Feel free to fork the repository and submit pull requests. Contributions are welcome!

if you need initial check the app it in the
https://github.com/Ruwan98116/SMS-Ringtone-Alert/blob/main/release/app-release.apk


