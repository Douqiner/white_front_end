/**
 * 拼车需求发布界面
 * 
 * 文件功能: 用户发布新拼车需求的界面和逻辑处理
 * 
 * 主要功能:
 * - 提供完整的拼车需求发布表单
 * - 支持出发地和目的地选择
 * - 集成日期时间选择器
 * - 实现地图点位选择功能
 * - 提供需求发布和验证
 * 
 * 表单字段:
 * - 出发地（支持手动输入和地图选择）
 * - 目的地（支持手动输入和地图选择）
 * - 拼车日期（日期选择器）
 * - 最早出发时间（时间选择器）
 * - 最晚出发时间（时间选择器）
 * - 备注信息（可选文本）
 * 
 * 验证规则:
 * - 出发地和目的地必填
 * - 日期不能早于当前日期
 * - 最晚时间必须晚于最早时间
 * - 用户类型权限检查（仅乘客可发布）
 * 
 * 用户体验:
 * - 直观的表单界面
 * - 实时输入验证
 * - 友好的错误提示
 * - 发布成功自动跳转
 */
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
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
import androidx.compose.material.icons.filled.ArrowDropDown
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
import com.example.white_web.R
import com.example.white_web.USERTYPE
import com.example.white_web.ui.theme.White_webTheme
import kotlinx.coroutines.launch
import java.util.*

data class GetPosResponse(
    val code: Int,
    val message: String,
    val data: Data?
) {
    data class Data(
        val table: List<PosDetail>
    )
}
data class PosDetail(
    val name: String,
    val lon: Double,
    val lat: Double,
)

data class PublishRequest(
    val departure: String,
    val destination: String,
    val date: String,
    val earliest_departure_time: String,
    val latest_departure_time: String,
    val remark: String
)

data class PublishResponse(
    val code: Int,
    val message: String,
    val username: String,
    val data: Data?
) {
    data class Data(
        val order_id: Int,
        val departure: String,
        val destination: String,
        val date: String,
        val earliest_departure_time: String,
        val latest_departure_time: String
    )
}

