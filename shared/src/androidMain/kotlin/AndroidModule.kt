import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.dsl.module
import presentation.util.BackgroundScheduler

val androidModule = module {
    // 1. Daftarkan Worker (DI untuk konstruktor Worker)
    workerOf(::VideoUploadWorker)

    // 2. Daftarkan Scheduler (DI untuk ViewModel)
    single<BackgroundScheduler> { AndroidBackgroundScheduler(context = androidContext()) }
}