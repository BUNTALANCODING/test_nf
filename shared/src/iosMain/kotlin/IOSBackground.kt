import io.ktor.utils.io.ByteReadChannel
import org.koin.dsl.module
import presentation.util.BackgroundScheduler

class IOSBackgroundScheduler : BackgroundScheduler {
    override fun enqueueVideoUpload(filePath: String, token: String) {
        println("⚠️ WARNING: Video Upload Scheduler dipanggil di iOS. Fitur ini tidak diimplementasikan.")
    }
}

val iosModule = module {
    // 1. Definisikan implementasi kosong untuk BackgroundScheduler
    single<BackgroundScheduler> { IOSBackgroundScheduler() }

    // 2. Definisikan implementasi kosong untuk PlatformFile (jika belum)
    // Jika PlatformFile juga expect, Anda harus mendefinisikannya di sini:
    // actual class PlatformFile actual constructor(...) { ... }
}