package com.example.lojasocial.domain.repository

import com.example.lojasocial.data.model.UserData

interface AuthRepository {
    suspend fun login(email: String, password: String): UserData
    suspend fun register(email: String, password: String): UserData
}