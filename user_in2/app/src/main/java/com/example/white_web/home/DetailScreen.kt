package com.example.white_web.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.white_web.APISERVICCE
import com.example.white_web.USERNAME
import com.example.white_web.USERTYPE
import com.example.white_web.ui.theme.White_webTheme
import kotlinx.coroutines.launch
import retrofit2.Response

data class DetailResponse(
    val code: Int,
    val message: String,
    val data: DetailData?
)
data class DetailData(
    val order_id: Int,
    val user1: String,
    val user2: String?,
    val user3: String?,
    val user4: String?,
    val driver: String?,
    val departure: String,
    val destination: String,
    val date: String,
    val earliest_departure_time: String,
    val latest_departure_time: String,
    val remark: String
)

data class JoinLeaveRequest(
    val order_id: Int
)

data class JoinLeaveRedponse(
    val code: Int,
    val message: String
)

data class UserDetailResponse(
    val code: Int,
    val message: String,
    val data: Data?
) {
    data class Data(
        val phonenumber: String,
        val usertype: String
    )
}

@Composable
fun DetailScreen(orderId: Int, navController: NavHostController) {
    var detailData by remember { mutableStateOf<DetailData?>(null) }
    var phonenumber by remember { mutableStateOf<String?>(null) }
    var errorMsg by remember { mutableStateOf<String?>("载入中···") }

    LaunchedEffect(Unit) {
        try {
            val response = APISERVICCE.detail(orderId)
            if (response.isSuccessful && response.body()?.code == 200) {
                detailData = response.body()?.data

                try {
                    val response2 = APISERVICCE.getUserDetail(detailData!!.user1)
                    if (response2.isSuccessful && response2.body()?.code == 200) {
                        phonenumber = response2.body()?.data?.phonenumber
                    } else {
                        // 处理错误
                        errorMsg = response.body()?.message
                    }
                } catch (e: Exception) {
                    // 处理异常
                    errorMsg = e.message
                }

            } else {
                // 处理错误
                errorMsg = response.body()?.message
            }

        } catch (e: Exception) {
            // 处理异常
            errorMsg = e.message
        }

    }
//    detailData = DetailData(0,"未登录", "","","", "嘉定校区", "上海虹桥火车站", "2024-3-30", "5:30", "6:30")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.secondaryContainer
                    )
                )
            )
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        // 顶部栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.Start, // 将内容对齐到左侧
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 返回按钮
            IconButton(
                onClick = { navController.popBackStack() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "返回",
                    tint = MaterialTheme.colorScheme.surfaceTint
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (detailData != null && phonenumber != null) {
                DisplayDetail(detailData!!, navController, phonenumber!!)
            } else {
                Text(
                    text = "$errorMsg",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }

}
@Composable
fun DisplayDetail(detailData: DetailData, navController: NavHostController, phonenumber: String) {

    val userCount = listOfNotNull(
        detailData.user1.takeIf { it.isNotBlank() },
        detailData.user2?.takeIf { it.isNotBlank() },
        detailData.user3?.takeIf { it.isNotBlank() },
        detailData.user4?.takeIf { it.isNotBlank() }).size

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val buttonColor: Color
    val buttonText: String
    val isClickable: Boolean

     if (USERTYPE == 1) {
         if (listOf(detailData.user1, detailData.user2, detailData.user3, detailData.user4).contains(USERNAME)) {
             // 如果包含该用户，显示红色按钮 "退出"
             buttonColor = MaterialTheme.colorScheme.error
             if (userCount == 1) {
                 buttonText = "移除拼单"
             }
             else {
                 buttonText = "退出拼单"
             }
             isClickable = true
         }
        else if (userCount == 4) {
            // 如果当前人数为 4，显示灰色按钮 "人数已满"
            buttonColor = MaterialTheme.colorScheme.tertiary
            buttonText = "人数已满"
            isClickable = false
        } else {
            // 否则，显示蓝色按钮 "加入"
            buttonColor = MaterialTheme.colorScheme.primary
            buttonText = "加入拼单"
            isClickable = true
        }
    }
    else if (USERTYPE == 2) {
        if (detailData.driver.isNullOrEmpty()) {
            // 否则，显示蓝色按钮 "加入"
            buttonColor = MaterialTheme.colorScheme.primary
            buttonText = "接受拼单"
            isClickable = true
        }
         else if (detailData.driver == USERNAME) {
            // 如果包含该用户，显示红色按钮 "退出"
            buttonColor = MaterialTheme.colorScheme.error
            buttonText = "放弃接受"
            isClickable = true
        }
         else {
            // 显示灰色按钮 "人数已满"
            buttonColor = MaterialTheme.colorScheme.tertiary
            buttonText = "人数已满"
            isClickable = false
        }
     }
    else {
        buttonColor = MaterialTheme.colorScheme.tertiary
        buttonText = "参数错误"
        isClickable = false
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
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
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 第一行：文字“拼车需求填写”
                Text(
                    text = "拼车需求详情",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // 第二行：出发地
                OutlinedTextField(
                    value = detailData.departure,
                    onValueChange = {},
                    label = { Text("出发地") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.outline
                    )
                )

                OutlinedTextField(
                    value = detailData.destination,
                    onValueChange = {},
                    label = { Text("目的地") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.outline
                    )
                )

                // 第四行：日期选择框
                OutlinedTextField(
                    value = detailData.date,
                    onValueChange = {},
                    label = { Text("拼车日期") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = {}) {
                            Icon(
                                Icons.Default.DateRange,
                                contentDescription = "Date Picker",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.outline
                    )
                )

                // 第五行：最早出发时间选择框
                OutlinedTextField(
                    value = detailData.earliest_departure_time,
                    onValueChange = {},
                    label = { Text("最早出发时间") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = {}) {
                            Icon(
                                Icons.Default.DateRange,
                                contentDescription = "Time Picker",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.outline
                    )
                )

                // 第六行：最晚出发时间选择框
                OutlinedTextField(
                    value = detailData.latest_departure_time,
                    onValueChange = {},
                    label = { Text("最晚出发时间") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = {}) {
                            Icon(
                                Icons.Default.DateRange,
                                contentDescription = "Time Picker",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.outline
                    )
                )

                // 第七行：备注
                OutlinedTextField(
                    value = detailData.remark,
                    onValueChange = {},
                    label = { Text("备注") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default,
                    keyboardActions = KeyboardActions.Default,
                    readOnly = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.outline
                    )
                )

                // 第八行：电话
                OutlinedTextField(
                    value = phonenumber,
                    onValueChange = {},
                    label = { Text("联系方式") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.outline
                    )
                )

                // 第九行：当前人数
                OutlinedTextField(
                    value = userCount.toString(),
                    onValueChange = {},
                    label = { Text("当前人数") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.outline
                    )
                )

                // 第十行：司机
                OutlinedTextField(
                    value = if (detailData.driver.isNullOrEmpty()) "无" else detailData.driver,
                    onValueChange = {},
                    label = { Text("司机") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.outline
                    )
                )

            }
        }

        // 第十行：按钮
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .clip(RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor
            ),
            onClick = {
                // 检查
                if (isClickable){
                    scope.launch {
                        try {
                            val response : Response<JoinLeaveRedponse>
                            if (buttonText == "加入拼单" || buttonText == "接受拼单") {
                                response = APISERVICCE.joinOrder(
                                    request = JoinLeaveRequest(
                                        detailData.order_id
                                    )
                                )
                            }
                            else {
                                response = APISERVICCE.leaveOrder(
                                    request = JoinLeaveRequest(
                                        detailData.order_id
                                    )
                                )
                            }

                            if (response.isSuccessful && response.body()?.code == 200) {

                                // 加入成功后 跳转到详情界面
                                if (buttonText == "加入拼单" || buttonText == "接受拼单") {
                                    Toast.makeText(context, "加入成功", Toast.LENGTH_SHORT)
                                        .show()
                                    navController.popBackStack()
                                    navController.navigate("tripDetail/${detailData.order_id}")
                                }
                                else {
                                    if (USERTYPE == 1) {
                                        // 离开成功后，如果还有人跳转到详情界面 否则退出
                                        if (userCount > 1) {
                                            Toast.makeText(context, "退出成功", Toast.LENGTH_SHORT)
                                                .show()
                                            navController.popBackStack()
                                            navController.navigate("tripDetail/${detailData.order_id}")
                                        }
                                        else {
                                            Toast.makeText(context, "订单已删除", Toast.LENGTH_SHORT)
                                                .show()
                                            navController.popBackStack()
                                        }
                                    }
                                    else if (USERTYPE == 2) {
                                        Toast.makeText(context, "退出成功", Toast.LENGTH_SHORT)
                                            .show()
                                    }

                                }


                            } else {
                                // 发布失败
                                Toast.makeText(
                                    context,
                                    response.body()?.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: Exception) {
                            // 网络请求异常
                            Toast.makeText(
                                context,
                                "请求失败，请检查网络连接",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            },
            enabled = isClickable
        ) {
            Text(
                buttonText,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    }

}


@Preview
@Composable
fun PreviewDetailScreen() {
    White_webTheme {
        val navController = rememberNavController()
        DetailScreen(0, navController)
    }
}