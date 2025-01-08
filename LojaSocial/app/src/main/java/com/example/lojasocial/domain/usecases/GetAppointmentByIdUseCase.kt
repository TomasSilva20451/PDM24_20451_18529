package com.example.lojasocial.domain.usecases

import com.example.lojasocial.domain.model.Appointment
import com.example.lojasocial.domain.repository.AppointmentRepository

class GetAppointmentByIdUseCase(
    private val repository: AppointmentRepository
) {
    suspend operator fun invoke(id: String): Appointment? {
        return repository.getAppointmentById(id)
    }
}