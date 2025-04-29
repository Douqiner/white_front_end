import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.white_web.APISERVICCE
import com.example.white_web.ui.theme.White_webTheme
import kotlinx.coroutines.launch
import java.util.*

data class DetailResponse(
    val code: Int,
    val message: String,
    val data: DetailData?
)
data class DetailData(
    val order_id: Int,
    val user1: String,
    val user2: String,
    val user3: String,
    val user4: String,
    val departure: String,
    val destination: String,
    val date: String,
    val earliest_departure_time: String,
    val latest_departure_time: String,
//    val notes: String,
//    val telephony: String
)

@Composable
fun DetailScreen(orderId: Int) {
    var detailData by remember { mutableStateOf<DetailData?>(null) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        try {
            val response = APISERVICCE.detail(orderId)
            if (response.isSuccessful && response.body()?.code == 200) {
                detailData = response.body()?.data
            } else {
                // 处理错误
                Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            // 处理异常
            Toast.makeText(context, "获取详情失败，请检查网络连接", Toast.LENGTH_SHORT).show()
        }
    }
//    detailData = DetailData(0,"user1", "2","3","4", "嘉定校区", "上海虹桥火车站", "2024-3-30", "5:30", "6:30")

    if (detailData != null) {
        DisplayDetail(detailData!!)
    } else {
        Text("载入中...")
    }
}

@Composable
fun DisplayDetail(detailData: DetailData) {

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
            ),
        contentAlignment = Alignment.Center
    ) {
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
//                        value = detailData.note,
                        value = "未实现",
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
//                        value = detailData.telephony,
                        value = "未实现",
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
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewDetailScreen() {
    White_webTheme {
        DetailScreen(0)
    }
}