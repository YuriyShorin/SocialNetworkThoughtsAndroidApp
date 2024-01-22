package hse.course.socialnetworkthoughtsandroidapp.ui


import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

import dagger.hilt.android.AndroidEntryPoint

import hse.course.socialnetworkthoughtsandroidapp.ui.authentication.LoginActivity
import hse.course.socialnetworkthoughtsandroidapp.ui.posts.PostsActivity
import hse.course.socialnetworkthoughtsandroidapp.viewmodel.AuthenticationViewModel

import java.time.LocalDateTime

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val authenticationViewModel: AuthenticationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        val jwtToken: String? = authenticationViewModel.getJwtToken()
        val jwtTokenExpiresIn: LocalDateTime? = authenticationViewModel.getTokenExpiresIn()

        val intent: Intent =
            if ((jwtToken == null) || (jwtTokenExpiresIn == null) || jwtTokenExpiresIn.isBefore(
                    LocalDateTime.now()
                )
            ) {
                Intent(this, LoginActivity::class.java)
            } else {
                Intent(this, PostsActivity::class.java)
            }

        startActivity(intent)
    }
}