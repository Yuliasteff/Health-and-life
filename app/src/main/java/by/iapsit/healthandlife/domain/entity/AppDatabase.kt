package by.iapsit.healthandlife.domain.entity

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AppUser::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao() : UserDao
}