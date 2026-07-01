package com.example.studygether.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.studygether.Model.ProfileModel
import com.example.studygether.Repository.ProfileRepository
import com.example.studygether.Utility.validateEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class LoginState(
    val email:String ="",
    val password:String="",

    val emailError: String="",
    val passwordError:String=""



)
class LoginViewModel: ViewModel() {
    private val _loginState = MutableStateFlow(LoginState())

    val loginState =_loginState.asStateFlow()


    fun onEmailChange(value:String)
    {
        _loginState.update {
            LoginState(
                email = value,
                emailError = validateEmail(value)
            )
        }

    }
    fun onPasswordChange(value:String)
    {
        _loginState.update {
            LoginState(password = value)
        }
    }

    fun onLogin()
    {

    }
}
