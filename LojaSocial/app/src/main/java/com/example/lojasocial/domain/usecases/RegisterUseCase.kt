package com.example.lojasocial.domain.usecases

import com.example.lojasocial.data.model.UserData
import com.example.lojasocial.domain.repository.AuthRepository

class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): UserData {
        return authRepository.register(email, password)
    }
}