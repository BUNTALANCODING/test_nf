package presentation

import androidx.compose.ui.window.ComposeUIViewController
import common.Context
import business.core.AppDataStore
import di.initKoinOnce
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object IosKoinHelper : KoinComponent {
    val appDataStore: AppDataStore by inject()
}

fun MainViewController() = ComposeUIViewController {
    val context = Context()
    initKoinOnce(context)

    val appDataStore = IosKoinHelper.appDataStore

    App(
        context = context,
        appDataStore = appDataStore
    )
}

