package com.example.tetris1v1.game.data

import com.example.tetris1v1.game.Shape

data class Piece(
    val shape: Shape,
    val xCoordinate: Int,
    val yCoordinate: Int,
    val colorIndex: Int
)
