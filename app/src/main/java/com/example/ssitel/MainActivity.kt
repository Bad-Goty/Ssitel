package com.example.ssitel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ssitel.presentation.navigation.MyNavigation
import com.example.ssitel.presentation.ui.theme.SsitelTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SsitelTheme {
                MyNavigation()
            }
        }
    }
}

