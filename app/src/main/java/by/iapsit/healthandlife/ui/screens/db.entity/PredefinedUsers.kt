package by.iapsit.healthandlife.ui.screens.db.entity

object PredefinedUsers {
    val users = listOf(
        AppUser(
            id = 0,
            login = "julia",
            password = "12345",
            firstName = "Julia",
            lastName = "Stefniak",
            dateOfBirth = "30-04-2001",
            gender = "Female",
            weight = "58",
            phoneNumber = "+375291111111",
            email = "example@gmail.com"
        ),
        AppUser(
            id = 0,
            login = "ivan",
            password = "12345",
            firstName = "Ivan",
            lastName = "Ivanov",
            dateOfBirth = "02-08-1997",
            gender = "Male",
            weight = "72",
            phoneNumber = "+375291111111",
            email = "ivan.example@gmail.com"
        )
    )
}