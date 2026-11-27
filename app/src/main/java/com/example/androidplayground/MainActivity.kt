package com.example.androidplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.androidplayground.ui.theme.AndroidPlaygroundTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.androidplayground.timer.HomeScreen
import com.example.androidplayground.timer.ServiceTimerScreen
import com.example.androidplayground.timer.SimpleTimerScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidPlaygroundTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = "home", modifier = Modifier.padding(innerPadding)) {
                        composable("home") { HomeScreen(onOpen = { navController.navigate(it) }) }
                        composable("simple") { SimpleTimerScreen() }
                        composable("service") { ServiceTimerScreen() }
                    }
                }
            }
        }
    }
}
