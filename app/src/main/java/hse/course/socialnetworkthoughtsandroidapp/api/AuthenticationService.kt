package hse.course.socialnetworkthoughtsandroidapp.api

import hse.course.socialnetworkthoughtsandroidapp.model.JwtToken
import hse.course.socialnetworkthoughtsandroidapp.model.LoginUserCredentials
import hse.course.socialnetworkthoughtsandroidapp.model.RegisterUserCredentials

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthenticationService {

    @POST("api/v1/auth/login")
    suspend fun authenticate(
        @Body loginUserCredentials: LoginUserCredentials
    ): Response<JwtToken>

    @POST("api/v1/auth/signup")
    suspend fun register(
        @Body registerUserCredentials: RegisterUserCredentials
    ): Response<Void>

    @GET("api/v1/auth/me")
    suspend fun me(@Header("Authorization") authorization: String): Response<Void>

    companion object Factory {

        private const val BASE_URL: String = "http://10.0.2.2:8080/"

        fun create(): AuthenticationService {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(AuthenticationService::class.java)
        }
    }
}