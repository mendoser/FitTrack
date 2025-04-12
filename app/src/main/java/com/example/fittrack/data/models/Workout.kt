package com.example.fittrack.data.models

data class Workout(
    val id: String = "",
    val userId: String = "",
    val type: WorkoutType = WorkoutType.RUNNING,
    val startTime: Long = 0,
    val endTime: Long = 0,
    val duration: Int = 0,  // in seconds
    val distance: Float = 0f,  // in kilometers
    val caloriesBurned: Int = 0,
    val avgHeartRate: Int = 0,
    val maxHeartRate: Int = 0,
    val steps: Int = 0,


    val title: String,
    val timestamp: Long, // Epoch milliseconds
    //val distance: Double, // Distance in kilometers
    val durationSeconds: Int, // Duration in seconds
   // val caloriesBurned: Int // Calories burned


)