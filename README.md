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

The `reinitialize()` method generates a fresh session ID and resets the SDK's data collection. This is essential for maintaining data integrity when user context changes.

**Example:**

```kotlin
Verisoul.reinitialize(
    callback = object : VerisoulCallback {
        override fun onSuccess() {
            // SDK reinitialized successfully
            // Now ready for a new user to log in with a fresh session
        }

        override fun onFailure(exception: Exception) {
            // Handle exception
        }
    }
)
```

After calling this method, you can call `getSessionId()` to retrieve the new session identifier.

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

| Error Code          | Value                 | Description                                                                                                                                                | Recommended Action                                                                                                                                                                                                                                                                                                                                    |
| ------------------- | --------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| INVALID_ENVIRONMENT | "INVALID_ENVIRONMENT" | The environment parameter passed to init() is invalid. Valid values are "dev", "sandbox", or "prod".                                                       | Integration Error. This is a developer configuration issue, not a user error. Verify that the environment string passed to Verisoul.init() is exactly one of: dev, sandbox, or prod. Environment values are case-sensitive. Check for typos, extra whitespace, or incorrect values like "production" or "DEV".                                        |
| SESSION_UNAVAILABLE | "SESSION_UNAVAILABLE" | A valid session ID could not be obtained. This typically occurs when Verisoul's servers are unreachable due to network blocking or a very slow connection. | Retry with backoff. Verisoul may be blocked by a firewall, VPN, or the user has poor connectivity. Implement retry logic with exponential backoff. If the error persists, prompt the user to check their network connection or try disabling VPN/proxy settings. Consider logging this for debugging network issues in specific regions or networks.  |
| WEBVIEW_UNAVAILABLE | "WEBVIEW_UNAVAILABLE" | WebView is not available on the device. This can occur when WebView is disabled, missing, uninstalled, or corrupted on the device.                         | Prompt user action. This error is not retried by the SDK since WebView availability won't change during the session. Recommend prompting the user to: (1) Use a device that supports WebViews, (2) Enable WebView if it has been disabled in device settings, or (3) Update Android System WebView from the Play Store if it's outdated or corrupted. |

#### Detailed Error Code Documentation

**INVALID_ENVIRONMENT**

Type: Integration Error (Developer)

When it occurs:

- Passing an invalid string to `VerisoulEnvironment.fromValue()` or equivalent
- Environment value not matching exactly: `dev`, `sandbox`, or `prod`
- Case sensitivity issues (e.g., "DEV" instead of "dev")
- Extra whitespace (e.g., " dev ")
- Typos (e.g., "production" instead of "prod")

SDK Behavior:

- Exception thrown immediately during initialization
- No retries attempted

Developer Action:

```kotlin
// ✅ Correct
Verisoul.init(context, VerisoulEnvironment.Prod, "your-project-id")

// ❌ Incorrect - will throw INVALID_ENVIRONMENT
// VerisoulEnvironment.fromValue("production")
// VerisoulEnvironment.fromValue("DEV")
```

**SESSION_UNAVAILABLE**

Type: Runtime Error (Network/Connectivity)

When it occurs:

- Network timeout waiting for session
- Verisoul servers unreachable
- Network blocking (firewall, corporate proxy, VPN)
- Very slow network connection
- All retry attempts exhausted

SDK Behavior:

- SDK automatically retries up to 4 times with delays
- WebView initialization retries up to 3 times
- Error thrown only after all retries are exhausted

Developer Action:

```kotlin
Verisoul.getSessionId(object : VerisoulSessionCallback {
    override fun onSuccess(sessionId: String) {
        // Use sessionId
    }

    override fun onFailure(exception: Throwable) {
        if (exception is VerisoulException &&
            exception.code == VerisoulErrorCodes.SESSION_UNAVAILABLE) {
            // Implement retry with backoff or prompt user about connectivity
        }
    }
})
```

**WEBVIEW_UNAVAILABLE**

Type: Device Limitation Error

When it occurs:

- WebView is disabled on the device
- WebView component is missing or uninstalled
- WebView is corrupted or incompatible
- Device doesn't support WebView (rare, older/custom ROMs)

SDK Behavior:

- No retries - fails immediately
- This is intentional since WebView availability won't change during the app session

Developer Action:

```kotlin
Verisoul.getSessionId(object : VerisoulSessionCallback {
    override fun onSuccess(sessionId: String) {
        // Use sessionId
    }

    override fun onFailure(exception: Throwable) {
        if (exception is VerisoulException &&
            exception.code == VerisoulErrorCodes.WEBVIEW_UNAVAILABLE) {
            // Show user-friendly message:
            // "Please enable WebView in your device settings or
            //  update Android System WebView from the Play Store"
        }
    }
})
```

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
