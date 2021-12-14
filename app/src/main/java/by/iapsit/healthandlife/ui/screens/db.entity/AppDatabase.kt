package by.iapsit.healthandlife.ui.screens.db.entity

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AppUser::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao() : UserDao
}