package by.iapsit.healthandlife.domain.util

import by.iapsit.healthandlife.domain.dto.User
import by.iapsit.healthandlife.domain.entity.UserEntity
import java.time.LocalDate

class UserMapper : GenericMapper<UserEntity, User> {

    override fun fromEntity(userEntity: UserEntity): User {
        return User(
            id = userEntity.id,
            login = userEntity.login,
            email = userEntity.email,
            firstName = userEntity.firstName,
            lastName = userEntity.lastName,
            dateOfBirth = userEntity.dateOfBirth?.let { LocalDate.ofEpochDay(it) },
            gender = userEntity.gender,
            phoneNumber = userEntity.phoneNumber,
            weight = userEntity.weight,
            heartRate = userEntity.heartRate,
            saturation = userEntity.saturation,
            temperature = userEntity.temperature
        )
    }

    override fun toEntity(user: User): UserEntity {
        return UserEntity(
            id = user.id,
            login = user.login,
            email = user.email,
            password = null,
            firstName = user.firstName,
            lastName = user.lastName,
            dateOfBirth = user.dateOfBirth?.toEpochDay(),
            gender = user.gender,
            phoneNumber = user.phoneNumber,
            weight = user.weight,
            heartRate = user.heartRate,
            saturation = user.saturation,
            temperature = user.temperature
        )
    }
}