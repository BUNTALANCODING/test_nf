package business.datasource.network.main

import business.constants.BASE_URL
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
import business.datasource.network.main.request.UploadPetugasRequestDTO
import business.datasource.network.main.request.UploadVideoRequestDTO
import business.datasource.network.main.request.VehiclePhotoRequestDTO
import business.datasource.network.main.responses.CheckQRDTO

import business.datasource.network.main.responses.GetLocationDTO
import business.datasource.network.main.responses.GetStepDTO
import business.datasource.network.main.responses.GetVehicleDTO
import business.datasource.network.main.responses.HistoryRampcheckDTO
import business.datasource.network.main.responses.IdentifyDTO
import business.datasource.network.main.responses.KIRCompareDTO
import business.datasource.network.main.responses.PlatKIRDTO
import business.datasource.network.main.responses.PreviewBADTO
import business.datasource.network.main.responses.ProfileDTO
import business.datasource.network.main.responses.QuestionDTO
import business.datasource.network.main.responses.RampcheckStartDTO
import business.datasource.network.main.responses.SendEmailBADTO
import business.datasource.network.main.responses.SubmitSignatureDTO
import business.datasource.network.main.responses.UploadPetugasDTO
import business.datasource.network.main.responses.VehiclePhotoDTO
import business.datasource.network.splash.responses.ForgotRequestDTO
import common.PlatformFile
import common.platformModule
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.forms.InputProvider
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentDisposition.Companion.File
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.http.takeFrom
import io.ktor.util.InternalAPI
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeFully
import kotlinx.datetime.Clock
import okio.FileNotFoundException

