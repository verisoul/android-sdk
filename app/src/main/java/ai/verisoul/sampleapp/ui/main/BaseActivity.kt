package ai.verisoul.sampleapp.ui.main

import ai.verisoul.sdk.Verisoul
import android.view.MotionEvent
import androidx.activity.ComponentActivity

open class BaseActivity : ComponentActivity() {

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Verisoul.onTouchEvent(ev)
        return super.dispatchTouchEvent(ev)
    }
}