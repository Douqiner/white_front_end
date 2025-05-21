package com.example.white_web

import DetailScreen
import PublishScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.white_web.ui.theme.White_webTheme

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.white_web.home.CurrentOrdersScreen
import com.example.white_web.home.CurrentOrdersViewModel
import com.example.white_web.home.HomePage


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            White_webTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val mainNavController = rememberNavController()

    NavHost(navController = mainNavController, startDestination = "login") {
        composable("login") {
            LoginScreen(mainNavController)
        }
        composable("register") {
            RegisterScreen(mainNavController)
        }
        composable("home") {
            HomePage(
                navController = mainNavController,
            )
        }
        composable("createTrip") {
            PublishScreen(mainNavController)
        }
        composable("tripDetail/{tripId}") { backStackEntry ->
            val tripId = backStackEntry.arguments?.getString("tripId")?.toIntOrNull()
            DetailScreen(tripId!!, mainNavController)
        }
        composable("currentOrders") {
            val viewModel = viewModel<CurrentOrdersViewModel>()
            CurrentOrdersScreen(mainNavController, viewModel)
        }
    }
}
