package common.auth

import androidx.compose.runtime.Composable

expect class AuthUiContext

@Composable
expect fun rememberAuthUiContext(): AuthUiContext
