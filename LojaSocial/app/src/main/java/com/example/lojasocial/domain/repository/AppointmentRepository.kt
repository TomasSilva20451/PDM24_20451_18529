package com.example.lojasocial.domain.repository

import com.example.lojasocial.domain.model.Appointment

interface AppointmentRepository {
    suspend fun createAppointment(appointment: Appointment): Appointment
    suspend fun getAllAppointments(): List<Appointment>
    suspend fun updateAppointment(appointment: Appointment): Appointment
    suspend fun deleteAppointment(id: String)
    suspend fun getAppointmentById(id: String): Appointment?
}