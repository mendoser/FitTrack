package com.example.fittrack.data.models

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val weight: Float = 0f,
    val height: Float = 0f,
    val age: Int = 0,
    val gender: String = "",
    val isPremium: Boolean = false
)