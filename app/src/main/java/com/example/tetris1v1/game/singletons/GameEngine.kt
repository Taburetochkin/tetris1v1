package com.example.tetris1v1.game.singletons

import com.example.tetris1v1.game.calculateLevel
import com.example.tetris1v1.game.calculateLines
import com.example.tetris1v1.game.calculateScore
import com.example.tetris1v1.game.data.GameState
import com.example.tetris1v1.game.enums.GamePhase
import com.example.tetris1v1.game.interfaces.GameAction
import com.example.tetris1v1.game.randomPiece
import com.example.tetris1v1.game.rotateClockwise

object GameEngine {

    fun reduce(state: GameState, action: GameAction): GameState = when (action) {
        GameAction.Start -> startGame()

        GameAction.Pause ->
            if (state.phase == GamePhase.PLAYING) state.copy(phase = GamePhase.PAUSED) else state

        GameAction.Resume ->
            if (state.phase == GamePhase.PAUSED) state.copy(phase = GamePhase.PLAYING) else state

        GameAction.MoveLeft -> move(state, dx = -1, dy = 0)
        GameAction.MoveRight -> move(state, dx = 1, dy = 0)
        GameAction.RotateClockwise -> rotate(state)

        GameAction.SoftDrop ->
            if (state.phase == GamePhase.PLAYING) drop(state) else state

        GameAction.HardDrop ->
            if (state.phase == GamePhase.PLAYING) hardDrop(state) else state

        GameAction.Tick ->
            if (state.phase == GamePhase.PLAYING) drop(state) else state

        is GameAction.AddGarbage ->
            if (state.phase == GamePhase.PLAYING) addGarbage(state, action) else state
    }

    private fun startGame(): GameState = GameState(
        board = BoardLogic.createEmptyBoard(),
        activePiece = randomPiece(),
        nextPiece = randomPiece(),
        score = 0,
        level = 1,
        lines = 0,
        phase = GamePhase.PLAYING
    )

    private fun move(state: GameState, dx: Int, dy: Int): GameState {
        if (state.phase != GamePhase.PLAYING) return state
        val piece = state.activePiece
        return if (BoardLogic.isPositionValid(state.board, piece, dx, dy)) {
            state.copy(
                activePiece = piece.copy(
                    xCoordinate = piece.xCoordinate + dx,
                    yCoordinate = piece.yCoordinate + dy
                )
            )
        } else state
    }

    private fun rotate(state: GameState): GameState {
        if (state.phase != GamePhase.PLAYING) return state
        val piece = state.activePiece
        val rotated = rotateClockwise(piece.shape)
        for (kick in listOf(0, -1, 1, -2, 2)) {
            if (BoardLogic.isPositionValid(state.board, piece, kick, 0, rotated)) {
                return state.copy(
                    activePiece = piece.copy(
                        shape = rotated,
                        xCoordinate = piece.xCoordinate + kick
                    )
                )
            }
        }
        return state
    }

    private fun hardDrop(state: GameState): GameState {
        val piece = state.activePiece
        var dist = 0
        while (BoardLogic.isPositionValid(state.board, piece, 0, dist + 1)) dist++
        val droppedPiece = piece.copy(yCoordinate = piece.yCoordinate + dist)
        return drop(state.copy(activePiece = droppedPiece))
    }

    private fun drop(state: GameState): GameState {
        val piece = state.activePiece
        val board = state.board

        if (BoardLogic.isPositionValid(board, piece, 0, 1)) {
            return state.copy(
                activePiece = piece.copy(yCoordinate = piece.yCoordinate + 1),
                lastClearedLines = 0
            )
        }

        val locked = BoardLogic.lockPiece(board, piece)
        val (clearedBoard, clearedCount) = BoardLogic.clearLines(locked)

        var score = state.score
        var lines = state.lines
        var level = state.level

        if (clearedCount > 0) {
            score = calculateScore(score, clearedCount, level)
            lines = calculateLines(lines, clearedCount)
            level = calculateLevel(lines)
        }

        val spawned = state.nextPiece

        if (!BoardLogic.isPositionValid(clearedBoard, spawned)) {
            return state.copy(
                board = clearedBoard,
                activePiece = spawned,
                score = score,
                lines = lines,
                level = level,
                phase = GamePhase.OVER,
                lastClearedLines = clearedCount,
                clearEventId = System.nanoTime()
            )
        }

        return state.copy(
            board = clearedBoard,
            activePiece = spawned,
            nextPiece = randomPiece(),
            score = score,
            lines = lines,
            level = level,
            lastClearedLines = clearedCount,
            clearEventId = System.nanoTime()
        )
    }

    private fun addGarbage(state: GameState, action: GameAction.AddGarbage): GameState {
        val newBoard = BoardLogic.addGarbage(state.board, action.count, action.gapColumn)
        var piece = state.activePiece

        while (!BoardLogic.isPositionValid(newBoard, piece) && piece.yCoordinate > -4) {
            piece = piece.copy(yCoordinate = piece.yCoordinate - 1)
        }

        return state.copy(board = newBoard, activePiece = piece)
    }
}