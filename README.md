<p align="center">
<picture>
  <source media="(prefers-color-scheme: dark)" srcset="resources/verisoul-logo-dark.svg">
  <source media="(prefers-color-scheme: light)" srcset="resources/verisoul-logo-light.svg">
  <img src="resources/verisoul-logo-light.svg" alt="Verisoul logo" width="312px" style="visibility:visible;max-width:100%;">
</picture>
</p>

# Android SDK

## Overview
The purpose of this app is to demonstrate Verisoul's Android SDK integration.

_To run the app a Verisoul Project ID is required._ Schedule a call [here](https://meetings.hubspot.com/henry-legard) to get started.

<!-- <img src="resources/verisoul.gif" width="128"/> -->

## Getting Started

### 1. Add Repository

Add these lines to your `settings.gradle` file.

```kotlin
dependencyResolutionManagement {
    repositories {
        ...
        maven { url = uri("https://us-central1-maven.pkg.dev/verisoul/android") }
    }
}
```

### 2. Add Dependency

Add these lines to your `build.gradle` file.

#### For Groovy DSL

```kotlin
dependencies {
  ...
  implementation "ai.verisoul:android:0.3.0"
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
verisoul = "1.0.3"
```

Under the `[libraries]` add:

```kotlin
verisoul-android = { group = "ai.verisoul", name = "android", version.ref = "verisoul" }
```

## Usage

### 1. Initialization

Initialization should be called in overridden `onCreate()` function from `Application` class that should be defined in the `AndroidManifest.xml` file. For example:

**Application** class

```kotlin
class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Verisoul.init(
            this,
            VerisoulEnvironment.Prod, // or Sandbox
            "<VERISOUL_PROJECT_ID>"
        )
    }
}
```

**AndroidManifest.xml**

```xml
<manifest>
    <application
        android:name=".SampleApplication">
            ...
    </application>
</manifest>
```

When this is called Verisoul library will be initialized, initial data together with **session ID** will be gathered and uploaded to Verisoul backend.

### 2. Get Session ID

Once the minimum amount of data is gathered the session ID becomes available. 
The session ID is needed in order to request a risk assessment from Verisoul's API. Note that session IDs are short lived and will expire after 24 hours. The application can obtain session ID by providing the callback as shown below:

```kotlin
Verisoul.getSessionId(
    callback = object : VerisoulSessionCallback {
        override fun onSuccess(sessionId: String) {
            // Upload session ID to backend
        }

        override fun onFailure(exception: Exception) {
            // Handle exception
        }
    }
)
```

### 3. Provide Touch Events

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

## Questions and Feedback
Comprehensive documentation about Verisoul's Android SDK and API can be found at [docs.verisoul.ai](https://docs.verisoul.ai/). Additionally, reach out to Verisoul at [help@verisoul.ai](mailto:help@verisoul.ai) for any questions or feedback.