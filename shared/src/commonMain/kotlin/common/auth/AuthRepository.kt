package common.auth


interface AuthRepository {
    suspend fun signInWithGoogle(ui: AuthUiContext): AuthUser
    suspend fun signOut()
    suspend fun currentUserOrNull(): AuthUser?
}
