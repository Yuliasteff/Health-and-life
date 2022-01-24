package by.iapsit.healthandlife.db

import androidx.room.Database
import androidx.room.RoomDatabase
import by.iapsit.healthandlife.domain.entity.UserEntity

@Database(entities = [UserEntity::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao() : UserDao
}