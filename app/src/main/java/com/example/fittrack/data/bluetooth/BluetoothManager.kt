package com.example.fittrack.data.bluetooth

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.example.fittrack.data.models.HealthMetrics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class BluetoothManager(private val context: Context) {

    // Simple data class to represent a Bluetooth device
    data class DeviceInfo(
        val name: String,
        val address: String
    )

    // In a real app, we would implement actual BLE functionality
    // For this example, we're keeping it simple with a mocked implementation
    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    val connectionState: StateFlow<ConnectionState> = _connectionState

    private val _healthData = MutableStateFlow<HealthMetrics?>(null)
    val healthData: StateFlow<HealthMetrics?> = _healthData

    private val _scanResults = MutableStateFlow<List<DeviceInfo>>(emptyList())
    val scanResults: StateFlow<List<DeviceInfo>> = _scanResults

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning

    private var connectedDeviceInfo: DeviceInfo? = null

    fun startScan() {
        if (!hasRequiredPermissions()) {
            return
        }

        _isScanning.value = true

        // Simulate finding devices
        CoroutineScope(Dispatchers.IO).launch {
            // In a real app, this would involve actual BLE scanning
            // For this demo, we'll simulate finding devices with a delay
            delay(2000)

            // Generate some mock devices
            val mockDevices = listOf(
                DeviceInfo("FitBand Pro", "00:11:22:33:44:55"),
                DeviceInfo("HeartRate Monitor", "00:11:22:33:44:56"),
                DeviceInfo("Smart Watch X1", "00:11:22:33:44:57")
            )

            _scanResults.value = mockDevices
            delay(3000)
            _isScanning.value = false
        }
    }

    fun stopScan() {
        _isScanning.value = false
    }

    fun connectToDevice(deviceInfo: DeviceInfo) {
        if (!hasRequiredPermissions()) {
            return
        }

        _connectionState.value = ConnectionState.Connecting

        // Simulate connection process
        CoroutineScope(Dispatchers.IO).launch {
            delay(1500) // Simulate connection delay

            connectedDeviceInfo = deviceInfo
            _connectionState.value = ConnectionState.Connected(deviceInfo)

            // Start simulating health data
            startReceivingData()
        }
    }

    fun disconnect() {
        connectedDeviceInfo?.let {
            _connectionState.value = ConnectionState.Disconnected
            connectedDeviceInfo = null
        }
    }

    private fun startReceivingData() {
        // Simulate receiving health data periodically
        CoroutineScope(Dispatchers.IO).launch {
            while (_connectionState.value is ConnectionState.Connected) {
                val heartRate = 60 + Random.nextInt(0, 40) // Random heart rate between 60-100
                val steps = _healthData.value?.steps?.plus(Random.nextInt(10, 30)) ?: Random.nextInt(1000, 5000)

                _healthData.value = HealthMetrics(
                    userId = "current_user_id",
                    timestamp = System.currentTimeMillis(),
                    heartRate = heartRate,
                    steps = steps,
                    caloriesBurned = (steps / 20), // Simple estimation
                    distance = (steps * 0.0008f) // Simple estimation in km
                )

                delay(1000) // Update every second
            }
        }
    }

    private fun hasRequiredPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH
            ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH_ADMIN
                    ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
        }
    }
}

// Connection state sealed class
sealed class ConnectionState {
    object Disconnected : ConnectionState()
    object Connecting : ConnectionState()
    data class Connected(val deviceInfo: BluetoothManager.DeviceInfo) : ConnectionState()
    data class Failed(val message: String) : ConnectionState()
}