package com.example.white_web.map

import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps2d.CameraUpdateFactory
import com.amap.api.maps2d.MapView
import com.amap.api.maps2d.model.LatLng
import com.amap.api.maps2d.model.MyLocationStyle

@Composable
fun GDMap(
    modifier: Modifier = Modifier,
    onLocationUpdated: (LatLng) -> Unit = {}
) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    var hasInitialized by remember { mutableStateOf(false) }

    // 定位回调处理
    LaunchedEffect(Unit) {
        initLocationListener(context, mapView) { lat, lng ->
            onLocationUpdated(LatLng(lat, lng))

            if (!hasInitialized) {
                mapView.getMap().moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(lat, lng),
                        17f
                    )
                )
                hasInitialized = true
            }
        }
    }

    // 纯地图视图
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            mapView.apply {
                onCreate(null)
                onResume()
                map.uiSettings.isZoomControlsEnabled = true
            }
        }
    )
}
/*      此函数进行位置的监听，图标显示              */
/*      注意：高德默认定位图标显示存在不明bug        */
/*      定位日志正确 但图标异常不显示               */
private fun initLocationListener(
    context: Context,
    mapView: MapView,
    onLocationChanged: (latitude: Double, longitude: Double) -> Unit
) {
    val locationClient = AMapLocationClient(context)
    val locationListener = AMapLocationListener { location ->
        if (location != null && location.errorCode == 0) {
            val latitude = location.latitude
            val longitude = location.longitude
            onLocationChanged(latitude, longitude)

            val aMap = mapView.map

            val myLocationStyle = MyLocationStyle()
            myLocationStyle.interval(1000)

            aMap.setMyLocationStyle(myLocationStyle)//设置定位蓝点的Style
            aMap.setMyLocationEnabled(true)
            Log.d("Location", "Latitude: $latitude, Longitude: $longitude")
        } else {
            Log.e("Location", "Location error: ${location?.errorCode}, ${location?.errorInfo}")
        }
    }

    locationClient.apply {
        setLocationListener(locationListener)
        startLocation()
    }
}