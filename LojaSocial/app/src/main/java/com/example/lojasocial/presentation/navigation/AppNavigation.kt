package com.example.lojasocial.presentation.navigation

import UserManagementViewModel
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
import com.example.lojasocial.presentation.usermanagement.UserManagementScreen
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
            val auth = FirebaseAuth.getInstance()
            val firestore = FirebaseFirestore.getInstance()
            val authRepository = AuthRepositoryImpl(auth, firestore)
            val loginUseCase = LoginUseCase(authRepository)

            val loginViewModel = remember {
                LoginViewModel(loginUseCase)
            }

            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = { email ->
                    if (email == "admin@gmail.com") {
                        navController.navigate("userManagement") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        navController.navigate("appointmentList") {
                            popUpTo("login") { inclusive = true }
                        }
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

        // EDIT APPOINTMENT
        composable("editAppointment/{appointmentId}") { backStackEntry ->
            val appointmentId = backStackEntry.arguments?.getString("appointmentId") ?: ""

            val firestore = FirebaseFirestore.getInstance()
            val appointmentRepository = AppointmentRepositoryImpl(firestore)

            val createAppointmentUseCase = CreateAppointmentUseCase(appointmentRepository)
            val getAllAppointmentsUseCase = GetAllAppointmentsUseCase(appointmentRepository)
            val updateAppointmentUseCase = UpdateAppointmentUseCase(appointmentRepository)
            val deleteAppointmentUseCase = DeleteAppointmentUseCase(appointmentRepository)
            val getAppointmentByIdUseCase = GetAppointmentByIdUseCase(appointmentRepository)

            val appointmentViewModel = remember {
                AppointmentViewModel(
                    createAppointmentUseCase,
                    getAllAppointmentsUseCase,
                    updateAppointmentUseCase,
                    deleteAppointmentUseCase,
                    getAppointmentByIdUseCase
                )
            }

            AppointmentEditScreen(
                viewModel = appointmentViewModel,
                appointmentId = appointmentId,
                onFinish = {
                    navController.popBackStack()
                },
                navController = navController
            )
        }

        // USER MANAGEMENT
        composable("userManagement") {
            val firestore = FirebaseFirestore.getInstance()
            val auth = FirebaseAuth.getInstance() // Obter instância do FirebaseAuth
            val userManagementViewModel = remember { UserManagementViewModel(firestore, auth) }

            UserManagementScreen(viewModel = userManagementViewModel)
        }
    }
}