package com.example.taller3.model

data class User(
    val uid: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val email: String = "",
    val idNumber: String = "",
    val profilePicUrl: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val disponible: Boolean = false
)