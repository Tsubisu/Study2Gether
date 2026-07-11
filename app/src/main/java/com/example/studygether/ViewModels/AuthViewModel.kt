package com.example.studygether.ViewModels

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studygether.App.SessionState
import com.example.studygether.Repository.AppRepositories
import com.example.studygether.Utility.validateEmail
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class LoginScreenState(
    val email:String ="",
    val password:String="",

    val emailError: String="",
    val passwordError:String="",
    val isLoading: Boolean = false
)

class LoginScreenViewModel: ViewModel() {
    private val _loginState = MutableStateFlow(LoginScreenState())

    val loginState =_loginState.asStateFlow()


    fun onEmailChange(value:String)
    {
        _loginState.update {
            it.copy(
                email = value,
                emailError = validateEmail(value)
            )
        }

    }
    fun onPasswordChange(value:String)
    {
        _loginState.update {
           it.copy(password = value)
        }
    }

    fun onLogin() {
        val emailError: String = _loginState.value.emailError
        if (emailError.isNotEmpty()) return
        viewModelScope.launch {
            _loginState.update { it.copy(isLoading = true, passwordError = "") }
            AppRepositories.authenticationRepository.loginAndFetchUser(
                _loginState.value.email,
                _loginState.value.password
            ).fold(
                onSuccess = {
                    SessionState.setUser(it)
                    _loginState.update { it.copy(isLoading = false) }
                },
                onFailure = {
                        e ->
                    _loginState.update {
                        it.copy(passwordError = e.message.orEmpty(), isLoading = false)
                    }
                }
            )
        }
    }
}

data class ForgetPasswordScreenState(
    val email:String="",
    val emailError:String=""

)
class ForgetPasswordScreenViewModel:ViewModel()
{
    private val _forgetPasswordState = MutableStateFlow(ForgetPasswordScreenState())
    val forgetPasswordState = _forgetPasswordState.asStateFlow()

    fun onEmailChange(value: String)
    {
        _forgetPasswordState.update {
            it.copy(
                email = value,
                emailError = validateEmail(value)
            )
        }
    }

    fun onSendResendLink()
    {
        val currentEmail = _forgetPasswordState.value.email
        val emailError = validateEmail(currentEmail)

        if (emailError.isNotEmpty()) {
            _forgetPasswordState.update { it.copy(emailError = emailError) }
            return
        }

        viewModelScope.launch {
           val result= AppRepositories.authenticationRepository.resetPassword(currentEmail)
            result.fold(
                onSuccess = { _forgetPasswordState.update {
                    it.copy(
                        emailError = "Reset Link has been Shared to the Email"
                    )
                }},
                onFailure = {e->
                    _forgetPasswordState.update {
                        it.copy(emailError = e.message.orEmpty())
                    }
                }
            )
        }

    }





}