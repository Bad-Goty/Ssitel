package com.example.ssitel.presentation.components

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun MyOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    isPassword: Boolean = false    // 👈 agregado
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = {
            if (!placeholder.isNullOrEmpty()) {
                Text(text = placeholder)
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            disabledPlaceholderColor = Color.Gray,
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Black,
            cursorColor = Color.Black,
            selectionColors = TextSelectionColors(
                handleColor = Color(0xFF0B557D),
                backgroundColor = Color(0x330B557D) // con alpha para que se vea bien
            )
        ),

        // 👇 parte mágica para contraseña
        visualTransformation = when {
            !isPassword -> VisualTransformation.None
            passwordVisible -> VisualTransformation.None
            else -> PasswordVisualTransformation()
        },

        // 👇 ícono del ojito
        trailingIcon = {
            if (isPassword) {
                IconButton (onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible)
                            Icons.Default.Clear
                        else
                            Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            }
        },

        singleLine = true
    )
}
