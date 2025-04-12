package com.example.fittrack.domain.usecases

import com.example.fittrack.data.models.User
import com.example.fittrack.data.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SignInUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke(email: String, password: String): Flow<Result<User>> = flow {
        try {
            val result = userRepository.signIn(email, password)
            emit(result)
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
