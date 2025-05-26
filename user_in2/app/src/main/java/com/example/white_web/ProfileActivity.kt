//package com.tongji.user
//
//import android.os.Bundle
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.lifecycleScope
//import com.tongji.user.network.UserInfo
//import com.tongji.user.network.UserService
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//class ProfileActivity : AppCompatActivity() {
//    private lateinit var tvPhone: TextView
//    private lateinit var tvEstimatedLoan: TextView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_profile)
//
//        // 初始化视图
//        tvPhone = findViewById(R.id.tvPhone)
//        tvEstimatedLoan = findViewById(R.id.tvEstimatedLoan)
//
//        // 加载用户数据
//        loadUserData()
//    }
//
//    private fun loadUserData() {
//        // 显示加载状态
//        tvPhone.text = "加载中..."
//        tvEstimatedLoan.text = "加载中..."
//
//        // 使用协程进行异步网络请求
//        lifecycleScope.launch {
//            try {
//                // 从IO线程获取数据
//                val userInfo = withContext(Dispatchers.IO) {
//                    UserService.getUserInfo()
//                }
//
//                // 更新UI
//                updateUI(userInfo)
//            } catch (e: Exception) {
//                // 处理错误
//                Toast.makeText(this@ProfileActivity, "加载用户数据失败", Toast.LENGTH_SHORT).show()
//                tvPhone.text = "尾号0713" // 默认显示尾号
//                tvEstimatedLoan.text = "0" // 默认显示
//            }
//        }
//    }
//
//    private fun updateUI(userInfo: UserInfo) {
//        // 直接显示手机尾号0713
//        tvPhone.text = "尾号0713"
//
//        // 格式化贷款金额显示
//        tvEstimatedLoan.text = if (userInfo.estimatedLoan >= 10000) {
//            "${(userInfo.estimatedLoan / 10000).toInt()}万"
//        } else {
//            userInfo.estimatedLoan.toString()
//        }
//    }
//}

//package com.example.white_web
//
//import android.os.Bundle
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.lifecycleScope
//import com.example.white_web.network.RetrofitClient
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//class ProfileActivity : AppCompatActivity() {
//    private lateinit var tvPhone: TextView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_profile)
//        tvPhone = findViewById(R.id.tvPhone)
//        loadPhoneNumber()
//    }
//
//    private fun loadPhoneNumber() {
//        tvPhone.text = "加载中..." // 初始状态
//
//        lifecycleScope.launch {
//            try {
//                val phone = withContext(Dispatchers.IO) {
//                    RetrofitClient.instance.getUserInfo().phone
//                }
//                showPhoneSuffix(phone)
//            } catch (e: Exception) {
//                Toast.makeText(
//                    this@ProfileActivity,
//                    "获取号码失败",
//                    Toast.LENGTH_SHORT
//                ).show()
//                // 保持"加载中"状态不改变
//            }
//        }
//    }
//
//    private fun showPhoneSuffix(fullPhone: String) {
//        val suffix = if (fullPhone.length >= 4) {
//            fullPhone.takeLast(4)
//        } else {
//            fullPhone // 不足4位显示全部
//        }
//        tvPhone.text = "尾号$suffix"
//    }
//}

//package com.example.white_web
//
//import android.os.Bundle
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.lifecycleScope
//import com.example.white_web.network.RetrofitClient
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//class ProfileActivity : AppCompatActivity() {
//    private lateinit var tvPhone: TextView
//    private lateinit var currentUser: String // 用于存储用户名
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_profile)
//
//        // 从intent中获取USERNAME
//        currentUser = intent.getStringExtra("CURRENT_USER") ?: ""
//
//        // 你可以在这里使用currentUser，比如显示在界面上或用于API请求
//        Toast.makeText(this, "当前用户: $currentUser", Toast.LENGTH_SHORT).show()
//
//        tvPhone = findViewById(R.id.tvPhone)
//        loadPhoneNumber()
//    }
//
//    private fun loadPhoneNumber() {
//        tvPhone.text = "加载中..." // 初始状态
//
//        lifecycleScope.launch {
//            try {
//                val phone = withContext(Dispatchers.IO) {
//                    // 这里可以使用currentUser作为参数
//                    RetrofitClient.instance.getUserInfo(currentUser).phone
//                }
//                showPhoneSuffix(phone)
//            } catch (e: Exception) {
//                Toast.makeText(
//                    this@ProfileActivity,
//                    "获取号码失败",
//                    Toast.LENGTH_SHORT
//                ).show()
//                // 保持"加载中"状态不改变
//            }
//        }
//    }
//
//    private fun showPhoneSuffix(fullPhone: String) {
//        val suffix = if (fullPhone.length >= 4) {
//            fullPhone.takeLast(4)
//        } else {
//            fullPhone // 不足4位显示全部
//        }
//        tvPhone.text = "尾号$suffix"
//    }
//}
package com.example.white_web

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {
    private lateinit var tvUsername: TextView  // 改为显示用户名

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // 从 Intent 获取用户名（CURRENT_USER）
        val username = intent.getStringExtra("CURRENT_USER") ?: "未知用户"

        // 显示用户名（替换之前的手机号逻辑）
        tvUsername = findViewById(R.id.tvPhone)  // 沿用原有布局（假设 tvPhone 是 TextView）
        tvUsername.text = "用户名: $username"     // 直接显示
    }
}