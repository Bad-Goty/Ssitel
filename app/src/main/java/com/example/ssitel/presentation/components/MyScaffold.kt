package com.example.ssitel.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun MyScaffold(
    content: @Composable () -> Unit
) {

    val systemUiController = rememberSystemUiController()
    val statusBarColor = Color(0xFF1C71C5)

    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons =  true //Esto hace negro la hora, wifi y todo
        )
    }


    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerpadding ->
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .fillMaxSize()
                .padding(innerpadding)
        ) {
            content()
        }
    }
}