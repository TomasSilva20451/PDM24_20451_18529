package com.example.lojasocial.domain.usecases

import com.example.lojasocial.data.model.UserData
import com.example.lojasocial.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): UserData {
        return authRepository.login(email, password)
    }
}