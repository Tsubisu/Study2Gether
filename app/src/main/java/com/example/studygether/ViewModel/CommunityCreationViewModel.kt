package com.example.studygether.ViewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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
    val uiState: StateFlow<CommunityCreationUiState> = _uiState.asStateFlow()

    // --- Field Updates ---

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

    private fun validateEmail(value: String): String {
        return when {
            value.isBlank() -> "Email cannot be empty"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches() -> "Invalid email format"
            else -> ""
        }
    }

    private fun validatePassword(value: String): String {
        return when {
            value.isBlank() -> "Password cannot be empty"
            value.length < 8 -> "Must be at least 8 characters"
            !value.any { it.isDigit() } -> "Must contain at least one number"
            !value.any { it.isUpperCase() } -> "Must contain at least one uppercase letter"
            !value.any { it.isLowerCase() } -> "Must contain at least one lowercase letter"
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