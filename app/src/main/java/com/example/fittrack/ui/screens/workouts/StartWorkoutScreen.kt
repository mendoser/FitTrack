package com.example.fittrack.ui.screens.workouts

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun StartWorkoutScreen(
    navigateUp: () -> Unit,
    navigateToWorkoutDetails: (String) -> Unit
) {
    Column {
        Text("Start a new workout")
        Button(onClick = { navigateToWorkoutDetails("startedWorkoutId") }) {
            Text("Finish Workout")
        }
        Button(onClick = navigateUp) {
            Text("Cancel")
        }
    }
}
