package com.example.fittrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.fittrack.ui.navigation.FitTrackBottomNavigation
import com.example.fittrack.ui.navigation.FitTrackNavHost
import com.example.fittrack.ui.theme.FitTrackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitTrackTheme {
                FitTrackApp()
            }
        }
    }
}

@Composable
fun FitTrackApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { FitTrackBottomNavigation(navController) }
    ) { innerPadding ->
        FitTrackNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}