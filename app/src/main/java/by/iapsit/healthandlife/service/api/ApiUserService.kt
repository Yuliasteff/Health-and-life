package by.iapsit.healthandlife.service.api

import by.iapsit.healthandlife.domain.dto.UpdateUserRequest
import by.iapsit.healthandlife.domain.model.User
import by.iapsit.healthandlife.utils.Constants
import retrofit2.Call
import retrofit2.http.*

interface ApiUserService {

    @PUT(Constants.USERS_URL)
    @Headers("Content-type: application/json; charset=UTF-8")
    fun updateUser(@Header("Authorization") authHeader: String, @Body updateRequest: UpdateUserRequest): Call<User>

    @GET(Constants.USERS_URL)
    @Headers("Content-type: application/json; charset=UTF-8")
    fun getUser(@Header("Authorization") authHeader: String, userEmail: String) : Call<User>

}