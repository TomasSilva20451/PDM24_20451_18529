package com.example.lojasocial.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lojasocial.domain.usecases.LoginUseCase
import com.example.lojasocial.data.model.UserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun doLogin(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val userData = loginUseCase(email, password)
                _loginState.value = LoginState.Success(userData)
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Erro inesperado.")
            }
        }
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val userData: UserData) : LoginState()
    data class Error(val message: String) : LoginState()
}