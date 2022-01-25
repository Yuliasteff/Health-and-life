package by.iapsit.healthandlife.domain.dto

import by.iapsit.healthandlife.domain.entity.Gender
import java.time.LocalDate

data class User(
    val id: Int?,
    val login: String?,
    val firstName: String?,
    val lastName: String?,
    val dateOfBirth: LocalDate?,
    val gender: Gender?,
    val weight: Int?,
    val phoneNumber: String?,
    val email: String?,
    val heartRate: Int?,
    val saturation: Int?,
    val temperature: Double?,
    val medications: String?
)