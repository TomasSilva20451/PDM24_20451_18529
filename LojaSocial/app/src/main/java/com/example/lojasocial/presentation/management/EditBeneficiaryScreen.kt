package com.example.lojasocial.presentation.management

import android.app.Application
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
import com.example.lojasocial.core.Utils.generateBeneficiaryReport
import com.example.lojasocial.domain.model.Beneficiary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBeneficiaryScreen(
    viewModel: ManagementViewModel,
    beneficiaryId: String,
    onFinish: () -> Unit,
    navController: NavController
) {
    var loaded by remember { mutableStateOf(false) }
    var localBeneficiary by remember { mutableStateOf<Beneficiary?>(null) }
    val context = LocalContext.current

    // Buscar beneficiário pelo ID
    LaunchedEffect(beneficiaryId) {
        val b = viewModel.getBeneficiaryById(beneficiaryId)
        localBeneficiary = b
        loaded = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Beneficiário") },
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
                if (!loaded) {
                    // Indicador de carregamento
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    if (localBeneficiary == null) {
                        // Mensagem de erro se beneficiário não for encontrado
                        Text(
                            "Beneficiário não encontrado!",
                            color = Color.Red,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        // Conteúdo principal com formulário
                        BeneficiaryEditForm(
                            beneficiary = localBeneficiary!!,
                            onSave = { updatedBeneficiary ->
                                viewModel.updateBeneficiary(updatedBeneficiary)
                                onFinish()
                                // Opcional: Mostrar uma mensagem de sucesso
                                // Por exemplo, usando Snackbar
                            },
                            onGenerateReport = { beneficiary ->
                                generateBeneficiaryReport(context, beneficiary)
                            }
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun BeneficiaryEditForm(
    beneficiary: Beneficiary,
    onSave: (Beneficiary) -> Unit,
    onGenerateReport: (Beneficiary) -> Unit
) {
    // Campos de edição
    var nome by remember { mutableStateOf(beneficiary.nome) }
    var agregadoFamiliar by remember { mutableStateOf(beneficiary.agregadoFamiliar.toString()) }
    var dataNascimento by remember { mutableStateOf(beneficiary.dataNascimento) }
    var nacionalidade by remember { mutableStateOf(beneficiary.nacionalidade) }

    // Estados para mensagens de erro (opcional)
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Torna o conteúdo rolável
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Exibe mensagem de erro, se houver
        if (errorMessage != null) {
            Text(
                text = errorMessage ?: "",
                color = Color.Red,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Campos de texto para edição
        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = agregadoFamiliar,
            onValueChange = { agregadoFamiliar = it },
            label = { Text("Agregado Familiar") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = dataNascimento,
            onValueChange = { dataNascimento = it },
            label = { Text("Data Nascimento (string)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = nacionalidade,
            onValueChange = { nacionalidade = it },
            label = { Text("Nacionalidade") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Botão Salvar Alterações
        Button(
            onClick = {
                val agregadoInt = agregadoFamiliar.toIntOrNull()
                if (agregadoInt == null) {
                    errorMessage = "Agregado Familiar deve ser um número válido."
                } else {
                    val updatedBeneficiary = beneficiary.copy(
                        nome = nome,
                        agregadoFamiliar = agregadoInt,
                        dataNascimento = dataNascimento,
                        nacionalidade = nacionalidade
                    )
                    onSave(updatedBeneficiary)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Salvar Alterações")
        }

        // Botão Gerar Relatório (PDF)
        Button(
            onClick = { onGenerateReport(beneficiary) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Gerar Relatório (PDF)")
        }
    }
}