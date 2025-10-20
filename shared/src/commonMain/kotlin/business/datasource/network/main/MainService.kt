package business.datasource.network.main

import business.datasource.network.common.MainGenericResponse
import business.datasource.network.main.request.RampcheckStartRequestDTO
import business.datasource.network.main.responses.GetLocationDTO
import business.datasource.network.main.responses.ProfileDTO
import business.datasource.network.main.responses.RampcheckStartDTO

interface MainService {
    companion object {

        const val HOME = "initialization"
        const val PROFILE = "getprofile"
        const val FORGOT_PASSWORD = "forgotpassword"
        const val NOTIFICATIONS = "getnotification"
        const val LOCATION = "getlocation"
        const val RAMPCHECK_START = "rampcheckstart"
    }


    suspend fun getProfile(token: String): MainGenericResponse<ProfileDTO>

    suspend fun getLocation(token: String): MainGenericResponse<List<GetLocationDTO>>

    suspend fun rampcheckStart(request : RampcheckStartRequestDTO,token: String): MainGenericResponse<RampcheckStartDTO>

    suspend fun updateProfile(
        token: String,
        name: String,
        age: String,
        image: ByteArray?,
    ): MainGenericResponse<Boolean>



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