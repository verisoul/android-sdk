package ai.verisoul.sampleapp

import ai.verisoul.sdk.Verisoul
import android.app.Application

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Verisoul SDK
        Verisoul.init(this, "<VERISOUL_ENV>", "<VERISOUL_PROJECT_ID>")
    }
}