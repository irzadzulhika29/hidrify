package ka.mobile.hidrify.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ka.mobile.hidrify.data.local.entity.WaterLog

@Database(entities = [WaterLog::class], version = 2, exportSchema = false)
abstract class WaterDatabase : RoomDatabase() {
    abstract fun waterIntakeDao(): WaterIntakeDao
}