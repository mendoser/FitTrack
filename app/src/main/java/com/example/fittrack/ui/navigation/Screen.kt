package com.example.fittrack.ui.navigation

sealed class Screen(val route: String) {

    object Login : Screen("login")
    object Register : Screen("register")

    object Home : Screen("home")
    object Workouts : Screen("workouts")

    object WorkoutDetails : Screen("workout_details/{workoutId}") {
        fun createRoute(workoutId: String): String = "workout_details/$workoutId"
        const val ARG_WORKOUT_ID = "workoutId"
    }

    object StartWorkout : Screen("start_workout")
    object Profile : Screen("profile")
    object Devices : Screen("devices")
    object Settings : Screen("settings")
}
