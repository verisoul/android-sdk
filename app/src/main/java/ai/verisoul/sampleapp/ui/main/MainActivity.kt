package ai.verisoul.sampleapp.ui.main

import ai.verisoul.sampleapp.ui.theme.VerisoulSampleAppTheme
import ai.verisoul.sdk.Verisoul
import ai.verisoul.sdk.webview.VerisoulSessionCallback
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat

class MainActivity : BaseActivity() {

    private val tag: String = this::class.java.simpleName

    private val requiredPermissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_PHONE_STATE
    )

    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
            val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val phoneStateGranted = permissions[Manifest.permission.READ_PHONE_STATE] ?: false

            Log.d(tag, "Coarse Location Granted? $coarseGranted")
            Log.d(tag, "Fine Location Granted? $fineGranted")
            Log.d(tag, "Read Phone State Granted? $phoneStateGranted")
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            var sessionId by remember { mutableStateOf("Press the button to get session") }

            VerisoulSampleAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    VerisoulExample(
                        modifier = Modifier.padding(innerPadding),
                        sessionId = sessionId,
                        callback = {
                            getSessionId { newSessionId ->
                                sessionId = newSessionId
                            }
                        }
                    )
                }
            }
        }

        handlePermissionFlow()
    }

    private fun handlePermissionFlow() {
        if (!arePermissionsGranted()) {
            requestAllPermissions()
        }
    }

    private fun arePermissionsGranted(): Boolean {
        return requiredPermissions.all { permission ->
            ActivityCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestAllPermissions() {
        requestPermissionsLauncher.launch(requiredPermissions)
    }

    private fun getSessionId(callBack: (String) -> Unit) {
        Verisoul.getSessionId(object : VerisoulSessionCallback {
            override fun onSuccess(sessionId: String) {
                callBack(sessionId)
            }

            override fun onFailure(exception: Exception) {
                Log.e(
                    tag,
                    "Error occurred while trying to fetch sessionId with message: ${exception.message}"
                )
            }
        })
    }
}