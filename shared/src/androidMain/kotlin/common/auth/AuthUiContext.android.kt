package common.auth

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

actual typealias AuthUiContext = Activity

@Composable
actual fun rememberAuthUiContext(): AuthUiContext {
    val ctx = LocalContext.current
    return ctx as Activity
}
