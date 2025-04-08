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

data class DetailRequest(
    val id: Int
)

data class DetailResponse(
    val code: Int,
    val message: String,
    val data: DetailData?
)
data class DetailData(
    val departure: String,
    val destination: String,
    val date: String,
    val earliestTime: String,
    val latestTime: String,
    val notes: String,
    val telephony: String
)

@Composable
fun DetailScreen(id: Int) {
    var detailData by remember { mutableStateOf<DetailData?>(null) }
    val context = LocalContext.current

//    LaunchedEffect(Unit) {
//        try {
//            val response = APISERVICCE.detail(request = DetailRequest(id))
//            if (response.isSuccessful && response.body()?.code == 200) {
//                detailData = response.body()?.data
//            } else {
//                // 处理错误
//                Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
//            }
//        } catch (e: Exception) {
//            // 处理异常
//            Toast.makeText(context, "获取详情失败，请检查网络连接", Toast.LENGTH_SHORT).show()
//        }
//    }
    detailData = DetailData("嘉定校区", "上海虹桥火车站", "2024-3-30", "5:30", "6:30", "男生，打车", "12312341234")

    if (detailData != null) {
        DisplayDetail(detailData!!)
    } else {
        Text("载入中...")
    }
}

@Composable
fun DisplayDetail(detailData: DetailData) {

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
        Text("拼车需求详情", style = MaterialTheme.typography.headlineSmall)

        // 第二行：出发地
        OutlinedTextField(
            value = detailData.departure,
            onValueChange = {},
            label = { Text("出发地") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = detailData.destination,
            onValueChange = {},
            label = { Text("目的地") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )

        // 第四行：日期选择框
        OutlinedTextField(
            value = detailData.date,
            onValueChange = {},
            label = { Text("拼车日期") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.DateRange, contentDescription = "Date Picker")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // 第五行：最早出发时间选择框
        OutlinedTextField(
            value = detailData.earliestTime,
            onValueChange = {},
            label = { Text("最早出发时间") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.DateRange, contentDescription = "Time Picker")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // 第六行：最晚出发时间选择框
        OutlinedTextField(
            value = detailData.latestTime,
            onValueChange = {},
            label = { Text("最晚出发时间") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.DateRange, contentDescription = "Time Picker")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // 第七行：备注
        OutlinedTextField(
            value = detailData.notes,
            onValueChange = {},
            label = { Text("备注") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default,
            keyboardActions = KeyboardActions.Default,
            readOnly = true
        )

        // 第八行：电话
        OutlinedTextField(
            value = detailData.telephony,
            onValueChange = {},
            label = { Text("联系方式") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Preview
@Composable
fun PreviewDetailScreen() {
    DetailScreen(0)
}