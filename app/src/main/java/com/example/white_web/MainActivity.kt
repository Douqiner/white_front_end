package com.example.white_web

import DetailScreen
import PublishScreen
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.white_web.ui.theme.White_webTheme

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.amap.api.location.AMapLocationClient
import com.amap.api.maps2d.MapsInitializer
import com.example.white_web.home.CurrentOrdersScreen
import com.example.white_web.home.CurrentOrdersViewModel
import com.example.white_web.home.HomePage


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置隐私合规
        AMapLocationClient.updatePrivacyShow(this, true, true)
        AMapLocationClient.updatePrivacyAgree(this, true)
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
        // createTrip 路由组
        navigation(startDestination = "createTrip", route = "createTripRoot") {
            composable("createTrip") {
                PublishScreen(mainNavController)
            }
            composable("createTrip/from/{name}") { backStackEntry ->
                val name = backStackEntry.arguments?.getString("name") ?: ""
                PublishScreen(mainNavController, initDeparture = name)
            }
            composable("createTrip/to/{name}") { backStackEntry ->
                val name = backStackEntry.arguments?.getString("name") ?: ""
                PublishScreen(navController = mainNavController, initDestination = name)
            }
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