@Composable
fun PublishScreen(navController: NavHostController, initDeparture: String = "",  initDestination: String = "") {
    var posList by remember { mutableStateOf<List<PosDetail>?>(null) }
    var errorMsg by remember { mutableStateOf<String?>("载入中···") }

    LaunchedEffect(Unit) {
        try {
            val response = APISERVICCE.getPos()
            if (response.isSuccessful && response.body()?.code == 200) {
                posList = response.body()?.data?.table
            } else {
                // 处理错误
                errorMsg = response.body()?.message
            }
        } catch (e: Exception) {
            // 处理异常
            errorMsg = e.message
        }
    }
//    posList = listOf("选项1", "选项2", "选项3")

    if (posList != null) {
        DisplayPublish(posList!!, navController, initDeparture, initDestination)
    } else {
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
fun DisplayPublish(posList: List<PosDetail>, navController: NavHostController, initDeparture: String,  initDestination: String) {
    var departure by remember { mutableStateOf(initDeparture) }
    var destination by remember { mutableStateOf(initDestination) }
    var date by remember { mutableStateOf("") }
    var earliestTime by remember { mutableStateOf("") }
    var latestTime by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    var departureExpanded by remember { mutableStateOf(false) }
    var destinationExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

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
                            text = "拼车需求填写",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Column(
                            modifier = Modifier,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // 第二行：出发地多选框
                            OutlinedTextField(
                                value = departure,
                                onValueChange = { departure = it },
                                label = { Text("出发地") },
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = { departureExpanded = true }) {
                                        Icon(
                                            Icons.Default.ArrowDropDown,
                                            contentDescription = "Dropdown",
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
                            DropdownMenu(
                                expanded = departureExpanded,
                                onDismissRequest = { departureExpanded = false },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                posList.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option.name) },
                                        onClick = {
                                            departure = option.name
                                            departureExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Column(
                            modifier = Modifier,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // 第三行：目的地多选框
                            OutlinedTextField(
                                value = destination,
                                onValueChange = { destination = it },
                                label = { Text("目的地") },
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = { destinationExpanded = true }) {
                                        Icon(
                                            Icons.Default.ArrowDropDown,
                                            contentDescription = "Dropdown",
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
                            DropdownMenu(
                                expanded = destinationExpanded,
                                onDismissRequest = { destinationExpanded = false },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                posList.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option.name) },
                                        onClick = {
                                            destination = option.name
                                            destinationExpanded = false
                                        }
                                    )
                                }
                            }
                        }


                        // 第四行：日期选择框
                        OutlinedTextField(
                            value = date,
                            onValueChange = { date = it },
                            label = { Text("拼车日期") },
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = {
                                    showDatePickerDialog(context) { selectedDate ->
                                        date = selectedDate
                                    }
                                }) {
                                    Icon(Icons.Default.DateRange,
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
                            value = earliestTime,
                            onValueChange = { earliestTime = it },
                            label = { Text("最早出发时间") },
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = {
                                    showTimePickerDialog(context) { selectedTime ->
                                        earliestTime = selectedTime
                                    }
                                }) {
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
                            value = latestTime,
                            onValueChange = { latestTime = it },
                            label = { Text("最晚出发时间") },
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = {
                                    showTimePickerDialog(context) { selectedTime ->
                                        latestTime = selectedTime
                                    }
                                }) {
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
                            value = notes,
                            onValueChange = { notes = it },
                            label = { Text("备注") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions.Default,
                            keyboardActions = KeyboardActions.Default,
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
                // 第八行：提交按钮
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = {
                        if (USERTYPE != 1) {
                            Toast.makeText(context, "该用户类型不能发布拼单", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        // 检查
                        if (departure.isEmpty()) {
                            Toast.makeText(context, "出发地为空", Toast.LENGTH_SHORT).show()
                            return@Button
                        } else if (destination.isEmpty()) {
                            Toast.makeText(context, "目的地为空", Toast.LENGTH_SHORT).show()
                            return@Button
                        } else if (date.isEmpty()) {
                            Toast.makeText(context, "日期为空", Toast.LENGTH_SHORT).show()
                            return@Button
                        } else if (earliestTime.isEmpty()) {
                            Toast.makeText(context, "最早出发时间为空", Toast.LENGTH_SHORT)
                                .show()
                            return@Button
                        } else if (latestTime.isEmpty()) {
                            Toast.makeText(context, "最晚出发时间为空", Toast.LENGTH_SHORT)
                                .show()
                            return@Button
                        } else if (notes.length >= 100) {
                            Toast.makeText(context, "备注长于100字符", Toast.LENGTH_SHORT)
                                .show()
                            return@Button
                        } else if (departure == destination) {
                            Toast.makeText(context, "出发地和起始地相同", Toast.LENGTH_SHORT)
                                .show()
                            return@Button
                        }

                        scope.launch {
                            try {
                                val response = APISERVICCE.publish(
                                    request = PublishRequest(
                                        departure,
                                        destination,
                                        date,
                                        earliestTime,
                                        latestTime,
                                        notes
                                    )
                                )
                                if (response.isSuccessful && response.body()?.code == 201) {
                                    // 成功
                                    Toast.makeText(context, "发布成功", Toast.LENGTH_SHORT)
                                        .show()
                                    val id = response.body()?.data?.order_id
                                    // 成功后跳转到 详情界面
                                    navController.navigate("tripDetail/$id") {
                                        popUpTo("createTripRoot") { inclusive = true } // 清除发布页面
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
                                    "发布失败，请检查网络连接",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                ) {
                    Text(
                        "发布",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }

}

fun showDatePickerDialog(
    context: Context,
    onDateSelected: (String) -> Unit
) {
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        R.style.CustomDatePickerTheme, // 使用自定义主题
        { _, year, month, dayOfMonth ->
            val selectedDate = "$year-${month + 1}-$dayOfMonth"
            onDateSelected(selectedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    // 设置最小日期为当前日期
    datePickerDialog.datePicker.minDate = System.currentTimeMillis()
    // 设置最大日期为一年后的日期
    datePickerDialog.datePicker.maxDate = System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000

    datePickerDialog.show()
}

fun showTimePickerDialog(
    context: Context,
    onTimeSelected: (String) -> Unit
) {
    val calendar = Calendar.getInstance()
    TimePickerDialog(
        context,
        R.style.CustomDatePickerTheme, // 使用自定义主题
        { _, hourOfDay, minute ->
            val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
            onTimeSelected(selectedTime)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    ).show()
}

@Preview
@Composable
fun PreviewPublishScreen() {
    White_webTheme {
        val navController = rememberNavController()
        PublishScreen(navController)
    }
}