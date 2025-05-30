package com.example.white_web

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.white_web.ui.theme.White_webTheme
import kotlinx.coroutines.launch

data class RegisterRequest(
    val username: String, val password: String, val phonenumber: String, val usertype: Int
)

data class RegisterResponse(
    val code: Int, val message: String, val data: Data?
) {
    data class Data(
        val token: String,
        val username: String,
    )
}

// 人像图标
@Composable
fun UserGroupIcon(
    modifier: Modifier = Modifier, color: Color = MaterialTheme.colorScheme.secondary
) {
    Canvas(modifier = modifier.size(24.dp)) {
        val w = size.width
        val h = size.height
        // 头部
        drawCircle(
            color = color, radius = w * 0.20f, center = Offset(w / 2, h * 0.32f)
        )
        // 身体（半圆肩膀+直线胸部）
        val bodyPath = Path().apply {
            // 半圆肩膀
            arcTo(
                rect = androidx.compose.ui.geometry.Rect(
                    left = w * 0.18f, top = h * 0.52f, right = w * 0.82f, bottom = h * 0.92f
                ), startAngleDegrees = 200f, sweepAngleDegrees = 140f, forceMoveTo = false
            )
        }
        drawPath(
            path = bodyPath, color = color, style = Stroke(width = w * 0.10f, cap = StrokeCap.Round)
        )
    }
}

// 汽车图标
@Composable
fun CarIcon(
    modifier: Modifier = Modifier, color: Color = MaterialTheme.colorScheme.tertiary
) {
    Canvas(modifier = modifier.size(24.dp)) {
        val w = size.width
        val h = size.height

        // 车身
        val bodyPath = Path().apply {
            moveTo(w * 0.18f, h * 0.60f)
            lineTo(w * 0.28f, h * 0.40f)
            cubicTo(w * 0.30f, h * 0.36f, w * 0.70f, h * 0.36f, w * 0.72f, h * 0.40f)
            lineTo(w * 0.82f, h * 0.60f)
            close()
        }
        drawPath(
            path = bodyPath, color = color
        )

        // 车窗
        val windowPath = Path().apply {
            moveTo(w * 0.36f, h * 0.44f)
            lineTo(w * 0.40f, h * 0.40f)
            lineTo(w * 0.60f, h * 0.40f)
            lineTo(w * 0.64f, h * 0.44f)
        }
        drawPath(
            path = windowPath,
            color = Color.White.copy(alpha = 0.7f),
            style = Stroke(width = w * 0.07f, cap = StrokeCap.Round)
        )

        // 车轮
        drawCircle(
            color = Color.DarkGray, radius = w * 0.10f, center = Offset(w * 0.30f, h * 0.68f)
        )
        drawCircle(
            color = Color.DarkGray, radius = w * 0.10f, center = Offset(w * 0.70f, h * 0.68f)
        )
        // 轮毂
        drawCircle(
            color = Color.White, radius = w * 0.045f, center = Offset(w * 0.30f, h * 0.68f)
        )
        drawCircle(
            color = Color.White, radius = w * 0.045f, center = Offset(w * 0.70f, h * 0.68f)
        )
        // 前灯
        drawCircle(
            color = Color.Yellow.copy(alpha = 0.7f),
            radius = w * 0.025f,
            center = Offset(w * 0.18f, h * 0.62f)
        )
        // 后灯
        drawCircle(
            color = Color.Red.copy(alpha = 0.6f),
            radius = w * 0.025f,
            center = Offset(w * 0.82f, h * 0.62f)
        )
    }
}

@Composable
@Preview
fun RegisterPreview() {
    White_webTheme {
        val navController = rememberNavController()
        RegisterScreen(navController)
    }
}

