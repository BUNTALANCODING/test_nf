package common

import io.ktor.utils.io.ByteChannel
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.close
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream

actual class PlatformFile actual constructor(actual val path: String) {
    private val file = File(path)

    actual fun length(): Long = file.length()

}