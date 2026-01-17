package app.net2software.rampcheck.android

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import business.core.AppDataStore
import di.initKoinOnce
import org.koin.android.ext.android.get
import presentation.App
import common.Context

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val platformContext = Context(this)

        initKoinOnce(platformContext)

        val appDataStore: AppDataStore = get()

        setContent {
            App(
                context = platformContext,
                appDataStore = appDataStore
            )
        }
    }
}
