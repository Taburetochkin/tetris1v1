package com.example.tetris1v1.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tetris1v1.ui.theme.TetrisRed

@Composable
fun TetrisLogo(modifier: Modifier){
    val blockSize = 40.dp
    val gap = 2.dp

    Box(modifier = modifier){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                repeat(3){
                    Box(
                        modifier = Modifier
                            .size(blockSize)
                            .background(TetrisRed)
                            .border(gap,Color.White, RoundedCornerShape(2))
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(gap)) {
                Spacer(modifier = Modifier.width(blockSize))
                Box(
                    modifier = Modifier
                        .size(blockSize)
                        .background(TetrisRed)
                        .border(gap,Color.White, RoundedCornerShape(2))
                )
                Spacer(modifier = Modifier.width(blockSize))
            }

            Row(horizontalArrangement = Arrangement.spacedBy(gap)) {
                Spacer(modifier = Modifier.width(blockSize))
                Box(
                    modifier = Modifier
                        .size(blockSize)
                        .background(TetrisRed)
                        .border(gap,Color.White, RoundedCornerShape(2))
                )
                Spacer(modifier = Modifier.width(blockSize))
            }
        }
    }
}