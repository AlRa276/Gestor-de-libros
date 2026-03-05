package com.marianaalra.booklog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.marianaalra.booklog.ui.theme.VistaTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VistaTheme{
                MainContent()
            }
        }
    }
}

@Composable
private fun MainContent() {
    Text(text = "BookLog")
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MainContentPreview() {
    VistaTheme {
        MainContent()
    }
}