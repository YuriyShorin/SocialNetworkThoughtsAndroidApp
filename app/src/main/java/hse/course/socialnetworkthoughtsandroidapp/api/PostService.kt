package hse.course.socialnetworkthoughtsandroidapp.api

import hse.course.socialnetworkthoughtsandroidapp.model.Comment
import hse.course.socialnetworkthoughtsandroidapp.model.CreateComment
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import java.util.UUID

interface PostService {

    @Multipart
    @POST("api/v1/post")
    suspend fun createPost(
        @Header("Authorization") authorization: String,
        @Part("theme") theme: RequestBody,
        @Part("content") content: RequestBody,
        @Part files: List<MultipartBody.Part>?
    ): Response<Void>

    @DELETE("api/v1/post/{postId}")
    suspend fun deletePost(
        @Header("Authorization") authorization: String,
        @Path("postId") postId: UUID
    ): Response<Void>

    @POST("api/v1/post/like/{postId}")
    suspend fun likePost(
        @Header("Authorization") authorization: String,
        @Path("postId") postId: UUID
    ): Response<Void>

    @GET("api/v1/post/{postId}/comments")
    suspend fun getComments(
        @Header("Authorization") authorization: String,
        @Path("postId") postId: UUID
    ): Response<List<Comment>>

    @DELETE("api/v1/post/unlike/{postId}")
    suspend fun unlikePost(
        @Header("Authorization") authorization: String,
        @Path("postId") postId: UUID
    ): Response<Void>

    @POST("api/v1/post/comment")
    suspend fun commentPost(
        @Header("Authorization") authorization: String,
        @Body createComment: CreateComment
    ): Response<Void>

    @DELETE("api/v1/post/comment/{commentId}")
    suspend fun deleteComment(
        @Header("Authorization") authorization: String,
        @Path("commentId") commentId: UUID
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