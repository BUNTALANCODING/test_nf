package presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import business.constants.AUTHORIZATION_BEARER_TOKEN
import business.constants.DataStoreKeys
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
//import coil3.fetch.NetworkFetcher
import coil3.network.NetworkFetcher
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.PayloadData
import common.Context
import common.platformModule
import di.appModule
import logger.Logger
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import presentation.navigation.AppNavigation
import presentation.theme.AppTheme
import presentation.ui.main.MainNav
import presentation.ui.main.home.view_model.HomeEvent
import presentation.ui.splash.SplashNav

@OptIn(ExperimentalCoilApi::class)
@Composable
internal fun App(context: Context) {

    NotifierManager.setLogger { message ->
        Logger.d("Notifier Logger => $message")
    }


    LaunchedEffect(true) {
        NotifierManager.addListener(object : NotifierManager.Listener {
            override fun onNewToken(token: String) {
                Logger.d("onNewToken: $token")
            }

            override fun onPushNotificationWithPayloadData(
                title: String?,
                body: String?,
                data: PayloadData
            ) {
                super.onPushNotificationWithPayloadData(title, body, data)
                Logger.i("Push Notification received: Title: $title, Body: $body, Data: $data")
            }
        })
    }

//    KoinApplication(application = {
//        modules(appModule(context), platformModule())
//
//    }) {

        /*setSingletonImageLoaderFactory { context ->
            ImageLoader.Builder(context)
                .components {
                    add(NetworkFetcher.Factory())
                }
                .build()
        }*/

        AppTheme {
            val navigator = rememberNavController()
            val viewModel: SharedViewModel = koinInject()

            LaunchedEffect(key1 = viewModel.tokenManager.state.value.isTokenAvailable) {
                if (!viewModel.tokenManager.state.value.isTokenAvailable) {
                    navigator.popBackStack()
                    navigator.navigate(AppNavigation.Main)
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                NavHost(
                    navController = navigator,
                    startDestination = AppNavigation.Main,
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable<AppNavigation.Main> {
                        MainNav(context = context) {
                            navigator.popBackStack()
                            navigator.navigate(AppNavigation.Main)
                        }
                    }
                }
            }
//        }
    }
}




