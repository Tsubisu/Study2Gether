package com.example.studygether.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.studygether.Model.ChannelModel
import com.example.studygether.Repository.ChannelRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.example.studygether.Utility.validateEmail
import com.example.studygether.Utility.validatePassword
data class CommunityCreationUiState(
    val firstName: String = "",
    val lastName: String = "",
    val communityName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",

    // Errors
    val firstNameError: String = "",
    val lastNameError: String = "",
    val communityNameError: String = "",
    val emailError: String = "",
    val passwordError: String = "",
    val confirmPasswordError: String = "",

    val isRegistering: Boolean = false,
    val registrationSuccess: Boolean = false,
    val registrationError: String = ""
)

class CommunityCreationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CommunityCreationUiState())
    val uiState = _uiState.asStateFlow()


    fun onFirstNameChange(value: String) {
        _uiState.update {
            it.copy(
                firstName = value,
                firstNameError = validateName(value, "First Name")
            )
        }
    }

    fun onLastNameChange(value: String) {
        _uiState.update {
            it.copy(
                lastName = value,
                lastNameError = validateName(value, "Last Name")
            )
        }
    }

    fun onCommunityNameChange(value: String) {
        _uiState.update {
            it.copy(
                communityName = value,
                communityNameError = validateCommunityName(value)
            )
        }
    }

    fun onEmailChange(value: String) {
        _uiState.update {
            it.copy(
                email = value,
                emailError = validateEmail(value)
            )
        }
    }

    fun onPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                password = value,
                passwordError = validatePassword(value),
                // re-validate confirm password when password changes
                confirmPasswordError = validateConfirmPassword(value, it.confirmPassword)
            )
        }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                confirmPassword = value,
                confirmPasswordError = validateConfirmPassword(_uiState.value.password, value)
            )
        }
    }

    // --- Register ---

    fun onRegister() {
        // Validate all fields on submit
        val state = _uiState.value
        val firstNameError = validateName(state.firstName, "First Name")
        val lastNameError = validateName(state.lastName, "Last Name")
        val communityNameError = validateCommunityName(state.communityName)
        val emailError = validateEmail(state.email)
        val passwordError = validatePassword(state.password)
        val confirmPasswordError = validateConfirmPassword(state.password, state.confirmPassword)

        _uiState.update {
            it.copy(
                firstNameError = firstNameError,
                lastNameError = lastNameError,
                communityNameError = communityNameError,
                emailError = emailError,
                passwordError = passwordError,
                confirmPasswordError = confirmPasswordError
            )
        }

        val hasErrors = listOf(
            firstNameError, lastNameError, communityNameError,
            emailError, passwordError, confirmPasswordError
        ).any { it.isNotEmpty() }

        if (!hasErrors) {
            // TODO: call your repository here
            _uiState.update { it.copy(isRegistering = true) }
        }
    }

    // --- Validators ---

    private fun validateName(value: String, fieldName: String): String {
        return when {
            value.isBlank() -> "$fieldName cannot be empty"
            value.length < 2 -> "$fieldName is too short"
            !value.all { it.isLetter() || it.isWhitespace() } -> "$fieldName contains invalid characters"
            else -> ""
        }
    }

    private fun validateCommunityName(value: String): String {
        return when {
            value.isBlank() -> "Community name cannot be empty"
            value.length < 3 -> "Community name is too short"
            value.length > 50 -> "Community name is too long"
            else -> ""
        }
    }



    private fun validateConfirmPassword(password: String, confirm: String): String {
        return when {
            confirm.isBlank() -> "Please confirm your password"
            confirm != password -> "Passwords do not match"
            else -> ""
        }
    }
}

class ChannelViewModel(val repo: ChannelRepo) : ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _allChannels = MutableLiveData<List<ChannelModel?>?>()
    val allChannels: LiveData<List<ChannelModel?>?> get() = _allChannels

    private val _channel = MutableLiveData<ChannelModel?>()
    val channel: LiveData<ChannelModel?> get() = _channel

    fun joinChannel(channelId: String, userId: String, callback: (Boolean, String) -> Unit) {
        _loading.value = true
        repo.joinChannel(channelId, userId) { success, message ->
            _loading.value = false
            callback(success, message)
        }
    }

    fun leaveChannel(channelId: String, userId: String, callback: (Boolean, String) -> Unit) {
        _loading.value = true
        repo.leaveChannel(channelId, userId) { success, message ->
            _loading.value = false
            callback(success, message)
        }
    }

    fun assignModerator(channelId: String, userId: String, callback: (Boolean, String) -> Unit) {
        _loading.value = true
        repo.assignModerator(channelId, userId) { success, message ->
            _loading.value = false
            callback(success, message)
        }
    }

    fun removeModerator(channelId: String, userId: String, callback: (Boolean, String) -> Unit) {
        _loading.value = true
        repo.removeModerator(channelId, userId) { success, message ->
            _loading.value = false
            callback(success, message)
        }
    }
}