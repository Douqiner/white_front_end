import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.white_web.APISERVICCE
import kotlinx.coroutines.launch
import java.util.*

data class GetPosResponse(
    val code: Int,
    val message: String,
    val data: Data?
) {
    data class Data(
        val table: List<String>
    )
}

data class PublishRequest(
    val departure: String,
    val destination: String,
    val date: String,
    val earliestTime: String,
    val latestTime: String,
    val notes: String
)

data class PublishResponse(
    val code: Int,
    val message: String,
    val data: Data?
) {
    data class Data(
        val id: Int,
    )
}

@Composable
fun PublishScreen() {
    var posList by remember { mutableStateOf<List<String>?>(null) }

//    LaunchedEffect(Unit) {
//        try {
//            val response = APISERVICCE.getPos()
//            if (response.isSuccessful && response.body()?.code == 200) {
//                posList = response.body()?.data?.table
//            } else {
//                // 处理错误
//                println("Error: ${response.body()?.message}")
//            }
//        } catch (e: Exception) {
//            // 处理异常
//            println("Network error: ${e.message}")
//        }
//    }
    posList = listOf("选项1", "选项2", "选项3")

    if (posList != null) {
        DisplayPublish(posList!!)
    } else {
        Text("载入中...")
    }
}

@Composable
fun DisplayPublish(posList: List<String>) {
    var departure by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }
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
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // 添加垂直滚动
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 第一行：文字“拼车需求填写”
        Text("拼车需求填写", style = MaterialTheme.typography.headlineSmall)


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
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            DropdownMenu(
                expanded = departureExpanded,
                onDismissRequest = { departureExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                posList.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            departure = option
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
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            DropdownMenu(
                expanded = destinationExpanded,
                onDismissRequest = { destinationExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                posList.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            destination = option
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
                    Icon(Icons.Default.DateRange, contentDescription = "Date Picker")
                }
            },
            modifier = Modifier.fillMaxWidth()
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
                    Icon(Icons.Default.DateRange, contentDescription = "Time Picker")
                }
            },
            modifier = Modifier.fillMaxWidth()
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
                    Icon(Icons.Default.DateRange, contentDescription = "Time Picker")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // 第七行：备注
        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("备注") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default,
            keyboardActions = KeyboardActions.Default
        )

        // 第八行：提交按钮
        Button(
            onClick = {
                // 检查
                if (departure.isEmpty()) {
                    Toast.makeText(context, "出发地为空", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                else if (destination.isEmpty()) {
                    Toast.makeText(context, "目的地为空", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                else if (date.isEmpty()) {
                    Toast.makeText(context, "日期为空", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                else if (earliestTime.isEmpty()) {
                    Toast.makeText(context, "最早出发时间为空", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                else if (latestTime.isEmpty()) {
                    Toast.makeText(context, "最晚出发时间为空", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                else if (notes.length >= 100) {
                    Toast.makeText(context, "备注长于100字符", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                else if (departure == destination) {
                    Toast.makeText(context, "出发地和起始地相同", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                scope.launch {
                    try {
                        val response = APISERVICCE.publish(request = PublishRequest(departure, destination, date, earliestTime, latestTime, notes))
                        if (response.isSuccessful && response.body()?.code == 200) {
                            // 成功
                            Toast.makeText(context, "发布成功", Toast.LENGTH_SHORT).show()
                            var id = response.body()?.data?.id
                            // 成功后跳转到 详情界面
//                            navController.navigate("home") {
//
//                            }
                        } else {
                            // 发布失败
                            Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        // 网络请求异常
                        Toast.makeText(context, "发布失败，请检查网络连接", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("发布")
        }
    }
}

fun showDatePickerDialog(
    context: Context,
    onDateSelected: (String) -> Unit
) {
    val calendar = Calendar.getInstance()
    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedDate = "$year-${month + 1}-$dayOfMonth"
            onDateSelected(selectedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}

fun showTimePickerDialog(
    context: Context,
    onTimeSelected: (String) -> Unit
) {
    val calendar = Calendar.getInstance()
    TimePickerDialog(
        context,
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
    PublishScreen()
}