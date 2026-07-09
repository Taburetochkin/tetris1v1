package com.example.tetris1v1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.tetris1v1.ui.auth.AuthNavigation
import com.example.tetris1v1.ui.theme.Tetris1v1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tetris1v1Theme {
                AuthNavigation()
            }
        }
    }
}