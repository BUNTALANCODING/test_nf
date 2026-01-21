package presentation.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import business.core.AppDataStore
import business.domain.model.Student
import common.Context
import kotlinx.coroutines.delay
import org.koin.compose.koinInject
import presentation.navigation.AppNavigation
import presentation.navigation.LoginNavigation
import presentation.navigation.StudentNavigation
import presentation.ui.main.detail.StudentDetailScreen
import presentation.ui.main.list.StudentListScreen
import presentation.ui.main.login.LoginScreen
import presentation.ui.main.register.RegisterScreen
import presentation.ui.splash.SplashScreen
import presentation.ui.main.register.view_model.StudentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNav(
    context: Context,
    appDataStore: AppDataStore
) {
    val navigator = rememberNavController()
    val vm: StudentViewModel = koinInject()

    var selectedStudent by remember { mutableStateOf<Student?>(null) }

    Scaffold { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {

            NavHost(
                navController = navigator,
                startDestination = AppNavigation.Splash,
                modifier = Modifier.fillMaxSize()
            ) {

                composable<AppNavigation.Splash> {
                    SplashScreen()

                    LaunchedEffect(Unit) {
                        delay(1500)
                        navigator.navigate(LoginNavigation.Login) {
                            popUpTo(AppNavigation.Splash) { inclusive = true }
                        }
                    }
                }

                composable<LoginNavigation.Login> {
                    LoginScreen(
                        vm = vm,
                        onSuccess = {
                            navigator.navigate(StudentNavigation.Register) {
                                popUpTo(LoginNavigation.Login) { inclusive = true }
                            }
                        }
                    )
                }

                composable<StudentNavigation.Register> {
                    RegisterScreen(
                        vm = vm,
                        onSuccess = {
                            navigator.navigate(StudentNavigation.List)
                        }
                    )
                }

                composable<StudentNavigation.List> {
                    StudentListScreen(
                        vm = vm,
                        onDetail = { student ->
                            selectedStudent = student
                            navigator.navigate(StudentNavigation.Detail)
                        },
                        onBack = { navigator.popBackStack() }
                    )
                }

                composable<StudentNavigation.Detail> {
                    val s = selectedStudent
                    if (s == null) {
                        Text("Data siswa tidak ditemukan")
                    } else {
                        StudentDetailScreen(
                            student = s,
                            onBack = { navigator.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
