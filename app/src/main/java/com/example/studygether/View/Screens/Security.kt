package com.example.studygether.View.Screens

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studygether.ViewModels.AppBarsViewModel
import com.example.studygether.View.AppBars.BottomBars
import com.example.studygether.ViewModels.BottomBarState

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studygether.R
import com.example.studygether.ui.theme.StudyGetherTheme
import com.example.studygether.ui.theme.loginbutton
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.example.studygether.ui.theme.Typography
import com.example.studygether.ui.theme.ThemeManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.OutlinedTextFieldDefaults

import androidx.compose.ui.text.font.FontWeight

@Composable
fun SecurityBody(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val activity = LocalActivity.current as ComponentActivity
    val appBarsViewModel: AppBarsViewModel = viewModel(activity)

    val currentTheme by ThemeManager.currentTheme.collectAsStateWithLifecycle()
    val isDarkMode by ThemeManager.isDarkMode.collectAsStateWithLifecycle()
    val barBgColor = MaterialTheme.colorScheme.background

    LaunchedEffect(currentTheme, isDarkMode) {
        appBarsViewModel.setTitleBar(
            title = { Text("Security", style = Typography.headlineMedium) },
            showBackButton = true,
            actions = {},
            barColor = barBgColor
        )
        appBarsViewModel.setBottomBarType(BottomBarState(BottomBars.None))
    }

    val isPreview = LocalInspectionMode.current
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    
    var currentPasswordVisible by remember { mutableStateOf(false) }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Update Password",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Text(
                    text = "Ensure your account security by updating your password. Choose a strong combination of at least 6 characters.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 22.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Current Password
                PasswordField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    label = "Current Password",
                    isVisible = currentPasswordVisible,
                    onToggleVisibility = { currentPasswordVisible = !currentPasswordVisible }
                )

                // New Password
                PasswordField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = "New Password",
                    isVisible = newPasswordVisible,
                    onToggleVisibility = { newPasswordVisible = !newPasswordVisible }
                )

                // Confirm New Password
                PasswordField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = "Confirm New Password",
                    isVisible = confirmPasswordVisible,
                    onToggleVisibility = { confirmPasswordVisible = !confirmPasswordVisible }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Update Button
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    enabled = !isLoading,
                    onClick = {
                        if (isPreview) {
                            Toast.makeText(context, "Mock Update Success", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if (newPassword.length < 6) {
                            Toast.makeText(context, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if (newPassword != confirmPassword) {
                            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        isLoading = true
                        val user = FirebaseAuth.getInstance().currentUser
                        if (user != null && user.email != null) {
                            val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)
                            user.reauthenticate(credential).addOnCompleteListener { reauthTask ->
                                if (reauthTask.isSuccessful) {
                                    user.updatePassword(newPassword).addOnCompleteListener { updateTask ->
                                        isLoading = false
                                        if (updateTask.isSuccessful) {
                                            Toast.makeText(context, "Password updated successfully", Toast.LENGTH_SHORT).show()
                                            onNavigateBack()
                                        } else {
                                            Toast.makeText(context, updateTask.exception?.message ?: "Update failed", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } else {
                                    isLoading = false
                                    Toast.makeText(context, "Authentication failed. Check current password.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            isLoading = false
                            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            "Update Password",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isVisible: Boolean,
    onToggleVisibility: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = onToggleVisibility) {
                Icon(
                    painter = painterResource(
                        id = if (isVisible) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24
                    ),
                    contentDescription = if (isVisible) "Hide password" else "Show password"
                )
            }
        },
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
            focusedLabelColor = MaterialTheme.colorScheme.primary
        ),
        singleLine = true
    )
}

//@Preview(showBackground = true)
//@Composable
//fun SecurityPreview() {
//    StudyGetherTheme {
//        SecurityBody()
//    }
//}
