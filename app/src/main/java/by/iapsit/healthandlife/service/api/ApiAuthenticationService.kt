package by.iapsit.healthandlife.service.api

import by.iapsit.healthandlife.domain.dto.AuthenticationRequest
import by.iapsit.healthandlife.domain.dto.AuthenticationResponse
import by.iapsit.healthandlife.domain.dto.RegistrationRequest
import by.iapsit.healthandlife.domain.entity.AppUser
import by.iapsit.healthandlife.domain.model.User
import by.iapsit.healthandlife.utils.Constants
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
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