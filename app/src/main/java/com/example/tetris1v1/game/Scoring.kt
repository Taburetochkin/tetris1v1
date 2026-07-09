package com.example.tetris1v1.game

fun calculateScore(current: Int, linesCleared: Int, level: Int): Int =
    current + (SCORE_TABLE.getOrElse(linesCleared) { 0 }) * level

fun calculateLines(current: Int, cleared: Int): Int = current + cleared

fun calculateLevel(totalLines: Int): Int = totalLines / 10 + 1

fun calculateSpeed(level: Int): Long =
    maxOf(MIN_SPEED_MS, BASE_SPEED_MS - level * SPEED_STEP_MS)