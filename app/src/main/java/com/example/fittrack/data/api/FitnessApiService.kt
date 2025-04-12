package com.example.fittrack.data.api

import com.example.fittrack.data.models.HealthMetrics
import com.example.fittrack.data.models.User
import com.example.fittrack.data.models.Workout
import retrofit2.Response
import retrofit2.http.*

interface FitnessApiService {
    @GET("users/{userId}")
    suspend fun getUser(@Path("userId") userId: String): User

    @GET("users/{userId}/workouts")
    suspend fun getUserWorkouts(@Path("userId") userId: String): List<Workout>

    @POST("users/{userId}/workouts")
    suspend fun saveWorkout(
        @Path("userId") userId: String,
        @Body workout: Workout
    ): Response<Workout>

    @GET("users/{userId}/health-metrics")
    suspend fun getHealthMetrics(
        @Path("userId") userId: String,
        @Query("startDate") startDate: Long,
        @Query("endDate") endDate: Long
    ): List<HealthMetrics>

    @POST("users/{userId}/health-metrics")
    suspend fun saveHealthMetrics(
        @Path("userId") userId: String,
        @Body healthMetrics: HealthMetrics
    ): Response<HealthMetrics>
}