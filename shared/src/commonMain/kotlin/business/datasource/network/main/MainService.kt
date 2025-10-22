package business.datasource.network.main

import business.datasource.network.common.MainGenericResponse
import business.datasource.network.main.request.CheckQRRequestDTO
import business.datasource.network.main.request.KIRCompareRequestDTO
import business.datasource.network.main.request.PlatKIRRequestDTO
import business.datasource.network.main.request.RampcheckStartRequestDTO
import business.datasource.network.main.request.SubmitQuestionsRequestDTO
import business.datasource.network.main.request.UploadPetugasRequestDTO
import business.datasource.network.main.request.VehiclePhotoRequestDTO
import business.datasource.network.main.responses.CheckQRDTO
import business.datasource.network.main.responses.GetLocationDTO
import business.datasource.network.main.responses.GetVehicleDTO
import business.datasource.network.main.responses.KIRCompareDTO
import business.datasource.network.main.responses.PlatKIRDTO
import business.datasource.network.main.responses.ProfileDTO
import business.datasource.network.main.responses.QuestionDTO
import business.datasource.network.main.responses.RampcheckStartDTO
import business.datasource.network.main.responses.UploadPetugasDTO
import business.datasource.network.main.responses.VehiclePhotoDTO

interface MainService {
    companion object {

        const val HOME = "initialization"
        const val PROFILE = "getprofile"
        const val FORGOT_PASSWORD = "forgotpassword"
        const val NOTIFICATIONS = "getnotification"
        const val LOCATION = "getlocation"
        const val RAMPCHECK_START = "rampcheckstart"
        const val VEHICLE_PHOTO = "vehiclephoto"
        const val OFFICER_IMAGE = "officerimage"
        const val CHECKQR = "check_qr"
        const val PLAT_KIR = "platkir"
        const val CHECK_PLAT = "check_plat"
        const val QUESTION = "question"
        const val SUBMIT_QUESTION = "submitquestion"
        const val GET_VEHICLE = "getvehicle"
    }


    suspend fun getProfile(token: String): MainGenericResponse<ProfileDTO>

    suspend fun getLocation(token: String): MainGenericResponse<List<GetLocationDTO>>

    suspend fun rampcheckStart(
        request: RampcheckStartRequestDTO,
        token: String
    ): MainGenericResponse<RampcheckStartDTO>

    suspend fun getVehicle(token: String): MainGenericResponse<List<GetVehicleDTO>>

    suspend fun vehiclePhoto(request: VehiclePhotoRequestDTO, token: String): MainGenericResponse<VehiclePhotoDTO>

    suspend fun checkQR(request: CheckQRRequestDTO, token: String): MainGenericResponse<CheckQRDTO>

    suspend fun platKIR(request: PlatKIRRequestDTO, token: String): MainGenericResponse<PlatKIRDTO>

    suspend fun kirCompare(
        request: KIRCompareRequestDTO,
        token: String
    ): MainGenericResponse<KIRCompareDTO>

    suspend fun question(token: String): MainGenericResponse<QuestionDTO>

    suspend fun submitQuestion(
        requestDTO: SubmitQuestionsRequestDTO,
        token: String
    ): MainGenericResponse<String>


    suspend fun uploadFotoPetugas(
        request: UploadPetugasRequestDTO,
        token: String,
    ): MainGenericResponse<UploadPetugasDTO>


    suspend fun getNotification(
        token: String,
    ): MainGenericResponse<List<String>>


//    suspend fun updateDeviceToken(
//        token: String,
//        requestDTO: UpdateDeviceTokenRequestDTO
//    ): MainGenericResponse<String>


    suspend fun forgotPassword(
        token: String,
        email: String
    ): MainGenericResponse<String>

}