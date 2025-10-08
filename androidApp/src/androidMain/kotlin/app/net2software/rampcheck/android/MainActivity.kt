package app.net2software.rampcheck.android

import MainView
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mmk.kmpnotifier.extensions.onCreateOrOnNewIntent
import com.mmk.kmpnotifier.notification.NotifierManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ContextProvider.initialize(this)
        ContextProvider.initializePermission(this)

        NotifierManager.onCreateOrOnNewIntent(intent)

        setContent {
            MainView(application)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        NotifierManager.onCreateOrOnNewIntent(intent)
    }

    override fun onDestroy() {
        ContextProvider.clearPermission()
        super.onDestroy()
    }
}