package com.example.tetris1v1.game

import androidx.compose.ui.graphics.Color

const val COLUMNS = 10
const val ROWS = 20

val PIECE_COLORS = mapOf(
    0 to Color.Transparent,
    1 to Color(0xFFFF3366),
    2 to Color(0xFFFF9933),
    3 to Color(0xFFFFFF33),
    4 to Color(0xFF33FF66),
    5 to Color(0xFF33CCFF),
    6 to Color(0xFF9966FF),
    7 to Color(0xFFFF66CC),
    8 to Color(0xFF6B7280)
)

val SCORE_TABLE = listOf<Int>(0, 100, 300, 500, 800)

const val BASE_SPEED_MS = 1000L
const val MIN_SPEED_MS  = 100L
const val SPEED_STEP_MS = 90L