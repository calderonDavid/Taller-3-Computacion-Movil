package com.example.taller3.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.taller3.MainActivity
import com.example.taller3.MyApp
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.i(
            "FirebaseApp", "Message Received!!!"
        )
        val title = remoteMessage.notification?.title ?: "¡Nuevo Usuario!"
        val body = remoteMessage.notification?.body ?: "Alguien está disponible."
        val trackUserId = remoteMessage.data["trackUserId"]

        showNotification(title, body, trackUserId)
    }
    private fun showNotification(title: String, message: String, trackUserId: String?) {
        val notManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("TRACK_USER_ID", trackUserId)
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, MyApp.NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}