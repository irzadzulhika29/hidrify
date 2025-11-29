package ka.mobile.hidrify.data.repository

import ka.mobile.hidrify.data.local.WaterIntakeDao
import ka.mobile.hidrify.data.local.entity.WaterLog
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WaterRepository @Inject constructor(
    private val dao: WaterIntakeDao
) {
    // Ambil data (mengembalikan Flow agar real-time update)
    fun getLogsByDate(date: String): Flow<List<WaterLog>> {
        return dao.getLogsByDate(date)
    }

    // Ambil satu log berdasarkan ID
    fun getLogById(logId: Int): Flow<WaterLog?> {
        return dao.getLogById(logId)
    }

    // Simpan data
    suspend fun insertLog(log: WaterLog) {
        dao.insertLog(log)
    }

    // Hapus satu log spesifik
    suspend fun deleteLog(log: WaterLog) {
        dao.deleteLog(log)
    }

    // Reset data berdasarkan tanggal
    suspend fun resetLogs(date: String) {
        dao.deleteLogsByDate(date)
    }
}