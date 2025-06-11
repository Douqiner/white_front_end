package com.example.white_web.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.white_web.APISERVICCE

data class User(
    val username: String,
    val password: String
)

data class LookResponse(
    val code: Int,
    val message: String,
    val data: Data?
) {
    data class Data(
        val table: List<User>
    )
}


@Composable
fun MyInfoScreen() {
    var tableData by remember { mutableStateOf<List<User>?>(null) }

    LaunchedEffect(Unit) {
        try {
            val response = APISERVICCE.look()
            if (response.isSuccessful && response.body()?.code == 200) {
                tableData = response.body()?.data?.table
            } else {
                // 处理错误
                println("Error: ${response.body()?.message}")
            }
        } catch (e: Exception) {
            // 处理异常
            println("Network error: ${e.message}")
        }
    }

    if (tableData != null) {
        DisplayTable(tableData!!)
    } else {
        Text("载入中...")
    }
}

@Composable
fun DisplayTable(users: List<User>) {
    val columns = listOf("用户名", "密码")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {// 标题行
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            columns.forEach { column ->
                Text(
                    text = column,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // 数据行
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            items(users) { user ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = user.username, modifier = Modifier.weight(1f))
                    Text(text = user.password, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewMyinfo() {
    MyInfoScreen()
}