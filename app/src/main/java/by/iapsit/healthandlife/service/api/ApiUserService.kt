package by.iapsit.healthandlife.service.api

import by.iapsit.healthandlife.domain.dto.UpdateUserRequest
import by.iapsit.healthandlife.domain.dto.User
import by.iapsit.healthandlife.constants.Constants
import retrofit2.Call
import retrofit2.http.*

interface ApiUserService {

    @PATCH(Constants.USERS_URL)
    @Headers("Content-type: application/json")
    fun updateUser(@Header("Authorization") authHeader: String, @Body updateRequest: UpdateUserRequest): Call<User>

    @GET(Constants.USERS_URL)
    @Headers("Content-type: application/json")
    fun getUser(@Header("Authorization") authHeader: String, @Query("email") userEmail: String) : Call<User>

}