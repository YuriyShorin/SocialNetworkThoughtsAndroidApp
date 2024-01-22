package hse.course.socialnetworkthoughtsandroidapp.ui.authentication

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint

import hse.course.socialnetworkthoughtsandroidapp.databinding.RegisterActivityLayoutBinding
import hse.course.socialnetworkthoughtsandroidapp.viewmodel.AuthenticationViewModel

import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private val authenticationViewModel: AuthenticationViewModel by viewModels()

    private lateinit var binding: RegisterActivityLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterActivityLayoutBinding.inflate(layoutInflater)
        init()
    }

    private fun init() {
        supportActionBar?.hide()
        setContentView(binding.root)

        binding.registerAppBar.setNavigationOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.registerButton.setOnClickListener { register() }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                authenticationViewModel.code.collect { code ->
                    if (code == 200) {
                        val intent = Intent(applicationContext, LoginActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                authenticationViewModel.usernameErrorMessage.collect { usernameErrorMessage ->
                    if (usernameErrorMessage != "") {
                        binding.usernameTextInputLayout.error = usernameErrorMessage
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                authenticationViewModel.passwordErrorMessage.collect { passwordErrorMessage ->
                    if (passwordErrorMessage != "") {
                        binding.passwordTextInputLayout.error = passwordErrorMessage
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                authenticationViewModel.nicknameErrorMessage.collect { nicknameErrorMessage ->
                    if (nicknameErrorMessage != "") {
                        binding.nicknameTextInputLayout.error = nicknameErrorMessage
                    }
                }
            }
        }
    }

    private fun register() {
        val username = binding.usernameTextInputLayout.editText?.text.toString()
        val password = binding.passwordTextInputLayout.editText?.text.toString()
        val nickname = binding.nicknameTextInputLayout.editText?.text.toString()

        authenticationViewModel.register(username, password, nickname)
    }
}