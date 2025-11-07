package common

import com.kashif.cameraK.utils.toByteArray
import io.ktor.client.request.forms.InputProvider
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.Input
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeFully
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfFile
import platform.Foundation.dataWithContentsOfURL

actual class PlatformFile actual constructor(path: String) {
    // Memuat NSData. Jika file sangat besar, ini akan menyebabkan OOM.
    private val nsData: NSData? = NSData.dataWithContentsOfFile(path)
    private val fileUrl = NSURL.fileURLWithPath(path)
    private val fileData: NSData? = NSData.dataWithContentsOfURL(fileUrl)

    init {
        if (nsData == null) {
            throw IllegalStateException("Failed to load file at path: $path")
        }
    }

    actual fun asInput(): Input {
        val bytes = fileData?.toByteArray() ?: ByteArray(0)
        // 🔥 `buildPacket` menghasilkan `ByteReadPacket` yang bisa diperlakukan sebagai `Input`
        return buildPacket { writeFully(bytes) }
    }

    // --- FUNGSI UTAMA YANG PERLU DIPERBAIKI ---
    actual fun readChannel(): ByteReadChannel {
        val data = nsData!!

        // **SOLUSI: Buat ByteArray berukuran NSData, lalu salin data dari pointer.**
        // Anda perlu fungsi helper untuk menyalin data dari pointer C ke ByteArray Kotlin.
        // Jika Anda menggunakan KMP terbaru, beberapa utilitas sudah tersedia.

        val byteBuffer = data.toByteArray() // Gunakan ekstensi helper (lihat catatan di bawah)

        return ByteReadChannel(byteBuffer)
    }

    actual val totalBytes: Long
        get() = nsData?.length?.toLong() ?: 0

    actual val fileName: String
        get() = path.substringAfterLast("/") // Contoh sederhana

    // Properti path diperlukan untuk implementasi
    private val path: String = path
}