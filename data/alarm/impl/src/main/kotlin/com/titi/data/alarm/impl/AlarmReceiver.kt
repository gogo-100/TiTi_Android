package com.titi.data.alarm.impl

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.titi.core.util.goAsync
import com.titi.data.alarm.impl.local.AlarmDataStore
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope

internal class AlarmReceiver : BroadcastReceiver() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    internal interface AlarmReceiverEntryPoint {
        fun getAlarmDataStore(): AlarmDataStore
    }

    lateinit var alarmDataStore: AlarmDataStore

    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("ALARM_TITLE") ?: return
        val message = intent.getStringExtra("ALARM_MESSAGE")
        val channelId = "titiChannelId"
        val entryPoint = EntryPointAccessors.fromApplication(
            context,
            AlarmReceiverEntryPoint::class.java
        )
        alarmDataStore = entryPoint.getAlarmDataStore()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_android_black_24dp)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        notificationManager.notify(0, builder.build())

        goAsync(GlobalScope, Dispatchers.IO) {
            alarmDataStore.removeAlarms()
        }
    }

}