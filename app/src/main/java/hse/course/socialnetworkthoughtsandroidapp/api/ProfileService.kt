package hse.course.socialnetworkthoughtsandroidapp.api

import hse.course.socialnetworkthoughtsandroidapp.model.Profile
import hse.course.socialnetworkthoughtsandroidapp.model.SearchProfile
import okhttp3.MultipartBody
import okhttp3.RequestBody

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

import java.util.UUID

interface ProfileService {

    @GET("api/v1/profile")
    suspend fun getCurrentProfile(@Header("Authorization") authorization: String): Response<Profile>

    @GET("api/v1/profile/{profileId}")
    suspend fun getProfile(
        @Header("Authorization") authorization: String,
        @Path("profileId") profileId: UUID
    ): Response<Profile>

    @POST("api/v1/profile/subscribe/{profileId}")
    suspend fun subscribe(
        @Header("Authorization") authorization: String,
        @Path("profileId") profileId: UUID
    ): Response<Void>

    @POST("api/v1/profile/unsubscribe/{profileId}")
    suspend fun unsubscribe(
        @Header("Authorization") authorization: String,
        @Path("profileId") profileId: UUID
    ): Response<Void>

    @GET("api/v1/profile/subscriptions")
    suspend fun getCurrentProfileSubscriptions(
        @Header("Authorization") authorization: String
    ): Response<List<SearchProfile>>

    @GET("api/v1/profile/{profileId}/subscriptions")
    suspend fun getProfileSubscriptions(
        @Header("Authorization") authorization: String,
        @Path("profileId") profileId: UUID
    ): Response<List<SearchProfile>>

    @GET("api/v1/profile/subscribers")
    suspend fun getCurrentProfileSubscribers(
        @Header("Authorization") authorization: String
    ): Response<List<SearchProfile>>

    @GET("api/v1/profile/{profileId}/subscribers")
    suspend fun getProfileSubscribers(
        @Header("Authorization") authorization: String,
        @Path("profileId") profileId: UUID
    ): Response<List<SearchProfile>>

    @Multipart
    @PUT("api/v1/profile")
    suspend fun updateProfile(
        @Header("Authorization") authorization: String,
        @Part("nickname") nickname: RequestBody,
        @Part("status") status: RequestBody,
        @Part profilePicture: MultipartBody.Part?
    ): Response<Void>

    companion object Factory {

        private const val BASE_URL: String = "http://10.0.2.2:8080/"

        fun create(): ProfileService {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(ProfileService::class.java)
        }
    }
}