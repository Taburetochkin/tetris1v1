package com.example.tetris1v1.game.data

import com.example.tetris1v1.game.Board
import com.example.tetris1v1.game.data.GameResult
import com.example.tetris1v1.game.enums.GamePhase
import com.example.tetris1v1.game.randomPiece
import com.example.tetris1v1.game.singletons.BoardLogic

data class GameState(

    val board: Board = BoardLogic.createEmptyBoard(),

    val activePiece: Piece = randomPiece(),

    val nextPiece: Piece = randomPiece(),

    val score: Int = 0,

    val level: Int = 1,

    val lines: Int = 0,

    val phase: GamePhase = GamePhase.IDLE,

    val lastClearedLines: Int = 0,

    val clearEventId: Long = 0L
)

fun GameState.toResult() =
    GameResult(
        score = score,
        level = level,
        lines = lines
    )