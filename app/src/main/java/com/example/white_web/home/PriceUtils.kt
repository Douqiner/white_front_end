package com.example.white_web.home

/**
 * 价格计算相关的工具类
 */
object PriceUtils {
    
    /**
     * 根据距离计算预期价格
     * @param distanceInMeters 距离，单位：米（字符串格式）
     * @return 格式化的价格字符串，如 "15元"
     */
    fun calculateExpectedPrice(distanceInMeters: String): String {
        return try {
            val distance = distanceInMeters.toDoubleOrNull() ?: return "价格计算中..."
            val distanceInKm = distance / 1000.0

            // 价格计算规则：
            // 起步价：8元（3公里内）
            // 超过3公里：每公里2.5元
            // 最低价格：8元
            // 最高价格：1000元（防止异常情况）
            val basePrice = 8.0
            val basePriceDistance = 3.0
            val pricePerKm = 2.5

            val totalPrice = if (distanceInKm <= basePriceDistance) {
                basePrice
            } else {
                basePrice + (distanceInKm - basePriceDistance) * pricePerKm
            }

            // 限制价格范围并四舍五入到整数
            val finalPrice = totalPrice.coerceIn(8.0, 1000.0).toInt()
            "${finalPrice}元"

        } catch (e: Exception) {
            "价格计算中..."
        }
    }

    /**
     * 格式化距离显示
     * @param distanceInMeters 距离，单位：米（字符串格式）
     * @return 格式化的距离字符串，如 "1.5公里" 或 "500米"
     */
    fun formatDistanceDisplay(distanceInMeters: String): String {
        return try {
            val distance = distanceInMeters.toDoubleOrNull() ?: return "距离计算中..."
            val distanceInKm = distance / 1000.0
            if (distanceInKm < 1.0) {
                "${distance.toInt()}米"
            } else {
                "%.1f公里".format(distanceInKm)
            }
        } catch (e: Exception) {
            "距离计算中..."
        }
    }

    /**
     * 计算人均价格
     * @param totalPriceString 总价格字符串，如 "20元"
     * @param participantCount 参与人数
     * @return 人均价格字符串，如 "5元/人"
     */
    fun calculatePricePerPerson(totalPriceString: String, participantCount: Int): String {
        return try {
            val totalPrice = totalPriceString.replace("元", "").toInt()
            if (participantCount > 0) {
                val perPerson = totalPrice / participantCount
                "${perPerson}元/人"
            } else {
                "计算中..."
            }
        } catch (e: Exception) {
            "计算中..."
        }
    }
}