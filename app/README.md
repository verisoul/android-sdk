# Verisoul Android Sample App

This example app demonstrates how to integrate the Verisoul SDK into an Android application for fraud detection and account security.

## Requirements

- Android Studio Arctic Fox or higher
- Android 7.0 (API level 24) or higher
- Kotlin 1.5 or higher
- Android device or emulator

> **Note**: Make sure you have completed the [Android Studio setup](https://developer.android.com/studio/install) before proceeding.

## Configure

Before running the sample app, you need to configure it with your Verisoul credentials.

1. Open the project in Android Studio

2. Locate the Verisoul configuration in your `Application` class (e.g., `SampleApplication.kt`) and add your credentials:

```kotlin
Verisoul.init(
    this,
    VerisoulEnvironment.Sandbox, // Change to .Prod for production
    "YOUR_PROJECT_ID" // Replace with your actual project ID
)
```

3. If you don't have a Verisoul Project ID, schedule a call [here](https://meetings.hubspot.com/henry-legard) to get started.

## Get Started

### Step 1: Sync Project

Open the project in Android Studio and sync the Gradle files:

1. Open Android Studio
2. Select `File > Open` and choose the project directory
3. Wait for Gradle to sync automatically, or click `File > Sync Project with Gradle Files`

### Step 2: Run the App

1. Connect an Android device via USB or start an Android emulator

2. Select your target device from the device dropdown in Android Studio

3. Click the Run button (▶) or press `Shift + F10`

The app will build and launch on your selected device/emulator.

> **Note:** To run on a physical device, make sure USB debugging is enabled in Developer Options.

## Troubleshooting

### Gradle Sync Issues

If Gradle sync fails:

- Check that the Maven repository is correctly configured in `settings.gradle`
- Try `File > Invalidate Caches / Restart`
- Ensure you have an internet connection to download dependencies

### Build Errors

If the project won't build:

- Clean the project: `Build > Clean Project`
- Rebuild the project: `Build > Rebuild Project`
- Check that your Android SDK is up to date in SDK Manager

### Dependency Resolution Issues

If you see dependency resolution errors:

- Verify the Verisoul Maven repository URL is correct
- Check that the SDK version in `build.gradle` is available
- Try deleting the `.gradle` folder in your project and syncing again

## Learn More

- [Verisoul Documentation](https://docs.verisoul.ai/)
- [Android SDK Documentation](https://docs.verisoul.ai/integration/frontend/android)
- [Android Developer Documentation](https://developer.android.com/docs)
