package com.example.lojasocial.presentation.management

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lojasocial.core.Utils.generateBeneficiaryReport
import com.example.lojasocial.domain.model.Beneficiary

@Composable
fun EditBeneficiaryScreen(
    viewModel: ManagementViewModel,
    beneficiaryId: String,
    onFinish: () -> Unit,
    navController: NavController
) {
    var loaded by remember { mutableStateOf(false) }
    var localBeneficiary by remember { mutableStateOf<Beneficiary?>(null) }

    // Buscar beneficiário pelo ID
    LaunchedEffect(beneficiaryId) {
        val b = viewModel.getBeneficiaryById(beneficiaryId)
        localBeneficiary = b
        loaded = true
    }

    if (!loaded) {
        Text("Carregando...")
        return
    }

    if (localBeneficiary == null) {
        Text("Beneficiário não encontrado!")
        return
    }

    val beneficiary = localBeneficiary!!

    // Campos de edição
    var nome by remember { mutableStateOf(beneficiary.nome) }
    var agregadoFamiliar by remember { mutableStateOf(beneficiary.agregadoFamiliar.toString()) }
    var dataNascimento by remember { mutableStateOf(beneficiary.dataNascimento) }
    var nacionalidade by remember { mutableStateOf(beneficiary.nacionalidade) }

    // 1) Pegue o contexto no escopo do composable
    val context = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {

        Spacer(modifier = Modifier.height(16.dp))

        // Botão para voltar
        Button(onClick = {
            navController.navigate("management") {
                popUpTo("management") { inclusive = false }
            }
        }) {
            Text("Voltar")
        }

        Spacer(modifier = Modifier.height(16.dp))

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

        // Botão Salvar
        Button(onClick = {
            val agregadoInt = agregadoFamiliar.toIntOrNull() ?: 0
            val updated = beneficiary.copy(
                nome = nome,
                agregadoFamiliar = agregadoInt,
                dataNascimento = dataNascimento,
                nacionalidade = nacionalidade
            )
            viewModel.updateBeneficiary(updated)
            onFinish() // volta para tela anterior
        }) {
            Text("Salvar Alterações")
        }

        Spacer(modifier = Modifier.height(16.dp))



        // Botão Gerar Relatório
        Button(onClick = {
            // 2) Use o contexto aqui
            generateBeneficiaryReport(context, beneficiary)
        }) {
            Text("Gerar Relatório (PDF)")
        }
    }
}