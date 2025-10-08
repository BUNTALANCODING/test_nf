import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.MutableSharedFlow
import org.jetbrains.compose.ui.tooling.preview.Preview
import presentation.component.AlertDialog
import presentation.theme.AppTheme
import presentation.ui.main.auth.RegisterScreen
import presentation.ui.main.auth.view_model.LoginState
import presentation.ui.main.home.HomeScreen
import presentation.ui.main.home.news.NewsScreen
import presentation.ui.main.home.view_model.HomeState

/*@Composable
@androidx.compose.ui.tooling.preview.Preview
fun PreviewSearchTicketScreen() {
    AppTheme {
        GeneralAlertDialog(
            title = "Kata Sandi berhasil diubah",
            message = TODO(),
            isShowIcon = true,
            isShowButton = false,
            isShowDescription = false,
            onPositiveClick = {},
            onNegativeClick = {  }
        ) { }
    }
}*/

/*@Composable
@Preview
fun FerryTicketScreenPreview() {
    AppTheme {
        FerryTicketScreen()
    }
}*/

/*@Composable
@androidx.compose.ui.tooling.preview.Preview
fun PreviewBookingScreen() {
    AppTheme {
        BookingScreen(
            errors = emptyFlow(),
            state = HomeState(),
            events = {},
            onCheckout = {},
            onBack = {}
        )
    }
}*/

/*@Composable
@Preview
fun PreviewPaymentDetailScreen() {
    AppTheme {
        PaymentDetailsScreen(
            errors = MutableSharedFlow(),
            state = HomeState(),
            events = {},
            onViewOrders = {}
        )
    }
}*/

/*@Composable
@Preview
fun PreviewBuktiTransaksi() {
    AppTheme {
        BuktiTransaksiScreen(
            state = HomeState(),
            events = {},
            errors = MutableSharedFlow(),
            popup = {},
            navigateToAlamat = {},
            navigateToMetodePembayaran = {},
            navigateToMenungguPembayaran = {  }
        )
    }
}*/

//@Composable
//@Preview
//fun SplashScreenPreview() {
//    AppTheme {
//        SplashScreen()
//    }
//}

/*@Composable
@Preview
fun HomeScreen() {
    AppTheme {
        KonfirmasiTransferSaldoScreen(
            state = HomeState(),
            events = {},
            errors = MutableSharedFlow(),
            popup = {},
        ) { }
    }
}*/

@Composable
@Preview
fun HomeScreenPreview() {
    AppTheme {
        HomeScreen(
            state = HomeState(),
            events = {},
            errors = MutableSharedFlow(),
            navigateToNotifications = {},
            navigateToSaldo = {},
            navigateToLogin = {},
            navigateToNews = {},
            navigateToMilikOrangLain = {},
            navigateToDaftarKendaraan = {},
            navigateToDaftarKendaraanSaya = {},
            navigateToRiwayatPembayaran = {},
            navigateToEtbpkp = {  },
            navigateToEPengesahan = {},
        ) { }
    }
}

//@Composable
//@Preview
//fun ProfileScreenPrev() {
//    AppTheme {
//        AlertDialog(
//            onDismissRequest = {},
//            title = "Verifikasi Akun",
//            subtitle = "Untuk memastikan keamanan transaksi pembayaran Pajak Kendaraan, mohon untuk verifikasi identitas diri kamu.",
//        )
//    }
//}
/*@Composable
@Preview
fun LoginScreen() {
    AppTheme {
        LoginScreen(
            state = LoginState(),
            events = {},
            errors = MutableSharedFlow(),
            navigateToRegister = {}
        ) { }
    }
}*/

/*@Composable
@Preview
fun PreviewProfileScreen() {
    AppTheme {
        ProfileScreen(
            errors = MutableSharedFlow(),
            state = ProfileState(),
            events = {},
            navigateToChangePassword = {},
            navigateToEditProfile = {},
            exit = {},
            navigateToMyOrders = {}
        )
    }
}*/

