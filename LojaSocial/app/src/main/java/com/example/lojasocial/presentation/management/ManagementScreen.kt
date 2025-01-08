package com.example.lojasocial.presentation.management

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lojasocial.domain.model.Beneficiary

@Composable
fun ManagementScreen(
    viewModel: ManagementViewModel,
    navController: NavController
) {
    // Carrega a lista ao abrir a tela
    LaunchedEffect(Unit) {
        viewModel.loadBeneficiaries()
    }

    val beneficiaries by viewModel.beneficiaries.collectAsState()
    val error by viewModel.error.collectAsState()

    // Estados para o formulário
    var nome by remember { mutableStateOf("") }
    var agregadoFamiliar by remember { mutableStateOf("0") }
    var dataNascimento by remember { mutableStateOf("") }
    var nacionalidade by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Seção do formulário
            item {
                Text(
                    text = "Adicionar Novo Beneficiário",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                TextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = agregadoFamiliar,
                    onValueChange = { agregadoFamiliar = it },
                    label = { Text("Agregado Familiar") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = dataNascimento,
                    onValueChange = { dataNascimento = it },
                    label = { Text("Data Nascimento (string)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = nacionalidade,
                    onValueChange = { nacionalidade = it },
                    label = { Text("Nacionalidade") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Botão Criar
                Button(
                    onClick = {
                        val agregado = agregadoFamiliar.toIntOrNull() ?: 0
                        viewModel.createBeneficiary(
                            nome = nome,
                            agregadoFamiliar = agregado,
                            dataNascimento = dataNascimento,
                            nacionalidade = nacionalidade
                        )
                        // Limpa os campos
                        nome = ""
                        agregadoFamiliar = "0"
                        dataNascimento = ""
                        nacionalidade = ""
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Criar Beneficiário")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botões de navegação
                Button(
                    onClick = { navController.navigate("appointmentList") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver Agendamentos")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate("appointmentCreate") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Novo Agendamento")
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Título da lista de beneficiários
                Text(
                    text = "Lista de Beneficiários:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Lista de beneficiários
            items(beneficiaries) { beneficiary ->
                BeneficiaryRow(
                    beneficiary = beneficiary,
                    onEdit = { b ->
                        // Navega para outra tela passando o ID do beneficiário
                        navController.navigate("editBeneficiary/${b.id}")
                    },
                    onDelete = { id ->
                        // Chamar delete no ViewModel
                        viewModel.deleteBeneficiary(id)
                    }
                )
                Divider()
            }
        }

        // Tratamento de erro simples
        if (error != null) {
            AlertDialog(
                onDismissRequest = { /* Ação ao dismiss */ },
                title = { Text("Erro") },
                text = { Text(error ?: "") },
                confirmButton = {
                    OutlinedButton(onClick = {
                        // Exemplo: limpar erro no ViewModel
                        // viewModel.clearError()
                    }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

// BeneficiaryRow com botões Editar e Apagar
@Composable
fun BeneficiaryRow(
    beneficiary: Beneficiary,
    onEdit: (Beneficiary) -> Unit,
    onDelete: (String) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
        Text("Nome: ${beneficiary.nome}", style = MaterialTheme.typography.bodyLarge)
        Text("Agregado Familiar: ${beneficiary.agregadoFamiliar}", style = MaterialTheme.typography.bodyMedium)
        Text("Nascimento: ${beneficiary.dataNascimento}", style = MaterialTheme.typography.bodyMedium)
        Text("Nacionalidade: ${beneficiary.nacionalidade}", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Button(
                onClick = { onEdit(beneficiary) },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text("Editar")
            }
            Button(onClick = { onDelete(beneficiary.id) }) {
                Text("Apagar")
            }
        }
    }
}