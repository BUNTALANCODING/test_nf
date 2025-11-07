package common

import io.ktor.client.request.forms.InputProvider
import io.ktor.util.cio.readChannel
import io.ktor.utils.io.ByteChannel
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.close
import io.ktor.utils.io.core.Input
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeFully
import io.ktor.utils.io.streams.asInput
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.IOException

actual class PlatformFile actual constructor(path: String) {
    private val file = File(path)

    init {
        if (!file.exists()) {
            throw IOException("File not found at path: $path")
        }
    }

    actual fun asInput(): Input {
        val bytes = file.readBytes()
        return buildPacket { writeFully(bytes) }
    }

    actual fun readChannel(): ByteReadChannel {
//        return ByteReadChannel(file.readBytes()) // Sederhana: Baca semua bytes
//        // Untuk file SANGAT BESAR, pertimbangkan menggunakan asFileChannel().asByteReadChannel()

        return file.readChannel()
    }

    actual val totalBytes: Long
        get() = file.length()

    actual val fileName: String
        get() = file.name
}