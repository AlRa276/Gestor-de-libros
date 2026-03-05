package com.marianaalra.booklog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.marianaalra.booklog.ui.navigation.AppNavigation
import com.marianaalra.booklog.ui.theme.VistaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VistaTheme {
                // 👇 ESTO INICIA TODO EL FLUJO DE TU APP
                AppNavigation()
            }
        }
    }
}