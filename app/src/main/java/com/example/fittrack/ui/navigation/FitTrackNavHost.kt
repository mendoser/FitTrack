package com.example.fittrack.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.fittrack.ui.screens.workouts.DevicesScreen
import com.example.fittrack.ui.screens.workouts.HomeScreen
import com.example.fittrack.ui.screens.workouts.LoginScreen
import com.example.fittrack.ui.screens.workouts.ProfileScreen
import com.example.fittrack.ui.screens.workouts.RegisterScreen
import com.example.fittrack.ui.screens.workouts.SettingsScreen
import com.example.fittrack.ui.screens.workouts.StartWorkoutScreen
import com.example.fittrack.ui.screens.workouts.WorkoutDetailScreen
import com.example.fittrack.ui.screens.workouts.WorkoutsScreen

@Composable
fun FitTrackNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = modifier
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                navigateToRegister = { navController.navigate(Screen.Register.route) },
                navigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                navigateToLogin = { navController.popBackStack() },
                navigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                navigateToWorkouts = { navController.navigate(Screen.Workouts.route) },
                navigateToStartWorkout = { navController.navigate(Screen.StartWorkout.route) },
                navController = TODO(),
                navigateToWorkoutDetails = TODO(),
                viewModel = TODO()
            )
        }

        composable(Screen.Workouts.route) {
            WorkoutsScreen(
                navigateToWorkoutDetails = { workoutId ->
                    navController.navigate(Screen.WorkoutDetails.createRoute(workoutId))
                }
            )
        }

        composable(
            route = Screen.WorkoutDetails.route,
            arguments = listOf(
                navArgument("workoutId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val workoutId = backStackEntry.arguments?.getString("workoutId") ?: ""
            WorkoutDetailScreen(
                workoutId = workoutId,
                navigateUp = { navController.popBackStack() }
            )
        }

        composable(Screen.StartWorkout.route) {
            StartWorkoutScreen(
                navigateUp = { navController.popBackStack() },
                navigateToWorkoutDetails = { workoutId ->
                    navController.navigate(Screen.WorkoutDetails.createRoute(workoutId)) {
                        popUpTo(Screen.Home.route)
                    }
                }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen()
        }

        composable(Screen.Devices.route) {
            DevicesScreen()
        }

        composable(Screen.Settings.route) {
            SettingsScreen()
        }
    }
}

@Composable
fun SettingsScreen() {
    TODO("Not yet implemented")
}

@Composable
fun DevicesScreen() {
    TODO("Not yet implemented")
}

@Composable
fun ProfileScreen() {
    TODO("Not yet implemented")
}

@Composable
fun RegisterScreen(navigateToLogin: () -> Boolean, navigateToHome: () -> Unit) {
    TODO("Not yet implemented")
}
