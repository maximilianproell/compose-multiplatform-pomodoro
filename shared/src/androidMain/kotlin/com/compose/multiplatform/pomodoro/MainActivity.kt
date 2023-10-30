package com.compose.multiplatform.pomodoro

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val lifecycleHelper = LifecycleHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainView()
        }
    }

    override fun onResume() {
        super.onResume()

        Log.d(this::class.simpleName, "onResume")
        lifecycleHelper.onAppMovedToForeground()
    }

    override fun onPause() {
        super.onPause()

        Log.d(this::class.simpleName, "onPause")
        lifecycleHelper.onAppMovedToBackGround()
    }
}