package common

import android.content.Context as AndroidContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import business.core.APP_DATASTORE
import kotlinx.coroutines.flow.first

private val AndroidContext.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = APP_DATASTORE
)

actual suspend fun Context?.getData(key: String): String? {
    val android = this?.androidContext ?: return null
    val prefs = android.dataStore.data.first()
    return prefs[stringPreferencesKey(key)]
}

actual suspend fun Context?.putData(key: String, value: String) {
    val android = this?.androidContext ?: return
    android.dataStore.edit { prefs ->
        prefs[stringPreferencesKey(key)] = value
    }
}
