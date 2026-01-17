package common.auth

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import di.WEB_CLIENT_ID
import kotlinx.coroutines.tasks.await
import org.json.JSONObject
import java.util.Base64

object LastGoogleUserCache {
    var email: String? = null
    var name: String? = null
    var photoUrl: String? = null
}

class PlatformAuthRepository(
    private val webClientId: String = WEB_CLIENT_ID
) : AuthRepository {

    private val auth = FirebaseAuth.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun signInWithGoogle(ui: AuthUiContext): AuthUser {
        val activity: Activity = ui
        val credentialManager = CredentialManager.create(activity)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(webClientId)
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val result = credentialManager.getCredential(activity, request)
        val credential = result.credential

        require(
            credential is CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        )

        val googleIdTokenCred = GoogleIdTokenCredential.createFrom(credential.data)
        val idToken = googleIdTokenCred.idToken

        val payload = decodeJwtPayload(idToken)
        val tokenEmail = payload.optString("email", null)
        val tokenName = payload.optString("name", null)
        val tokenPhoto = payload.optString("picture", null)
        val aud = payload.optString("aud", null)
        val iss = payload.optString("iss", null)

        Log.d("GoogleTokenCheck", "✅ Google ID Token aud=$aud | iss=$iss")

        LastGoogleUserCache.email = tokenEmail
        LastGoogleUserCache.name = tokenName
        LastGoogleUserCache.photoUrl = tokenPhoto

        val firebaseCred = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(firebaseCred).await()

        Log.d(
            "PlatformAuthRepo",
            """
            🔥 Google login success:
            name = $tokenName
            email = $tokenEmail
            photo = $tokenPhoto
            aud   = $aud
            iss   = $iss
            """.trimIndent()
        )

        return AuthUser(
            uid = payload.optString("sub", ""),
            email = tokenEmail,
            displayName = tokenName,
            photoUrl = tokenPhoto,
            googleIdToken = idToken
        )
    }

    override suspend fun signOut() {
        auth.signOut()
        LastGoogleUserCache.email = null
        LastGoogleUserCache.name = null
        LastGoogleUserCache.photoUrl = null
    }

    override suspend fun currentUserOrNull(): AuthUser? {
        val user = auth.currentUser
        return if (user == null && LastGoogleUserCache.email == null) {
            null
        } else {
            AuthUser(
                uid = user?.uid ?: "",
                email = user?.email ?: LastGoogleUserCache.email,
                displayName = user?.displayName ?: LastGoogleUserCache.name,
                photoUrl = user?.photoUrl?.toString() ?: LastGoogleUserCache.photoUrl,
                googleIdToken = ""
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun decodeJwtPayload(jwt: String): JSONObject {
        return try {
            val parts = jwt.split(".")
            if (parts.size < 2) return JSONObject()
            val decoded = String(Base64.getUrlDecoder().decode(parts[1]))
            JSONObject(decoded)
        } catch (e: Exception) {
            Log.e("PlatformAuthRepo", "JWT decode error: ${e.message}")
            JSONObject()
        }
    }
}
