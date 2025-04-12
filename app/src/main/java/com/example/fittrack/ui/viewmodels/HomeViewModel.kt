package com.example.fittrack.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fittrack.data.models.Workout
import com.example.fittrack.domain.usecases.GetUserUseCase
import com.example.fittrack.domain.usecases.GetWorkoutsUseCase
import com.example.fittrack.domain.util.DateTimeUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getUserUseCase: GetUserUseCase,
    private val getWorkoutsUseCase: GetWorkoutsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadUserData()
        loadWorkouts()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            getUserUseCase().collect { result ->
                result.onSuccess { user ->
                    _uiState.value = _uiState.value.copy(
                        userName = user.name,
                        isPremium = user.isPremium
                    )
                }
            }
        }
    }

    private fun loadWorkouts() {
        viewModelScope.launch {
            getWorkoutsUseCase().collect { result ->
                result.onSuccess { workouts ->
                    val recentWorkouts = workouts.sortedByDescending { it.timestamp }.take(3)

                    val totalDistance = workouts.sumOf { it.distance.toDouble() }
                    val totalDurationSeconds = workouts.sumOf { it.durationSeconds.toLong() }
                    val totalCalories = workouts.sumOf { it.caloriesBurned.toLong() }

                    _uiState.value = _uiState.value.copy(
                        recentWorkouts = recentWorkouts,
                        totalDistance = totalDistance,
                        totalDuration = DateTimeUtils.formatDuration(totalDurationSeconds),
                        totalCalories = totalCalories.toInt()
                    )
                }
            }
        }
    }

    data class HomeUiState(
        val userName: String = "",
        val isPremium: Boolean = false,
        val recentWorkouts: List<Workout> = emptyList(),
        val totalDistance: Double = 0.0,
        val totalDuration: String = "0:00",
        val totalCalories: Int = 0
    )
}
