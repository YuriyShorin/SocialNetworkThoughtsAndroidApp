package hse.course.socialnetworkthoughtsandroidapp.ui.authentication

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle

import dagger.hilt.android.AndroidEntryPoint

import hse.course.socialnetworkthoughtsandroidapp.databinding.LoginActivityLayoutBinding
import hse.course.socialnetworkthoughtsandroidapp.ui.socialmedia.SocialMediaActivity
import hse.course.socialnetworkthoughtsandroidapp.viewmodel.authentication.AuthenticationViewModel

import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val authenticationViewModel: AuthenticationViewModel by viewModels()

    private lateinit var binding: LoginActivityLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LoginActivityLayoutBinding.inflate(layoutInflater)
        init()
    }

    private fun init() {
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.loginButton.setOnClickListener { login() }

        binding.registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                authenticationViewModel.jwtToken.collect { jwtToken ->
                    if (jwtToken.jwtToken != null) {
                        val intent = Intent(applicationContext, SocialMediaActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                authenticationViewModel.usernameErrorMessage.collect { usernameErrorMessage ->
                    if (usernameErrorMessage.isNotEmpty()) {
                        binding.usernameTextInputLayout.error = usernameErrorMessage
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                authenticationViewModel.passwordErrorMessage.collect { passwordErrorMessage ->
                    if (passwordErrorMessage.isNotEmpty()) {
                        binding.passwordTextInputLayout.error = passwordErrorMessage
                    }
                }
            }
        }
    }

    private fun login() {
        val username = binding.usernameTextInputLayout.editText?.text.toString()
        val password = binding.passwordTextInputLayout.editText?.text.toString()

        authenticationViewModel.authenticate(username, password)
    }
}