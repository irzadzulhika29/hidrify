package ka.mobile.hidrify.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "water_logs")
data class WaterLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Int,       // Jumlah air (ml)
    val timestamp: Long,   // Waktu input (milidetik)
    val date: String       // Tanggal (format YYYY-MM-DD) untuk grouping
)