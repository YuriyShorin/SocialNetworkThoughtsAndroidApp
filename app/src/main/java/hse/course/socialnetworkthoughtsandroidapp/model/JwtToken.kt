package hse.course.socialnetworkthoughtsandroidapp.model

data class JwtToken (
    val token: String? = null,
    val expiresIn: Long? = null,
)