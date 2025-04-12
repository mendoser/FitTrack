//package com.example.fittrack.ui.viewmodels
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.viewModelScope
//import com.example.fittrack.data.models.Workout
//import com.example.fittrack.data.repositories.WorkoutRepository
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//
//class WorkoutDetailsViewModel(
//    private val workoutId: String,
//    private val workoutRepository: WorkoutRepository
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow(WorkoutDetailsUiState(isLoading = true))
//    val uiState: StateFlow<WorkoutDetailsUiState> = _uiState
//
//    init {
//        loadWorkout()
//    }
//
//    private fun loadWorkout() {
//        viewModelScope.launch {
//            try {
//                val result: Unit = workoutRepository.getWorkoutById(workoutId)
//
//                if (result.isSuccess) {
//                    val workout = result.getOrNull()
//                    _uiState.value = WorkoutDetailsUiState(workout = workout)
//                } else {
//                    val error = result.exceptionOrNull()?.message ?: "Unknown error"
//                    _uiState.value = WorkoutDetailsUiState(error = error)
//                }
//
//            } catch (e: Exception) {
//                _uiState.value = WorkoutDetailsUiState(error = e.message ?: "Exception occurred")
//            }
//        }
//    }
//
//
//    companion object {
//        fun provideFactory(
//            workoutId: String,
//            workoutRepository: WorkoutRepository
//        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
//            @Suppress("UNCHECKED_CAST")
//            override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                return WorkoutDetailsViewModel(workoutId, workoutRepository) as T
//            }
//        }
//    }
//}
//
//data class WorkoutDetailsUiState(
//    val isLoading: Boolean = false,
//    val workout: Workout? = null,
//    val error: String? = null
//)
