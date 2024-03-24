package hse.course.socialnetworkthoughtsandroidapp.api

import hse.course.socialnetworkthoughtsandroidapp.model.Feed
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

interface FeedService {

    @GET("api/v1/feed")
    suspend fun getFeed(
        @Header("Authorization") authorization: String,
    ): Response<List<Feed>>

    companion object Factory {

        private const val BASE_URL: String = "http://10.0.2.2:8080/"

        fun create(): FeedService {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(FeedService::class.java)
        }
    }
}