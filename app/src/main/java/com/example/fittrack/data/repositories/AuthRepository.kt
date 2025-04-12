package com.example.fittrack.data.repository

import com.example.fittrack.data.api.FitnessApiService
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: FitnessApiService
) {
    suspend fun login(email: String, password: String): Boolean {
        // Simulate login logic – replace with actual API call
        return email == "test@fittrack.com" && password == "123456"
    }
}
