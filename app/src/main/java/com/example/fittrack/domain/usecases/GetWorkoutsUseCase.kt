package com.example.fittrack.domain.usecases

import com.example.fittrack.data.models.Workout
import com.example.fittrack.data.repositories.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetWorkoutsUseCase(private val workoutRepository: WorkoutRepository) {

    suspend operator fun invoke(): Flow<Result<List<Workout>>> = flow {
        try {
            val result = workoutRepository.getUserWorkouts()
            emit(result)
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
