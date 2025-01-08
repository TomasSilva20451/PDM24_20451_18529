package com.example.lojasocial.presentation.appointment

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lojasocial.domain.model.Appointment

@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Agendamentos") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("management") {
                            popUpTo("management") { inclusive = false }
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (error != null) {
                    // Exibe o erro no topo
                    Text(
                        text = "Erro: $error",
                        color = Color.Red,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopCenter)
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(appointments) { appointment ->
                        AppointmentRow(
                            appointment = appointment,
                            onEdit = onEdit,
                            onDelete = { id ->
                                viewModel.deleteAppointment(id) {
                                    // Refresh após deletar
                                    viewModel.loadAppointments()
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    )
}

@Composable
fun AppointmentRow(
    appointment: Appointment,
    onEdit: (Appointment) -> Unit,
    onDelete: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Data: ${appointment.data}", style = MaterialTheme.typography.bodyLarge)
            Text("Horário: ${appointment.horario}", style = MaterialTheme.typography.bodyMedium)
            Text("Beneficiary ID: ${appointment.beneficiaryId}", style = MaterialTheme.typography.bodyMedium)
            Text("Status: ${appointment.status}", style = MaterialTheme.typography.bodyMedium)
            Text("Observações: ${appointment.observacoes}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Button(
                    onClick = { onEdit(appointment) },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Editar")
                }
                Button(
                    onClick = { onDelete(appointment.id) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Apagar", color = Color.White)
                }
            }
        }
    }
}