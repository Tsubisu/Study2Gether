package com.example.studygether.Utility

fun validateEmail(value: String): String {
    return when {
        value.isBlank() -> "Email cannot be empty"
        !android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches() -> "Invalid email format"
        else -> ""
    }
}

fun validatePassword(value: String): String {
    return when {
        value.isBlank() -> "Password cannot be empty"
        value.length < 8 -> "Must be at least 8 characters"
        !value.any { it.isDigit() } -> "Must contain at least one number"
        !value.any { it.isUpperCase() } -> "Must contain at least one uppercase letter"
        !value.any { it.isLowerCase() } -> "Must contain at least one lowercase letter"
        else -> ""
    }
}

 fun validateName(value: String, fieldName: String): String {
    return when {
        value.isBlank() -> "$fieldName cannot be empty"
        value.length < 2 -> "$fieldName is too short"
        !value.all { it.isLetter() || it.isWhitespace() } -> "$fieldName contains invalid characters"
        else -> ""
    }
}