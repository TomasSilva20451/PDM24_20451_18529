package com.example.lojasocial.presentation.appointment

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lojasocial.domain.model.Appointment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentEditScreen(
    viewModel: AppointmentViewModel,
    appointmentId: String,
    onFinish: () -> Unit,
    navController: NavController // Adicionado para navegação
) {
    // Estados de carregamento e dados
    var loaded by remember { mutableStateOf(false) }
    var localAppointment by remember { mutableStateOf<Appointment?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Buscar appointment específico pelo ID
    LaunchedEffect(appointmentId) {
        try {
            val a = viewModel.getAppointmentById(appointmentId)
            localAppointment = a
            loaded = true
        } catch (e: Exception) {
            errorMessage = "Erro ao carregar agendamento: ${e.message}"
            loaded = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Agendamento") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("appointmentList") {
                            popUpTo("appointmentList") { inclusive = false }
                        }
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                when {
                    !loaded -> {
                        // Indicador de carregamento
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    errorMessage != null -> {
                        // Mensagem de erro
                        Text(
                            text = errorMessage ?: "Erro desconhecido",
                            color = Color.Red,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    localAppointment == null -> {
                        // Mensagem de agendamento não encontrado
                        Text(
                            text = "Agendamento não encontrado!",
                            color = Color.Red,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    else -> {
                        // Conteúdo principal com formulário
                        AppointmentEditForm(
                            appointment = localAppointment!!,
                            onSave = { updatedAppointment ->
                                viewModel.updateAppointment(updatedAppointment) {
                                    onFinish()
                                    // Opcional: Mostrar uma mensagem de sucesso, como um Snackbar
                                }
                            }
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun AppointmentEditForm(
    appointment: Appointment,
    onSave: (Appointment) -> Unit
) {
    // Campos de edição
    var beneficiaryId by remember { mutableStateOf(appointment.beneficiaryId) }
    var data by remember { mutableStateOf(appointment.data) }
    var horario by remember { mutableStateOf(appointment.horario) }
    var status by remember { mutableStateOf(appointment.status) }
    var observacoes by remember { mutableStateOf(appointment.observacoes) }

    // Estado para mensagens de erro (opcional)
    var formError by remember { mutableStateOf<String?>(null) }

    // Torna o conteúdo rolável
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        // Exibe mensagem de erro, se houver
        if (formError != null) {
            Text(
                text = formError ?: "",
                color = Color.Red,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
        }

        // Campos de texto para edição
        OutlinedTextField(
            value = beneficiaryId,
            onValueChange = { beneficiaryId = it },
            label = { Text("Beneficiary ID") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = data,
            onValueChange = { data = it },
            label = { Text("Data (yyyy-mm-dd)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = horario,
            onValueChange = { horario = it },
            label = { Text("Horário (HH:MM)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = status,
            onValueChange = { status = it },
            label = { Text("Status") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = observacoes,
            onValueChange = { observacoes = it },
            label = { Text("Observações") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Botão Salvar Alterações
        Button(
            onClick = {
                // Validação simples
                if (beneficiaryId.isBlank()) {
                    formError = "Beneficiary ID não pode estar vazio."
                    return@Button
                }
                if (!data.matches(Regex("""\d{4}-\d{2}-\d{2}"""))) {
                    formError = "Data deve estar no formato yyyy-mm-dd."
                    return@Button
                }
                if (!horario.matches(Regex("""\d{2}:\d{2}"""))) {
                    formError = "Horário deve estar no formato HH:MM."
                    return@Button
                }
                if (status.isBlank()) {
                    formError = "Status não pode estar vazio."
                    return@Button
                }

                // Se tudo estiver válido, limpa a mensagem de erro e salva
                formError = null

                // Cria objeto atualizado
                val updated = appointment.copy(
                    beneficiaryId = beneficiaryId,
                    data = data,
                    horario = horario,
                    status = status,
                    observacoes = observacoes
                )
                // Chama função de salvar
                onSave(updated)
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Salvar Alterações")
        }
    }
}