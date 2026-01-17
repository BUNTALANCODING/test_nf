package di

import business.core.AppDataStore
import business.core.AppDataStoreManager
import business.core.KtorHttpClient
import business.datasource.network.main.MainService
import business.datasource.network.main.MainServiceImpl
import business.datasource.network.splash.SplashService
import business.datasource.network.splash.SplashServiceImpl
import business.domain.usecase.LoginUseCase
import business.interactor.GetRouteDetailUseCase
import business.interactor.GetRouteListUseCase
import business.interactor.GetTripUseCase
import business.interactors.main.GetNearestHalteUseCase
import business.interactors.main.GetRouteMapUseCase
import business.interactors.splash.CheckFCMTokenUseCase
import business.interactors.splash.CheckTokenUseCase
import business.interactors.splash.RegisterUseCase
import common.Context
import common.auth.AuthRepository
import common.auth.provideAuthRepository
import kotlinx.serialization.json.Json
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.module
import presentation.SharedViewModel
import presentation.token_manager.TokenManager
import presentation.ui.main.auth.view_model.LoginViewModel
import presentation.ui.main.home.view_model.HomeViewModel
import presentation.ui.main.inforute.view_model.detail.DetailRuteViewModel
import presentation.ui.main.inforute.view_model.info.InfoRuteViewModel
import presentation.ui.main.inforute.view_model.map.RouteMapViewModel
import presentation.util.BackgroundScheduler

const val WEB_CLIENT_ID = "67666257857-upcbt8r5ah8nv0gkk0ferfrq459qrbu7.apps.googleusercontent.com"


fun appModule(context: Context) = module {
    single { Json { isLenient = true; ignoreUnknownKeys = true } }
    single {
        KtorHttpClient.httpClient(get())
    }
    single<SplashService> { SplashServiceImpl(get()) }
    single<MainService> { MainServiceImpl(get(), get()) }
    single<AppDataStore> { AppDataStoreManager(context) }
    single<BackgroundScheduler> { get() }
    factory { SharedViewModel(get()) }
//    factory { UploadChunkViewModel(get()) }
    factory { HomeViewModel(get())}
    single { TokenManager(get(), get()) }
    single { LoginUseCase(get(), get()) }
    single { RegisterUseCase(get(), get()) }
    single { CheckTokenUseCase(get()) }
    single { CheckFCMTokenUseCase(get()) }

    single<AuthRepository> { provideAuthRepository(WEB_CLIENT_ID) }
    single { LoginUseCase(get(), get()) }

    factory { LoginViewModel(get(), get()) }

    factory { GetNearestHalteUseCase(get(), get()) }
    factory { HomeViewModel(get()) }

    factory { GetRouteListUseCase(get()) }
    factory { InfoRuteViewModel(get()) }

    factory { GetRouteDetailUseCase(get())}
    factory { DetailRuteViewModel(get(), get()) }

    factory { GetTripUseCase(get()) }

    factory { GetRouteMapUseCase(get()) }
    factory { RouteMapViewModel(get()) }




}

private var started = false

fun initKoinOnce(context: Context): KoinApplication? {
    if (started) return null
    started = true
    return startKoin {
        modules(appModule(context))
    }
}