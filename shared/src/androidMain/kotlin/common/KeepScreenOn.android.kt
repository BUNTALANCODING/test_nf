package common

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

private tailrec fun Context.findActivity(): Activity? =
    when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }

private object KeepScreenOnManager {
    var counter: Int = 0
}

@Composable
actual fun KeepScreenOn(keepOn: Boolean) {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }

    DisposableEffect(activity, keepOn) {
        if (keepOn) {
            KeepScreenOnManager.counter++
            if (KeepScreenOnManager.counter == 1) {
                activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        } else {
        }

        onDispose {
            if (keepOn) {
                KeepScreenOnManager.counter--
                if (KeepScreenOnManager.counter <= 0) {
                    KeepScreenOnManager.counter = 0
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                }
            }
        }
    }
}
