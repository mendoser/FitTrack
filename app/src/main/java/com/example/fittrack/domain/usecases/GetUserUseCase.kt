package com.example.fittrack.domain.usecases

import com.example.fittrack.data.models.User
import com.example.fittrack.data.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetUserUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke(): Flow<Result<User>> = flow {
        try {
            val user = userRepository.getCurrentUser()
            if (user != null) {
                emit(Result.success(user))
            } else {
                emit(Result.failure(Exception("User not found")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}