/*@Composable
@Preview
fun PreviewEditProfileScreen() {
    AppTheme {
        EditProfileScreen(
            errors = MutableSharedFlow(),
            state = EditProfileState(),
            events = {},
            popup = {},
            navigateToProfile = {}
        )
    }
}*/

//@Composable
//@Preview
//fun PreviewSplashScreen() {
//    AppTheme {
//        NewsScreen(
//            state = HomeState(),
//            events = {},
//            errors = MutableSharedFlow(),
//            popup = {},
//            navigateToDetail = {}
//        )
//    }
//}

/*@Composable
@Preview
fun PreviewChangePasswordScreen() {
    AppTheme {
        ChangePasswordScreen(
            errors = MutableSharedFlow(),
            state = ProfileState(),
            events = {},
            popup = {}
        )
    }
}*/

/*@Composable
@Preview
fun PreviewBoardingPassScreen() {
    AppTheme {
        BoardingPassScreen(
            errors = MutableSharedFlow(),
            state = TransactionState(listTransaction = listOf(
                TransactionDTO(
                    bookingId = 1,
                    bookingCode = "34235345345",
                    ticketStatusId = 1,
                    paymentExpireTime = "2025-07-10 10:00:00",
                    originPortName = "Merak",
                    destinationPortName = "Ketapang",
                    departureTime = "",
                    totalPrice = 100000
                ),
            ),
                DetailTransaction(
                    bookingId = 1,
                    bookingCode = "234234234234234",
                    ticketStatusId = 1,
                    paymentExpireTime = "2025-07-10 10:00:00",
                    originPortName = "",
                    originPortDescription = "",
                    desinationPortName = "",
                    desinationPortDescription = "",
                    departureTime = " ",
                    cargoCategoryName = "",
                    nrkb = "",
                    shipTypeName = "",
                    duration = 3600,
                    totalPrice = 10000,
                    paymentTypeId = 1,
                    paymentReff = "",
                    paymentCode = "ID4891029587302945",
                    shipId = 1,
                    facility = arrayListOf(),
                    passenger = arrayListOf(PassengerTransaction(
                        ticketDetailCode = "4023957829587039457",
                        passengerName = "Tes",
                        identityNumber = "98240927834987234",
                        identityTypeName = "1",
                        passengerCategoryId = 3,
                        passengerCategoryName = "Pejalan Kaki",
                        genderName = "Tuan",
                        brithday = ""
                    ))
                )
            ),
            events = {},
            popup = {}
        )
    }
}*/

/*@Composable
@Preview
fun PreviewSendEmailScreen() {
    AppTheme {
        SendEmailScreen(
            errors = MutableSharedFlow(),
            state = LoginState(),
            events = {},
            navigateToLogin = {}
        )
    }
}*/

/*@Composable
@Preview
fun PreviewCheckoutScreen() {
    AppTheme {
        CheckoutScreen(
            errors = MutableSharedFlow(),
            state = HomeState(),
            events = {},
            action = MutableSharedFlow(),
            navigateToPayment = {},
            popup = {}
        )
    }
}*/

/*
@Composable
@Preview
fun PreviewHomeScreen() {
    AppTheme {
        HomeScreen(
            errors = MutableSharedFlow(),
            state = HomeState(),
            events = {},
            navigateToNotifications = {},
            navigateToFerry = {},
            navigateToLogin = {}
        )
    }
}*/

/*@Composable
@Preview
fun PreviewDetailTicketScreen() {
    AppTheme {
        DetailTicketScreen(
            errors = MutableSharedFlow(),
            state = TransactionState(),
            events = {},
            onDownloadClick = {},
            popup = {}
        )
    }
}*/

/*@Composable
@Preview
fun PreviewMyTicketScreen() {
    AppTheme {
        MyTicketScreen(
            errors = MutableSharedFlow(),
            state = TransactionState(),
            events = {},
            navigateToPayment = {},
            popup = {}
        )
    }
}*/
