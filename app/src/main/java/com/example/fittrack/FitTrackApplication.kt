package com.example.fittrack

import android.app.Application
import com.example.fittrack.data.bluetooth.BluetoothManager
import com.example.fittrack.data.repositories.HealthRepository
import com.example.fittrack.data.repositories.UserRepository
import com.example.fittrack.data.repositories.WorkoutRepository
import com.example.fittrack.domain.billing.BillingManager
import com.google.firebase.FirebaseApp

class FitTrackApplication : Application() {

    // Lazy initialization of repositories and managers
    val userRepository by lazy { UserRepository() }
    val workoutRepository by lazy { WorkoutRepository() }
    val healthRepository by lazy { HealthRepository() }
    val bluetoothManager by lazy { BluetoothManager(applicationContext) }
    val billingManager by lazy { BillingManager(applicationContext) }

    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
    }

    companion object {
        private var instance: FitTrackApplication? = null

        fun getInstance(): FitTrackApplication {
            return instance ?: throw IllegalStateException("Application not initialized")
        }
    }

    init {
        instance = this
    }
}