package com.example.studygether.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studygether.Model.ChannelModel
import com.example.studygether.Model.Community
import com.example.studygether.Repository.AppRepositories
import com.example.studygether.Repository.ChannelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.example.studygether.Utility.validateEmail
import com.example.studygether.Utility.validatePassword
import kotlinx.coroutines.launch

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

    fun onRegister() {
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

        if (hasErrors) return  // stop here, errors are already shown in UI

        viewModelScope.launch {
            _uiState.update { it.copy(isRegistering = true, registrationError = "") }

            val community = Community(
                name = state.communityName,
                createdAt = System.currentTimeMillis()
            )

            val result = AppRepositories.communityRepository.registerAndCreateCommunity(
                community = community,
                email = state.email,
                username = "${state.firstName} ${state.lastName}",
                password = state.password
            )

            result.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isRegistering = false,
                            registrationSuccess = true
                        )
                    }

                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isRegistering = false,
                            registrationError = when {
                                error.message?.contains("email already exists") == true ->
                                    "This email is already registered. Please log in instead."
                                error.message?.contains("network") == true ->
                                    "No internet connection. Please try again."
                                else ->
                                    error.message ?: "Something went wrong. Please try again."
                            }
                        )
                    }
                }
            )
        }
    }


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
