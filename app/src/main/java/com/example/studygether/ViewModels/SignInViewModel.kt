package com.example.studygether.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studygether.Model.Community
import com.example.studygether.Repository.AppRepositories
import com.example.studygether.Utility.validateEmail
import com.example.studygether.Utility.validateName
import com.example.studygether.Utility.validatePassword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class SignInScreenState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",

    val firstNameError: String = "",
    val lastNameError: String = "",
    val emailError: String = "",
    val passwordError: String = "",
    val confirmPasswordError: String = "",

    val isRegistering: Boolean = false,
    val registrationSuccess: Boolean = false,
    val registrationError: String = ""
)

class SignInViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(SignInScreenState())
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
        val emailError = validateEmail(state.email)
        val passwordError = validatePassword(state.password)
        val confirmPasswordError = validateConfirmPassword(state.password, state.confirmPassword)

        _uiState.update {
            it.copy(
                firstNameError = firstNameError,
                lastNameError = lastNameError,
                emailError = emailError,
                passwordError = passwordError,
                confirmPasswordError = confirmPasswordError
            )
        }

        val hasErrors = listOf(
            firstNameError, lastNameError,
            emailError, passwordError, confirmPasswordError
        ).any { it.isNotEmpty() }

        if (hasErrors) return  

        viewModelScope.launch {
            _uiState.update { it.copy(isRegistering = true, registrationError = "")
            }
            val email = state.email
            val username = "${state.firstName} ${state.lastName}"
            val password = state.password



            val result = AppRepositories.userRepository.registerUser(email,username,password)

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




    private fun validateConfirmPassword(password: String, confirm: String): String {
        return when {
            confirm.isBlank() -> "Please confirm your password"
            confirm != password -> "Passwords do not match"
            else -> ""
        }
    }
}