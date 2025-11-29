package ka.mobile.hidrify.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ka.mobile.hidrify.data.local.entity.WaterLog
import ka.mobile.hidrify.data.repository.WaterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryDetailViewModel @Inject constructor(
    private val repository: WaterRepository
) : ViewModel() {

    private val _waterLog = MutableStateFlow<WaterLog?>(null)
    val waterLog: StateFlow<WaterLog?> = _waterLog.asStateFlow()

    fun loadLog(logId: Int) {
        viewModelScope.launch {
            // Kita perlu menambahkan fungsi getLogById di repository
            repository.getLogById(logId).collect { log ->
                _waterLog.value = log
            }
        }
    }

    fun deleteLog(log: WaterLog) {
        viewModelScope.launch {
            repository.deleteLog(log)
        }
    }
}

