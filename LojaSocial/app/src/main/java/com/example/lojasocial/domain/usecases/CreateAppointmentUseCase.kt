package com.example.lojasocial.domain.usecases

import com.example.lojasocial.domain.model.Appointment
import com.example.lojasocial.domain.repository.AppointmentRepository

class CreateAppointmentUseCase(
    private val repository: AppointmentRepository
) {
    suspend operator fun invoke(appointment: Appointment): Appointment {
        return repository.createAppointment(appointment)
    }
}