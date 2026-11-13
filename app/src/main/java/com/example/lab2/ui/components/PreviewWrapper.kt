package com.example.lab2.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun PreviewWrapper(content: @Composable () -> Unit) {
    MaterialTheme {
        content()
    }
}