package com.example.tetris1v1.models

data class FriendRequest(
    val id: String,
    val requesterId: String,
    val requesterUsername: String,
    val receiverId: String,
    val status: String
)