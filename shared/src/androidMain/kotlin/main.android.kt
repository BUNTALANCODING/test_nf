import android.app.Application
import androidx.compose.runtime.Composable
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import logger.Logger
import logger.LoggerDefaults
import app.net2software.rampcheck.BuildKonfig
import app.net2software.rampcheck.android.R
import business.constants.GetContext
import presentation.App
import video.api.uploader.api.work.stores.VideosApiStore

@Composable
fun MainView(application: Application) {

    ContextProvider.initialize(application.applicationContext)
    GetContext.context = application

    val config = if (BuildKonfig.debug) {
        LoggerDefaults.debugConfig()
    } else {
        LoggerDefaults.releaseConfig()
    }

    Logger.initialize(config)
    Logger.i("Application started - Debug: ${BuildKonfig.debug}")

    NotifierManager.initialize(
        configuration = NotificationPlatformConfiguration.Android(
            notificationIconResId = R.drawable.ic_logo,
            showPushNotification = true
        )
    )

    App(application)

    VideosApiStore.initialize()
}


