package hse.course.socialnetworkthoughtsandroidapp.api

import hse.course.socialnetworkthoughtsandroidapp.model.CreatePost
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.UUID

interface PostService {

    @POST("api/v1/post")
    suspend fun createPost(
        @Header("Authorization") authorization: String,
        @Body post: CreatePost
    ): Response<Void>

    @POST("api/v1/post/like/{postId}")
    suspend fun likePost(
        @Header("Authorization") authorization: String,
        @Path("postId") postId: UUID
    ): Response<Void>

    @DELETE("api/v1/post/unlike/{postId}")
    suspend fun unlikePost(
        @Header("Authorization") authorization: String,
        @Path("postId") postId: UUID
    ): Response<Void>

    companion object Factory {

        private const val BASE_URL: String = "http://10.0.2.2:8080/"

        fun create(): PostService {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(PostService::class.java)
        }
    }
}