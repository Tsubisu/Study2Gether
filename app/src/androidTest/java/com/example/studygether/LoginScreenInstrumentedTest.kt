package com.example.studygether

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.studygether.View.Screens.LoginScreen
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenInstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testLoginScreen_initialState() {
        composeTestRule.setContent {
            LoginScreen()
        }

        
        composeTestRule.onNodeWithTag("email_input").assertExists()
        composeTestRule.onNodeWithTag("password_input").assertExists()
        composeTestRule.onNodeWithTag("login_button").assertExists()
        composeTestRule.onNodeWithTag("sign_in_button").assertExists()
    }

    @Test
    fun testLoginScreen_typingEmail() {
        composeTestRule.setContent {
            LoginScreen()
        }

        
        composeTestRule.onNodeWithTag("email_input").performTextInput("test@example.com")
        composeTestRule.onNodeWithTag("email_input").assert(hasText("test@example.com"))
    }

    @Test
    fun testLoginScreen_typingPassword() {
        composeTestRule.setContent {
            LoginScreen()
        }

        
        composeTestRule.onNodeWithTag("password_input").performTextInput("Secret123")
        composeTestRule.onNodeWithTag("password_input").assertExists()
    }

    @Test
    fun testLoginScreen_togglePasswordVisibility() {
        composeTestRule.setContent {
            LoginScreen()
        }

        
        composeTestRule.onNodeWithTag("password_toggle").assertExists()
        composeTestRule.onNodeWithTag("password_toggle").performClick()
    }

    @Test
    fun testLoginScreen_clickSignIn_triggersCallback() {
        var signInClicked = false
        composeTestRule.setContent {
            LoginScreen(
                onSignIn = {
                    signInClicked = true
                }
            )
        }

        
        composeTestRule.onNodeWithTag("sign_in_button").performClick()

        
        assertTrue(signInClicked)
    }
}
