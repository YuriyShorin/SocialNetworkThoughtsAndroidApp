package hse.course.socialnetworkthoughtsandroidapp.api

import hse.course.socialnetworkthoughtsandroidapp.model.Feed
import hse.course.socialnetworkthoughtsandroidapp.model.SearchProfile
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface SearchService {

    @GET("api/v1/search/profiles/{nickname}")
    suspend fun searchProfiles(
        @Header("Authorization") authorization: String,
        @Path("nickname") nickname: String
    ): Response<List<SearchProfile>>

    @GET("api/v1/search/posts/{theme}")
    suspend fun searchPosts(
        @Header("Authorization") authorization: String,
        @Path("theme") theme: String
    ): Response<List<Feed>>

    companion object Factory {

        private const val BASE_URL: String = "http://10.0.2.2:8080/"

        fun create(): SearchService {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(SearchService::class.java)
        }
    }
}