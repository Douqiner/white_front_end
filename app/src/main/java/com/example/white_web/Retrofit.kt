package com.example.white_web

import DetailResponse
import GetPosResponse
import JoinLeaveRedponse
import JoinLeaveRequest
import PublishRequest
import PublishResponse
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

    @GET("/api/getpos")
    suspend fun getPos(): Response<GetPosResponse>

    @POST("/api/orders/add")
    suspend fun publish(@Header("Authorization") token: String? = TOKEN, @Body request: PublishRequest): Response<PublishResponse>

    @GET("/api/orders/{order_id}")
    suspend fun detail(@Path("order_id") orderId: Int): Response<DetailResponse>

    @POST("/api/orders/join")
    suspend fun joinOrder(@Header("Authorization") token: String? = TOKEN, @Body request: JoinLeaveRequest): Response<JoinLeaveRedponse>

    @POST("/api/orders/leave")
    suspend fun leaveOrder(@Header("Authorization") token: String? = TOKEN, @Body request: JoinLeaveRequest): Response<JoinLeaveRedponse>
}

val APISERVICCE = retrofit.create(ApiService::class.java)
