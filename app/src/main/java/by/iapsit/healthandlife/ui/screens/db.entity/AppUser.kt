package by.iapsit.healthandlife.ui.screens.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["login"], unique = true)])
data class AppUser(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "login") val login: String?,
    @ColumnInfo(name = "password") val password: String?
) {
    constructor(login: String, password: String) : this(0, login, password)
}