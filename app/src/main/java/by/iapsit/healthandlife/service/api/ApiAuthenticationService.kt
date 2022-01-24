package by.iapsit.healthandlife.service.api

import by.iapsit.healthandlife.domain.dto.AuthenticationRequest
import by.iapsit.healthandlife.domain.dto.AuthenticationResponse
import by.iapsit.healthandlife.domain.dto.RegistrationRequest
import by.iapsit.healthandlife.domain.dto.User
import by.iapsit.healthandlife.constants.Constants
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiAuthenticationService {

    @POST(Constants.LOGIN_URL)
    @Headers("Content-type: application/json;charset=UTF-8")
    fun login(@Body authRequest: AuthenticationRequest): Call<AuthenticationResponse>

    @POST(Constants.REGISTRATION_URL)
    @Headers("Content-type: application/json;charset=UTF-8")
    fun registration(@Body registrationRequest: RegistrationRequest): Call<User>

}