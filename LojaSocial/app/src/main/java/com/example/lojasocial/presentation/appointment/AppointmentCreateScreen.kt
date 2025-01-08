package com.example.lojasocial.presentation.appointment

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lojasocial.domain.model.Appointment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentCreateScreen(
    viewModel: AppointmentViewModel,
    onCreateSuccess: () -> Unit,
    navController: NavController
) {
    val context = LocalContext.current

    var beneficiaryId by remember { mutableStateOf("") }  // ou selecione de uma lista
    var data by remember { mutableStateOf("") }
    var horario by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("pendente") }
    var observacoes by remember { mutableStateOf("") }

    // Estado para mensagens de erro (opcional)
    var formError by remember { mutableStateOf<String?>(null) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Criar Agendamento") },
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

                    // Botão "Agendar Visita"
                    Button(
                        onClick = {
                            // Validação simples (opcional)
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
                            val appointment = Appointment(
                                beneficiaryId = beneficiaryId,
                                data = data,
                                horario = horario,
                                status = status,
                                observacoes = observacoes
                            )
                            // Chama função de salvar
                            viewModel.createAppointment(appointment) {
                                // Callback de sucesso
                                onCreateSuccess()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text("Agendar Visita")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botão "Gerar Relatório" (opcional)
                    Button(
                        onClick = {
                            // Exemplo: Função para gerar relatório (implementação necessária)
                            // generateAppointmentReport(context, appointment)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Gerar Relatório (PDF)")
                    }
                }
            }
        }
    )
}
