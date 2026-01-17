package common.auth

import cocoapods.FirebaseAuth.FIRAuth
import cocoapods.FirebaseAuth.FIRGoogleAuthProvider
import cocoapods.GoogleSignIn.GIDConfiguration
import cocoapods.GoogleSignIn.GIDSignIn
import cocoapods.GoogleSignIn.GIDSignInResult
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.UIKit.UIViewController
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalForeignApi::class)
class PlatformAuthRepository(
    private val iosClientId: String,
    private val webClientId: String
) : AuthRepository {

    override suspend fun signInWithGoogle(ui: AuthUiContext): AuthUser {
        val vc = ui as UIViewController

        val config = GIDConfiguration(clientID = iosClientId)
        GIDSignIn.sharedInstance.configuration = config

        val result = suspendCancellableCoroutine<GIDSignInResult> { cont ->
            GIDSignIn.sharedInstance.signInWithPresentingViewController(vc) { res, err ->
                when {
                    err != null -> cont.resumeWithException(
                        Exception(err.localizedDescription ?: "Google sign-in failed")
                    )
                    res != null -> cont.resume(res)
                    else -> cont.resumeWithException(Exception("No Google sign-in result"))
                }
            }
        }

        val idToken = result.user.idToken?.tokenString
            ?: throw IllegalStateException("Missing Google ID token")
        val accessToken = result.user.accessToken?.tokenString
            ?: throw IllegalStateException("Missing Google access token")

        val credential = FIRGoogleAuthProvider.credentialWithIDToken(idToken, accessToken)
        suspendCancellableCoroutine<Unit> { cont ->
            FIRAuth.auth().signInWithCredential(credential) { _, error ->
                if (error != null)
                    cont.resumeWithException(
                        Exception(error.localizedDescription ?: "Firebase login failed")
                    )
                else cont.resume(Unit)
            }
        }

        val firebaseUser = FIRAuth.auth().currentUser() ?: error("Firebase user null")

        return AuthUser(
            uid = firebaseUser.uid(),
            email = firebaseUser.email(),
            displayName = firebaseUser.displayName(),
            photoUrl = firebaseUser.photoURL()?.absoluteString,
            googleIdToken = idToken
        )
    }

    override suspend fun signOut() {
        FIRAuth.auth().signOut(null)
        GIDSignIn.sharedInstance.signOut()
    }

    override suspend fun currentUserOrNull(): AuthUser? {
        val user = FIRAuth.auth().currentUser() ?: return null
        return AuthUser(
            uid = user.uid(),
            email = user.email(),
            displayName = user.displayName(),
            photoUrl = user.photoURL()?.absoluteString,
            googleIdToken = ""
        )
    }
}
