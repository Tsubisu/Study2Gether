package com.example.studygether.ViewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.studygether.data.repository.AuthRepository
import com.example.studygether.data.repository.AuthRepositoryImpl

class AuthViewModel(
    private val repository: AuthRepository = AuthRepositoryImpl()
) : ViewModel() {

    private val _resetPasswordState = mutableStateOf<Result<Unit>?>(null)
    val resetPasswordState: State<Result<Unit>?> = _resetPasswordState

    fun resetPassword(email: String) {
        repository.resetPassword(email) { result ->
            _resetPasswordState.value = result
        }
    }
}
