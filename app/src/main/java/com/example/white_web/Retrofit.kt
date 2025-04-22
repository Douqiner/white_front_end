package com.example.white_web

import com.example.white_web.home.LookResponse
import com.example.white_web.home.AllOrdersResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

private val retrofit = Retrofit.Builder()
    .baseUrl("http://10.0.2.2:8443/")  // 后端地址
    .addConverterFactory(GsonConverterFactory.create())
    .build()

var USERNAME : String? = "未登录"
var TOKEN : String? = ""

interface ApiService {
    @POST("/api/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("/api/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @GET("/api/look")
    suspend fun look(@Header("Authorization") token: String? = TOKEN): Response<LookResponse>

    @GET("/api/orders")
    suspend fun getOrders(): Response<AllOrdersResponse>

    @GET("api/orders/search/{keyword}")
    suspend fun getSearchResults(@Path("keyword") keyword: String): Response<AllOrdersResponse>
}

val APISERVICCE = retrofit.create(ApiService::class.java)
