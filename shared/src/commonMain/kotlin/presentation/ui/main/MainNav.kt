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
import presentation.ui.main.auth.pin.AturPinScreen
import presentation.ui.main.auth.pin.KonfirmasiPinScreen
import presentation.ui.main.auth.LoginScreen
import presentation.ui.main.auth.RegisterScreen
import presentation.ui.main.auth.SendEmailScreen
import presentation.ui.main.auth.SuccessRegisterScreen
import presentation.ui.main.auth.forgot.ForgotPasswordScreen
import presentation.ui.main.auth.pin.SuccessPinScreen
import presentation.ui.main.auth.view_model.LoginAction
import presentation.ui.main.auth.view_model.LoginViewModel
import presentation.ui.main.home.HomeScreen
import presentation.ui.main.home.NotificationScreen
import presentation.ui.main.home.news.DetailNewsScreen
import presentation.ui.main.home.news.NewsScreen
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
    val showBottomBar = currentRoute == BottomNavigation.Home.route ||
                currentRoute == BottomNavigation.Notification.route ||
                currentRoute == BottomNavigation.Profile.route

    LaunchedEffect(Unit) {
        viewModel.action.collectLatest { effect ->
            when (effect) {
                is HomeAction.Navigation.NavigateToMain -> {
                    delay(2000)
                    navigator.popBackStack()
                }

                is HomeAction.Navigation.NavigateToLogin -> {
                    navigator.navigate(HomeNavigation.Login)
                }

                is HomeAction.Navigation.NavigateToFerry -> {
                    navigator.navigate(HomeNavigation.Search) {
                        popUpTo(BottomNavigation.Home.route) { saveState = true }
                    }
                }

                is HomeAction.Navigation.NavigateToCheckout -> {
                    navigator.navigate(HomeNavigation.Checkout) {
                        popUpTo(BottomNavigation.Home.route) { saveState = true }
                    }
                }

                is HomeAction.Navigation.NavigateToBooking -> {
                    navigator.navigate(HomeNavigation.Booking) {
                        popUpTo(BottomNavigation.Home.route) { saveState = true }
                    }
                }

                is HomeAction.Navigation.NavigateToDetailPayment -> {
                    navigator.navigate(HomeNavigation.PaymentDetail) {
                        popUpTo(HomeNavigation.Checkout) { inclusive = true }
                    }
                }
            }
        }
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) BottomNavigationUI(navController = navigator)
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                startDestination = BottomNavigation.Home.route,
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
                        navigateToNotifications = {
                            navigator.navigate(HomeNavigation.Notification)
                        },
                        navigateToLogin = {
                            navigator.navigate(HomeNavigation.Login)
                        },
                        navigateToSaldo = {
                            navigator.navigate(HomeNavigation.DashboardSaldo)
                        },
                        navigateToNews = {
                            navigator.navigate(HomeNavigation.News)
                        },
                        navigateToMilikOrangLain = {
                            navigator.navigate(HomeNavigation.MilikOrangLain)
                        },
                        navigateToDaftarKendaraan = {
                            navigator.navigate(HomeNavigation.DaftarKendaraan)
                        },
                        navigateToDaftarKendaraanSaya = {
                            navigator.navigate(HomeNavigation.DaftarKendaraanSaya)
                        },
                        navigateToRiwayatPembayaran = {
                            navigator.navigate(HomeNavigation.RiwayatPembayaran)
                        },
                        navigateToEtbpkp = {
                            navigator.navigate(HomeNavigation.Etbpkp)
                        },
                        navigateToEPengesahan = {
                            navigator.navigate(HomeNavigation.EPengesahan)
                        },
                        navigateToEKD = {
                            navigator.navigate(HomeNavigation.EKD)
                        },
                    )
                }
                composable<HomeNavigation.DashboardSaldo> {
//                    IsiSaldoScreen(
//                        state = viewModel.state.value,
//                        events = viewModel::onTriggerEvent,
//                        errors = viewModel.errors,
//                        popup = { navigator.popBackStack() },
//                        navigateToVerify = {  },
//                    )
                }
                composable<HomeNavigation.News> {
                    NewsScreen(
                        state = viewModel.state.value,
                        events = viewModel::onTriggerEvent,
                        errors = viewModel.errors,
                        popup = { navigator.popBackStack() },
                        navigateToDetail = { navigator.navigate(HomeNavigation.DetailNews) },
                    )
                }

                composable<HomeNavigation.DetailNews> {
                    DetailNewsScreen(
                        state = viewModel.state.value,
                        events = viewModel::onTriggerEvent,
                        errors = viewModel.errors,
                        popup = { navigator.popBackStack() },
                    )
                }

                composable<HomeNavigation.SamsatCeria> {
//                    SamsatCeriaScreen(
//                        state = viewModel.state.value,
//                        events = viewModel::onTriggerEvent,
//                        errors = viewModel.errors,
//                        popup = { navigator.popBackStack() },
//                        navigateToDaftarKendaraan = {
//                            navigator.navigate(HomeNavigation.DaftarKendaraan)
//                        },
//                        navigateToMilikOrangLain = {
//                            navigator.navigate(HomeNavigation.MilikOrangLain)
//                        },
//                        navigateToKendaraanSaya = {
//                            navigator.navigate(HomeNavigation.DaftarKendaraanSaya)
//                        },
//                        navigateToRiwayatPembayaran = {
//                            navigator.navigate(HomeNavigation.RiwayatPembayaran)
//                        },
//                        navigateToEtbpkp = {
//                            navigator.navigate(HomeNavigation.Etbpkp)
//                        },
//                        navigateToEPengesahan = {
//                            navigator.navigate(HomeNavigation.EPengesahan)
//                        },
//                        navigateToEKD = {
//                            navigator.navigate(HomeNavigation.EKD)
//                        },
//
//                    )
                }
                
                /*
                CekPajak
                */
                
                composable<HomeNavigation.DaftarKendaraan> {
//                    DaftarKendaraanScreen(
//                        state = viewModel.state.value,
//                        events = viewModel::onTriggerEvent,
//                        errors = viewModel.errors,
//                        popup = { navigator.popBackStack() },
//                        navigateToDetailPajak = {
//                            navigator.navigate(HomeNavigation.DetailPajak)
//                        }
//                    )
                }
                composable<HomeNavigation.DetailPajak> {
//                    DetailPajakScreen(
//                        state = viewModel.state.value,
//                        events = viewModel::onTriggerEvent,
//                        errors = viewModel.errors,
//                        popup = { navigator.popBackStack() },
//                        navigateToVerify = {
//                            navigator.navigate(HomeNavigation.VerifyGuideKTP)
//                        }
//                    )
                }
                composable<HomeNavigation.VerifyGuideKTP> {
//                    VerifyGuideKTPScreen(
//                        state = viewModel.state.value,
//                        events = viewModel::onTriggerEvent,
//                        errors = viewModel.errors,
//                        popup = { navigator.popBackStack() },
//                        navigateToCameraKTP = {
//                            navigator.navigate(HomeNavigation.CameraKTP)
//                        },
//                    )
                }

                composable<HomeNavigation.CameraKTP> {
//                    CameraKTPScreen(
//                        state = viewModel.state.value,
//                        events = viewModel::onTriggerEvent,
//                        errors = viewModel.errors,
//                        popup = { navigator.popBackStack() },
//                        navigateToVerifyPhotoKTP = {
//                            navigator.navigate(HomeNavigation.VerifyPhotoKTP)
//                        }
//                    )
                }

                composable<HomeNavigation.VerifyPhotoKTP> {
//                    VerifyPhotoKTPScreen(
//                        state = viewModel.state.value,
//                        events = viewModel::onTriggerEvent,
//                        errors = viewModel.errors,
//                        popup = { navigator.popBackStack() },
//                        navigateToGuideFace = {
//                            navigator.navigate(HomeNavigation.VerifyGuideFace)
//                        },
//                    )
                }
                composable<HomeNavigation.VerifyGuideFace> {
//                    VerifyGuideFaceScreen(
//                        state = viewModel.state.value,
//                        events = viewModel::onTriggerEvent,
//                        errors = viewModel.errors,
//                        popup = { navigator.popBackStack() },
//                        navigateToCameraFace = {
//                            navigator.navigate(HomeNavigation.CameraFace)
//                        },
//                    )
                }
                composable<HomeNavigation.CameraFace> {
//                    CameraFaceScreen(
//                        state = viewModel.state.value,
//                        events = viewModel::onTriggerEvent,
//                        errors = viewModel.errors,
//                        popup = { navigator.popBackStack() },
//                        navigateToVerifyPhotoFace = {
//                            navigator.navigate(HomeNavigation.VerifyPhotoFace)
//                        },
//                    )
                }

                composable<HomeNavigation.VerifyPhotoFace> {
//                    VerifyPhotoFaceScreen(
//                        state = viewModel.state.value,
//                        events = viewModel::onTriggerEvent,
//                        errors = viewModel.errors,
//                        popup = { navigator.popBackStack() },
//                        navigateToPembayaranPajak = {
//                            navigator.navigate(HomeNavigation.PembayaranPajak)
//                        },
//                    )
                }
                composable<HomeNavigation.PembayaranPajak> {
//                    PembayaranPajakScreen(
//                        state = viewModel.state.value,
//                        events = viewModel::onTriggerEvent,
//                        errors = viewModel.errors,
//                        popup = { navigator.popBackStack() },
//                        navigateToAlamat = {
//                            navigator.navigate(HomeNavigation.PilihAlamat)
//                        },
//                        navigateToMetodePembayaran = {
//                            navigator.navigate(HomeNavigation.MetodePembayaran)
//                        },
//                        navigateToMenungguPembayaran = {
//                            navigator.navigate(HomeNavigation.MenungguPembayaran)
//                        }
//                    )
                }

                composable<HomeNavigation.MenungguPembayaran> {
//                    MenungguPembayaranScreen(
//                        state = viewModel.state.value,
//                        events = viewModel::onTriggerEvent,
//                        errors = viewModel.errors,
//                        popup = { navigator.popBackStack() },
//                        navigateToAlamat = {
//
//                        },
//                        navigateToMetodePembayaran = {
//
//                        }
//                    )
                }

                composable<HomeNavigation.PilihAlamat> {
//                    PilihAlamatScreen(
//                        state = viewModel.state.value,
//                        events = viewModel::onTriggerEvent,
//                        errors = viewModel.errors,
//                        popup = { navigator.popBackStack() },
//                        navigateToDetailPajak = {},
//                    )
                }

                composable<HomeNavigation.MetodePembayaran> {
//                    MetodePembayaranPajakScreen(
//                        state = viewModel.state.value,
//                        events = viewModel::onTriggerEvent,
//                        errors = viewModel.errors,
//                        popup = { navigator.popBackStack() },
//                        navigateToDetail = {
//                            navigator.popBackStack()
//                        }
//                    )
                }

                composable<HomeNavigation.MilikOrangLain> {
//                    MilikOrangLainScreen(
//                        state = viewModel.state.value,
//                        events = viewModel::onTriggerEvent,
//                        errors = viewModel.errors,
//                        popup = { navigator.popBackStack() },
//                        navigateToCameraFace = {},
//                    )
                }
                /**
                *DaftarKendaraanSaya
                */

                composable<HomeNavigation.DaftarKendaraanSaya> {
//                    DaftarKendaraanSayaScreen(
//                        state = viewModel.state.value,
//                        events = viewModel::onTriggerEvent,
//                        errors = viewModel.errors,
//                        popup = { navigator.popBackStack() },
//                        navigateToDetail = {
//                            navigator.navigate(HomeNavigation.DetailKendaraanSaya)
//                        },
//                        navigateToTambah = {
//                            navigator.navigate(HomeNavigation.TambahKendaraanSaya)
//                        },
//                    )
                }

                composable<HomeNavigation.DetailKendaraanSaya> {
//                    DetailKendaraanSayaScreen(
//                        state = viewModel.state.value,
//                        events = viewModel::onTriggerEvent,
//                        errors = viewModel.errors,
//                        popup = { navigator.popBackStack() },
//                        navigateToDetailPajak = {},
//                    )
                }
                composable<HomeNavigation.TambahKendaraanSaya> {
//                    TambahKendaraanSayaScreen(
//                        state = viewModel.state.value,
//                        events = viewModel::onTriggerEvent,
//                        errors = viewModel.errors,
//                        popup = { navigator.popBackStack() },
//                    )
                }

                composable<HomeNavigation.RiwayatPembayaran> {
//                    RiwayatPembayaranScreen(
//                        state = viewModel.state.value,
//                        events = viewModel::onTriggerEvent,
//                        errors = viewModel.errors,
//                        popup = { navigator.popBackStack() },
//                        navigateToDetailRiwayat = {
//                            navigator.navigate(HomeNavigation.DetailMenungguPembayaran)
//                        },
//                    )
                }

                composable<HomeNavigation.DetailRiwayat> {
//                    DetailRiwayatPembayaranScreen(
//                        state = viewModel.state.value,
//                        events = viewModel::onTriggerEvent,
//                        errors = viewModel.errors,
//                        popup = { navigator.popBackStack() },
//                        navigateToDetailPajak = {  },
//                    )
                }

                composable<HomeNavigation.DetailMenungguPembayaran> {
//                    DetailMenungguPembayaranScreen(
//                        state = viewModel.state.value,
//                        events = viewModel::onTriggerEvent,
//                        errors = viewModel.errors,
//                        popup = { navigator.popBackStack() },
//                        navigateToDetailPajak = {  },
//                    )
                }

                composable<HomeNavigation.Etbpkp> {
//                    ETbpkpScreen(
//                        state = viewModel.state.value,
//                        events = viewModel::onTriggerEvent,
//                        errors = viewModel.errors,
//                        popup = { navigator.popBackStack() },
//                        navigateToDetail = {
//                            navigator.navigate(HomeNavigation.DetailEtbpkp)
//                        },
//                    )
                }

                composable<HomeNavigation.DetailEtbpkp> {
//                    DetailETbpkpScreen(
//                        state = viewModel.state.value,
//                        events = viewModel::onTriggerEvent,
//                        errors = viewModel.errors,
//                        popup = { navigator.popBackStack() },
//                        navigateToDetail = {  },
//                    )
                }

                composable<HomeNavigation.EKD> {
//                    EKDScreen(
//                        state = viewModel.state.value,
//                        events = viewModel::onTriggerEvent,
//                        errors = viewModel.errors,
//                        popup = { navigator.popBackStack() },
//                        navigateToDetail = {
//                            navigator.navigate(HomeNavigation.DetailEKD)
//                        },
//                    )
                }

                composable<HomeNavigation.DetailEKD> {
//                    DetailEKDScreen(
//                        state = viewModel.state.value,
//                        events = viewModel::onTriggerEvent,
//                        errors = viewModel.errors,
//                        popup = { navigator.popBackStack() },
//                        navigateToDetail = {  },
//                    )
                }

                composable<HomeNavigation.EPengesahan> {
//                    EPengesahanScreen(
//                        state = viewModel.state.value,
//                        events = viewModel::onTriggerEvent,
//                        errors = viewModel.errors,
//                        popup = { navigator.popBackStack() },
//                        navigateToDetail = {
//                            navigator.navigate(HomeNavigation.DetailEPengesahan)
//                        },
//                    )
                }

                composable<HomeNavigation.DetailEPengesahan> {
//                    DetailEPengesahanScreen(
//                        state = viewModel.state.value,
//                        events = viewModel::onTriggerEvent,
//                        errors = viewModel.errors,
//                        popup = { navigator.popBackStack() },
//                        navigateToDetail = {  },
//                    )
                }

                composable<HomeNavigation.TopUpListrik> {
//                    TopUpListrikScreen(
//                        state = viewModel.state.value,
//                        events = viewModel::onTriggerEvent,
//                        errors = viewModel.errors,
//                        popup = { navigator.popBackStack() },
//                        navigateToDetailRiwayat = {
//                            navigator.navigate(HomeNavigation.DetailTopUpListrik)
//                        },
//                    )
                }
                composable<HomeNavigation.DetailTopUpListrik> {
//                    DetailPembayaranListrikScreen(
//                        state = viewModel.state.value,
//                        events = viewModel::onTriggerEvent,
//                        errors = viewModel.errors,
//                        popup = { navigator.popBackStack() },
//                        navigateToPinScreen = {
//                            navigator.navigate(HomeNavigation.PinListrikScreen)
//                        },
//                        navigateToMetodePembayaran = {
//                            navigator.navigate(HomeNavigation.MetodePembayaran)
//                        },
//                        navigateToMenungguPembayaran = {},
//                    )
                }
                composable<HomeNavigation.PinListrikScreen> {
//                    PinScreen(
//                        state = viewModel.state.value,
//                        events = viewModel::onTriggerEvent,
//                        errors = viewModel.errors,
//                        popup = { navigator.popBackStack() },
//                        navigateToBuktiTransaksi = {
//                            navigator.navigate(HomeNavigation.BuktiTransaksi)
//                        },
//                    )
                }

                composable<HomeNavigation.BuktiTransaksi> {
//                    BuktiTransaksiScreen(
//                        state = viewModel.state.value,
//                        events = viewModel::onTriggerEvent,
//                        errors = viewModel.errors,
//                        popup = { navigator.popBackStack() },
//                        navigateToAlamat = {},
//                        navigateToMetodePembayaran = { },
//                        navigateToMenungguPembayaran = {},
//                    )
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
                    LaunchedEffect(Unit) {
                        viewModelLogin.action.onEach { effect ->
                            when (effect) {
                                LoginAction.Navigation.NavigateToMain -> {
                                    navigator.popBackStack()
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
                composable<HomeNavigation.Register> {
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

                    RegisterScreen(
                        navigateToLogin = {
                            navigator.popBackStack()
                            navigator.navigate(HomeNavigation.Login)
                        },
                        navigateToSuccess = {
                            navigator.popBackStack()
                            navigator.navigate(HomeNavigation.SuccessRegister)
                        },
                        popUp = { navigator.popBackStack() },
                        state = viewModelLogin.state.value,
                        errors = viewModelLogin.errors,
                        events = { event -> viewModelLogin.setEvent(event) }
                    )
                }
                composable<HomeNavigation.SuccessRegister> {
                    SuccessRegisterScreen(
                        state = viewModelLogin.state.value,
                        errors = viewModelLogin.errors,
                        events = { event -> viewModelLogin.setEvent(event) },
                        navigateToAturPin = {
                            navigator.popBackStack()
                            navigator.navigate(HomeNavigation.AturPin)
                        }
                    )
                }
                composable<HomeNavigation.AturPin> {
                    AturPinScreen(
                        state = viewModelLogin.state.value,
                        errors = viewModelLogin.errors,
                        events = { event -> viewModelLogin.setEvent(event) },
                        navigateToKonfirmasi = {
                            navigator.popBackStack()
                            navigator.navigate(HomeNavigation.KonfirmasiPin)
                        },
                        popup = {
                            navigator.popBackStack()
                        }
                    )
                }

                composable<HomeNavigation.KonfirmasiPin> {
                    KonfirmasiPinScreen(
                        state = viewModelLogin.state.value,
                        errors = viewModelLogin.errors,
                        events = { event -> viewModelLogin.setEvent(event) },
                        navigateToSuccess = {
                            navigator.popBackStack()
                            navigator.navigate(HomeNavigation.SuccessPin)
                        },
                        popup = {
                            navigator.popBackStack()
                        }
                    )
                }
                composable<HomeNavigation.SuccessPin> {
                    SuccessPinScreen(
                        state = viewModelLogin.state.value,
                        errors = viewModelLogin.errors,
                        events = { event -> viewModelLogin.setEvent(event) },
                        navigateToLogin = {
                            navigator.popBackStack()
                            navigator.navigate(HomeNavigation.Login)
                        },
                    )
                }

                composable<HomeNavigation.Settings> {
//                    val viewModelSetting: SettingsViewModel = koinViewModel()
//                    SettingsScreen(
//                        state = viewModelSetting.state.value,
//                        events = viewModelSetting::onTriggerEvent,
//                        errors = viewModelSetting.errors,
//                        action = viewModelSetting.action,
//                        logout = logout,
//                        popup = { navigator.popBackStack() },
//                    )
                }

                /**
                 * PROFILE
                 */
                composable(route = BottomNavigation.Profile.route) {
//                    val isTokenValid = viewModel.state.value.isTokenValid
//                    LaunchedEffect(isTokenValid) {
//                        if (!isTokenValid) {
//                            navigator.navigate(HomeNavigation.Login) {
//                                popUpTo(BottomNavigation.Profile.route) { inclusive = true }
//                            }
//                        } else {
//                            viewModelProfile.onTriggerEvent(ProfileEvent.OnRetryNetwork)
//                        }
//                    }

//                    LaunchedEffect(Unit) {
//                        viewModelProfile.action.collectLatest { effect ->
//                            when (effect) {
//                                is ProfileAction.Navigation.Logout -> {
//                                    logout()
//                                }
//
//                                is ProfileAction.Navigation.ToProfile -> {
//                                    navigator.popBackStack()
//                                }
//                            }
//                        }
//                    }

//                    ProfileScreen(
//                        state = viewModelProfile.state.value,
//                        events = viewModelProfile::onTriggerEvent,
//                        errors = viewModelProfile.errors,
//                        popup = {},
//                        navigateToVerify = {},
//                        navigateToDetailProfile = {
//                            navigator.navigate(ProfileNavigation.DetailProfile)
//                        },
//                        navigateToChangePassword = {
//                            navigator.navigate(ProfileNavigation.ChangePassword)
//                        },
//                        navigateToMyAddress = {
//                            navigator.navigate(ProfileNavigation.AlamatSaya)
//                        },
//                        navigateToChangePin = {
//                            navigator.navigate(ProfileNavigation.UbahPin)
//                        }
//
////                        navigateToEditProfile = {
////                            navigator.navigate(ProfileNavigation.EditProfile)
////                        },
////                        navigateToMyOrders = {
////                            navigator.navigate(MyTicketNavigation.HistoryTransaction)
////                        },
////                        exit = {
////                            viewModelProfile.onTriggerEvent(ProfileEvent.Logout)
////                        }
//                    )
                }
                composable<ProfileNavigation.Settings> {
//                    val viewModelSettings: SettingsViewModel = koinViewModel()
//                    SettingsScreen(
//                        state = viewModelSettings.state.value,
//                        events = viewModelSettings::onTriggerEvent,
//                        logout = logout,
//                        errors = viewModelSettings.errors,
//                        action = viewModelSettings.action,
//                        popup = {
//                            navigator.popBackStack()
//                        },
//                    )
                }
                composable<ProfileNavigation.DetailProfile> {
//                    DetailProfileScreen(
//                        state = viewModelProfile.state.value,
//                        events = viewModelProfile::onTriggerEvent,
////                        logout = logout,
//                        errors = viewModelProfile.errors,
////                        action = viewModelSettings.action,
//                        popup = {
//                            navigator.popBackStack()
//                        },
//                        navigateToVerify = {},
//                    )
                }
                composable<ProfileNavigation.AlamatSaya> {
//                    MyAddressScreen(
//                        state = viewModel.state.value,
//                        events = viewModel::onTriggerEvent,
////                        logout = logout,
//                        errors = viewModel.errors,
////                        action = viewModelSettings.action,
//                        popup = {
//                            navigator.popBackStack()
//                        },
//                        navigateToDetailPajak = {},
//                    )
                }
                composable<ProfileNavigation.ChangePassword> {
//                    val viewModelProfile: ProfileViewModel = koinViewModel()
//                    ChangePasswordScreen(
//                        errors = viewModelProfile.errors,
//                        state = viewModelLogin.state.value,
//                        events = viewModelLogin::onTriggerEvent,
//                        navigateToLogin = {},
//                        navigateToSuccess = {},
//                        popUp = {
//                            navigator.popBackStack()
//                        },
//                    )
//                        navigateToProfile = { navigator.popBackStack() }
//                    ) { navigator.popBackStack() }
                }
                composable<ProfileNavigation.UbahPin> {
//                    val viewModelProfile: ProfileViewModel = koinViewModel()
//                    ChangePinScreen(
//                        errors = viewModelLogin.errors,
//                        state = viewModelLogin.state.value,
//                        events = viewModelLogin::onTriggerEvent,
//                        navigateToKonfirmasi = {},
//                        popup = {
//                            navigator.popBackStack()
//                        },
//                    )
//                        navigateToProfile = { navigator.popBackStack() }
//                    ) { navigator.popBackStack() }
                }
                composable<ProfileNavigation.EditProfile> {
//                    val viewModelEditProfile: EditProfileViewModel = koinViewModel()
//                    EditProfileScreen(
//                        state = viewModelEditProfile.state.value,
//                        errors = viewModelEditProfile.errors,
//                        events = viewModelEditProfile::onTriggerEvent,
//                        navigateToProfile = { navigator.popBackStack() }
//                    ) { navigator.popBackStack() }
                }

                //Notification
                composable(route = BottomNavigation.Notification.route) {
//                    val isTokenValid = viewModel.state.value.isTokenValid
//                    LaunchedEffect(isTokenValid) {
//                        if (!isTokenValid) {
//                            navigator.navigate(HomeNavigation.Login) {
//                                popUpTo(BottomNavigation.Profile.route) { inclusive = true }
//                            }
//                        } else {
//                            viewModelProfile.onTriggerEvent(ProfileEvent.OnRetryNetwork)
//                        }
//                    }

                    LaunchedEffect(Unit) {
//                        viewModelProfile.action.collectLatest { effect ->
//                            when (effect) {
//                                is ProfileAction.Navigation.Logout -> {
//                                    logout()
//                                }
//
//                                is ProfileAction.Navigation.ToProfile -> {
//                                    navigator.popBackStack()
//                                }
//                            }
//                        }
                    }

//                    NotificationScreen(
//                        errors = viewModelProfile.errors,
//                        popup = {},
//                        navigateToVerify = {},
////                        navigateToChangePassword = {
////                            navigator.navigate(ProfileNavigation.ChangePassword) {
////                                popUpTo(BottomNavigation.Profile.route) { saveState = true }
////                            }
////                        },
////                        navigateToEditProfile = {
////                            navigator.navigate(ProfileNavigation.EditProfile)
////                        },
////                        navigateToMyOrders = {
////                            navigator.navigate(MyTicketNavigation.HistoryTransaction)
////                        },
////                        exit = {
////                            viewModelProfile.onTriggerEvent(ProfileEvent.Logout)
////                        }
//                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigationUI(
    navController: NavController,
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Card(
        colors = DefaultCardColorsTheme(),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(10.dp),
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp
        )
    ) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.background,
            tonalElevation = 8.dp
        ) {

            val items = listOf(
                BottomNavigation.Home,
                BottomNavigation.Notification,
                BottomNavigation.Profile
            )
            items.forEach { item ->
                NavigationBarItem(
                    label = { Text(text = item.title) },
                    colors = DefaultNavigationBarItemTheme(),
                    selected = item.route == currentRoute,
                    icon = {
                        Icon(
                            painterResource(if (item.route == currentRoute) item.selectedIcon else item.unSelectedIcon),
                            item.title
                        )
                    },
                    onClick = {
                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
                                navController.graph.findStartDestination().route?.let { r ->
                                    popUpTo(r) {
                                        saveState = true
                                    }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    }
}
