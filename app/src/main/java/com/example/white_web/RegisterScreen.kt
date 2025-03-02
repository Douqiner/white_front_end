package com.example.white_web

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch


data class RegisterRequest(
    val username: String,
    val password: String
)

data class RegisterResponse(
    val code: Int,
    val message: String,
    val data: Data?
) {
    data class Data(
        val token: String,
        val username: String,
    )
}

@Composable
@Preview
fun RegisterPreview() {
    // 创建一个模拟的 NavController
    val navController = rememberNavController()
    RegisterScreen(navController)
}

@Composable
fun RegisterScreen(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "注册",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("用户名") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("密码") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("确认密码") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                // 检查
                if (username.length < 4) {
                    Toast.makeText(context, "用户名太短", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                else if (username.length > 20) {
                    Toast.makeText(context, "用户名太长", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                else if (password.length < 6) {
                    Toast.makeText(context, "密码太短", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                else if (password.length > 20) {
                    Toast.makeText(context, "密码太长", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                else if (password != confirmPassword) {
                    Toast.makeText(context, "两次密码不相同", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                scope.launch {
                    try {
                        val response = APISERVICCE.register(RegisterRequest(username, password))
                        if (response.isSuccessful && response.body()?.code == 200) {
                            // 注册并登录成功
                            USERNAME = response.body()?.data?.username
                            TOKEN = response.body()?.data?.token
                            Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT).show()
                            // 登录成功后跳转到 HomeScreen 并清除注册页面
                            navController.navigate("home") {
                                popUpTo("register") { inclusive = true } // 清除登录页面
                            }
                        } else {
                            // 登录失败
                            Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        // 网络请求异常
                        Toast.makeText(context, "登录失败，请检查网络连接", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        ) {
            Text("注册")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                navController.navigate("login") {
                    popUpTo("register") { inclusive = true } // 清除登录页面
                }
            }
        ){
            Text("已有账号？登录")
        }
    }
}