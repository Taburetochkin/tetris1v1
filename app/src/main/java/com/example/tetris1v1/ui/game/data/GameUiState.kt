package com.example.tetris1v1.ui.game.data

import com.example.tetris1v1.game.Board
import com.example.tetris1v1.game.data.GameState
import com.example.tetris1v1.game.data.Piece
import com.example.tetris1v1.game.enums.GamePhase
import com.example.tetris1v1.game.randomPiece
import com.example.tetris1v1.game.singletons.BoardLogic

data class GameUiState(
    val displayBoard: Board = BoardLogic.createEmptyBoard(),
    val nextPiece: Piece = randomPiece(),
    val score: Int = 0,
    val level: Int = 1,
    val lines: Int = 0,
    val phase: GamePhase = GamePhase.IDLE
)

fun GameState.toUiState(): GameUiState = GameUiState(
    displayBoard = BoardLogic.buildDisplayBoard(board, activePiece),
    nextPiece = nextPiece,
    score = score,
    level = level,
    lines = lines,
    phase = phase
)
