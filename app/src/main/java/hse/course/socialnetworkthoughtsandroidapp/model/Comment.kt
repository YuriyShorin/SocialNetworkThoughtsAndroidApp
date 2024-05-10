package hse.course.socialnetworkthoughtsandroidapp.model

import java.sql.Timestamp
import java.util.UUID

data class Comment(
    val id: UUID,
    val profileId: UUID,
    val postId: UUID,
    val nickname: String,
    val content: String,
    val likes: Long,
    val createdAt: Timestamp,
    val editedAt: Timestamp
)
