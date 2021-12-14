package by.iapsit.healthandlife.ui.screens.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AppUser(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "login") val login: String?,
    @ColumnInfo(name = "password") val password: String?
)