package com.example.lojasocial.presentation.login

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
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: (String) -> Unit // Agora aceita o email
) {
    val loginState by viewModel.loginState.collectAsState()

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

            when (loginState) {
                is LoginState.Loading -> CircularProgressIndicator()
                is LoginState.Error -> Text((loginState as LoginState.Error).message)
                is LoginState.Success -> {
                    LaunchedEffect(Unit) {
                        onLoginSuccess((loginState as LoginState.Success).userData.email)
                    }
                    Text("Login bem-sucedido!")
                }
                is LoginState.AdminSuccess -> {
                    LaunchedEffect(Unit) {
                        onLoginSuccess((loginState as LoginState.AdminSuccess).userData.email)
                    }
                    Text("Login de administrador bem-sucedido!")
                }
                else -> {}
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.doLogin(email, password) },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Fazer Login")
            }
        }
    }
}
