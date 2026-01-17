package common.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.uikit.LocalUIViewController
import platform.UIKit.UIViewController

actual typealias AuthUiContext = UIViewController

@Composable
actual fun rememberAuthUiContext(): AuthUiContext = LocalUIViewController.current
