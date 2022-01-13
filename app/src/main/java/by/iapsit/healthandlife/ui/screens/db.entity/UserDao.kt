package by.iapsit.healthandlife.ui.screens.db.entity

import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM AppUser")
    fun getAll(): List<AppUser>

    @Query("SELECT * FROM appuser WHERE id = (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<AppUser>

    @Query(
        "SELECT * FROM AppUser WHERE login LIKE :login AND " +
                "password LIKE :password LIMIT 1"
    )
    fun findByLoginAndPassword(login: String, password: String): AppUser

    @Query("SELECT * FROM AppUser WHERE login = :login LIMIT 1")
    fun findByLogin(login: String) : AppUser

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg users: AppUser)

    @Update
    fun update(user: AppUser)

    @Delete
    fun delete(user: AppUser)
}