package com.example.fittrack.domain.usecases

import com.example.fittrack.data.models.Workout
import com.example.fittrack.data.repositories.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SaveWorkoutUseCase(private val workoutRepository: WorkoutRepository) {

    suspend operator fun invoke(workout: Workout): Flow<Result<Workout>> = flow {
        try {
            val result = workoutRepository.saveWorkout(workout)
            emit(result)
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}