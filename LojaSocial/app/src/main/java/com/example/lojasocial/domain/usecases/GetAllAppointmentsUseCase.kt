package com.example.lojasocial.domain.usecases

import com.example.lojasocial.domain.model.Appointment
import com.example.lojasocial.domain.repository.AppointmentRepository

class GetAllAppointmentsUseCase(
    private val repository: AppointmentRepository
) {
    suspend operator fun invoke(): List<Appointment> {
        return repository.getAllAppointments()
    }
}