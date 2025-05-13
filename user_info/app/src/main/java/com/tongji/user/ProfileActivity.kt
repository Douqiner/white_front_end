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

package com.tongji.user

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tongji.user.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileActivity : AppCompatActivity() {
    private lateinit var tvPhone: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        tvPhone = findViewById(R.id.tvPhone)
        loadPhoneNumber()
    }

    private fun loadPhoneNumber() {
        tvPhone.text = "加载中..." // 初始状态

        lifecycleScope.launch {
            try {
                val phone = withContext(Dispatchers.IO) {
                    RetrofitClient.instance.getUserInfo().phone
                }
                showPhoneSuffix(phone)
            } catch (e: Exception) {
                Toast.makeText(
                    this@ProfileActivity,
                    "获取号码失败",
                    Toast.LENGTH_SHORT
                ).show()
                // 保持"加载中"状态不改变
            }
        }
    }

    private fun showPhoneSuffix(fullPhone: String) {
        val suffix = if (fullPhone.length >= 4) {
            fullPhone.takeLast(4)
        } else {
            fullPhone // 不足4位显示全部
        }
        tvPhone.text = "尾号$suffix"
    }
}