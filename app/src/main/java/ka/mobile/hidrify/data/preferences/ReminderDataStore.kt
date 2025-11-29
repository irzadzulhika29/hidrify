package ka.mobile.hidrify.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Membuat ekstensi .dataStore agar otomatis dibuatkan filenya
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "reminder_settings")

@Singleton
class ReminderDataStore @Inject constructor(@ApplicationContext private val context: Context) {

    private val IS_ENABLED = booleanPreferencesKey("is_enabled")
    private val WAKE_TIME = stringPreferencesKey("wake_time")
    private val BED_TIME = stringPreferencesKey("bed_time")

    // Membaca data (Default: Aktif, 07:00, 22:00)
    val preferenceFlow: Flow<Triple<Boolean, String, String>> = context.dataStore.data
        .map { preferences ->
            Triple(
                preferences[IS_ENABLED] ?: true,
                preferences[WAKE_TIME] ?: "07:00",
                preferences[BED_TIME] ?: "22:00"
            )
        }

    // Menyimpan data
    suspend fun saveSettings(isEnabled: Boolean, wakeTime: String, bedTime: String) {
        context.dataStore.edit { preferences ->
            preferences[IS_ENABLED] = isEnabled
            preferences[WAKE_TIME] = wakeTime
            preferences[BED_TIME] = bedTime
        }
    }
}