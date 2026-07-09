package com.example.tetris1v1.game.singletons

import com.example.tetris1v1.game.Board
import com.example.tetris1v1.game.COLUMNS
import com.example.tetris1v1.game.ROWS
import com.example.tetris1v1.game.Shape
import com.example.tetris1v1.game.data.Piece

object BoardLogic {
    fun createEmptyBoard(): Board = Array(ROWS) {
        IntArray(COLUMNS)
    }

    fun isPositionValid(
        board: Board,
        piece: Piece,
        offsetX: Int = 0,
        offsetY: Int = 0,
        overrideShape: Shape? = null
    ): Boolean {
        val shape = overrideShape ?: piece.shape

        for (row in shape.indices) {
            for (column in shape[row].indices) {

                if (shape[row][column] == 0) continue

                val boardColumn = piece.xCoordinate + column + offsetX
                val boardRow = piece.yCoordinate + row + offsetY

                if (boardColumn !in 0 until COLUMNS) return false
                if (boardRow >= ROWS) return false

                if (boardRow >= 0 && board[boardRow][boardColumn] != 0) {
                    return false
                }
            }
        }

        return true
    }

    fun lockPiece(board: Board, piece: Piece): Board {
        val updated = board.map { it.copyOf()}.toTypedArray()
        piece.shape.forEachIndexed { row, cells ->
            cells.forEachIndexed { column, cell ->
                val boardRow = piece.yCoordinate + row
                val boardColumn = piece.xCoordinate + column
                if (cell != 0 && boardRow >= 0) {
                    updated[boardRow][boardColumn] = piece.colorIndex
                }
            }
        }
        return updated
    }

    fun clearLines(board: Board): Pair<Board, Int> {
        val remaining = board.filter { row -> row.any { it == 0 } }
        val cleared = ROWS - remaining.size
        val empty = Array(cleared) { IntArray(COLUMNS) }
        val result: Board = empty + remaining.toTypedArray()
        return result to cleared
    }

    fun ghostRow(board: Board, piece: Piece): Int {
        var drop = 0
        while (isPositionValid(board, piece, 0, drop + 1)) drop++
        return piece.yCoordinate + drop
    }

    fun buildDisplayBoard(board: Board, piece: Piece): Board {
        val display = board.map { it.copyOf() }.toTypedArray()
        val ghost = ghostRow(board, piece)

        piece.shape.forEachIndexed { row, cells ->
            cells.forEachIndexed { column, cell ->
                if (cell == 0) return@forEachIndexed
                val r = ghost + row
                val c = piece.xCoordinate + column
                if (r in 0 until ROWS && display[r][c] == 0) {
                    display[r][c] = -piece.colorIndex
                }
            }
        }

        piece.shape.forEachIndexed { row, cells ->
            cells.forEachIndexed { col, cell ->
                if (cell == 0) return@forEachIndexed
                val r = piece.yCoordinate + row
                val c = piece.xCoordinate + col
                if (r in 0 until ROWS) display[r][c] = piece.colorIndex
            }
        }
        return display
    }

    fun addGarbage(
        board: Board,
        count: Int,
        gapColumn: Int
    ): Board {

        if (count <= 0) return board

        val garbageLines = Array(count) {
            IntArray(COLUMNS) { column ->
                if (column == gapColumn) 0 else 8
            }
        }

        val shiftedBoard = board.drop(count)

        return (shiftedBoard + garbageLines).toTypedArray()
    }
}