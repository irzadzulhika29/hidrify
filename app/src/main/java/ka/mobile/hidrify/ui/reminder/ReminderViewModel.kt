package ka.mobile.hidrify.ui.reminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ka.mobile.hidrify.data.preferences.ReminderDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReminderUiState(
    val isEnabled: Boolean = true,
    val wakeTime: String = "07:00",
    val bedTime: String = "22:00"
)

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val dataStore: ReminderDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReminderUiState())
    val uiState: StateFlow<ReminderUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            dataStore.preferenceFlow.collect { (enabled, wake, bed) ->
                _uiState.value = ReminderUiState(enabled, wake, bed)
            }
        }
    }

    fun updateState(isEnabled: Boolean? = null, wakeTime: String? = null, bedTime: String? = null) {
        _uiState.value = _uiState.value.copy(
            isEnabled = isEnabled ?: _uiState.value.isEnabled,
            wakeTime = wakeTime ?: _uiState.value.wakeTime,
            bedTime = bedTime ?: _uiState.value.bedTime
        )
    }

    fun saveSettings() {
        viewModelScope.launch {
            val s = _uiState.value
            dataStore.saveSettings(s.isEnabled, s.wakeTime, s.bedTime)
            // Di sini nantinya kamu panggil WorkManager logic
            // calculateInterval(s.wakeTime, s.bedTime)
        }
    }
}