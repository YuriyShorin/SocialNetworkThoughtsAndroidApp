package hse.course.socialnetworkthoughtsandroidapp.ui


import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import hse.course.socialnetworkthoughtsandroidapp.ui.authentication.LoginActivity
import hse.course.socialnetworkthoughtsandroidapp.ui.socialmedia.SocialMediaActivity
import hse.course.socialnetworkthoughtsandroidapp.viewmodel.authentication.AuthenticationViewModel

import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val authenticationViewModel: AuthenticationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        authenticationViewModel.isAuthenticated()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                authenticationViewModel.isAuthenticated.collect { isAuthenticated ->
                    if (isAuthenticated) {
                        val intent = Intent(applicationContext, SocialMediaActivity::class.java)
                        startActivity(intent)
                    } else {
                        val intent = Intent(applicationContext, LoginActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}