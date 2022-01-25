package by.iapsit.healthandlife.db

import androidx.room.*
import by.iapsit.healthandlife.domain.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM UserEntity")
    fun getAll(): List<UserEntity>

    @Query("SELECT * FROM UserEntity WHERE id = (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<UserEntity>

    @Query(
        "SELECT * FROM UserEntity WHERE login LIKE :email AND password LIKE :password LIMIT 1"
    )
    fun findByEmailAndPassword(email: String, password: String): UserEntity

    @Query("SELECT * FROM UserEntity WHERE email = :email LIMIT 1")
    fun findByEmail(email: String) : UserEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg userEntities: UserEntity)

    @Update
    fun update(userEntity: UserEntity)

    @Delete
    fun delete(userEntity: UserEntity)
}