class MainServiceImpl(
    private val httpClient: HttpClient
) : MainService {

    override suspend fun getProfile(token: String): MainGenericResponse<ProfileDTO> {
        return httpClient.post {
            url {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                takeFrom(BASE_URL)
                encodedPath += MainService.PROFILE
            }
            contentType(ContentType.Application.Json)
        }.body()
    }

    override suspend fun getLocation(token: String): MainGenericResponse<List<GetLocationDTO>> {
        return httpClient.post {
            url {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                takeFrom(BASE_URL)
                encodedPath += MainService.LOCATION
            }
            contentType(ContentType.Application.Json)
        }.body()
    }

    override suspend fun checkQR(
        request: CheckQRRequestDTO,
        token: String,
    ): MainGenericResponse<CheckQRDTO> {
        return httpClient.post {
            url {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                takeFrom(BASE_URL)
                encodedPath += MainService.CHECKQR
            }
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun platKIR(
        request: PlatKIRRequestDTO,
        token: String
    ): MainGenericResponse<PlatKIRDTO> {
        return httpClient.post {
            url {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                takeFrom(BASE_URL)
                encodedPath += MainService.PLAT_KIR
            }
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun kirCompare(
        request: KIRCompareRequestDTO,
        token: String
    ): MainGenericResponse<KIRCompareDTO> {
        return httpClient.post {
            url {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                takeFrom(BASE_URL)
                encodedPath += MainService.CHECK_PLAT
            }
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun question(token: String): MainGenericResponse<QuestionDTO> {
        return httpClient.post {
            url {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                takeFrom(BASE_URL)
                encodedPath += MainService.QUESTION
            }
            contentType(ContentType.Application.Json)
        }.body()
    }

    override suspend fun submitQuestion(
        requestDTO: SubmitQuestionsRequestDTO,
        token: String
    ): MainGenericResponse<String> {
        return httpClient.post {
            url {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                takeFrom(BASE_URL)
                encodedPath += MainService.SUBMIT_QUESTION
            }
            contentType(ContentType.Application.Json)
            setBody(requestDTO)
        }.body()
    }

    override suspend fun uploadFotoPetugas(
        request: UploadPetugasRequestDTO,
        token: String,
    ): MainGenericResponse<UploadPetugasDTO> {
        return httpClient.post {
            url {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                takeFrom(BASE_URL)
                encodedPath += MainService.OFFICER_IMAGE
            }
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun rampcheckStart(
        request: RampcheckStartRequestDTO,
        token: String
    ): MainGenericResponse<RampcheckStartDTO> {
        return httpClient.post {
            url {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                takeFrom(BASE_URL)
                encodedPath += MainService.RAMPCHECK_START
            }
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun getVehicle(token: String): MainGenericResponse<List<GetVehicleDTO>> {
        return httpClient.post {
            url {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                takeFrom(BASE_URL)
                encodedPath += MainService.GET_VEHICLE
            }
            contentType(ContentType.Application.Json)
        }.body()
    }

    override suspend fun vehiclePhoto(
        request: VehiclePhotoRequestDTO,
        token: String
    ): MainGenericResponse<VehiclePhotoDTO> {
        return httpClient.post {
            url {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                takeFrom(BASE_URL)
                encodedPath += MainService.VEHICLE_PHOTO
            }
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun previewBA(
        token: String,
        params: PreviewBARequestDTO,
    ): MainGenericResponse<PreviewBADTO> {
        return httpClient.post {
            url {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                takeFrom(BASE_URL)
                encodedPath += MainService.PREVIEW_BA
            }
            contentType(ContentType.Application.Json)
            setBody(params)
        }.body()
    }

    override suspend fun sendEmailBA(
        token: String,
        params: SendEmailBARequestDTO,
    ): MainGenericResponse<SendEmailBADTO> {
        return httpClient.post {
            url {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                takeFrom(BASE_URL)
                encodedPath += MainService.PREVIEW_BA
            }
            contentType(ContentType.Application.Json)
            setBody(params)
        }.body()
    }

    override suspend fun getStep(
        token: String,
        params: GetStepRequestDTO
    ): MainGenericResponse<GetStepDTO> {
        return httpClient.post {
            url {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                takeFrom(BASE_URL)
                encodedPath += MainService.GETSTEP
            }
            contentType(ContentType.Application.Json)
            setBody(params)
        }.body()
    }

    override suspend fun identity(
        token: String,
        params: IdentifyRequestDTO
    ): MainGenericResponse<IdentifyDTO> {
        return httpClient.post {
            url {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                takeFrom(BASE_URL)
                encodedPath += MainService.IDENTIFY
            }
            contentType(ContentType.Application.Json)
            setBody(params)
        }.body()
    }

    override suspend fun historyRampcheck(
        token: String,
        params: HistoryRampcheckRequestDTO
    ): MainGenericResponse<HistoryRampcheckDTO> {
        return httpClient.post {
            url {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                takeFrom(BASE_URL)
                encodedPath += MainService.HISTORY_RAMPCHECK
            }
            contentType(ContentType.Application.Json)
            setBody(params)
        }.body()
    }

    override suspend fun negativeAnswer(
        token: String,
        params: NegativeAnswerRequestDTO
    ): MainGenericResponse<String> {
        return httpClient.post {
            url {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                takeFrom(BASE_URL)
                encodedPath += MainService.NEGATIVE_ANSWER
            }
            contentType(ContentType.Application.Json)
            setBody(params)
        }.body()
    }


//    @OptIn(InternalAPI::class)
//    override suspend fun uploadFotoPetugas(
//        token: String,
//        officerImage: ByteArray?
//    ): MainGenericResponse<UploadPetugasDTO> {
//        return httpClient.post {
//            url {
//                headers {
//                    append(HttpHeaders.Authorization, token)
//                }
//                takeFrom(BASE_URL)
//                encodedPath += MainService.OFFICER_IMAGE
//            }
//            // contentType(ContentType.MultiPart.FormData)
//            /*body = formData {
//                append("name", name)
//                if (image != null) {
//                    append("image", image)
//                }
//                append("age", age)
//            }*/
//
//
//            contentType(ContentType.MultiPart.FormData)
//            setBody(
//                MultiPartFormDataContent(
//                    formData {
//                        appendInput(
//                            key = "officerImage",
//                            headers = Headers.build {
//                                append(HttpHeaders.ContentDisposition, "form-data; name=\"officerImage\"; filename=\"image.png\"")
//                                append(HttpHeaders.ContentType, "image/png")
//                            }
//                        ) {
//                            buildPacket {
//                                officerImage?.let { writeFully(it) }
//                            }
//                        }
//                    }
//                )
//            )
//
//        }.body()
//    }

    /*override suspend fun uploadFotoPetugas(
        token: String,
        officerImage: ByteArray?,
    ): MainGenericResponse<UploadPetugasDTO> {
        val timestamp = Clock.System.now()
        val fileName = "image_${timestamp}.jpeg"
        val imageBytes = officerImage ?: throw IllegalArgumentException("Officer image bytes cannot be null.")

        println("Upload size: ${officerImage?.size ?: 0}")

        return httpClient.submitFormWithBinaryData(
            url = BASE_URL + MainService.OFFICER_IMAGE,
            formData = formData {
                // field file
                append(
                    key = "officer_image",
                    value = imageBytes,
                    headers = Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(HttpHeaders.ContentDisposition, "filename=$fileName")
                    }
                )
            }
        ).body()
//        return httpClient.post {
//            url {
//                takeFrom(BASE_URL)
//                encodedPath += MainService.OFFICER_IMAGE
//            }
//
//            headers {
//                append(HttpHeaders.Authorization, token)
//            }
//
//            setBody(
//                MultiPartFormDataContent(
//                    formData {
//                        appendInput(
//                            key = "officer_image",
//                            headers = Headers.build {
//                                append(HttpHeaders.ContentType, "image/jpeg")
//                            }
//                        ) {
//                            buildPacket {
//                                writeFully(imageBytes)
//                            }
//                        }
//                    }
//                )
//            )
//        }.body()
    }*/

    override suspend fun getNotification(token: String): MainGenericResponse<List<String>> {
        return httpClient.post {
            url {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                takeFrom(BASE_URL)
                encodedPath += MainService.NOTIFICATIONS
            }
            contentType(ContentType.Application.Json)
        }.body()
    }

    override suspend fun submitSignature(
        token: String,
        params: SubmitSignatureRequestDTO
    ): MainGenericResponse<SubmitSignatureDTO> {
        return httpClient.post {
            url {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                takeFrom(BASE_URL)
                encodedPath += MainService.SUBMIT_SIGNATURE
            }
            contentType(ContentType.Application.Json)
            setBody(params)
        }.body()
    }


    override suspend fun forgotPassword(
        token: String,
        email: String
    ): MainGenericResponse<String> {
        return httpClient.post {
            url {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                takeFrom(BASE_URL)
                encodedPath += MainService.FORGOT_PASSWORD
            }
            contentType(ContentType.Application.Json)
            setBody(ForgotRequestDTO(email))
        }.body()
    }


    @OptIn(InternalAPI::class)
    override suspend fun uploadVideo(
        token: String,
        params: UploadVideoRequestDTO
    ): Boolean {
        // 1️⃣ Siapkan file
        val platformFile = try {
            PlatformFile(params.filePath)
        } catch (e: Exception) {
            throw FileNotFoundException("File video tidak ditemukan: ${params.filePath}")
        }

        val totalBytes = platformFile.totalBytes

        return httpClient.submitFormWithBinaryData(
            url = BASE_URL + MainService.CHECK_FILE,
            formData = formData {
                append(
                    "file",
                    InputProvider {
                        platformFile.asInput()
                    },
                    Headers.build {
                        append(HttpHeaders.ContentType, "video/mp4")
                        append(
                            HttpHeaders.ContentDisposition,
                            "form-data; name=\"file\"; filename=\"${platformFile.fileName}\""
                        )
                    }
                )
            }
        ) {
            // 3️⃣ Header Authorization
            headers {
                append(HttpHeaders.Authorization, token)
            }

            // 4️⃣ Progress bar
            onUpload { bytesSentTotal, _ ->
                if (totalBytes > 0) {
                    val progress = bytesSentTotal.toFloat() / totalBytes.toFloat()
                    params.onProgress(progress)
                }
            }
        }.body()
       /* val platformFile = try {
            PlatformFile(params.filePath)
        } catch (e: Exception) {
            throw FileNotFoundException("File video tidak ditemukan: ${params.filePath}")
        }

        val totalBytes = platformFile.totalBytes

        return httpClient.submitFormWithBinaryData(
            url = BASE_URL + MainService.VEHICLE_PHOTO,
            formData = formData {

                // SOLUSI: Gunakan fungsi 'append' yang menerima ByteReadChannel sebagai 'value'
                append(
                    key = "file",
                    value = platformFile.readChannel(),
                    headers = Headers.build {
                        append(HttpHeaders.ContentType, "video/mp4")
                        append(HttpHeaders.ContentDisposition, "filename=\"${platformFile.fileName}\"")
                    }
                )
            }
        ) {
            // ... (Header dan onUpload tetap sama)
            headers {
                append(HttpHeaders.Authorization, token)
            }

            onUpload { bytesSentTotal, _ ->
                println("cek totalBytes $totalBytes")
                if (totalBytes > 0) {
                    val progress = bytesSentTotal.toFloat() / totalBytes.toFloat()
                    params.onProgress(progress)
                }
            }
        }.body()*/
    }
//        val file = File(filePath)
//        val totalBytes = file.length()
//        var sentBytes = 0L
//
//        return httpClient.submitFormWithBinaryData(
//            url = "https://file.io", // endpoint testing
//            formData = formData {
//                append(
//                    "file",
//                    object : InputProvider {
//                        override fun toInput(): Input = object : Input {
//                            override fun read(buffer: ByteArray, offset: Int, length: Int): Int {
//                                val read = file.inputStream().read(buffer, offset, length)
//                                if (read > 0) {
//                                    sentBytes += read
//                                    onProgress(sentBytes.toFloat() / totalBytes)
//                                }
//                                return read
//                            }
//
//                            override fun close() {}
//                        }
//                    },
//                    Headers.build {
//                        append(HttpHeaders.ContentType, "video/mp4")
//                        append(HttpHeaders.ContentDisposition, "filename=\"${file.name}\"")
//                    }
//                )
//            },
//            encodeInQuery = false
//        ).body()
//    }
//    }
}