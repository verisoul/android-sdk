package ai.verisoul.sampleapp.ui.main

import ai.verisoul.sampleapp.ui.theme.VerisoulSampleAppTheme
import ai.verisoul.sdk.Verisoul
import ai.verisoul.sdk.webview.VerisoulSessionCallback
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

class MainActivity : BaseActivity() {

    private val tag: String = this::class.java.simpleName

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