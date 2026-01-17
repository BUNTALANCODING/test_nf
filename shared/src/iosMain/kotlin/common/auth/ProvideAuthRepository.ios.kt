package common.auth


actual fun provideAuthRepository(webClientId: String): AuthRepository {
    return PlatformAuthRepository(
        iosClientId = "67666257857-vlnjc1rkr0dlhg9ulu4k2c1ect7m0aqa.apps.googleusercontent.com",
        webClientId = webClientId
    )
}
