package hse.course.socialnetworkthoughtsandroidapp.repository

import android.content.SharedPreferences

import hse.course.socialnetworkthoughtsandroidapp.api.AuthenticationService
import hse.course.socialnetworkthoughtsandroidapp.model.JwtToken
import hse.course.socialnetworkthoughtsandroidapp.model.LoginUserCredentials
import hse.course.socialnetworkthoughtsandroidapp.model.RegisterUserCredentials
import hse.course.socialnetworkthoughtsandroidapp.utils.SharedPreferencesKeys

import javax.inject.Inject

class AuthenticationRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val authenticationService: AuthenticationService
) {

    private fun getJwtToken(): String? {
        return sharedPreferences.getString(SharedPreferencesKeys.JWT_TOKEN.name, null)
    }

    suspend fun authenticate(username: String, password: String): JwtToken? {
        var jwtToken: JwtToken? = null

        val response =
            authenticationService.authenticate(LoginUserCredentials(username, password))

        if (response.code() == 200) {
            jwtToken = response.body()
        }

        return jwtToken
    }

    suspend fun register(username: String, password: String, nickname: String): Int {
        val response =
            authenticationService.register(
                RegisterUserCredentials(
                    username,
                    password,
                    nickname
                )
            )

        return response.code()
    }

    suspend fun isAuthenticated(): Boolean {
        val jwtToken = getJwtToken() ?: return false
        val response = authenticationService.me("Bearer $jwtToken")

        return response.code() == 200
    }
}