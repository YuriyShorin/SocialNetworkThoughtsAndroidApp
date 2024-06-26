package hse.course.socialnetworkthoughtsandroidapp.model

import java.util.UUID

data class Profile(
    val id: UUID? = null,
    val nickname: String? = null,
    val status: String? = null,
    val subscribes: Int? = null,
    val subscribers: Int? = null,
    val profileImage: String? = null,
    val posts: MutableList<Post>? = null
)
