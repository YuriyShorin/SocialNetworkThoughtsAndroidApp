package hse.course.socialnetworkthoughtsandroidapp.repository

import hse.course.socialnetworkthoughtsandroidapp.api.AuthenticationService
import hse.course.socialnetworkthoughtsandroidapp.model.JwtToken
import hse.course.socialnetworkthoughtsandroidapp.model.LoginUserCredentials
import hse.course.socialnetworkthoughtsandroidapp.model.RegisterUserCredentials

import javax.inject.Inject

class AuthenticationRepository @Inject constructor(private val authenticationService: AuthenticationService) {

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
}