package by.iapsit.healthandlife.ui.screens.db.entity

import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM AppUser")
    fun getAll(): List<AppUser>

    @Query("SELECT * FROM appuser WHERE id = (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<AppUser>

    @Query("SELECT * FROM AppUser WHERE login LIKE :login AND " +
            "password LIKE :password LIMIT 1")
    fun findByLoginAndPassword(login: String, password: String): AppUser

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg users: AppUser)

    @Delete
    fun delete(user: AppUser)
}