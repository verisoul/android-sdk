package ai.verisoul.sampleapp

import ai.verisoul.sdk.Verisoul
import ai.verisoul.sdk.VerisoulEnvironment
import android.app.Application

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Verisoul SDK
        Verisoul.init(this, VerisoulEnvironment.Sandbox, "<VERISOUL_PROJECT_ID>")
    }
}