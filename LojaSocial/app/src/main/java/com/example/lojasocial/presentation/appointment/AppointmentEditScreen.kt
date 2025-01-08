package com.example.lojasocial.presentation.appointment

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lojasocial.domain.model.Appointment

@Composable
fun AppointmentEditScreen(
    viewModel: AppointmentViewModel,
    appointmentId: String,
    onFinish: () -> Unit
) {
    // Controles de carregamento
    var loaded by remember { mutableStateOf(false) }
    var localAppointment by remember { mutableStateOf<Appointment?>(null) }

    // Buscar appointment específico pelo ID
    LaunchedEffect(appointmentId) {
        val a = viewModel.getAppointmentById(appointmentId)
        localAppointment = a
        loaded = true
    }

    if (!loaded) {
        Text("Carregando...")
        return
    }

    if (localAppointment == null) {
        Text("Agendamento não encontrado!")
        return
    }

    // Local copy para edição
    val appointment = localAppointment!!

    var beneficiaryId by remember { mutableStateOf(appointment.beneficiaryId) }
    var data by remember { mutableStateOf(appointment.data) }
    var horario by remember { mutableStateOf(appointment.horario) }
    var status by remember { mutableStateOf(appointment.status) }
    var observacoes by remember { mutableStateOf(appointment.observacoes) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        TextField(
            value = beneficiaryId,
            onValueChange = { beneficiaryId = it },
            label = { Text("Beneficiary ID") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = data,
            onValueChange = { data = it },
            label = { Text("Data (yyyy-mm-dd)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = horario,
            onValueChange = { horario = it },
            label = { Text("Horário (HH:MM)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = status,
            onValueChange = { status = it },
            label = { Text("Status") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = observacoes,
            onValueChange = { observacoes = it },
            label = { Text("Observações") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Cria objeto atualizado
            val updated = appointment.copy(
                beneficiaryId = beneficiaryId,
                data = data,
                horario = horario,
                status = status,
                observacoes = observacoes
            )
            // Chama update no ViewModel
            viewModel.updateAppointment(updated) {
                // onSuccess
                onFinish() // Volta ou fecha tela
            }
        }) {
            Text("Salvar Alterações")
        }
    }
}