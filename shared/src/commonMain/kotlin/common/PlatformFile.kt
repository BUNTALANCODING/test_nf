package common

import io.ktor.utils.io.ByteReadChannel

expect class PlatformFile(path: String) {
    val path: String
    fun length(): Long
}