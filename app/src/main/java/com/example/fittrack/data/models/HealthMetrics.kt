package com.example.fittrack.data.models

data class HealthMetrics(
    val userId: String = "",
    val timestamp: Long = 0,
    val heartRate: Int = 0,
    val steps: Int = 0,
    val caloriesBurned: Int = 0,
    val distance: Float = 0f
)