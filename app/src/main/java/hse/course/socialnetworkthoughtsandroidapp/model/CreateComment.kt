package hse.course.socialnetworkthoughtsandroidapp.model

import java.util.UUID

data class CreateComment(
    val postId: UUID,
    val content: String
)
