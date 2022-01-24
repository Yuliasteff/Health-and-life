package by.iapsit.healthandlife.service.api

import by.iapsit.healthandlife.constants.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    private lateinit var authService: ApiAuthenticationService
    private lateinit var userService: ApiUserService

    fun getAuthService(): ApiAuthenticationService {
        if (!::authService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            authService = retrofit.create(ApiAuthenticationService::class.java)
        }
        return authService
    }

    fun getUserService(): ApiUserService {
        if(!::userService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            userService = retrofit.create(ApiUserService::class.java)
        }
        return userService
    }

}