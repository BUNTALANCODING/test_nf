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
import presentation.navigation.AdministrasiNavigation
import presentation.navigation.AppNavigation
import presentation.navigation.BANavigation
import presentation.navigation.BottomNavigation
import presentation.navigation.HomeNavigation
import presentation.navigation.ProfileNavigation
import presentation.navigation.SplashNavigation
import presentation.navigation.TeknisNavigation
import presentation.theme.DefaultCardColorsTheme
import presentation.theme.DefaultNavigationBarItemTheme
import presentation.ui.main.auth.LoginScreen
import presentation.ui.main.auth.RegisterScreen
import presentation.ui.main.auth.SendEmailScreen
import presentation.ui.main.auth.SuccessRegisterScreen
import presentation.ui.main.auth.forgot.ForgotPasswordScreen
import presentation.ui.main.auth.view_model.LoginAction
import presentation.ui.main.auth.view_model.LoginViewModel
import presentation.ui.main.beritaacara.FormBeritaAcaraScreen
import presentation.ui.main.beritaacara.KemenhubBeritaAcaraScreen
import presentation.ui.main.beritaacara.PengemudiBeritaAcaraScreen
import presentation.ui.main.datapemeriksaan.fotokendaraan.CameraVehicleScreen
import presentation.ui.main.datapemeriksaan.fotokendaraan.UnggahFotoKendaraanScreen
import presentation.ui.main.datapemeriksaan.fotopetugas.CameraPetugasScreen
import presentation.ui.main.datapemeriksaan.fotopetugas.FormDataPemeriksaanScreen
import presentation.ui.main.datapemeriksaan.fotopetugas.GuideFotoPetugasScreen
import presentation.ui.main.datapemeriksaan.fotopetugas.VerifyFotoPetugasScreen
import presentation.ui.main.datapemeriksaan.kir.CameraKIRScreen
import presentation.ui.main.datapemeriksaan.kir.DataKendaraanScreen
import presentation.ui.main.datapemeriksaan.kir.DetailHasilScanScreen
import presentation.ui.main.datapemeriksaan.kir.QRKIRScreen
import presentation.ui.main.home.HomeScreen
import presentation.ui.main.home.view_model.HomeAction
import presentation.ui.main.home.view_model.HomeEvent
import presentation.ui.main.home.view_model.HomeViewModel
import presentation.ui.main.pemeriksaanadministrasi.GuidePemeriksaanAdministrasiScreen
import presentation.ui.main.pemeriksaanadministrasi.kartuuji.CameraKartuUjiScreen
import presentation.ui.main.pemeriksaanadministrasi.kartuuji.HasilPemeriksaanKartuUjiScreen
import presentation.ui.main.pemeriksaanadministrasi.kartuuji.PemeriksaanKartuUjiScreen
import presentation.ui.main.pemeriksaanadministrasi.kpcadangan.CameraKPCadanganScreen
import presentation.ui.main.pemeriksaanadministrasi.kpcadangan.HasilPemeriksaanKPCadanganScreen
import presentation.ui.main.pemeriksaanadministrasi.kpcadangan.PemeriksaanKPCadanganScreen
import presentation.ui.main.pemeriksaanadministrasi.kpreguler.CameraKPRegulerScreen
import presentation.ui.main.pemeriksaanadministrasi.kpreguler.HasilPemeriksaanKPRegulerScreen
import presentation.ui.main.pemeriksaanadministrasi.kpreguler.PemeriksaanKPRegularScreen
import presentation.ui.main.pemeriksaanadministrasi.simpengemudi.CameraSIMPengemudiScreen
import presentation.ui.main.pemeriksaanadministrasi.simpengemudi.HasilPemeriksaanSIMPengemudiScreen
import presentation.ui.main.pemeriksaanadministrasi.simpengemudi.PemeriksaanSIMPengemudiScreen
import presentation.ui.main.pemeriksaanteknis.CameraTeknisUtamaScreen
import presentation.ui.main.pemeriksaanteknis.GuidePemeriksaanTeknisUtamaScreen
import presentation.ui.main.pemeriksaanteknis.HasilPemeriksaanTeknisUtamaScreen
import presentation.ui.splash.SplashScreen

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

