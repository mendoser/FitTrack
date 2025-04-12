package com.example.fittrack.ui.screens.workouts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.fittrack.ui.components.StatsCard
import com.example.fittrack.ui.components.WorkoutCard
import com.example.fittrack.ui.navigation.FitTrackBottomNavigation
import com.example.fittrack.ui.viewmodels.HomeViewModel

// HomeScreen.kt
@Composable
fun HomeScreen(
    navController: NavHostController,
    navigateToWorkouts: () -> Unit,
    navigateToStartWorkout: () -> Unit,
    navigateToWorkoutDetails: (String) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = { FitTrackBottomNavigation(navController = navController) },
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToStartWorkout) {
                Icon(Icons.Default.Add, contentDescription = "Start Workout")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Text(
                    text = "Welcome, ${uiState.userName}",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            item {
                Text(
                    text = "Your Stats",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatsCard(
                        icon = Icons.AutoMirrored.Filled.DirectionsRun,
                        value = uiState.totalDistance.toString(),
                        unit = "km",
                        label = "Distance",
                        modifier = Modifier.weight(1f)
                    )

                    StatsCard(
                        icon = Icons.Default.Schedule,
                        value = uiState.totalDuration,
                        unit = "",
                        label = "Time",
                        modifier = Modifier.weight(1f)
                    )

                    StatsCard(
                        icon = Icons.Default.Favorite,
                        value = uiState.totalCalories.toString(),
                        unit = "kcal",
                        label = "Calories",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Workouts",
                        style = MaterialTheme.typography.titleLarge
                    )

                    TextButton(onClick = navigateToWorkouts) {
                        Text("See All")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            if (uiState.recentWorkouts.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No workouts yet",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Start tracking your fitness journey today!",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(uiState.recentWorkouts) { workout ->
                    WorkoutCard(
                        workout = workout,
                        onClick = { navigateToWorkoutDetails(workout.id) },
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}
