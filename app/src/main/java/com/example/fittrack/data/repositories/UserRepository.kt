package com.example.fittrack.data.repositories

import com.example.fittrack.data.api.ApiClient
import com.example.fittrack.data.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val apiService = ApiClient.fitnessApiService

    suspend fun signIn(email: String, password: String): Result<User> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: return Result.failure(Exception("Auth successful but UID is null"))
            val user = fetchUserProfile(uid)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUp(email: String, password: String, name: String): Result<User> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: return Result.failure(Exception("Auth successful but UID is null"))

            val newUser = User(
                id = uid,
                name = name,
                email = email
            )

            db.collection("users").document(uid).set(newUser).await()
            Result.success(newUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCurrentUser(): User? {
        val uid = auth.currentUser?.uid ?: return null
        return fetchUserProfile(uid)
    }

    private suspend fun fetchUserProfile(userId: String): User {
        // Try to get from API first
        return try {
            apiService.getUser(userId)
        } catch (e: Exception) {
            // Fallback to Firestore
            val document = db.collection("users").document(userId).get().await()
            document.toObject(User::class.java) ?: User(id = userId)
        }
    }

    suspend fun updateUserProfile(user: User): Result<User> {
        return try {
            db.collection("users").document(user.id).set(user).await()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        auth.signOut()
    }

    // Check if user is signed in
    fun isUserSignedIn(): Boolean {
        return auth.currentUser != null
    }
}