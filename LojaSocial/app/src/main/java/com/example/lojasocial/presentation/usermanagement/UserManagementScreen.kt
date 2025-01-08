package com.example.lojasocial.presentation.usermanagement

import UserManagementViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lojasocial.domain.model.User

@Composable
fun UserManagementScreen(viewModel: UserManagementViewModel) {
    val userList by viewModel.userList.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Formulário para criar novo utilizador
        Text("Criar Novo Utilizador", modifier = Modifier.padding(bottom = 8.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        Button(
            onClick = {
                viewModel.createUser(email, password)
                email = ""
                password = ""
            },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("Criar Utilizador")
        }

        // Lista de utilizadores
        Text("Lista de Utilizadores", modifier = Modifier.padding(bottom = 8.dp))
        if (userList.isEmpty()) {
            Text("Nenhum utilizador encontrado.")
        } else {
            LazyColumn {
                items(userList) { user ->
                    Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        Text(text = user.email, modifier = Modifier.weight(1f))
                        Button(onClick = { viewModel.deleteUser(user.id) }) {
                            Text("Excluir")
                        }
                    }
                }
            }
        }
    }
}
