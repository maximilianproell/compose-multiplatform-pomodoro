package com.compose.multiplatform.pomodoro

import MainView
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainView()
        }
    }

    override fun onResume() {
        super.onResume()

        Log.d(this::class.simpleName, "onResume")
    }

    override fun onPause() {
        super.onPause()

        Log.d(this::class.simpleName, "onPause")
    }
}