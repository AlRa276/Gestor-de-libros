package com.marianaalra.booklog

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.marianaalra.booklog.ui.navigation.AppNavigation
import com.marianaalra.booklog.ui.theme.VistaTheme

class MainActivity : ComponentActivity() {

    // Lanzador para pedir el permiso de notificaciones
    private val permisosNotificacion = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            android.util.Log.d("FCM", "Permiso de notificaciones concedido")
        } else {
            android.util.Log.d("FCM", "Permiso de notificaciones denegado")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Pedimos permiso solo en Android 13+
        pedirPermisoNotificaciones()

        // Obtenemos e imprimimos el token FCM (útil para pruebas)
        FirebaseMessaging.getInstance().token.addOnCompleteListener { tarea ->
            if (tarea.isSuccessful) {
                val token = tarea.result
                android.util.Log.d("FCM_TOKEN", "Token del dispositivo: $token")
            }
        }

        setContent {
            VistaTheme {
                AppNavigation()
            }
        }
    }

    private fun pedirPermisoNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permisosNotificacion.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}