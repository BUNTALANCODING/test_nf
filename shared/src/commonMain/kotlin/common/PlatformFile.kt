package common

import io.ktor.client.request.forms.InputProvider
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.Input

expect class PlatformFile(path: String) {
    fun asInput(): Input  // ✅ tambahin ini
    fun readChannel(): ByteReadChannel
    val totalBytes: Long
    val fileName: String
}