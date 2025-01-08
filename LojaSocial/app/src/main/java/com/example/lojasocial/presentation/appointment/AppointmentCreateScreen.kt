package com.example.lojasocial.presentation.appointment

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lojasocial.domain.model.Appointment

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

    Column(modifier = Modifier.padding(16.dp)) {
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
            val appointment = Appointment(
                beneficiaryId = beneficiaryId,
                data = data,
                horario = horario,
                status = status,
                observacoes = observacoes
            )
            viewModel.createAppointment(appointment) {
                // callback de sucesso
                onCreateSuccess()
            }
        }) {
            Text("Agendar Visita")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão "Voltar à Management"
        Button(onClick = {
            // Basta voltar no stack
            navController.popBackStack()
        }) {
            Text("Voltar")
        }
    }
}