package com.example.tetris1v1.game.interfaces

sealed interface GameAction {
    data object Start : GameAction
    data object Pause : GameAction
    data object Resume : GameAction
    data object MoveLeft : GameAction
    data object MoveRight : GameAction
    data object RotateClockwise : GameAction
    data object SoftDrop : GameAction
    data object HardDrop : GameAction
    data object Tick : GameAction

    data class AddGarbage(val count: Int, val gapColumn: Int) : GameAction
}