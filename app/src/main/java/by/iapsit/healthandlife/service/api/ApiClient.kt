package by.iapsit.healthandlife.service.api

import by.iapsit.healthandlife.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    private lateinit var authService: ApiAuthenticationService

    fun getAuthService(): ApiAuthenticationService {
        if(!::authService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            authService = retrofit.create(ApiAuthenticationService::class.java)
        }
        return authService
    }

}