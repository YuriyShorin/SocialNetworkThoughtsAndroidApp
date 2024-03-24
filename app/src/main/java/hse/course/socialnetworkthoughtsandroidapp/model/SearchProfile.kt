package hse.course.socialnetworkthoughtsandroidapp.model

import java.util.UUID

data class SearchProfile(
    val id: UUID? = null,
    val nickname: String? = null,
    var isSubscribed: Boolean? = null
)
