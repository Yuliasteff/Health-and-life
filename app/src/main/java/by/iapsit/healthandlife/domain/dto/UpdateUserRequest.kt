package by.iapsit.healthandlife.domain.dto


import by.iapsit.healthandlife.domain.entity.Gender
import java.time.LocalDate

data class UpdateUserRequest(
    val login: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val dateOfBirth: LocalDate? = null,
    val gender: Gender? = null,
    val weight: Int? = null,
    val phoneNumber: String? = null,
    val email: String? = null,
    val heartRate: Int? = null,
    val saturation: Int? = null,
    val temperature: Double? = null,
    val medications: String? = null
)