//    NavHost(
//        startDestination = AppNavigation.Main,
//        navController = navigator,
//        modifier = Modifier.fillMaxSize()
//    ) {
//        composable<AppNavigation.Main> {
//            LoginScreen(
//                state = viewModelLogin.state.value,
//                events = viewModelLogin::onTriggerEvent,
//                errors = viewModelLogin.errors,
//                navigateToRegister = {},
//                navigateToForgot = {}
//            )
//        }
//    }

    LaunchedEffect(Unit) {
        viewModel.action.collectLatest { effect ->
            when (effect) {
                is HomeAction.Navigation.NavigateToMain -> {
                    delay(2000)
                    navigator.navigate(BottomNavigation.Home.route) {
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

                HomeAction.Navigation.NavigateToQRKIR -> {
                    navigator.navigate(HomeNavigation.QRKIRScreen)
                }

                HomeAction.Navigation.NavigateToPemeriksaanAdministrasi -> {
                    navigator.navigate(AdministrasiNavigation.GuideAdministrasi)
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

                else -> { /* Handle other actions */
                }
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
//                            navigator.navigate(HomeNavigation.Pemeriksaan)
//                            navigator.navigate(HomeNavigation.FotoKendaraan)
//                            navigator.navigate(HomeNavigation.GuideFotoPetugas)
//                            navigator.navigate(TeknisNavigation.GuidePemeriksaanTeknisUtama)
                            navigator.navigate(BANavigation.FormBeritaAcara)
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
                        navigateToCameraKIR = {
                            navigator.navigate(HomeNavigation.CameraFotoKIR)
                        },
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
                        navigateToDataKendaraan = {
                            navigator.navigate(HomeNavigation.DataKendaraanKIR)
                        },
                    )
                }

                composable<HomeNavigation.QRKIRScreen> {
                    QRKIRScreen(

                        errors = viewModel.errors,
                        state = viewModel.state.value,
                        events = viewModel::onTriggerEvent,
                        popup = { navigator.popBackStack() },
                        navigateToVerifyPhotoKTP = {},
                    )
                }
                composable<HomeNavigation.FotoKendaraan> {
                    UnggahFotoKendaraanScreen(

                        errors = viewModel.errors,
                        state = viewModel.state.value,
                        events = viewModel::onTriggerEvent,
                        popup = { navigator.popBackStack() },
                        navigateToCamera = {
                            navigator.navigate(HomeNavigation.CameraVehicle)
                        },
                    )
                }
                composable<HomeNavigation.CameraVehicle> {
                    CameraVehicleScreen(

                        errors = viewModel.errors,
                        state = viewModel.state.value,
                        events = viewModel::onTriggerEvent,
                        popup = { navigator.popBackStack() },
                        navigateToFotoKendaraan = {
                            navigator.popBackStack()
                        },
                    )
                }

                composable<AdministrasiNavigation.GuideAdministrasi> {
                    GuidePemeriksaanAdministrasiScreen(

                        errors = viewModel.errors,
                        state = viewModel.state.value,
                        events = viewModel::onTriggerEvent,
                        popup = { navigator.popBackStack() },
                        navigateToKartuUji = {
                            navigator.navigate(AdministrasiNavigation.PemeriksaanKartuUji)
                        },
                    )
                }

                composable<AdministrasiNavigation.PemeriksaanKartuUji> {
                    PemeriksaanKartuUjiScreen(
                        errors = viewModel.errors,
                        state = viewModel.state.value,
                        events = viewModel::onTriggerEvent,
                        popup = { navigator.popBackStack() },
                        navigateToCameraKartuUji = {
                            navigator.navigate(AdministrasiNavigation.CameraKartuUji)
                        }
                    )
                }

                composable<AdministrasiNavigation.CameraKartuUji> {
                    CameraKartuUjiScreen(
                        errors = viewModel.errors,
                        state = viewModel.state.value,
                        events = viewModel::onTriggerEvent,
                        popup = { navigator.popBackStack() },
                        navigateToHasilKameraUji = {
                            navigator.navigate(AdministrasiNavigation.HasilPemeriksaanKartuUji)
                        },
                    )
                }

                composable<AdministrasiNavigation.HasilPemeriksaanKartuUji> {
                    HasilPemeriksaanKartuUjiScreen(
                        errors = viewModel.errors,
                        state = viewModel.state.value,
                        events = viewModel::onTriggerEvent,
                        popup = { navigator.popBackStack() },
                        navigateToCameraFace = {},
                    )
                }

                composable<AdministrasiNavigation.PemeriksaanKPReguler> {
                    PemeriksaanKPRegularScreen(
                        errors = viewModel.errors,
                        state = viewModel.state.value,
                        events = viewModel::onTriggerEvent,
                        popup = { navigator.popBackStack() },
                        navigateToCameraKPReguler = {
                            navigator.navigate(AdministrasiNavigation.CameraKPReguler)
                        }
                    )
                }

                composable<AdministrasiNavigation.CameraKPReguler> {
                    CameraKPRegulerScreen(
                        errors = viewModel.errors,
                        state = viewModel.state.value,
                        events = viewModel::onTriggerEvent,
                        popup = { navigator.popBackStack() },
                        navigateToHasilKPRegular = {
                            navigator.navigate(AdministrasiNavigation.HasilPemeriksaanKPReguler)
                        },
                    )
                }

                composable<AdministrasiNavigation.HasilPemeriksaanKPReguler> {
                    HasilPemeriksaanKPRegulerScreen(
                        errors = viewModel.errors,
                        state = viewModel.state.value,
                        events = viewModel::onTriggerEvent,
                        popup = { navigator.popBackStack() },
                        navigateToKPCadangan = {
                            navigator.navigate(AdministrasiNavigation.PemeriksaanKPCadangan)
                        },
                    )
                }

                composable<AdministrasiNavigation.PemeriksaanKPCadangan> {
                    PemeriksaanKPCadanganScreen(
                        errors = viewModel.errors,
                        state = viewModel.state.value,
                        events = viewModel::onTriggerEvent,
                        popup = { navigator.popBackStack() },
                        navigateToCameraKPCadangan = {
                            navigator.navigate(AdministrasiNavigation.CameraKPCadangan)
                        }
                    )
                }

                composable<AdministrasiNavigation.CameraKPCadangan> {
                    CameraKPCadanganScreen(
                        errors = viewModel.errors,
                        state = viewModel.state.value,
                        events = viewModel::onTriggerEvent,
                        popup = { navigator.popBackStack() },
                        navigateToHasilKPCadangan = {
                            navigator.navigate(AdministrasiNavigation.HasilPemeriksaanKPCadangan)
                        },
                    )
                }

                composable<AdministrasiNavigation.HasilPemeriksaanKPCadangan> {
                    HasilPemeriksaanKPCadanganScreen(
                        errors = viewModel.errors,
                        state = viewModel.state.value,
                        events = viewModel::onTriggerEvent,
                        popup = { navigator.popBackStack() },
                        navigateToSIM = {
                            navigator.navigate(AdministrasiNavigation.PemeriksaanSIM)
                        },
                    )
                }

                composable<AdministrasiNavigation.PemeriksaanSIM> {
                    PemeriksaanSIMPengemudiScreen(
                        errors = viewModel.errors,
                        state = viewModel.state.value,
                        events = viewModel::onTriggerEvent,
                        popup = { navigator.popBackStack() },
                        navigateToCameraSIM = {
                            navigator.navigate(AdministrasiNavigation.CameraSIM)
                        }
                    )
                }

                composable<AdministrasiNavigation.CameraSIM> {
                    CameraSIMPengemudiScreen(
                        errors = viewModel.errors,
                        state = viewModel.state.value,
                        events = viewModel::onTriggerEvent,
                        popup = { navigator.popBackStack() },
                        navigateToHasilSIM = {
                            navigator.navigate(AdministrasiNavigation.HasilPemeriksaanSIM)
                        },
                    )
                }

                composable<AdministrasiNavigation.HasilPemeriksaanSIM> {
                    HasilPemeriksaanSIMPengemudiScreen(
                        errors = viewModel.errors,
                        state = viewModel.state.value,
                        events = viewModel::onTriggerEvent,
                        popup = { navigator.popBackStack() },
                        navigateToPemeriksaanTeknis = {
                            navigator.navigate(TeknisNavigation.GuidePemeriksaanTeknisUtama)
                        },
                    )
                }

                composable<TeknisNavigation.GuidePemeriksaanTeknisUtama> {
                    GuidePemeriksaanTeknisUtamaScreen(
                        errors = viewModel.errors,
                        state = viewModel.state.value,
                        events = viewModel::onTriggerEvent,
                        popup = { navigator.popBackStack() },
                        navigateToTeknisUtama = {
                            navigator.navigate(TeknisNavigation.CameraTeknisUtama)
                        },
                    )
                }

                composable<TeknisNavigation.CameraTeknisUtama> {
                    CameraTeknisUtamaScreen(
                        errors = viewModel.errors,
                        state = viewModel.state.value,
                        events = viewModel::onTriggerEvent,
                        popup = { navigator.popBackStack() },
                        navigateToQuestionTeknisUtama = {
                            navigator.navigate(TeknisNavigation.QuestionTeknisUtama)
                        },
                    )
                }

                composable<TeknisNavigation.QuestionTeknisUtama> {
                    HasilPemeriksaanTeknisUtamaScreen(
                        errors = viewModel.errors,
                        state = viewModel.state.value,
                        events = viewModel::onTriggerEvent,
                        popup = { navigator.popBackStack() },
                        navigateToTeknisPenunjang = {
                            navigator.navigate(TeknisNavigation.GuidePemeriksaanTeknisPenunjang)
                        },
                    )
                }

                composable<BANavigation.FormBeritaAcara> {
                    FormBeritaAcaraScreen(
                        errors = viewModel.errors,
                        state = viewModel.state.value,
                        events = viewModel::onTriggerEvent,
                        popup = { navigator.popBackStack() },
                        loginState = viewModelLogin.state.value,
                        navigateToPengemudi = {
                            navigator.navigate(BANavigation.PengemudiBeritaAcara)
                        },
                        navigateToKemenhub = {
                            navigator.navigate(BANavigation.KemenhubBeritaAcara)
                        },
                    )
                }
                composable<BANavigation.PengemudiBeritaAcara> {
                    PengemudiBeritaAcaraScreen(
                        errors = viewModel.errors,
                        state = viewModel.state.value,
                        events = viewModel::onTriggerEvent,
                        popup = { navigator.popBackStack() },
                    )
                }

                composable<BANavigation.KemenhubBeritaAcara> {
                    KemenhubBeritaAcaraScreen(
                        errors = viewModel.errors,
                        state = viewModel.state.value,
                        events = viewModel::onTriggerEvent,
                        popup = { navigator.popBackStack() },
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
