<p align="center">
<picture>
  <source media="(prefers-color-scheme: dark)" srcset="resources/verisoul-logo-dark.svg">
  <source media="(prefers-color-scheme: light)" srcset="resources/verisoul-logo-light.svg">
  <img src="resources/verisoul-logo-light.svg" alt="Verisoul logo" width="312px" style="visibility:visible;max-width:100%;">
</picture>
</p>

# Android SDK

Verisoul provides an Android SDK that allows you to implement fraud prevention in your Android applications. This guide covers the installation, configuration, and usage of the Verisoul Android SDK.

_To run the SDK a Verisoul Project ID is required._ Schedule a call [here](https://meetings.hubspot.com/henry-legard) to get started.

## System Requirements

- Android 7.0 (API level 24) or higher
- Kotlin 1.5 or higher
- Gradle 7.0 or higher

## Installation

### Add Maven Repository

Add these lines to your `settings.gradle` file.

```kotlin
dependencyResolutionManagement {
    repositories {
        ...
        maven { url = uri("https://us-central1-maven.pkg.dev/verisoul/android") }
    }
}
```

### Add Dependency

Add these lines to your `build.gradle` file.

#### For Groovy DSL

```kotlin
dependencies {
  ...
  implementation "ai.verisoul:android:0.4.61"
}
```

#### For Kotlin DSL

```kotlin
dependencies {
  ...
  implementation(libs.verisoul.android)
}
```

Add these lines to your `libs.versions.toml` file.

Under the `[versions]` add:

```kotlin
verisoul = "0.4.61"
```

Under the `[libraries]` add:

```kotlin
verisoul-android = { group = "ai.verisoul", name = "android", version.ref = "verisoul" }
```

## Usage

### Initialize the SDK

Call `init()` in your `Application` class's `onCreate()` method. Make sure to register this Application class in your `AndroidManifest.xml`.

**Application class:**

```kotlin
import ai.verisoul.sdk.Verisoul
import ai.verisoul.sdk.VerisoulEnvironment

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Verisoul.init(
            this,
            VerisoulEnvironment.Prod, // or VerisoulEnvironment.Sandbox
            "your-project-id"
        )
    }
}
```

**AndroidManifest.xml:**

```xml
<manifest>
    <application
        android:name=".SampleApplication">
            ...
    </application>
</manifest>
```

The `init()` method initializes the Verisoul SDK with your project credentials. This method must be called once when your application starts.

**Parameters:**

- `context`: Your application context
- `environment`: The environment to use `VerisoulEnvironment.Prod` for production or `VerisoulEnvironment.Sandbox` for testing
- `projectId`: Your unique Verisoul project identifier

### Get Session ID

The `getSessionId()` method returns the current session identifier after the SDK has collected sufficient device data. This session ID is required to request a risk assessment from Verisoul's API.

**Important Notes:**

- Session IDs are short-lived and expire after 24 hours
- The session ID becomes available once minimum data collection is complete (typically within seconds)
- You should send this session ID to your backend, which can then call Verisoul's API to get a risk assessment

**Example:**

```kotlin
Verisoul.getSessionId(
    callback = object : VerisoulSessionCallback {
        override fun onSuccess(sessionId: String) {
            // Send sessionId to your backend for risk assessment
        }

        override fun onFailure(exception: Exception) {
            // Handle exception
        }
    }
)
```

### Reinitialize Session

The `reinitialize()` method triggers session regeneration in the background and resets the SDK's data collection. This is essential for maintaining data integrity when user context changes.

**Example:**

```kotlin
Verisoul.reinitialize()
```

After calling this method, you can call `getSessionId()` to retrieve the new session identifier:

```kotlin
Verisoul.getSessionId(
    callback = object : VerisoulSessionCallback {
        override fun onSuccess(sessionId: String) {
            // New session ID is ready
        }
        override fun onFailure(exception: Exception) {
            // Handle exception
        }
    }
)
```

### Provide Touch Events

In order to gather touch events and compare them to device accelerometer sensor data, the app will need to provide touch events to Verisoul. The way to achieve this is to create `BaseActivity`, to override `dispatchTouchEvent` function and pass the data to Verisoul like shown below.

```kotlin
open class BaseActivity : Activity() {

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        Verisoul.onTouchEvent(event)
        return super.dispatchTouchEvent(event)
    }

    // Other common BaseActivity code...
}
```

In the application, just use BaseActivity as an Activity base class.

```kotlin
class MainActivity : BaseActivity() {

    // Other Activity code...
}
```

### Error Codes

The SDK throws `VerisoulException` with the following error codes:

| Error Code | Description | Recommended Action |
| ---------- | ----------- | ------------------ |
| INVALID_ENVIRONMENT | The environment parameter passed to `Verisoul.init()` is invalid. Valid values are "dev", "sandbox", or "prod". | Ensure `Verisoul.init()` parameter is exactly:<br>• "dev", "sandbox", or "prod"<br>• Case-sensitive<br>• Free of whitespace |
| SESSION_UNAVAILABLE | A valid session ID could not be obtained. This typically occurs when Verisoul's servers are unreachable due to network blocking or a very slow connection. | • Implement exponential backoff.<br>• Prompt user to check network or disable network blocker.<br>• Log to identify blocking issues. |
| WEBVIEW_UNAVAILABLE | WebView is not available on the device. This can occur when WebView is disabled, missing, uninstalled, or corrupted on the device. | Prompt user to:<br>• Enable WebView in settings<br>• Update Android System WebView<br>• Switch devices |


#### Exception Structure

All errors are thrown as `VerisoulException` with the following properties:

| Property | Type       | Description                                             |
| -------- | ---------- | ------------------------------------------------------- |
| code     | String     | One of the error codes above                            |
| message  | String     | Human-readable error description                        |
| cause    | Throwable? | The underlying exception that caused the error (if any) |

## Google Play Data Safety

For information on how to complete the Google Play Data Safety section when using the Verisoul SDK, please refer to our [Data Safety Instructions](https://support.verisoul.ai/articles/2377394661-verisoul-android-sdk-play-store-data-safety-instructions).

## Example

For a complete working example, see the [app folder](https://github.com/verisoul/android-sdk/tree/main/app) in this repository.
