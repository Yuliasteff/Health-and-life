package by.iapsit.healthandlife.domain.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["email"], unique = true)])
data class AppUser constructor(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "login") val login: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "last_name") val lastName: String,
    @ColumnInfo(name = "date_of_birth") val dateOfBirth: Long,
    @ColumnInfo(name = "gender") val gender: Gender,
    @ColumnInfo(name = "weight") val weight: Int,
    @ColumnInfo(name = "phone_number") val phoneNumber: String,
    @ColumnInfo(name = "email") val email: String
) {
    constructor(login: String, password: String) : this(
        id = 0, login = login,
        password = password,
        firstName = "",
        lastName = "",
        dateOfBirth = 0,
        gender = Gender.EMPTY,
        weight = 0,
        phoneNumber = "",
        email = ""
    )
}