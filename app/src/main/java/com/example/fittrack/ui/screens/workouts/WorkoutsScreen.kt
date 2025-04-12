package com.example.fittrack.ui.screens.workouts

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun WorkoutsScreen(
    navigateToWorkoutDetails: (String) -> Unit
) {
    Column {
        Text("Workout List")
        Button(onClick = { navigateToWorkoutDetails("exampleWorkoutId") }) {
            Text("Go to Workout Details")
        }
    }
}
