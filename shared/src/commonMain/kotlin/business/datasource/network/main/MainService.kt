package business.datasource.network.main

import business.datasource.network.common.JRNothing
import business.datasource.network.common.MainGenericResponse
import business.datasource.network.main.request.ChangePasswordRequestDTO
import business.datasource.network.main.request.CheckStatusRequestDTO
import business.datasource.network.main.request.CheckoutRequestDTO
import business.datasource.network.main.request.DetailTransactionRequestDTO
import business.datasource.network.main.request.FerryRequestDTO
import business.datasource.network.main.request.NotificationRequestDTO
import business.datasource.network.main.request.PaymentRequestDTO
import business.datasource.network.main.request.TransactionRequestDTO
import business.datasource.network.main.request.UpdateDeviceTokenRequestDTO
import business.datasource.network.main.request.UpdateProfileRequestDTO
import business.datasource.network.main.request.UploadFileRequestDTO
import business.datasource.network.main.responses.CheckStatusDTO
import business.datasource.network.main.responses.CheckoutDTO
import business.datasource.network.main.responses.DetailTransaction
import business.datasource.network.main.responses.FerryDTO
import business.datasource.network.main.responses.HomeDTO
import business.datasource.network.main.responses.NotificationDTO
import business.datasource.network.main.responses.ProfileDTO
import business.datasource.network.main.responses.SearchFilterDTO
import business.datasource.network.main.responses.HistoryDTO
import business.datasource.network.main.responses.PaymentDTO
import business.datasource.network.main.responses.PrivacyPolicyDTO
import business.datasource.network.main.responses.TransactionDTO

interface MainService {
    companion object {
        const val GET_FERRY = "getavailableferry"
        const val UPDATE_DEVICE_TOKEN = "updatedevicetoken"
        const val READ_NOTIFICATION = "readnotification"
        const val HOME = "initialization"
        const val CHECKOUT = "checkout"
        const val PRIVACY = "getprivacy"
        const val UPDATE_PROFILE = "updateprofile"
        const val PROFILE = "getprofile"
        const val UPLOAD_FILE = "uploadfile"
        const val HISTORY = "history"
        const val FORGOT_PASSWORD = "forgotpassword"
        const val NOTIFICATIONS = "getnotification"
        const val PAYMENT = "payment"
        const val CHECK_STATUS = "checkstatus"
        const val TRANSACTION = "gettransaction"
        const val TRANSACTION_DETAIL = "gettransactiondetail"
        const val CHANGE_PASSWORD = "changepassword"
    }

    suspend fun getNotifications(
        token: String,
    ): MainGenericResponse<List<NotificationDTO>>

    suspend fun getProfile(token: String): MainGenericResponse<ProfileDTO>

    suspend fun updateProfile(
        token: String,
        name: String,
        age: String,
        image: ByteArray?,
    ): MainGenericResponse<Boolean>

    suspend fun home(token: String): MainGenericResponse<HomeDTO>

    suspend fun getFerry(
        token: String,
        requestDTO: FerryRequestDTO
    ): MainGenericResponse<List<FerryDTO>>

    suspend fun checkout(
        token: String,
        requestDTO: CheckoutRequestDTO
    ): MainGenericResponse<CheckoutDTO>

    suspend fun updateProfile(
        token: String,
        requestDTO: UpdateProfileRequestDTO
    ): MainGenericResponse<Boolean>

    suspend fun uploadFile(
        token: String,
        requestDTO: UploadFileRequestDTO
    ): MainGenericResponse<String>

    suspend fun getPrivacy(
        token: String,
    ): MainGenericResponse<PrivacyPolicyDTO>

    suspend fun getNotification(
        token: String,
    ): MainGenericResponse<List<String>>

    suspend fun readNotification(
        token: String,
        requestDTO: NotificationRequestDTO
    ): MainGenericResponse<String>

//    suspend fun updateDeviceToken(
//        token: String,
//        requestDTO: UpdateDeviceTokenRequestDTO
//    ): MainGenericResponse<String>

    suspend fun payment(
        token: String,
        requestDTO: PaymentRequestDTO
    ): MainGenericResponse<PaymentDTO>

    suspend fun checkStatus(
        token: String,
        requestDTO: CheckStatusRequestDTO
    ): MainGenericResponse<CheckStatusDTO>

    suspend fun getTransaction(
        token: String,
        requestDTO: TransactionRequestDTO
    ): MainGenericResponse<List<TransactionDTO>>

    suspend fun getDetailTransaction(
        token: String,
        requestDTO: DetailTransactionRequestDTO
    ): MainGenericResponse<DetailTransaction>

    suspend fun changePassword(
        token: String,
        requestDTO: ChangePasswordRequestDTO
    ): MainGenericResponse<String>

    suspend fun forgotPassword(
        token: String,
        email: String
    ): MainGenericResponse<String>

}