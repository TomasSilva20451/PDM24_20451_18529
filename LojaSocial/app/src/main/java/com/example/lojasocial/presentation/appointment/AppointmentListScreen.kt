package com.example.lojasocial.presentation.appointment

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lojasocial.domain.model.Appointment

@Composable
fun AppointmentListScreen(
    viewModel: AppointmentViewModel,
    onEdit: (Appointment) -> Unit,
    navController: NavController
) {
    // Carrega a lista ao iniciar
    LaunchedEffect(Unit) {
        viewModel.loadAppointments()
    }

    val appointments by viewModel.appointments.collectAsState()
    val error by viewModel.error.collectAsState()

    if (error != null) {
        Text("Erro: $error")
    }


    Spacer(modifier = Modifier.height(16.dp))

    // Botão para voltar
    Button(onClick = {
        navController.navigate("management") {
            popUpTo("management") { inclusive = false }
        }
    }) {
        Text("Voltar")
    }

    Spacer(modifier = Modifier.height(32.dp))

    // Exibe a lista
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(appointments) { appointment ->
            AppointmentRow(
                appointment = appointment,
                onEdit = onEdit,
                onDelete = { id ->
                    viewModel.deleteAppointment(id) {
                        // refresh ou algo
                        viewModel.loadAppointments()
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }

    Spacer(modifier = Modifier.height(16.dp))


}

@Composable
fun AppointmentRow(
    appointment: Appointment,
    onEdit: (Appointment) -> Unit,
    onDelete: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Data: ${appointment.data}")
        Text("Horário: ${appointment.horario}")
        Text("Beneficiary ID: ${appointment.beneficiaryId}")
        Text("Status: ${appointment.status}")
        Text("Observações: ${appointment.observacoes}")

        Row {
            Button(
                onClick = { onEdit(appointment) },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text("Editar")
            }
            Button(onClick = { onDelete(appointment.id) }) {
                Text("Apagar")
            }
        }
    }
}