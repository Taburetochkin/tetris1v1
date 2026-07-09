package com.example.tetris1v1.game.interfaces

import com.example.tetris1v1.game.data.GameState
import kotlinx.coroutines.flow.StateFlow

interface GameController {
    val state: StateFlow<GameState>
    fun dispatch(action: GameAction)
    fun stop()
}