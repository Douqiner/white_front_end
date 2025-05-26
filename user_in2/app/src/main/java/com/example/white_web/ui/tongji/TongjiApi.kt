//// TongjiApi.kt
//package com.example.white_web.ui.tongji
//
////import com.example.white_web.UserDetailResponse
//import com.example.white_web.home.UserDetailResponse
//import retrofit2.Response
//import retrofit2.http.GET
//import retrofit2.http.Header
//import retrofit2.http.Path
//
//interface TongjiApiService {
//    @GET("/api/user/{username}")
//    suspend fun getUserInfo(
//        @Header("Authorization") token: String?,
//        @Path("username") username: String
//    ): Response<UserDetailResponse>
//}
//
//// 扩展原有Retrofit实例
//val tongjiApiService: TongjiApiService by lazy {
//    com.example.white_web.retrofit.create(TongjiApiService::class.java)
//}