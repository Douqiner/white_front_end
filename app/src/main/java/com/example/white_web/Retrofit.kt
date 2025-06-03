package com.example.white_web

import DetailResponse
import GetPosResponse
import JoinLeaveRedponse
import JoinLeaveRequest
import PublishRequest
import PublishResponse
import UserDetailResponse
import com.example.white_web.home.AllOrdersResponse
import com.example.white_web.home.BaseResponse
import com.example.white_web.home.ConfirmArrivalResponse
import com.example.white_web.home.CurrentOrderResponse
import com.example.white_web.home.DriverRatingRequest
import com.example.white_web.home.LookResponse
import com.example.white_web.home.OrderIdRequest
import com.google.gson.annotations.SerializedName
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
//    .baseUrl("http://59.110.22.187:8443/") // 服务器地址
    .addConverterFactory(GsonConverterFactory.create())
    .build()

var USERNAME: String? = "未登录"
var USERTYPE: Int? = -1
var TOKEN: String? = ""

// 司机评分数据响应
data class DriverRatingResponse(
    val code: Int,
    val message: String,
    val data: DriverRatingData?
)

data class DriverRatingData(
    val username: String,
    val rating: Float,
    val rating_count: Int
)

// 检查用户是否已评分请求
data class CheckUserRatingRequest(
    @SerializedName("order_id")
    val orderId: Int,
    @SerializedName("driver_username")
    val driverUsername: String
)

// 检查用户是否已评分响应
data class CheckUserRatingResponse(
    val code: Int,
    val message: String,
    val data: CheckUserRatingData?
)

data class CheckUserRatingData(
    @SerializedName("has_rated")
    val hasRated: Boolean
)

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
    suspend fun publish(
        @Header("Authorization") token: String? = TOKEN,
        @Body request: PublishRequest
    ): Response<PublishResponse>

    @GET("/api/orders/{order_id}")
    suspend fun detail(@Path("order_id") orderId: Int): Response<DetailResponse>

    @GET("/api/user/{username}")
    suspend fun getUserDetail(@Path("username") username: String): Response<UserDetailResponse>

    @POST("/api/orders/join")
    suspend fun joinOrder(
        @Header("Authorization") token: String? = TOKEN,
        @Body request: JoinLeaveRequest
    ): Response<JoinLeaveRedponse>

    @POST("/api/orders/leave")
    suspend fun leaveOrder(
        @Header("Authorization") token: String? = TOKEN,
        @Body request: JoinLeaveRequest
    ): Response<JoinLeaveRedponse>

    // 添加到ApiService接口中

    @GET("/api/user/current-order")
    suspend fun getCurrentOrder(@Header("Authorization") token: String? = TOKEN): Response<CurrentOrderResponse>

    @POST("/api/order/confirm-arrival")
    suspend fun confirmArrival(
        @Header("Authorization") token: String? = TOKEN,
        @Body request: OrderIdRequest
    ): Response<ConfirmArrivalResponse>

    @POST("/api/order/start-trip")
    suspend fun startTrip(
        @Header("Authorization") token: String? = TOKEN,
        @Body request: OrderIdRequest
    ): Response<BaseResponse>

    @POST("/api/order/confirm-destination")
    suspend fun confirmDestination(
        @Header("Authorization") token: String? = TOKEN,
        @Body request: OrderIdRequest
    ): Response<BaseResponse>

    @POST("/api/order/rate-driver")
    suspend fun rateDriver(
        @Header("Authorization") token: String? = TOKEN,
        @Body request: DriverRatingRequest
    ): Response<BaseResponse>

    @GET("/api/user/driver-rating/{username}")
    suspend fun getDriverRating(@Path("username") username: String): Response<DriverRatingResponse>

    @POST("/api/order/check-user-rating")
    suspend fun checkUserRating(
        @Header("Authorization") token: String? = TOKEN,
        @Body request: CheckUserRatingRequest
    ): Response<CheckUserRatingResponse>

    @GET("/api/orders/not-started")
    suspend fun getNotStartedOrders(): Response<AllOrdersResponse>
}

val APISERVICCE = retrofit.create(ApiService::class.java)
