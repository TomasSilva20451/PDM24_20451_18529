package com.example.lojasocial.domain.usecases

import com.example.lojasocial.domain.model.Beneficiary
import com.example.lojasocial.domain.repository.BeneficiaryRepository

class GetAllBeneficiariesUseCase(
    private val repository: BeneficiaryRepository
) {
    suspend operator fun invoke(): List<Beneficiary> {
        return repository.getAllBeneficiaries()
    }
}