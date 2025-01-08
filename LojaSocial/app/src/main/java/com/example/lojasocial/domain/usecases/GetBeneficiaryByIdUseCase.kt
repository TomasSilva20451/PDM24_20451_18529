package com.example.lojasocial.domain.usecases

import com.example.lojasocial.domain.model.Beneficiary
import com.example.lojasocial.domain.repository.BeneficiaryRepository

class GetBeneficiaryByIdUseCase(
    private val repository: BeneficiaryRepository
) {
    suspend operator fun invoke(id: String): Beneficiary? {
        return repository.getBeneficiaryById(id)
    }
}