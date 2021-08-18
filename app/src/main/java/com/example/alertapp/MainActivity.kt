package com.example.alertapp

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermissionOverlay()
        val svc =  Intent(this, OverlayShowingService::class.java)
        startService(svc)
        finish()
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun checkPermissionOverlay() {
        if(!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "Need permission to overlay", Toast.LENGTH_SHORT).show()
            val intentSettings = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            startActivityForResult(intentSettings, 1)
        }
    }
}