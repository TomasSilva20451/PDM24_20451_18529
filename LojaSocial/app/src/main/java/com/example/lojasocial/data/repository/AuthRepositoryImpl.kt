package com.example.lojasocial.data.repository

import com.example.lojasocial.data.model.UserData
import com.example.lojasocial.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun login(email: String, password: String): UserData {
        // Faz login com Firebase Auth
        val result = auth.signInWithEmailAndPassword(email, password).await()
        val user = result.user ?: throw Exception("Usuário nulo após login.")

        // Buscar dados no Firestore (opcional)
        val document = firestore.collection("usuarios").document(user.uid).get().await()
        val userData = document.toObject(UserData::class.java)

        return userData ?: UserData(
            uid = user.uid,
            email = user.email ?: "",
            createdAt = System.currentTimeMillis()
        )
    }

    override suspend fun register(email: String, password: String): UserData {
        // Cria usuário no Firebase Auth
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val newUser = result.user ?: throw Exception("Usuário nulo após registro.")

        // Prepara objeto para salvar no Firestore
        val userData = UserData(
            uid = newUser.uid,
            email = newUser.email ?: "",
            createdAt = System.currentTimeMillis()
        )

        // Salvar no Firestore
        firestore.collection("users").document(newUser.uid)
            .set(userData)
            .await()

        return userData
    }
}