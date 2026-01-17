package presentation

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import business.core.AppDataStore
import common.Context
import presentation.theme.TransportTheme
import presentation.ui.main.MainNav

@Composable
fun App(
    context: Context,
    appDataStore: AppDataStore
) {
    TransportTheme(dark = false) {
        Surface {
            MainNav(context = context, appDataStore = appDataStore)
        }
    }
}

