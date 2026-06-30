package com.example.studygether.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.studygether.Model.ProfileModel
import com.example.studygether.Repository.ProfileRepo
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

class ProfileViewModel(val repo: ProfileRepo): ViewModel(){
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _userProfile = MutableLiveData<ProfileModel?>()
    val userProfile: LiveData<ProfileModel?> get() = _userProfile
    val currentUserId: String? get() = repo.getCurrentId()

    fun getUserProfile(id: String) {
        _loading.value = true
        repo.getUserProfile(id) { success, _, profile ->
            _loading.value = false
            if (success) {
                _userProfile.value = profile
            }
        }
    }

    fun updateUsername(id: String, newUsername: String, callback: (Boolean, String) -> Unit) {
        _loading.value = true
        repo.updateUsername(id, newUsername) { success, message ->
            _loading.value = false
            callback(success, message)
        }
    }
}