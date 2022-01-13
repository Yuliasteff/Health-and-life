package by.iapsit.healthandlife.ui.screens.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["login"], unique = true)])
data class AppUser(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "login") val login: String?,
    @ColumnInfo(name = "password") val password: String?,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
    @ColumnInfo(name = "date_of_birth") val dateOfBirth: String?,
    @ColumnInfo(name = "gender") val gender: String?,
    @ColumnInfo(name = "weight") val weight: String?,
    @ColumnInfo(name = "phone_number") val phoneNumber: String?,
    @ColumnInfo(name = "email") val email: String?
) {
    constructor(login: String, password: String) : this(
        id = 0, login = login,
        password = password,
        firstName = "",
        lastName = "",
        dateOfBirth = "",
        gender = "",
        weight = "",
        phoneNumber = "",
        email = ""
    )
}