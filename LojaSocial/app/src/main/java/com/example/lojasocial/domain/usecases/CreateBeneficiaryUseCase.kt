package com.example.lojasocial.domain.usecases

import com.example.lojasocial.domain.model.Beneficiary
import com.example.lojasocial.domain.repository.BeneficiaryRepository

class CreateBeneficiaryUseCase(
    private val repository: BeneficiaryRepository
) {
    suspend operator fun invoke(beneficiary: Beneficiary): Beneficiary {
        return repository.createBeneficiary(beneficiary)
    }
}