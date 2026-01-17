package common

import platform.Foundation.NSUserDefaults

actual suspend fun Context?.getData(key: String): String? {
    val defaults: NSUserDefaults = this?.userDefaults ?: return null
    return defaults.stringForKey(key)
}

actual suspend fun Context?.putData(key: String, value: String) {
    val defaults: NSUserDefaults = this?.userDefaults ?: return
    defaults.setObject(value, forKey = key)
}
