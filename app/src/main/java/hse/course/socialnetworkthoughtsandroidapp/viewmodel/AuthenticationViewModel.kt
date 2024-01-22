package hse.course.socialnetworkthoughtsandroidapp.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import dagger.hilt.android.lifecycle.HiltViewModel

import hse.course.socialnetworkthoughtsandroidapp.model.JwtToken
import hse.course.socialnetworkthoughtsandroidapp.repository.AuthenticationRepository
import hse.course.socialnetworkthoughtsandroidapp.utils.SharedPreferencesKeys

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(private val sharedPreferences: SharedPreferences, private val authenticationRepository: AuthenticationRepository) : ViewModel() {

    private val _jwtToken = MutableStateFlow(JwtToken())
    val jwtToken: StateFlow<JwtToken> = _jwtToken.asStateFlow()

    private val _code = MutableStateFlow(0)
    val code: StateFlow<Int> = _code.asStateFlow()

    private val _usernameErrorMessage = MutableStateFlow("")
    val usernameErrorMessage: StateFlow<String> = _usernameErrorMessage.asStateFlow()

    private val _passwordErrorMessage = MutableStateFlow("")
    val passwordErrorMessage: StateFlow<String> = _passwordErrorMessage.asStateFlow()

    private val _nicknameErrorMessage = MutableStateFlow("")
    val nicknameErrorMessage: StateFlow<String> = _nicknameErrorMessage.asStateFlow()

    private fun saveJwtToken(token: String) {
        sharedPreferences.edit().putString(SharedPreferencesKeys.JWT_TOKEN.name, token).apply()
    }

    private fun saveTokenExpiresIn(seconds: Long) {
        val tokenExpiresIn: LocalDateTime = LocalDateTime.now().plusSeconds(seconds)
        sharedPreferences.edit().putString(
            SharedPreferencesKeys.JWT_TOKEN_EXPIRES_IN.name,
            tokenExpiresIn.format(
                DateTimeFormatter.ISO_LOCAL_DATE_TIME
            ).toString()
        ).apply()
    }

    fun getJwtToken(): String? {
        return sharedPreferences.getString(SharedPreferencesKeys.JWT_TOKEN.name, null)
    }

    fun getTokenExpiresIn(): LocalDateTime? {
        val jwtTokenExpiresInString: String? =
            sharedPreferences.getString(SharedPreferencesKeys.JWT_TOKEN_EXPIRES_IN.name, null)
        var jwtTokenExpiresIn: LocalDateTime? = null

        if (jwtTokenExpiresInString != null) {
            jwtTokenExpiresIn = LocalDateTime.parse(jwtTokenExpiresInString)
        }

        return jwtTokenExpiresIn
    }

    fun authenticate(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            if (username.isEmpty()) {
                _usernameErrorMessage.value = "Введите имя пользователя!"
            }
            if (password.isEmpty()) {
                _passwordErrorMessage.value = "Введите пароль!"
            }
            return
        }

        viewModelScope.launch {
            val jwtToken: JwtToken? = authenticationRepository.authenticate(username, password)
            _jwtToken.value = jwtToken ?: JwtToken()

            if (jwtToken == null) {
                _usernameErrorMessage.value = "Неверный логин или пароль"
                _passwordErrorMessage.value = "Неверный логин или пароль"
                return@launch
            }

            if (jwtToken.token != null && jwtToken.expiresIn != null) {
                saveJwtToken(jwtToken.token)
                saveTokenExpiresIn(jwtToken.expiresIn)
            }
        }
    }

    fun register(username: String, password: String, nickname: String) {
        if (username.isEmpty() || password.isEmpty() || nickname.isEmpty() || password.length < 8) {
            if (username.isEmpty()) {
                _usernameErrorMessage.value = "Введите имя пользователя!"
            }

            if (password.isEmpty()) {
                _passwordErrorMessage.value = "Введите пароль!"
            }
            if (password.isNotEmpty() && password.length < 8) {
                _passwordErrorMessage.value = "Пароль должен содержать не менее 8 символов"
            }

            if (nickname.isEmpty()) {
                _nicknameErrorMessage.value = "Введите никнейм!"
            }

            return
        }

        viewModelScope.launch {
            val code: Int = authenticationRepository.register(username, password, nickname)
            _code.value = code

            if (code == 409) {
                _usernameErrorMessage.value = "Пользователь уже существует"
                return@launch
            }

            if (code != 200) {
                _usernameErrorMessage.value = "Ошибка сервера"
            }
        }
    }
}