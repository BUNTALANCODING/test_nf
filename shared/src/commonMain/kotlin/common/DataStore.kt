package common

expect suspend fun Context?.getData(key: String): String?
expect suspend fun Context?.putData(key: String, value: String)

