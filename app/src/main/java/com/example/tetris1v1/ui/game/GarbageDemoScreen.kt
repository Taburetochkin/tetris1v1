package com.example.tetris1v1.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tetris1v1.game.enums.GamePhase
import com.example.tetris1v1.ui.game.components.*
import com.example.tetris1v1.ui.theme.NavyBackground
import com.example.tetris1v1.ui.theme.TextWhite

@Composable
fun GarbageDemoScreen(
    onExitToMenu: () -> Unit,
    viewModel: GarbageDemoViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    val isPlaying = uiState.phase == GamePhase.PLAYING
    val isPaused = uiState.phase == GamePhase.PAUSED
    val isGameOver = uiState.phase == GamePhase.OVER
    val isIdle = uiState.phase == GamePhase.IDLE

    val showControls = isPlaying || isPaused

    val controlsHeight = 168.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBackground)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Garbage Demo",
                color = TextWhite,
                fontSize = 22.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp)
            )

            Spacer(Modifier.height(8.dp))

            StatsRow(
                level = uiState.level,
                score = uiState.score,
                lines = uiState.lines,
                nextPiece = uiState.nextPiece
            )

            Spacer(Modifier.height(8.dp))

            val reservedBottom =
                if (showControls) controlsHeight else 0.dp

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = reservedBottom + 4.dp),
                contentAlignment = Alignment.Center
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .aspectRatio(
                            10f / 20f,
                            matchHeightConstraintsFirst = true
                        )
                ) {

                    TetrisBoardView(
                        displayBoard = uiState.displayBoard,
                        modifier = Modifier.fillMaxSize()
                    )

                    when {

                        isIdle ->
                            IdleOverlay(viewModel::onStart)

                        isPaused ->
                            PausedOverlay(viewModel::onTogglePause)

                        isGameOver ->
                            GameOverOverlay(
                                score = uiState.score,
                                onRestart = viewModel::onStart,
                                onMenu = onExitToMenu
                            )
                    }
                }
            }
        }

        if (showControls) {

            ControlPad(
                isPlaying = isPlaying,
                onMoveLeft = viewModel::onMoveLeft,
                onMoveRight = viewModel::onMoveRight,
                onRotate = viewModel::onRotate,
                onSoftDrop = viewModel::onSoftDrop,
                onHardDrop = viewModel::onHardDrop,
                onTogglePause = viewModel::onTogglePause,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(controlsHeight)
                    .padding(bottom = 8.dp)
            )
        }
    }
}