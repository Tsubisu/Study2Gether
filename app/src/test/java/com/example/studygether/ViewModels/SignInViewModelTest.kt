package com.example.studygether.ViewModels

import com.example.studygether.Repository.AppRepositories
import com.example.studygether.Repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class SignInViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val mockUserRepository: UserRepository = mock()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        AppRepositories.userRepository = mockUserRepository
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testFirstNameValidation_tooShort() {
        val viewModel = SignInViewModel()
        viewModel.onFirstNameChange("A")
        
        val state = viewModel.uiState.value
        assertEquals("A", state.firstName)
        assertEquals("First Name is too short", state.firstNameError)
    }

    @Test
    fun testPasswordValidation_missingUppercase() {
        val viewModel = SignInViewModel()
        viewModel.onPasswordChange("password123")
        
        val state = viewModel.uiState.value
        assertEquals("password123", state.password)
        assertEquals("Must contain at least one uppercase letter", state.passwordError)
    }

    @Test
    fun testConfirmPassword_mismatch() {
        val viewModel = SignInViewModel()
        viewModel.onPasswordChange("Password123")
        viewModel.onConfirmPasswordChange("Password12")
        
        val state = viewModel.uiState.value
        assertEquals("Passwords do not match", state.confirmPasswordError)
    }

    @Test
    fun testRegister_withErrors_doesNotCallRepository() = runTest {
        val viewModel = SignInViewModel()
        viewModel.onFirstNameChange("A")
        viewModel.onRegister()
        
        val state = viewModel.uiState.value
        assertFalse(state.isRegistering)
        assertFalse(state.registrationSuccess)
    }

    @Test
    fun testRegister_success_updatesUiState() = runTest {
        whenever(mockUserRepository.registerUser(any(), any(), any())).doReturn(Result.success("user-id-123"))

        val viewModel = SignInViewModel()
        viewModel.onFirstNameChange("John")
        viewModel.onLastNameChange("Doe")
        viewModel.onEmailChange("john.doe@example.com")
        viewModel.onPasswordChange("Password123")
        viewModel.onConfirmPasswordChange("Password123")

        viewModel.onRegister()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isRegistering)
        assertTrue(state.registrationSuccess)
        assertEquals("", state.registrationError)
    }
}
