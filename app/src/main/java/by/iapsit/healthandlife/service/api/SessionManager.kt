package by.iapsit.healthandlife.service.api

import android.content.Context
import android.content.SharedPreferences
import by.iapsit.healthandlife.R
import by.iapsit.healthandlife.domain.entity.AppUser

class SessionManager(context: Context) {

    private var prefs: SharedPreferences = context
        .getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
        const val CURRENT_USER_EMAIL = "current_user_email"
    }

    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun getAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun getCurrentUserEmail(): String? {
        return prefs.getString(CURRENT_USER_EMAIL, null)
    }

    fun saveCurrentUserEmail(userEmail: String) {
        val editor = prefs.edit()
        editor.putString(CURRENT_USER_EMAIL, userEmail)
        editor.apply()
    }

}