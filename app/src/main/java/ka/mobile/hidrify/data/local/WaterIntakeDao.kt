package ka.mobile.hidrify.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ka.mobile.hidrify.data.local.entity.WaterLog
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterIntakeDao {
    @Insert
    suspend fun insertLog(log: WaterLog)

    // Mengambil data berdasarkan tanggal hari ini (Real-time update dengan Flow)
    @Query("SELECT * FROM water_logs WHERE date = :todayDate")
    fun getLogsByDate(todayDate: String): Flow<List<WaterLog>>

    // Mengambil satu log berdasarkan ID
    @Query("SELECT * FROM water_logs WHERE id = :logId")
    fun getLogById(logId: Int): Flow<WaterLog?>

    // Menghapus semua data berdasarkan tanggal (untuk reset)
    @Query("DELETE FROM water_logs WHERE date = :date")
    suspend fun deleteLogsByDate(date: String)

    // Menghapus satu log spesifik
    @Delete
    suspend fun deleteLog(log: WaterLog)
}