@Composable
fun RegisterScreen(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var userType by remember { mutableIntStateOf(1) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.secondaryContainer
                    )
                )
            ), contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "创建新账户",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "请填写以下信息完成注册",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    // 用户名输入
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("用户名") },
                        placeholder = { Text("请输入至少6位用户名") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "用户名",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    // 电话号码
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("电话号码") },
                        placeholder = { Text("请输入11位手机号") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = "电话号码",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    // 密码输入
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("密码") },
                        placeholder = { Text("请输入至少6位密码") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "密码",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.outline
                        )
                    )

                    // 确认密码
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("确认密码") },
                        placeholder = { Text("请再次输入密码") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "确认密码",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.outline
                        )
                    )


                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)
                    )

                    // 用户类型选择标题
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "用户类型",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "选择用户类型",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    // 用户类型选择
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        // 普通用户选项
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (userType == 1) MaterialTheme.colorScheme.secondaryContainer
                                    else Color.Transparent
                                )
                                .selectable(
                                    selected = userType == 1, onClick = { userType = 1 })
                                .padding(horizontal = 8.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            RadioButton(
                                selected = userType == 1,
                                onClick = { userType = 1 },
                            )
                            UserGroupIcon(
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(start = 0.dp, end = 4.dp),
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Text(
                                text = "普通用户",
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (userType == 1) MaterialTheme.colorScheme.onSecondaryContainer
                                else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(start = 2.dp)
                            )
                        }

                        // 司机选项
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (userType == 2) MaterialTheme.colorScheme.secondaryContainer
                                    else Color.Transparent
                                )
                                .selectable(
                                    selected = userType == 2, onClick = { userType = 2 })
                                .padding(horizontal = 8.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            RadioButton(
                                selected = userType == 2, onClick = { userType = 2 })
                            CarIcon(
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(start = 0.dp, end = 4.dp),
                                color = MaterialTheme.colorScheme.tertiary
                            )
                            Text(
                                text = "司机",
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (userType == 2) MaterialTheme.colorScheme.onTertiaryContainer
                                else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(start = 2.dp)
                            )
                        }
                    }
                }
            }
            // 按钮区域
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clip(RoundedCornerShape(16.dp)), onClick = {
                        // 检查空值
                        if (username.isBlank()) {
                            Toast.makeText(context, "请输入用户名", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if (phoneNumber.isBlank()) {
                            Toast.makeText(context, "请输入电话号码", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if (password.isBlank()) {
                            Toast.makeText(context, "请输入密码", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if (confirmPassword.isBlank()) {
                            Toast.makeText(context, "请输入确认密码", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        // 参数验证
                        if (username.length < 6) {
                            Toast.makeText(context, "用户名至少需要6位", Toast.LENGTH_SHORT).show()
                            return@Button
                        } else if (username.length > 20) {
                            Toast.makeText(context, "用户名太长", Toast.LENGTH_SHORT).show()
                            return@Button
                        } else if (phoneNumber.length != 11) {
                            Toast.makeText(context, "电话号码必须为11位", Toast.LENGTH_SHORT).show()
                            return@Button
                        } else if (!phoneNumber.all { it.isDigit() }) {
                            Toast.makeText(context, "电话号码只能包含数字", Toast.LENGTH_SHORT)
                                .show()
                            return@Button
                        } else if (password.length < 6) {
                            Toast.makeText(context, "密码至少需要6位", Toast.LENGTH_SHORT).show()
                            return@Button
                        } else if (password.length > 20) {
                            Toast.makeText(context, "密码太长", Toast.LENGTH_SHORT).show()
                            return@Button
                        } else if (password != confirmPassword) {
                            Toast.makeText(context, "两次密码不相同", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        scope.launch {
                            try {
                                val response = APISERVICCE.register(
                                    RegisterRequest(
                                        username = username,
                                        password = password,
                                        phonenumber = phoneNumber,
                                        usertype = userType
                                    )
                                )

                                if (response.isSuccessful && response.body()?.code == 200) {
                                    // 注册并登录成功
                                    USERNAME = response.body()?.data?.username
                                    TOKEN = response.body()?.data?.token
                                    USERTYPE = userType
                                    Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT).show()
                                    // 注册成功后跳转到 HomePage 并清除注册页面
                                    navController.navigate("home") {
                                        popUpTo("register") { inclusive = true }
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        response.body()?.message ?: "注册失败",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(
                                    context,
                                    "注册失败，请检查网络连接: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }, shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        "注册",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    onClick = {
                        navController.navigate("login") {
                            popUpTo("register") { inclusive = true }
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        "已有账号？返回登录", style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}