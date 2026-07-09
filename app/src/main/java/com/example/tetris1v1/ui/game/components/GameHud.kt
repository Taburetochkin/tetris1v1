package com.example.tetris1v1.ui.game.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.KeyboardDoubleArrowDown
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tetris1v1.game.PIECE_COLORS
import com.example.tetris1v1.game.data.Piece
import com.example.tetris1v1.ui.theme.*

@Composable
fun StatsRow(
    level: Int,
    score: Int,
    lines: Int,
    nextPiece: Piece
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        StatCard(label = "Lvl", value = level.toString())
        StatCard(label = "Score", value = score.toString())
        NextPieceCard(piece = nextPiece)
        StatCard(label = "Line", value = lines.toString())
    }
}

@Composable
private fun StatCard(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, color = TextMuted, fontSize = 12.sp)
        Spacer(Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(CardBackground, RoundedCornerShape(12.dp))
                .border(1.dp, CardBorder, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = value, color = TextWhite, fontSize = 16.sp)
        }
    }
}

@Composable
private fun NextPieceCard(piece: Piece, cellSize: Dp = 8.dp) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Next", color = TextMuted, fontSize = 12.sp)
        Spacer(Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(CardBackground, RoundedCornerShape(12.dp))
                .border(1.dp, CardBorder, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                piece.shape.forEach { row ->
                    Row {
                        row.forEach { cell ->
                            Box(
                                modifier = Modifier
                                    .size(cellSize)
                                    .padding(0.5.dp)
                                    .then(
                                        if (cell != 0) {
                                            Modifier
                                                .background(PIECE_COLORS[piece.colorIndex] ?: Color.White)
                                                .border(1.dp, Color.White, RoundedCornerShape(1.dp))
                                        } else Modifier
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ControlPad(
    isPlaying: Boolean,
    onMoveLeft: () -> Unit,
    onMoveRight: () -> Unit,
    onRotate: () -> Unit,
    onSoftDrop: () -> Unit,
    onHardDrop: () -> Unit,
    onTogglePause: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
            modifier = Modifier.fillMaxWidth()
        ) {
            ControlButton(icon = Icons.Default.KeyboardDoubleArrowDown, onClick = onHardDrop)

            CircleControlButton(
                icon = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                onClick = onTogglePause
            )

            ControlButton(icon = Icons.Default.Refresh, onClick = onRotate)
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
            modifier = Modifier.fillMaxWidth()
        ) {
            ControlButton(icon = Icons.Default.ArrowBack, onClick = onMoveLeft)
            ControlButton(icon = Icons.Default.ArrowForward, onClick = onMoveRight)
            ControlButton(icon = Icons.Default.ArrowDownward, onClick = onSoftDrop)
        }
    }
}

@Composable
private fun ControlButton(icon: ImageVector, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.size(56.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = ControlButtonBackground),
        border = ButtonDefaults.outlinedButtonBorder.copy(brush = SolidColor(ControlButtonBorder)),
        contentPadding = PaddingValues(0.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = TextWhite)
    }
}

@Composable
private fun CircleControlButton(icon: ImageVector, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.size(56.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = ControlButtonBackground),
        border = ButtonDefaults.outlinedButtonBorder.copy(brush = SolidColor(ControlButtonBorder)),
        contentPadding = PaddingValues(0.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = TextWhite)
    }
}