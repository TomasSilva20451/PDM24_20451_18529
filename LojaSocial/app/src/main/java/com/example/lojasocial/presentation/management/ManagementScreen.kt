package com.example.lojasocial.presentation.management

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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

    // Formulário para adicionar novo beneficiário
    var nome by remember { mutableStateOf("") }
    var agregadoFamiliar by remember { mutableStateOf("0") }
    var dataNascimento by remember { mutableStateOf("") }
    var nacionalidade by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
        ) {
            // Formulário de criação
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
            Spacer(modifier = Modifier.height(8.dp))

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
                }
            ) {
                Text("Criar Beneficiário")
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Em algum lugar, um botão para listar agendamentos:
            Button(onClick = {
                navController.navigate("appointmentList")
            }) {
                Text("Ver Agendamentos")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Outro botão para criar:
            Button(onClick = {
                navController.navigate("appointmentCreate")
            }) {
                Text("Novo Agendamento")
            }

            Spacer(modifier = Modifier.height(16.dp))
            // Lista de beneficiários
            Text("Lista de Beneficiários:")
            LazyColumn {
                items(beneficiaries) { beneficiary ->
                    BeneficiaryRow(
                        beneficiary = beneficiary,
                        onEdit = { b ->
                            // Navega para outra tela
                            // Passe o ID do beneficiário
                            navController.navigate("editBeneficiary/${b.id}")
                        },
                        onDelete = { id ->
                            // Chamar delete no viewmodel
                            viewModel.deleteBeneficiary(id)
                        }
                    )
                }
            }
        }

        // Tratamento de erro simples
        if (error != null) {
            AlertDialog(
                onDismissRequest = { /* ... */ },
                title = { Text("Erro") },
                text = { Text(error ?: "") },
                confirmButton = {
                    OutlinedButton(onClick = {
                        // Exemplo: limpar erro
                        // viewModel.clearError()
                    }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

// BeneficiaryRow agora tem os botões Editar e Apagar
@Composable
fun BeneficiaryRow(
    beneficiary: Beneficiary,
    onEdit: (Beneficiary) -> Unit,
    onDelete: (String) -> Unit
) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text("Nome: ${beneficiary.nome}")
        Text("Agregado Familiar: ${beneficiary.agregadoFamiliar}")
        Text("Nascimento: ${beneficiary.dataNascimento}")
        Text("Nacionalidade: ${beneficiary.nacionalidade}")
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