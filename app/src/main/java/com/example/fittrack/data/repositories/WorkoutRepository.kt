package com.example.fittrack.data.repositories

import com.example.fittrack.data.api.ApiClient
import com.example.fittrack.data.models.Workout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.util.UUID

class WorkoutRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val apiService = ApiClient.fitnessApiService

    suspend fun saveWorkout(workout: Workout): Result<Workout> {
        val userId = auth.currentUser?.uid ?: return Result.failure(Exception("User not logged in"))

        return try {
            // Try to save to API
            val workoutWithUserId = workout.copy(userId = userId)
            val apiResponse = apiService.saveWorkout(userId, workoutWithUserId)

            if (apiResponse.isSuccessful) {
                Result.success(apiResponse.body() ?: workoutWithUserId)
            } else {
                // Fallback to Firestore
                val workoutId = workout.id.ifEmpty { UUID.randomUUID().toString() }
                val workoutToSave = workoutWithUserId.copy(id = workoutId)

                db.collection("users").document(userId)
                    .collection("workouts").document(workoutId)
                    .set(workoutToSave).await()

                Result.success(workoutToSave)
            }
        } catch (e: Exception) {
            // Always fallback to Firestore on exception
            try {
                val workoutId = workout.id.ifEmpty { UUID.randomUUID().toString() }
                val workoutToSave = workout.copy(id = workoutId, userId = userId)

                db.collection("users").document(userId)
                    .collection("workouts").document(workoutId)
                    .set(workoutToSave).await()

                Result.success(workoutToSave)
            } catch (e2: Exception) {
                Result.failure(e2)
            }
        }
    }

    suspend fun getUserWorkouts(): Result<List<Workout>> {
        val userId = auth.currentUser?.uid ?: return Result.failure(Exception("User not logged in"))

        return try {
            // Try to get from API
            val workouts = apiService.getUserWorkouts(userId)
            Result.success(workouts)
        } catch (e: Exception) {
            // Fallback to Firestore
            try {
                val snapshot = db.collection("users").document(userId)
                    .collection("workouts")
                    .orderBy("startTime", Query.Direction.DESCENDING)
                    .get().await()

                val workouts = snapshot.toObjects(Workout::class.java)
                Result.success(workouts)
            } catch (e2: Exception) {
                Result.failure(e2)
            }
        }
    }

    fun getWorkoutById(workoutId: String) {

    }
}