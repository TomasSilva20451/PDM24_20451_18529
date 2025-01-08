package com.example.lojasocial.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lojasocial.data.model.UserData
import com.example.lojasocial.domain.usecases.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    fun doRegister(email: String, password: String) {
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            try {
                val userData = registerUseCase(email, password)
                _registerState.value = RegisterState.Success(userData)
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error(e.message ?: "Erro inesperado.")
            }
        }
    }
}

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    data class Success(val userData: UserData) : RegisterState()
    data class Error(val message: String) : RegisterState()
}