package com.example.tetris1v1.game

import com.example.tetris1v1.game.data.Piece

private val TETROMINOES = listOf(
    Pair(arrayOf(
        intArrayOf(1, 1, 0),
        intArrayOf(0, 1, 1)
    ), 1), // Z, color - red

    Pair(arrayOf(
        intArrayOf(1, 0, 0),
        intArrayOf(1, 1, 1)
    ), 2), // J, color - orange

    Pair(arrayOf(
        intArrayOf(1, 1),
        intArrayOf(1, 1)
    ), 3), // O, color - yellow

    Pair(arrayOf(
        intArrayOf(0, 1, 1),
        intArrayOf(1, 1, 0)
    ), 4), // S, color - green

    Pair(arrayOf(
        intArrayOf(1, 1, 1, 1),
    ), 5), // I, color - blue

    Pair(arrayOf(
        intArrayOf(0, 1, 0),
        intArrayOf(1, 1, 1)
    ), 6), // T, color - purple

    Pair(arrayOf(
        intArrayOf(0, 0, 1),
        intArrayOf(1,1,1)
    ), 7), // L, color - pink
)

fun randomPiece(): Piece {
    val (shape, colorIndex) = TETROMINOES.random()
    return Piece(
        shape = shape,
        xCoordinate = (COLUMNS - shape[0].size) / 2,
        yCoordinate = 0,
        colorIndex = colorIndex
    )
}

fun rotateClockwise(shape: Shape): Shape {
    val rows = shape.size
    val columns = shape[0].size
    return Array(columns) { col ->
        IntArray(rows) {
            row -> shape[rows - 1 - row][col]
        }
    }
}
