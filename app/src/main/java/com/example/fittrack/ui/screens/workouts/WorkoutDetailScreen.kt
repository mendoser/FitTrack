package com.example.fittrack.ui.screens.workouts

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutDetailScreen(
    workoutId: String,
    navigateUp: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Workout Details") })
        }
    ) {
        Column {
            Text("Workout ID: $workoutId")
            Button(onClick = navigateUp) {
                Text("Back")
            }
        }
    }
}
