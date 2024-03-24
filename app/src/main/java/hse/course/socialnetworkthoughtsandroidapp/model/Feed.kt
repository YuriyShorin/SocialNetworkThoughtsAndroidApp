package hse.course.socialnetworkthoughtsandroidapp.model

import java.sql.Timestamp
import java.util.UUID

data class Feed(
    val postId: UUID,
    val profileId: UUID,
    val profileNickname: String,
    val theme: String,
    val content: String,
    var likes: Long,
    var isLiked: Boolean,
    val comments: Long,
    val reposts: Long,
    val views: Long,
    val createdAt: Timestamp,
    val editedAt: Timestamp
)
