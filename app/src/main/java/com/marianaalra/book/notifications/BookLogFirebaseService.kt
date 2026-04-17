package com.marianaalra.book.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.marianaalra.book.MainActivity
import com.marianaalra.book.R

class BookLogFirebaseService : FirebaseMessagingService() {

    companion object {
        const val CHANNEL_ID = "booklog_channel"
        const val CHANNEL_NAME = "Notificaciones BookLog"
    }

    // Se llama cuando llega una notificación con la app en primer plano
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Obtenemos título y cuerpo del mensaje
        val titulo = remoteMessage.notification?.title ?: "BookLog"
        val cuerpo = remoteMessage.notification?.body ?: "Tienes una nueva notificación"

        mostrarNotificacion(titulo, cuerpo)
    }

    // Se llama cuando Firebase asigna o renueva el token del dispositivo
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Aquí podrías guardar el token en tu base de datos o mostrarlo en logs
        Log.d("FCM_TOKEN", "Nuevo token: $token")
    }

    private fun mostrarNotificacion(titulo: String, cuerpo: String) {
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Crear el canal (necesario para Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(canal)
        }

        // Intent para abrir la app al tocar la notificación
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Construir la notificación
        val notificacion = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(titulo)
            .setContentText(cuerpo)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notificacion)
    }
}