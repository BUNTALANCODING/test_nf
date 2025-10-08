package presentation.ui.splash

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import common.ChangeStatusBarColors
import kotlinx.coroutines.delay
import presentation.navigation.SplashNavigation

@Composable
internal fun SplashNav(navigateToMain: () -> Unit) {
    val navigator = rememberNavController()

    LaunchedEffect(Unit) {
        delay(4000L)
        navigateToMain()
    }

    ChangeStatusBarColors(MaterialTheme.colorScheme.primary)

    NavHost(
        startDestination = SplashNavigation.Splash,
        navController = navigator,
        modifier = Modifier.fillMaxSize()
    ) {
        composable<SplashNavigation.Splash> {
            SplashScreen()
        }
    }

}