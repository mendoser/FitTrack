package com.example.fittrack.ui.screens.workouts

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RegisterScreen(
    navigateToLogin: () -> Unit,
    navigateToHome: () -> Unit
) {
    Scaffold { padding ->
        Button(
            onClick = navigateToHome,
            modifier = Modifier.padding(padding)
        ) {
            Text("Register (Go to Home)")
        }

        TextButton(onClick = navigateToLogin) {
            Text("Already have an account? Login")
        }
    }
}
