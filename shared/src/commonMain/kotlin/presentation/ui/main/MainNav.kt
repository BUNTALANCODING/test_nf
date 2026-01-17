package presentation.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.coroutines.delay
import arhud.LatLng
import business.core.AppDataStore
import common.Context
import presentation.navigation.AppNavigation
import presentation.navigation.HomeNavigation
import presentation.navigation.LoginNavigation
import presentation.ui.main.arcam.ArScreen
import presentation.ui.main.auth.LoginScreen
import presentation.ui.main.busdetail.DetailBussScreen
import presentation.ui.main.halte.ArahkanRuteScreen
import presentation.ui.main.halte.DetailHalteScreen
import presentation.ui.main.halte.HalteScreen
import presentation.ui.main.inforute.DetailRuteScreen
import presentation.ui.main.inforute.InfoRuteScreen
import presentation.ui.main.kedatangabuss.KedatanganBussScreen
import presentation.ui.main.ubahlokasi.MauKemanaScreen
import presentation.ui.main.ubahlokasi.PilihPetaScreen
import presentation.ui.main.ubahlokasi.UbahLokasiScreen
import presentation.ui.splash.SplashScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNav(
    context: Context,
    appDataStore: AppDataStore
) {
    val navigator = rememberNavController()

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
                        appName = "TransporKu",
                        navigateToHome = {
                            navigator.navigate(AppNavigation.Main) {
                                popUpTo(LoginNavigation.Login) { inclusive = true }
                            }
                        }
                    )
                }

                composable<AppNavigation.Main> {
                    MainScreen(
                        navigateToInfoRute = { navigator.navigate(HomeNavigation.InfoRute) },
                        navigateToCariHalte = { navigator.navigate(HomeNavigation.CariHalte) },
                        navigateToJadwalBus = { navigator.navigate(HomeNavigation.KedatanganBuss) },
                        navigateToUbahLokasi = { navigator.navigate(HomeNavigation.UbahLokasi) },
                        navigateToArCamScreen = { navigator.navigate(HomeNavigation.ArCam) },
                        navigateToArahkanRute = {
                            navigator.navigate(
                                HomeNavigation.ArahkanRute(
                                    origin = "Current Location",
                                    destination = "Halte X"
                                )
                            )
                        },
                        navigateToMauKemana = { navigator.navigate(HomeNavigation.MauKemana) },
                        appDataStore = appDataStore
                    )
                }

                composable<HomeNavigation.InfoRute> {
                    InfoRuteScreen(
                        onClose = { navigator.popBackStack() },
                        onRouteClick = { route ->
                            navigator.navigate(
                                HomeNavigation.DetailRute(
                                    corridorCode = route.number.toString()
                                )
                            )
                        }
                    )
                }

                composable<HomeNavigation.DetailRute> { backStackEntry ->
                    val args = backStackEntry.toRoute<HomeNavigation.DetailRute>()
                    DetailRuteScreen(
                        corridorCode = args.corridorCode,
                        onBack = { navigator.popBackStack() },
                        onOpenMap = { },
                        onBusDetail = { busId ->
                            navigator.navigate(HomeNavigation.DetailBus(busId = busId))
                        },
                        onHalteClick = { halteId ->
                            navigator.navigate(HomeNavigation.DetailHalte(halteId = halteId))
                        }
                    )
                }

                composable<HomeNavigation.DetailHalte> { backStackEntry ->
                    val args = backStackEntry.toRoute<HomeNavigation.DetailHalte>()
                    DetailHalteScreen(
                        halteId = args.halteId,
                        onBack = { navigator.popBackStack() },
                        onToggleFavorite = { },
                        onOpenMap = { },
                        onOpenAr = { navigator.navigate(HomeNavigation.ArCam) },
                        onRoute = {
                            navigator.navigate(
                                HomeNavigation.ArahkanRute(
                                    origin = "Current Location",
                                    destination = "Halte ${args.halteId}"
                                )
                            )
                        },
                        onBusDetail = { busId ->
                            navigator.navigate(HomeNavigation.DetailBus(busId = busId))
                        }
                    )
                }

                composable<HomeNavigation.DetailBus> { backStackEntry ->
                    val args = backStackEntry.toRoute<HomeNavigation.DetailBus>()
                    DetailBussScreen(
                        busId = args.busId,
                        onBack = { navigator.popBackStack() },
                        onOpenMap = { }
                    )
                }

                composable<HomeNavigation.CariHalte> {
                    HalteScreen(
                        onBack = { navigator.popBackStack() },
                        onHalteClick = { halteId ->
                            navigator.navigate(HomeNavigation.DetailHalte(halteId = halteId))
                        }
                    )
                }

                composable<HomeNavigation.ArahkanRute> { backStackEntry ->
                    val args = backStackEntry.toRoute<HomeNavigation.ArahkanRute>()
                    ArahkanRuteScreen(
                        originLabel = args.origin,
                        destinationLabel = args.destination,
                        onBack = { navigator.popBackStack() }
                    )
                }

                composable<HomeNavigation.KedatanganBuss> {
                    KedatanganBussScreen(
                        onBack = { navigator.popBackStack() }
                    )
                }

                composable<HomeNavigation.UbahLokasi> {
                    UbahLokasiScreen(
                        onBack = { navigator.popBackStack() },
                        onPickFromMap = { navigator.navigate(HomeNavigation.PilihPeta) }
                    )
                }

                composable<HomeNavigation.MauKemana> {
                    MauKemanaScreen(
                        onBack = { navigator.popBackStack() }
                    )
                }

                composable<HomeNavigation.PilihPeta> {
                    PilihPetaScreen(
                        onBack = { navigator.popBackStack() },
                        onConfirm = {
                            navigator.navigate(AppNavigation.Main) {
                                launchSingleTop = true
                                popUpTo(AppNavigation.Main) { inclusive = true }
                            }
                        },
                        appDataStore = appDataStore
                    )
                }

                composable<HomeNavigation.ArCam> {
                    ArScreen(
                        destination = LatLng(-6.200000, 106.816666)
                    )
                }
            }
        }
    }
}
