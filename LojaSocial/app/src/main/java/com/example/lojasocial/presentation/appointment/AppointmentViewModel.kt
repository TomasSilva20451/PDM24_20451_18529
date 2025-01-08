package com.example.lojasocial.presentation.appointment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lojasocial.domain.model.Appointment
import com.example.lojasocial.domain.usecases.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AppointmentViewModel(
    private val createAppointmentUseCase: CreateAppointmentUseCase,
    private val getAllAppointmentsUseCase: GetAllAppointmentsUseCase,
    private val updateAppointmentUseCase: UpdateAppointmentUseCase,
    private val deleteAppointmentUseCase: DeleteAppointmentUseCase,
    private val getAppointmentByIdUseCase: GetAppointmentByIdUseCase  // se precisar buscar 1 doc
) : ViewModel() {

    private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
    val appointments: StateFlow<List<Appointment>> = _appointments

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Carrega todos os appointments
    fun loadAppointments() {
        viewModelScope.launch {
            try {
                val result = getAllAppointmentsUseCase()
                _appointments.value = result
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    // Criar
    fun createAppointment(appointment: Appointment, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                createAppointmentUseCase(appointment)
                onSuccess()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    // Atualizar
    fun updateAppointment(appointment: Appointment, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                updateAppointmentUseCase(appointment)
                onSuccess()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    // getAppointmentById
    suspend fun getAppointmentById(id: String): Appointment? {
        return try {
            getAppointmentByIdUseCase(id)
        } catch (e: Exception) {
            _error.value = e.message
            null
        }
    }

    // Deletar
    fun deleteAppointment(id: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                deleteAppointmentUseCase(id)
                onSuccess()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}