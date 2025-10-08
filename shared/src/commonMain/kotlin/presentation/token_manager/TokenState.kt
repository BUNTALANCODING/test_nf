package presentation.token_manager

data class TokenState(
    val isTokenAvailable: Boolean = false,
    val isFCMTokenAvailable: Boolean = false,
)
