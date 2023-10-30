package com.compose.multiplatform.pomodoro

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.compose.multiplatform.pomodoro.data.di.initKoin
import org.koin.android.ext.koin.androidContext
import com.compose.multiplatform.pomodoro.utils.getString

class PomodoroApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@PomodoroApplication)
        }

        // Create the NotificationChannel.
        val name = getString(MR.strings.pomodoro_timer_channel_name)
        val descriptionText = getString(MR.strings.pomodoro_timer_channel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel("pomodoro_timer_channel", name, importance)
        mChannel.description = descriptionText
        // Register the channel with the system. You can't change the importance
        // or other notification behaviors after this.
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }
}