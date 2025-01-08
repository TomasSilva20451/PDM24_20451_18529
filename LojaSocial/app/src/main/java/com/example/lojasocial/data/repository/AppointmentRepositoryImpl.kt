package com.example.lojasocial.data.repository

import com.example.lojasocial.domain.model.Appointment
import com.example.lojasocial.domain.repository.AppointmentRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AppointmentRepositoryImpl(
    private val firestore: FirebaseFirestore
) : AppointmentRepository {

    private val collection = firestore.collection("appointments")

    override suspend fun createAppointment(appointment: Appointment): Appointment {
        val docRef = if (appointment.id.isEmpty()) {
            collection.document()
        } else {
            collection.document(appointment.id)
        }
        val toSave = appointment.copy(id = docRef.id)
        docRef.set(toSave).await()
        return toSave
    }

    override suspend fun getAllAppointments(): List<Appointment> {
        val snap = collection.get().await()
        return snap.toObjects(Appointment::class.java)
    }

    override suspend fun updateAppointment(appointment: Appointment): Appointment {
        if (appointment.id.isEmpty()) {
            throw Exception("Appointment sem ID. Não pode atualizar.")
        }
        collection.document(appointment.id).set(appointment).await()
        return appointment
    }

    override suspend fun deleteAppointment(id: String) {
        if (id.isEmpty()) {
            throw Exception("ID vazio ao deletar Appointment.")
        }
        collection.document(id).delete().await()
    }

    override suspend fun getAppointmentById(id: String): Appointment? {
        val docSnap = collection.document(id).get().await()
        return docSnap.toObject(Appointment::class.java)
    }
}