package com.example.tetris1v1.models

data class User(
    val id: String,
    val userName: String,
    val age: Int,
    val country: String,
    val highScore: Int,
    val friendsId: Array<String> = emptyArray()
)
