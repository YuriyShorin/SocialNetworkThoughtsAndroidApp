package hse.course.socialnetworkthoughtsandroidapp.repository

import android.content.SharedPreferences
import hse.course.socialnetworkthoughtsandroidapp.api.PostService
import hse.course.socialnetworkthoughtsandroidapp.model.CreatePost
import hse.course.socialnetworkthoughtsandroidapp.utils.SharedPreferencesKeys
import java.util.UUID
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val postService: PostService
) {

    private fun getJwtToken(): String? {
        return sharedPreferences.getString(SharedPreferencesKeys.JWT_TOKEN.name, null)
    }

    suspend fun createPost(post: CreatePost): Int {
        val jwtToken = getJwtToken() ?: return 403
        val response = postService.createPost("Bearer $jwtToken", post)
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
}