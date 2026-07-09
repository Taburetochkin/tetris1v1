package com.example.tetris1v1.models

data class LeaderboardEntry(
    val userId: String = "",
    val username: String,
    val highScore: Int,
    val country: String
)
