package business.datasource.network.main

import business.datasource.network.common.MainGenericResponse
import business.datasource.network.main.request.CheckQRRequestDTO
import business.datasource.network.main.request.GetStepRequestDTO
import business.datasource.network.main.request.HistoryRampcheckRequestDTO
import business.datasource.network.main.request.IdentifyRequestDTO
import business.datasource.network.main.request.KIRCompareRequestDTO
import business.datasource.network.main.request.NegativeAnswerRequestDTO
import business.datasource.network.main.request.PlatKIRRequestDTO
import business.datasource.network.main.request.PreviewBARequestDTO
import business.datasource.network.main.request.RampcheckStartRequestDTO
import business.datasource.network.main.request.SendEmailBARequestDTO
import business.datasource.network.main.request.SubmitQuestionsRequestDTO
import business.datasource.network.main.request.SubmitSignatureRequestDTO
import business.datasource.network.main.request.UploadChunkRequestDTO
import business.datasource.network.main.request.UploadPetugasRequestDTO
import business.datasource.network.main.request.UploadVideoRequestDTO
import business.datasource.network.main.request.VehiclePhotoRequestDTO
import business.datasource.network.main.responses.CheckQRDTO
import business.datasource.network.main.responses.ChunkResponse
import business.datasource.network.main.responses.GetLocationDTO
import business.datasource.network.main.responses.GetStepDTO
import business.datasource.network.main.responses.GetVehicleDTO
import business.datasource.network.main.responses.HasilTeknisDTO
import business.datasource.network.main.responses.HistoryRampcheckDTOItem
import business.datasource.network.main.responses.IdentifyDTO
import business.datasource.network.main.responses.IdentifyDTOItem
import business.datasource.network.main.responses.ItemsItemLoadCard
import business.datasource.network.main.responses.KIRCompareDTO
import business.datasource.network.main.responses.LoadCardDTOItem
import business.datasource.network.main.responses.PlatKIRDTO
import business.datasource.network.main.responses.PreviewBADTO
import business.datasource.network.main.responses.ProfileDTO
import business.datasource.network.main.responses.QuestionDTO
import business.datasource.network.main.responses.RampcheckStartDTO
import business.datasource.network.main.responses.SendEmailBADTO
import business.datasource.network.main.responses.SubmitSignatureDTO
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
        const val CHECK_FILE = "checkfile"
        const val OFFICER_IMAGE = "officerimage"
        const val CHECKQR = "check_qr"
        const val PLAT_KIR = "platkir"
        const val CHECK_PLAT = "check_plat"
        const val QUESTION = "question"
        const val SUBMIT_QUESTION = "submitquestion"
        const val GET_VEHICLE = "getvehicle"
        const val SUBMIT_SIGNATURE = "submitsignature"
        const val PREVIEW_BA = "show_ba"
        const val HISTORY_RAMPCHECK = "historyrampcheck"
        const val SEND_EMAIL_BA = "sendemail"
        const val GETSTEP = "getstep"
        const val IDENTIFY = "identify"
        const val NEGATIVE_ANSWER = "negativeanswer"

        const val INTERIOR_IDENTIFY = "interioridentify"

        const val GET_RESULT = "getresult"

        const val LOAD_CARD = "loadcard"
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

    suspend fun submitSignature(
        token: String,
        params: SubmitSignatureRequestDTO
    ): MainGenericResponse<SubmitSignatureDTO>

    suspend fun previewBA(
        token: String,
        params: PreviewBARequestDTO
    ): MainGenericResponse<PreviewBADTO>

    suspend fun sendEmailBA(
        token: String,
        params: SendEmailBARequestDTO
    ): MainGenericResponse<SendEmailBADTO>

    suspend fun getStep(
        token: String,
        params: GetStepRequestDTO
    ): MainGenericResponse<GetStepDTO>

    suspend fun identity(
        token: String,
        params: IdentifyRequestDTO
    ): MainGenericResponse<List<IdentifyDTOItem>>

    suspend fun historyRampcheck(
        token: String,
        params: HistoryRampcheckRequestDTO
    ): MainGenericResponse<List<HistoryRampcheckDTOItem>>

    suspend fun loadCard(
        token: String
    ): MainGenericResponse<List<LoadCardDTOItem>>



    suspend fun negativeAnswer(
        token: String,
        params: NegativeAnswerRequestDTO
    ): MainGenericResponse<String>

//    suspend fun updateDeviceToken(
//        token: String,
//        requestDTO: UpdateDeviceTokenRequestDTO
//    ): MainGenericResponse<String>


    suspend fun forgotPassword(
        token: String,
        email: String
    ): MainGenericResponse<String>



    suspend fun uploadVideo(
        token: String,
        params : UploadVideoRequestDTO
    ): Boolean

    suspend fun uploadChunkFile(
        token: String,
        fileName: String,
        uniqueKey: String,
        chunkIndex: Int,
        totalChunks: Int,
        chunk: ByteArray
    ): ChunkResponse

    suspend fun getInteriorResult(
        token: String,
        uniqueKey: String
    ): HasilTeknisDTO





}