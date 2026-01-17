package common.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

actual object AuthUserProvider {

    private val _currentUser = MutableStateFlow<AuthUser?>(null)
    actual val currentUser: StateFlow<AuthUser?> = _currentUser

    private val auth = FirebaseAuth.getInstance()
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        scope.launch { updateUser(auth.currentUser) }
        auth.addAuthStateListener { firebaseAuth ->
            scope.launch { updateUser(firebaseAuth.currentUser) }
        }
    }

    actual fun refresh() {
        scope.launch { updateUser(auth.currentUser, forceReload = true) }
    }

    private suspend fun updateUser(user: FirebaseUser?, forceReload: Boolean = false) {
        if (user == null) {
            _currentUser.value = null
            return
        }

        if (forceReload) runCatching { user.reload().await() }

        val refreshed = auth.currentUser ?: user
        val token = refreshed.getIdToken(false).await().token ?: ""

        val provider = refreshed.providerData.firstOrNull()
        val email = refreshed.email ?: provider?.email ?: LastGoogleUserCache.email
        val name = refreshed.displayName ?: provider?.displayName ?: LastGoogleUserCache.name
        val photo = refreshed.photoUrl?.toString() ?: provider?.photoUrl?.toString() ?: LastGoogleUserCache.photoUrl

        println("🔥 AuthUserProvider.refresh -> email=$email, name=$name, photo=$photo")

        _currentUser.value = AuthUser(
            uid = refreshed.uid,
            email = email,
            displayName = name,
            photoUrl = photo,
            googleIdToken = token
        )
    }
}
