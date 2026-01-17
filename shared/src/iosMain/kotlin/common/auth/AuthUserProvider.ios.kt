package common.auth

import cocoapods.FirebaseAuth.FIRAuth
import cocoapods.FirebaseAuth.FIRUser
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalForeignApi::class)
actual object AuthUserProvider {

    private val auth = FIRAuth.auth()
    private val _currentUser = MutableStateFlow<AuthUser?>(null)
    actual val currentUser: StateFlow<AuthUser?> = _currentUser

    init {
        updateUser(auth.currentUser())
        auth.addAuthStateDidChangeListener { _, user ->
            updateUser(user)
        }
    }

    actual fun refresh() {
        updateUser(auth.currentUser(), forceReload = true)
    }

    private fun updateUser(user: FIRUser?, forceReload: Boolean = false) {
        if (user == null) {
            _currentUser.value = null
            return
        }

        user.getIDTokenForcingRefresh(forceReload) { token, _ ->
            _currentUser.value = user.toAuthUser(googleIdToken = token ?: "")
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun FIRUser.toAuthUser(googleIdToken: String): AuthUser =
    AuthUser(
        uid = uid(),
        email = email(),
        displayName = displayName(),
        photoUrl = photoURL()?.absoluteString,
        googleIdToken = googleIdToken
    )

