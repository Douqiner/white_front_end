// TongjiActivities.kt
package com.example.white_web.ui.tongji

import android.content.Context
import android.content.Intent
import com.example.white_web.USERNAME
import com.example.white_web.ProfileActivity
import com.example.white_web.OwnerActivity

object TongjiLauncher {
    fun launchProfile(context: Context) {
        context.startActivity(Intent(context, ProfileActivity::class.java).apply {
            putExtra("CURRENT_USER", USERNAME)
        })
    }

    fun launchOwner(context: Context, vehicleId: String) {
        context.startActivity(Intent(context, OwnerActivity::class.java).apply {
            putExtra("CURRENT_USER", USERNAME)
        })
    }
}