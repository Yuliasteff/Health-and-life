package by.iapsit.healthandlife.constants

import by.iapsit.healthandlife.domain.entity.Gender
import by.iapsit.healthandlife.domain.entity.UserEntity
import java.time.LocalDate

object PredefinedUsers {

    val users = listOf<UserEntity>(
        UserEntity(
            id = 0,
            login = "default-login",
            email = "default@gmail.com",
            password = "default",
            firstName = "Default",
            lastName = "Default",
            dateOfBirth = LocalDate.of(2001, 4, 30).toEpochDay(),
            weight = 58,
            gender = Gender.EMPTY,
            phoneNumber = "+375111234567",
            heartRate = 0,
            saturation = 0,
            temperature = 0.0,
            medications = "data"
        )
    )

}