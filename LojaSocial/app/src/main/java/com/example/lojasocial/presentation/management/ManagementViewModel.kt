package com.example.lojasocial.presentation.management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lojasocial.domain.model.Beneficiary
import com.example.lojasocial.domain.usecases.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ManagementViewModel(
    private val getAllBeneficiariesUseCase: GetAllBeneficiariesUseCase,
    private val createBeneficiaryUseCase: CreateBeneficiaryUseCase,
    private val updateBeneficiaryUseCase: UpdateBeneficiaryUseCase,
    private val deleteBeneficiaryUseCase: DeleteBeneficiaryUseCase,
    private val getBeneficiaryByIdUseCase: GetBeneficiaryByIdUseCase // <-- Adicione aqui
    // Se quiser também getById, injete aqui: private val getBeneficiaryByIdUseCase: GetBeneficiaryByIdUseCase,
) : ViewModel() {

    private val _beneficiaries = MutableStateFlow<List<Beneficiary>>(emptyList())
    val beneficiaries: StateFlow<List<Beneficiary>> = _beneficiaries

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Carregar todos
    fun loadBeneficiaries() {
        viewModelScope.launch {
            try {
                val list = getAllBeneficiariesUseCase()
                _beneficiaries.value = list
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    suspend fun getBeneficiaryById(id: String): Beneficiary? {
        return try {
            // Se tiver getBeneficiaryByIdUseCase
            getBeneficiaryByIdUseCase(id)
        } catch (e: Exception) {
            null
        }
    }

    // Criar
    fun createBeneficiary(
        nome: String,
        agregadoFamiliar: Int,
        dataNascimento: String,     // <-- Recebe String
        nacionalidade: String
    ) {
        viewModelScope.launch {
            try {
                createBeneficiaryUseCase(
                    Beneficiary(
                        nome = nome,
                        agregadoFamiliar = agregadoFamiliar,
                        dataNascimento = dataNascimento, // <-- Passa como String
                        nacionalidade = nacionalidade
                    )
                )
                // Recarrega lista
                loadBeneficiaries()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    // Atualizar
    fun updateBeneficiary(beneficiary: Beneficiary) {
        viewModelScope.launch {
            try {
                updateBeneficiaryUseCase(beneficiary)
                // Recarrega lista
                loadBeneficiaries()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    // Deletar
    fun deleteBeneficiary(id: String) {
        viewModelScope.launch {
            try {
                deleteBeneficiaryUseCase(id)
                // Recarrega lista
                loadBeneficiaries()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}