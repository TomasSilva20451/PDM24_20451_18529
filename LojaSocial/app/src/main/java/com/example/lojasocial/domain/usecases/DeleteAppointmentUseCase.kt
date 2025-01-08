package com.example.lojasocial.domain.usecases

import com.example.lojasocial.domain.repository.AppointmentRepository

class DeleteAppointmentUseCase(
    private val repository: AppointmentRepository
) {
    suspend operator fun invoke(id: String) {
        repository.deleteAppointment(id)
    }
}