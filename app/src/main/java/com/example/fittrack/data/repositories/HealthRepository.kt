package com.example.fittrack.data.repositories

import com.example.fittrack.data.api.ApiClient
import com.example.fittrack.data.models.HealthMetrics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class HealthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val apiService = ApiClient.fitnessApiService

    suspend fun saveHealthMetrics(healthMetrics: HealthMetrics): Result<HealthMetrics> {
        val userId = auth.currentUser?.uid ?: return Result.failure(Exception("User not logged in"))

        return try {
            val metricsWithUserId = healthMetrics.copy(userId = userId)
            val apiResponse = apiService.saveHealthMetrics(userId, metricsWithUserId)

            if (apiResponse.isSuccessful) {
                Result.success(apiResponse.body() ?: metricsWithUserId)
            } else {
                // Fallback to Firestore
                val docId = "${userId}_${healthMetrics.timestamp}"

                db.collection("users").document(userId)
                    .collection("health_metrics").document(docId)
                    .set(metricsWithUserId).await()

                Result.success(metricsWithUserId)
            }
        } catch (e: Exception) {
            // Fallback to Firestore on exception
            try {
                val docId = "${userId}_${healthMetrics.timestamp}"

                db.collection("users").document(userId)
                    .collection("health_metrics").document(docId)
                    .set(healthMetrics.copy(userId = userId)).await()

                Result.success(healthMetrics.copy(userId = userId))
            } catch (e2: Exception) {
                Result.failure(e2)
            }
        }
    }

    suspend fun getHealthMetrics(startDate: Long, endDate: Long): Result<List<HealthMetrics>> {
        val userId = auth.currentUser?.uid ?: return Result.failure(Exception("User not logged in"))

        return try {
            // Try API first
            val metrics = apiService.getHealthMetrics(userId, startDate, endDate)
            Result.success(metrics)
        } catch (e: Exception) {
            // Fallback to Firestore
            try {
                val snapshot = db.collection("users").document(userId)
                    .collection("health_metrics")
                    .whereGreaterThanOrEqualTo("timestamp", startDate)
                    .whereLessThanOrEqualTo("timestamp", endDate)
                    .orderBy("timestamp", Query.Direction.ASCENDING)
                    .get().await()

                val metrics = snapshot.toObjects(HealthMetrics::class.java)
                Result.success(metrics)
            } catch (e2: Exception) {
                Result.failure(e2)
            }
        }
    }

    // Get the latest health metrics for the current user
    suspend fun getLatestHealthMetrics(): HealthMetrics? {
        val userId = auth.currentUser?.uid ?: return null

        return try {
            val snapshot = db.collection("users").document(userId)
                .collection("health_metrics")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(1)
                .get().await()

            if (snapshot.isEmpty) null else snapshot.documents[0].toObject(HealthMetrics::class.java)
        } catch (e: Exception) {
            null
        }
    }
}