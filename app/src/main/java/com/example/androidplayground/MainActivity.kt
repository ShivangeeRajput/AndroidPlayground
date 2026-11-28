package com.example.androidplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.androidplayground.products.DetailScreen
import com.example.androidplayground.products.ProductListScreen
import com.example.androidplayground.ui.theme.AndroidPlaygroundTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidPlaygroundTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "list",
                        modifier = Modifier.padding(innerPadding),
                    ) {
                        composable("list") {
                            ProductListScreen(onClick = { id -> navController.navigate("detail/$id") })
                        }
                        composable("detail/{id}") { entry ->
                            val id = entry.arguments?.getString("id")!!.toInt()
                            DetailScreen(id)
                        }
                    }
                }
            }
        }
    }
}
