package hse.course.socialnetworkthoughtsandroidapp.repository

import android.content.SharedPreferences
import hse.course.socialnetworkthoughtsandroidapp.api.PostService
import hse.course.socialnetworkthoughtsandroidapp.model.Comment
import hse.course.socialnetworkthoughtsandroidapp.model.CreateComment
import hse.course.socialnetworkthoughtsandroidapp.utils.SharedPreferencesKeys
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.UUID
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val postService: PostService
) {

    private fun getJwtToken(): String? {
        return sharedPreferences.getString(SharedPreferencesKeys.JWT_TOKEN.name, null)
    }

    suspend fun createPost(
        theme: RequestBody,
        content: RequestBody,
        images: List<MultipartBody.Part>?
    ): Int {
        val jwtToken = getJwtToken() ?: return 403
        val response = postService.createPost("Bearer $jwtToken", theme, content, images)
        return response.code()
    }

    suspend fun deletePost(postId: UUID): Int {
        val jwtToken = getJwtToken() ?: return 403
        val response = postService.deletePost("Bearer $jwtToken", postId)
        return response.code()
    }

    suspend fun likePost(postId: UUID): Int {
        val jwtToken = getJwtToken() ?: return 403
        val response = postService.likePost("Bearer $jwtToken", postId)
        return response.code()
    }

    suspend fun unlikePost(postId: UUID): Int {
        val jwtToken = getJwtToken() ?: return 403
        val response = postService.unlikePost("Bearer $jwtToken", postId)
        return response.code()
    }

    suspend fun getComments(postId: UUID): List<Comment>? {
        val jwtToken = getJwtToken()
        var comments: List<Comment>? = null

        val response = postService.getComments("Bearer $jwtToken", postId)
        if (response.code() == 200) {
            comments = response.body()
        }

        return comments
    }

    suspend fun commentPost(createComment: CreateComment): Int {
        val jwtToken = getJwtToken() ?: return 403
        val response = postService.commentPost("Bearer $jwtToken", createComment)
        return response.code()
    }

    suspend fun deleteComment(commentId: UUID): Int {
        val jwtToken = getJwtToken() ?: return 403
        val response = postService.deleteComment("Bearer $jwtToken", commentId)
        return response.code()
    }
}