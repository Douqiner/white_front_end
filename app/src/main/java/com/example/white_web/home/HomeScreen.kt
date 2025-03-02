package com.example.white_web.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.white_web.R
import com.example.white_web.USERNAME

data class BottomNavItem(
    val title: String,
    val iconResId: Int,  // 存储资源 ID
    val route: String
)

val bottomNavItems = listOf(
    BottomNavItem(
        title = "乘客",
        iconResId = R.drawable.day,  // 存储资源 ID
        route = "passenger"
    ),
    BottomNavItem(
        title = "车主",
        iconResId = R.drawable.day,  // 存储资源 ID
        route = "driver"
    ),
    BottomNavItem(
        title = "我的",
        iconResId = R.drawable.day,  // 存储资源 ID
        route = "myinfo"
    )
)

@Composable
fun NavigationBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                icon = { Icon(painterResource(id = item.iconResId), contentDescription = item.title) },
                label = { Text(item.title) }
            )
        }
    }
}

@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    var currentRoute by remember { mutableStateOf("passenger") }

    Scaffold(
        bottomBar = {
            NavigationBar(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(currentRoute) { inclusive = true } // 清除页面
                    }
                    currentRoute = route
                }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            NavHost(navController = navController, startDestination = "passenger") {
                composable("passenger") {
                    Text("$USERNAME, welcome to passenger")
                }
                composable("driver") {
                    Text("$USERNAME, welcome to driver")
                }
                composable("myinfo") {
                    MyInfoScreen()
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}