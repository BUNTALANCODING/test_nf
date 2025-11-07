import android.app.Application
import androidx.compose.runtime.Composable
import androidx.work.Configuration
import androidx.work.WorkManager
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import logger.Logger
import logger.LoggerDefaults
import app.net2software.rampcheck.BuildKonfig
import app.net2software.rampcheck.android.R
import business.constants.GetContext
import di.appModule
import logger.initializeApplicationContext
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.factory.KoinWorkerFactory
import org.koin.core.context.GlobalContext.startKoin
import presentation.App
import video.api.uploader.api.work.stores.VideosApiStore

@Composable
fun MainView(application: Application) {

    initializeApplicationContext(application)

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
    startKoin {
        androidContext(application)
        modules(
            appModule(application),
            androidModule
        )
    }
//     ✅ Integrasi WorkManager + Koin
//    val workerFactory = KoinWorkerFactory()
//    WorkManager.initialize(
//        application,
//        Configuration.Builder()
//            .setWorkerFactory(workerFactory)
//            .build()
//    )

    App(application)

    VideosApiStore.initialize()
}


