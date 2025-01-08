package com.example.lojasocial.domain.repository

import com.example.lojasocial.domain.model.Beneficiary

interface BeneficiaryRepository {
    suspend fun createBeneficiary(beneficiary: Beneficiary): Beneficiary
    suspend fun getBeneficiaryById(id: String): Beneficiary?
    suspend fun getAllBeneficiaries(): List<Beneficiary>
    suspend fun updateBeneficiary(beneficiary: Beneficiary): Beneficiary
    suspend fun deleteBeneficiary(id: String)
}