package by.iapsit.healthandlife.service.api

import by.iapsit.healthandlife.constants.Constants
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.time.LocalDate

class ApiClient {

    private lateinit var authService: ApiAuthenticationService
    private lateinit var userService: ApiUserService
    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateSerializer())
        .create()

    fun getAuthService(): ApiAuthenticationService {
        if (!::authService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            authService = retrofit.create(ApiAuthenticationService::class.java)
        }
        return authService
    }

    fun getUserService(): ApiUserService {
        if (!::userService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            userService = retrofit.create(ApiUserService::class.java)
        }
        return userService
    }

}