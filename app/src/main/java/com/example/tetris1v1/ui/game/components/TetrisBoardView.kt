package com.example.tetris1v1.ui.game.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tetris1v1.game.Board
import com.example.tetris1v1.game.PIECE_COLORS
import com.example.tetris1v1.ui.theme.FieldBackground

@Composable
fun TetrisBoardView(
    displayBoard: Board,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.background(FieldBackground)
    ) {
        displayBoard.forEach { rowData ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                rowData.forEach { value ->
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .padding(1.dp)
                    ) {
                        if (value != 0) {
                            val color = if (value > 0) {
                                PIECE_COLORS[value] ?: Color.White
                            } else {
                                (PIECE_COLORS[-value] ?: Color.White).copy(alpha = 0.33f)
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color)
                                    .border(2.dp, Color.White, RoundedCornerShape(2.dp))
                            )
                        }
                    }
                }
            }
        }
    }
}