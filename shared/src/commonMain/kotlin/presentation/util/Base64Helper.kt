package presentation.util

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okio.ByteString.Companion.decodeBase64
import okio.ByteString.Companion.encodeUtf8

class Base64Helper {

    inline fun <reified T> encodeToBase64(obj: T): String {
        val json = Json.encodeToString(obj)
        return json.encodeUtf8().base64()
    }

    inline fun <reified T> decodeFromBase64(base64: String): T? {
        val json = base64.decodeBase64()?.utf8()
        return json?.let { Json.decodeFromString<T>(it) }
    }

}

