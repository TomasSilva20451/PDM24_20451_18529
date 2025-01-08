package com.example.lojasocial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.lojasocial.data.repository.AuthRepositoryImpl
import com.example.lojasocial.domain.usecases.LoginUseCase
import com.example.lojasocial.domain.usecases.RegisterUseCase
import com.example.lojasocial.presentation.login.LoginScreen
import com.example.lojasocial.presentation.login.LoginViewModel
import com.example.lojasocial.presentation.navigation.AppNavigation
import com.example.lojasocial.presentation.register.RegisterScreen
import com.example.lojasocial.presentation.register.RegisterViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa Firebase
        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()

        // Cria o repository com as instâncias do Firebase
        val authRepository = AuthRepositoryImpl(auth, firestore)

        // Cria use cases
        val loginUseCase = LoginUseCase(authRepository)
        val registerUseCase = RegisterUseCase(authRepository)

        // Cria view models
        val loginViewModel = LoginViewModel(loginUseCase)
        val registerViewModel = RegisterViewModel(registerUseCase)

        setContent {
            val navController = rememberNavController()
            // APENAS isso aqui
            AppNavigation(navController = navController)
        }
    }
}