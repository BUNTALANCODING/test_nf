package common.auth

import kotlinx.coroutines.flow.StateFlow

expect object AuthUserProvider {
    val currentUser: StateFlow<AuthUser?>
    fun refresh()

}