package by.iapsit.healthandlife.domain.dto

import com.google.gson.annotations.SerializedName

data class AuthenticationResponse(
    @SerializedName("token")
    var token: String
)
