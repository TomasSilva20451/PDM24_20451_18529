package com.example.lojasocial.domain.usecases

import com.example.lojasocial.domain.repository.BeneficiaryRepository

class DeleteBeneficiaryUseCase(
    private val repository: BeneficiaryRepository
) {
    suspend operator fun invoke(id: String) {
        repository.deleteBeneficiary(id)
    }
}