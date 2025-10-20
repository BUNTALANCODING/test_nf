package di

import business.core.AppDataStore
import business.core.AppDataStoreManager
import business.core.KtorHttpClient
import business.datasource.network.main.MainService
import business.datasource.network.main.MainServiceImpl
import business.datasource.network.splash.SplashService
import business.datasource.network.splash.SplashServiceImpl
import business.interactors.main.GetLocationUseCase
import business.interactors.main.GetNotificationUseCase
import business.interactors.main.GetProfileUseCase
import business.interactors.main.LogoutUseCase
import business.interactors.main.RampcheckStartUseCase
import business.interactors.splash.CheckFCMTokenUseCase
import business.interactors.splash.CheckTokenUseCase
import business.interactors.splash.LoginUseCase
import business.interactors.splash.RegisterUseCase
import common.Context
import common.getImageSaveShare
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import presentation.SharedViewModel
import presentation.token_manager.TokenManager
import presentation.ui.main.home.view_model.HomeViewModel
import presentation.ui.main.auth.view_model.LoginViewModel

fun appModule(context: Context?) = module {
    single { Json { isLenient = true; ignoreUnknownKeys = true } }
    single {
        KtorHttpClient.httpClient(get())
    }
    single<SplashService> { SplashServiceImpl(get()) }
    single<MainService> { MainServiceImpl(get()) }
    single<AppDataStore> { AppDataStoreManager(context) }
    factory { SharedViewModel(get()) }
    factory { LoginViewModel(get(), get(), get()) }
    factory { HomeViewModel(get(), get(), get(),get()) }
    single { GetProfileUseCase(get(), get()) }
    single { TokenManager(get(), get(), get()) }
    single { LogoutUseCase(get()) }
    single { LoginUseCase(get(), get()) }
    single { RegisterUseCase(get(), get()) }
    single { CheckTokenUseCase(get()) }
    single { CheckFCMTokenUseCase(get()) }
    single { GetLocationUseCase(get(),get()) }
    single { RampcheckStartUseCase(get(),get()) }
    single { GetNotificationUseCase(get(), get()) }
//    single { UpdateDeviceTokenUseCase(get(), get()) }

}