package common.auth

actual fun provideAuthRepository(webClientId: String): AuthRepository {
    return PlatformAuthRepository(webClientId)
}
