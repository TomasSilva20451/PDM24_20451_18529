package com.example.lojasocial.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.lojasocial.data.repository.*
import com.example.lojasocial.domain.usecases.*
import com.example.lojasocial.presentation.appointment.*
import com.example.lojasocial.presentation.login.LoginScreen
import com.example.lojasocial.presentation.login.LoginViewModel
import com.example.lojasocial.presentation.management.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AppNavigation(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // LOGIN
        composable("login") {
            // Instanciar Auth
            val auth = FirebaseAuth.getInstance()
            val firestore = FirebaseFirestore.getInstance()
            val authRepository = AuthRepositoryImpl(auth, firestore)
            val loginUseCase = LoginUseCase(authRepository)

            // Usando remember para não recriar ViewModel a cada recomposição
            val loginViewModel = remember {
                LoginViewModel(loginUseCase)
            }

            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate("management") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // MANAGEMENT
        composable("management") {
            val firestore = FirebaseFirestore.getInstance()
            val beneficiaryRepository = BeneficiaryRepositoryImpl(firestore)

            val createBeneficiaryUseCase = CreateBeneficiaryUseCase(beneficiaryRepository)
            val getAllBeneficiariesUseCase = GetAllBeneficiariesUseCase(beneficiaryRepository)
            val updateBeneficiaryUseCase = UpdateBeneficiaryUseCase(beneficiaryRepository)
            val deleteBeneficiaryUseCase = DeleteBeneficiaryUseCase(beneficiaryRepository)
            val getBeneficiaryByIdUseCase = GetBeneficiaryByIdUseCase(beneficiaryRepository)

            // Usando remember
            val managementViewModel = remember {
                ManagementViewModel(
                    getAllBeneficiariesUseCase,
                    createBeneficiaryUseCase,
                    updateBeneficiaryUseCase,
                    deleteBeneficiaryUseCase,
                    getBeneficiaryByIdUseCase
                )
            }

            ManagementScreen(
                viewModel = managementViewModel,
                navController = navController
            )
        }

        // EDIT BENEFICIARY
        composable("editBeneficiary/{beneficiaryId}") { backStackEntry ->
            val beneficiaryId = backStackEntry.arguments?.getString("beneficiaryId") ?: ""
            val firestore = FirebaseFirestore.getInstance()
            val beneficiaryRepository = BeneficiaryRepositoryImpl(firestore)

            val createBeneficiaryUseCase = CreateBeneficiaryUseCase(beneficiaryRepository)
            val getAllBeneficiariesUseCase = GetAllBeneficiariesUseCase(beneficiaryRepository)
            val updateBeneficiaryUseCase = UpdateBeneficiaryUseCase(beneficiaryRepository)
            val deleteBeneficiaryUseCase = DeleteBeneficiaryUseCase(beneficiaryRepository)
            val getBeneficiaryByIdUseCase = GetBeneficiaryByIdUseCase(beneficiaryRepository)

            // lembre-se: se você quer MESMO usar o mesmo ViewModel de "management",
            // poderia navegar com popUpTo ao manager, mas aqui criamos outro
            val managementViewModel = remember {
                ManagementViewModel(
                    getAllBeneficiariesUseCase,
                    createBeneficiaryUseCase,
                    updateBeneficiaryUseCase,
                    deleteBeneficiaryUseCase,
                    getBeneficiaryByIdUseCase
                )
            }

            EditBeneficiaryScreen(
                viewModel = managementViewModel,
                beneficiaryId = beneficiaryId,
                onFinish = {
                    navController.popBackStack()
                },
                navController = navController
            )
        }

        // APPOINTMENT LIST
        composable("appointmentList") {
            val firestore = FirebaseFirestore.getInstance()
            val appointmentRepository = AppointmentRepositoryImpl(firestore)

            val createAppointmentUseCase = CreateAppointmentUseCase(appointmentRepository)
            val getAllAppointmentsUseCase = GetAllAppointmentsUseCase(appointmentRepository)
            val updateAppointmentUseCase = UpdateAppointmentUseCase(appointmentRepository)
            val deleteAppointmentUseCase = DeleteAppointmentUseCase(appointmentRepository)
            val getAppointmentByIdUseCase = GetAppointmentByIdUseCase(appointmentRepository)

            // remember para não recriar sempre
            val appointmentViewModel = remember {
                AppointmentViewModel(
                    createAppointmentUseCase,
                    getAllAppointmentsUseCase,
                    updateAppointmentUseCase,
                    deleteAppointmentUseCase,
                    getAppointmentByIdUseCase
                )
            }

            AppointmentListScreen(
                viewModel = appointmentViewModel,
                onEdit = { appointment ->
                    navController.navigate("editAppointment/${appointment.id}")
                },
                navController = navController
            )
        }

        // APPOINTMENT CREATE
        composable("appointmentCreate") {
            val firestore = FirebaseFirestore.getInstance()
            val appointmentRepository = AppointmentRepositoryImpl(firestore)

            val createAppointmentUseCase = CreateAppointmentUseCase(appointmentRepository)
            val getAllAppointmentsUseCase = GetAllAppointmentsUseCase(appointmentRepository)
            val updateAppointmentUseCase = UpdateAppointmentUseCase(appointmentRepository)
            val deleteAppointmentUseCase = DeleteAppointmentUseCase(appointmentRepository)
            val getAppointmentByIdUseCase = GetAppointmentByIdUseCase(appointmentRepository)

            // remember
            val appointmentViewModel = remember {
                AppointmentViewModel(
                    createAppointmentUseCase,
                    getAllAppointmentsUseCase,
                    updateAppointmentUseCase,
                    deleteAppointmentUseCase,
                    getAppointmentByIdUseCase
                )
            }

            AppointmentCreateScreen(
                viewModel = appointmentViewModel,
                onCreateSuccess = {
                    navController.navigate("appointmentList") {
                        popUpTo("appointmentList") { inclusive = false }
                    }
                },
                navController = navController
            )
        }

        // Edit Appointment
        composable("editAppointment/{appointmentId}") { backStackEntry ->
            val appointmentId = backStackEntry.arguments?.getString("appointmentId") ?: ""

            val firestore = FirebaseFirestore.getInstance()
            val appointmentRepository = AppointmentRepositoryImpl(firestore)

            // UseCases
            val createAppointmentUseCase = CreateAppointmentUseCase(appointmentRepository)
            val getAllAppointmentsUseCase = GetAllAppointmentsUseCase(appointmentRepository)
            val updateAppointmentUseCase = UpdateAppointmentUseCase(appointmentRepository)
            val deleteAppointmentUseCase = DeleteAppointmentUseCase(appointmentRepository)
            val getAppointmentByIdUseCase = GetAppointmentByIdUseCase(appointmentRepository)

            // ViewModel
            val appointmentViewModel = remember {
                AppointmentViewModel(
                    createAppointmentUseCase,
                    getAllAppointmentsUseCase,
                    updateAppointmentUseCase,
                    deleteAppointmentUseCase,
                    getAppointmentByIdUseCase
                )
            }

            // Chamamos a tela
            AppointmentEditScreen(
                viewModel = appointmentViewModel,
                appointmentId = appointmentId,
                onFinish = {
                    navController.popBackStack()
                }
            )
        }
    }
}