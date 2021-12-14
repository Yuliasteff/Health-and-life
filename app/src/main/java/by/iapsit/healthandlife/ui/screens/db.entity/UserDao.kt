package by.iapsit.healthandlife.ui.screens.db.entity

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM AppUser")
    fun getAll(): List<AppUser>

    @Query("SELECT * FROM appuser WHERE id = (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<AppUser>

    @Query("SELECT * FROM AppUser WHERE login LIKE :login AND " +
            "password LIKE :password LIMIT 1")
    fun findByName(login: String, password: String): AppUser

    @Insert
    fun insertAll(vararg users: AppUser)

    @Delete
    fun delete(user: AppUser)
}