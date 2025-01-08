package com.example.lojasocial.presentation.register

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RegisterScreen(viewModel: RegisterViewModel) {
    val registerState by viewModel.registerState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Senha") },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            when (registerState) {
                is RegisterState.Loading -> CircularProgressIndicator()
                is RegisterState.Error -> Text((registerState as RegisterState.Error).message)
                is RegisterState.Success -> {
                    Text("Registro bem-sucedido! Bem-vindo: ${(registerState as RegisterState.Success).userData.email}")
                }
                else -> {}
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.doRegister(email, password) },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Registrar")
            }
        }
    }
}