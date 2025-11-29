package ka.mobile.hidrify.ui.tracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ka.mobile.hidrify.data.local.entity.WaterLog
import ka.mobile.hidrify.data.repository.WaterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

// State untuk menyimpan data tampilan
data class TrackerUiState(
    val currentIntake: Int = 0,
    val targetIntake: Int = 2000,
    val progress: Float = 0f,
    val waterLogs: List<WaterLog> = emptyList()
)

@HiltViewModel
class TrackerViewModel @Inject constructor(
    private val repository: WaterRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TrackerUiState())
    val uiState: StateFlow<TrackerUiState> = _uiState.asStateFlow()

    init {
        loadTodayData()
    }

    private fun loadTodayData() {
        val today = LocalDate.now().toString()
        viewModelScope.launch {
            repository.getLogsByDate(today).collect { logs ->
                val total = logs.sumOf { it.amount }
                _uiState.value = _uiState.value.copy(
                    currentIntake = total,
                    progress = total.toFloat() / _uiState.value.targetIntake,
                    waterLogs = logs.sortedByDescending { it.timestamp }
                )
            }
        }
    }

    fun addDrink(amount: Int, photoUri: String? = null) {
        viewModelScope.launch {
            val log = WaterLog(
                amount = amount,
                timestamp = System.currentTimeMillis(),
                date = LocalDate.now().toString(),
                photoUri = photoUri
            )
            repository.insertLog(log)
        }
    }

    fun deleteLog(log: WaterLog) {
        viewModelScope.launch {
            repository.deleteLog(log)
        }
    }

    fun resetToday() {
        val today = LocalDate.now().toString()
        viewModelScope.launch {
            repository.resetLogs(today)
        }
    }
}