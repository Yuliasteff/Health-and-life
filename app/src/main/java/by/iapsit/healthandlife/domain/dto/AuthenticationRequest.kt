package by.iapsit.healthandlife.domain.dto

import com.google.gson.annotations.SerializedName

data class AuthenticationRequest(
    @SerializedName("email")
    var email: String,

    @SerializedName("password")
    var password: String
)