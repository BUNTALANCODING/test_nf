package di

import business.core.AppDataStore
import business.core.AppDataStoreManager
import business.core.KtorHttpClient
import business.data.local.StudentLocalDataSource
import business.data.repository.StudentRepositoryImpl
import business.datasource.network.main.MainService
import business.datasource.network.main.MainServiceImpl
import business.datasource.network.splash.SplashService
import business.datasource.network.splash.SplashServiceImpl
import business.domain.repository.StudentRepository
import business.interactors.main.GetStudentsUseCase
import business.interactors.main.LoginUseCase
import business.interactors.main.RegisterStudentUseCase
import business.interactors.splash.CheckFCMTokenUseCase
import business.interactors.splash.CheckTokenUseCase
import business.interactors.splash.RegisterUseCase
import common.Context
import kotlinx.serialization.json.Json
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.module
import presentation.SharedViewModel
import presentation.token_manager.TokenManager
import presentation.ui.main.register.view_model.StudentViewModel
import presentation.util.BackgroundScheduler

const val WEB_CLIENT_ID =
    "67666257857-upcbt8r5ah8nv0gkk0ferfrq459qrbu7.apps.googleusercontent.com"

fun appModule(context: Context) = module {

    single { Json { isLenient = true; ignoreUnknownKeys = true } }
    single { KtorHttpClient.httpClient(get()) }
    single<SplashService> { SplashServiceImpl(get()) }
    single<MainService> { MainServiceImpl(get(), get()) }
    single<AppDataStore> { AppDataStoreManager(context) }
    single<BackgroundScheduler> { get() }
    factory { SharedViewModel(get()) }
    single { TokenManager(get(), get()) }
    single { RegisterUseCase(get(), get()) }
    single { CheckTokenUseCase(get()) }
    single { CheckFCMTokenUseCase(get()) }

    single { StudentLocalDataSource() }

    single<StudentRepository> { StudentRepositoryImpl(get(),get()) }

    factory { LoginUseCase(get()) }
    factory { RegisterStudentUseCase(get()) }
    factory { GetStudentsUseCase(get()) }

    factory { StudentViewModel(get(), get(), get()) }
}

private var started = false

fun initKoinOnce(context: Context): KoinApplication? {
    if (started) return null
    started = true
    return startKoin {
        modules(appModule(context))
    }
}
