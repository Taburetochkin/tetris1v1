package com.example.tetris1v1.ui.game.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tetris1v1.ui.theme.*

@Composable
fun IdleOverlay(onStart: () -> Unit) {
    OverlayBox {
        Text(
            text = "TETRIS",
            color = TextWhite,
            fontSize = 28.sp,
            letterSpacing = 3.sp
        )
        Spacer(Modifier.height(16.dp))
        OverlayButton("Start", onStart)
    }
}

@Composable
fun PausedOverlay(onContinue: () -> Unit) {
    OverlayBox {
        Text(
            text = "PAUSE",
            color = TextWhite,
            fontSize = 26.sp,
            letterSpacing = 3.sp
        )
        Spacer(Modifier.height(16.dp))
        OverlayButton("Continue", onContinue)
    }
}

@Composable
fun GameOverOverlay(
    score: Int,
    onRestart: () -> Unit,
    onMenu: () -> Unit
) {
    OverlayBox {
        Text(
            text = "GAME OVER",
            color = TetrisRed,
            fontSize = 24.sp,
            letterSpacing = 2.sp
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Score: $score",
            color = TextWhite,
            fontSize = 16.sp
        )
        Spacer(Modifier.height(16.dp))
        OverlayButton("Restart", onRestart)
        Spacer(Modifier.height(10.dp))
        OverlayButton("Menu", onMenu, secondary = true)
    }
}

@Composable
private fun OverlayBox(content: @Composable ColumnScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBackground.copy(alpha = 0.85f), RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, content = content)
    }
}

@Composable
private fun OverlayButton(label: String, onClick: () -> Unit, secondary: Boolean = false) {
    if (secondary) {
        OutlinedButton(
            onClick = onClick,
            border = ButtonDefaults.outlinedButtonBorder.copy(brush = SolidColor(CardBorder)),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = AcceptGreen)
        ) {
            Text(label, color = TextWhite, letterSpacing = 1.sp)
        }
    } else {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = TetrisRed)
        ) {
            Text(label, color = TextWhite, letterSpacing = 1.sp)
        }
    }
}