package com.example.lojasocial.data.repository

import com.example.lojasocial.data.model.BeneficiaryData
import com.example.lojasocial.domain.model.Beneficiary
import com.example.lojasocial.domain.repository.BeneficiaryRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class BeneficiaryRepositoryImpl(
    private val firestore: FirebaseFirestore
) : BeneficiaryRepository {

    private val collection = firestore.collection("beneficiarios")

    override suspend fun createBeneficiary(beneficiary: Beneficiary): Beneficiary {
        // Se o ID estiver vazio, o Firestore gera um automaticamente
        val docRef = if (beneficiary.id.isEmpty()) {
            collection.document()
        } else {
            collection.document(beneficiary.id)
        }

        val beneficiaryToSave = beneficiary.copy(id = docRef.id)

        docRef.set(beneficiaryToSave).await()

        return beneficiaryToSave
    }

    override suspend fun getBeneficiaryById(id: String): Beneficiary? {
        val docSnap = collection.document(id).get().await()
        return docSnap.toObject(Beneficiary::class.java)
    }

    override suspend fun getAllBeneficiaries(): List<Beneficiary> {
        val querySnap = collection.get().await()
        return querySnap.toObjects(Beneficiary::class.java)
    }

    override suspend fun updateBeneficiary(beneficiary: Beneficiary): Beneficiary {
        // Presume que beneficiary.id não está vazio
        val docRef = collection.document(beneficiary.id)
        docRef.set(beneficiary).await()
        return beneficiary
    }

    override suspend fun deleteBeneficiary(id: String) {
        collection.document(id).delete().await()
    }
}