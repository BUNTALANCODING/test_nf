package presentation.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import common.ChangeStatusBarColors
import common.Context
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import presentation.navigation.BottomNavigation
import presentation.navigation.HomeNavigation
import presentation.navigation.ProfileNavigation
import presentation.theme.DefaultCardColorsTheme
import presentation.theme.DefaultNavigationBarItemTheme
import presentation.ui.main.auth.LoginScreen
import presentation.ui.main.auth.RegisterScreen
import presentation.ui.main.auth.SendEmailScreen
import presentation.ui.main.auth.SuccessRegisterScreen
import presentation.ui.main.auth.forgot.ForgotPasswordScreen
import presentation.ui.main.auth.view_model.LoginAction
import presentation.ui.main.auth.view_model.LoginViewModel
import presentation.ui.main.datapemeriksaan.fotopetugas.CameraPetugasScreen
import presentation.ui.main.datapemeriksaan.fotopetugas.FormDataPemeriksaanScreen
import presentation.ui.main.datapemeriksaan.fotopetugas.GuideFotoPetugasScreen
import presentation.ui.main.datapemeriksaan.fotopetugas.VerifyFotoPetugasScreen
import presentation.ui.main.datapemeriksaan.kir.CameraKIRScreen
import presentation.ui.main.datapemeriksaan.kir.DataKendaraanScreen
import presentation.ui.main.datapemeriksaan.kir.DetailHasilScanScreen
import presentation.ui.main.home.HomeScreen
import presentation.ui.main.home.view_model.HomeAction
import presentation.ui.main.home.view_model.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNav(context: Context?, logout: () -> Unit) {

    val navigator = rememberNavController()
    val navBackStackEntry by navigator.currentBackStackEntryAsState()
    /*val owner = remember(navigator) { navigator.getBackStackEntry(BottomNavigation.Home.route) }
    val viewModel: HomeViewModel = koinViewModel(viewModelStoreOwner = owner)*/
    val viewModel: HomeViewModel = koinViewModel()
    val viewModelLogin: LoginViewModel = koinViewModel()

    val currentRoute = navBackStackEntry?.destination?.route
//    val showBottomBar = currentRoute == BottomNavigation.Home.route ||
//                currentRoute == BottomNavigation.Notification.route ||
//                currentRoute == BottomNavigation.Profile.route

    LaunchedEffect(Unit) {
        viewModel.action.collectLatest { effect ->
            when (effect) {
                is HomeAction.Navigation.NavigateToMain -> {
                    delay(2000)
                    navigator.navigate(BottomNavigation.Home.route){
                        popUpTo(navigator.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                }

                is HomeAction.Navigation.NavigateToLogin -> {
                    navigator.navigate(HomeNavigation.Login) {

                        popUpTo(navigator.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                }

                is HomeAction.Navigation.NavigateToGuide -> {
                    navigator.navigate(HomeNavigation.GuideFotoPetugas)
                }

                HomeAction.Navigation.NavigateToKIR -> {
                    navigator.navigate(HomeNavigation.DataKendaraanKIR)
                }

                HomeAction.Navigation.NavigateToResultScreen -> {
                    navigator.navigate(HomeNavigation.DetailHasilFotoKIR)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModelLogin.action.collectLatest { effect ->
            when (effect) {
                is LoginAction.Navigation.NavigateToMain -> {

                    navigator.navigate(BottomNavigation.Home.route) {
                        popUpTo(navigator.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                }

                is LoginAction.Navigation.NavigateToLogin -> {
                    navigator.navigate(HomeNavigation.Login) {
                        popUpTo(navigator.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                }

                else -> { /* Handle other actions */ }
            }
        }
    }

    Scaffold(
//        bottomBar = {
//            if (showBottomBar) BottomNavigationUI(navController = navigator)
//        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                startDestination = HomeNavigation.Login,
                navController = navigator,
                modifier = Modifier.fillMaxSize()
            ) {

                /**
                 * HOME
                 */
                composable(route = BottomNavigation.Home.route) {
                    ChangeStatusBarColors(Color.White)


                    HomeScreen(
                        errors = viewModel.errors,
                        state = viewModel.state.value,
                        events = viewModel::onTriggerEvent,

                        navigateToLogin = {
                            navigator.navigate(HomeNavigation.Login)
                        },
                        navigateToPemeriksaan = {
//                            navigator.navigate(HomeNavigation.CameraFotoKIR)
                            navigator.navigate(HomeNavigation.Pemeriksaan)
                        },
                    )
                }

                composable<HomeNavigation.Pemeriksaan> {
                    FormDataPemeriksaanScreen(

                        errors = viewModel.errors,
                        state = viewModel.state.value,
                        events = viewModel::onTriggerEvent,
                        popup = { navigator.popBackStack() },
                        navigateToGuideFoto = {
                            navigator.navigate(HomeNavigation.GuideFotoPetugas)
                        }
                    )
                }

                composable<HomeNavigation.GuideFotoPetugas> {
                    GuideFotoPetugasScreen(
                        errors = viewModel.errors,
                        state = viewModel.state.value,
                        events = viewModel::onTriggerEvent,
                        popup = { navigator.popBackStack() },
                        navigateToCameraPetugas = {
                            navigator.navigate(HomeNavigation.CameraFotoPetugas)
                        },
                    )
                }

                composable<HomeNavigation.CameraFotoPetugas> {
                    CameraPetugasScreen(

                        errors = viewModel.errors,
                        state = viewModel.state.value,
                        events = viewModel::onTriggerEvent,
                        popup = { navigator.popBackStack() },
                        navigateToVerifyFoto = {
                            navigator.navigate(HomeNavigation.VerifyFotoPetugas)
                        },
                    )
                }

                composable<HomeNavigation.VerifyFotoPetugas> {
                    VerifyFotoPetugasScreen(
                        errors = viewModel.errors,
                        state = viewModel.state.value,
                        events = viewModel::onTriggerEvent,
                        popup = { navigator.popBackStack() },
                        navigateToDataKIR = {
                        },
                    )
                }

                composable<HomeNavigation.DataKendaraanKIR> {
                    DataKendaraanScreen(

                        errors = viewModel.errors,
                        state = viewModel.state.value,
                        events = viewModel::onTriggerEvent,
                        popup = { navigator.popBackStack() },
                        navigateToDetail = {},
                    )
                }

                composable<HomeNavigation.DetailHasilFotoKIR> {
                    DetailHasilScanScreen(

                        errors = viewModel.errors,
                        state = viewModel.state.value,
                        events = viewModel::onTriggerEvent,
                        popup = { navigator.popBackStack() },
                        navigateToDetail = {},
                    )
                }

                composable<HomeNavigation.CameraFotoKIR> {
                    CameraKIRScreen(

                        errors = viewModel.errors,
                        state = viewModel.state.value,
                        events = viewModel::onTriggerEvent,
                        popup = { navigator.popBackStack() },
                        navigateToVerifyPhotoKTP = {},
                    )
                }

                
                /**
                 * Auth
                 */
                composable<HomeNavigation.Forgot> {
                    LaunchedEffect(Unit) {
                        viewModelLogin.action.onEach { effect ->
                            when (effect) {
                                LoginAction.Navigation.NavigateToMain -> {
                                    navigator.popBackStack()
                                    navigator.navigate(HomeNavigation.SuccessRegister)
                                }

                                LoginAction.Navigation.NavigateToLogin -> {
                                    navigator.navigate(HomeNavigation.Login)
                                }

                                LoginAction.Navigation.NavigateToSuccess -> {
                                    navigator.navigate(HomeNavigation.ForgotSuccess) {
                                        popUpTo(BottomNavigation.Home.route) { inclusive = false }
                                    }
                                }

                                LoginAction.Navigation.NavigateToRegister -> {
                                    delay(2000)
                                    navigator.navigate(HomeNavigation.Register) {
                                        popUpTo(BottomNavigation.Home.route) { inclusive = false }
                                    }
                                }
                            }
                        }.collect {}
                    }

                    ForgotPasswordScreen(
                        navigateToSuccess = {
                            navigator.popBackStack()
                            navigator.navigate(HomeNavigation.Login)
                        },
                        popUp = { navigator.popBackStack() },
                        errors = viewModelLogin.errors,
                        state = viewModelLogin.state.value,
                        events = { event -> viewModelLogin.setEvent(event) },
                        navigateToLogin = {},
                    )
                }
                composable<HomeNavigation.ForgotSuccess> {
                    SendEmailScreen(
                        navigateToLogin = {
                            navigator.popBackStack()
                            navigator.navigate(HomeNavigation.Login)
                        },
                        errors = viewModelLogin.errors,
                        state = viewModelLogin.state.value,
                        events = { event -> viewModelLogin.setEvent(event) }
                    )
                }
                composable<HomeNavigation.Login> {
                    LoginScreen(
                        navigateToRegister = {
                            navigator.popBackStack()
                            navigator.navigate(HomeNavigation.Register)
                        },
                        navigateToForgot = {
                            navigator.navigate(HomeNavigation.Forgot)
                        },
                        errors = viewModelLogin.errors,
                        state = viewModelLogin.state.value,
                        events = { event -> viewModelLogin.setEvent(event) }
                    )
                }

            }
        }
    }
}
