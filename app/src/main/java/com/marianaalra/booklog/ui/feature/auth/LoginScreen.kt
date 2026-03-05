package com.marianaalra.booklog.ui.feature.auth

import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marianaalra.booklog.ui.theme.VistaTheme

@Composable
fun LoginScreen(
    onLoginClick: (username: String, password: String) -> Unit,
    onNavigateToRegister: () -> Unit,
    modifier: Modifier = Modifier
) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "BookLog",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary)
            Text(text = "Inicia sesión para continuar",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondary)

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Correo",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondary) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    // El color hexadecimal 0x4DFFB6C1 es un rosa claro (Pink) con ~30% de opacidad
                    unfocusedContainerColor = Color(0x4DFF96A6),
                    focusedContainerColor = Color(0x66FFB6C1), // Un poco más sólido al hacer clic
                    unfocusedBorderColor = Color.Transparent, // Sin borde cuando no está seleccionado
                    focusedBorderColor = Color.White, // Borde blanco al escribir
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondary) },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    // El color hexadecimal 0x4DFFB6C1 es un rosa claro (Pink) con ~30% de opacidad
                    unfocusedContainerColor = Color(0x4DFF96A6),
                    focusedContainerColor = Color(0x66FFB6C1), // Un poco más sólido al hacer clic
                    unfocusedBorderColor = Color.Transparent, // Sin borde cuando no está seleccionado
                    focusedBorderColor = Color.White, // Borde blanco al escribir
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Button(
                onClick = { onLoginClick(username, password) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Entrar")
            }

            TextButton(onClick = onNavigateToRegister) {
                Text(text = "¿No tienes cuenta? Regístrate")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginScreenPreview() {
    VistaTheme {
        LoginScreen(
            onLoginClick = { _, _ -> },
            onNavigateToRegister = {}
        )
    }
}
