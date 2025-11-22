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
import business.datasource.network.main.request.UploadChunkRequestDTO
import business.datasource.network.main.request.UploadPetugasRequestDTO
import business.datasource.network.main.request.UploadVideoRequestDTO
import business.datasource.network.main.request.VehiclePhotoRequestDTO
import business.datasource.network.main.responses.CheckQRDTO

import business.datasource.network.main.responses.GetLocationDTO
import business.datasource.network.main.responses.GetStepDTO
import business.datasource.network.main.responses.GetVehicleDTO
import business.datasource.network.main.responses.HistoryRampcheckDTO
import business.datasource.network.main.responses.HistoryRampcheckDTOItem
import business.datasource.network.main.responses.IdentifyDTO
import business.datasource.network.main.responses.IdentifyDTOItem
import business.datasource.network.main.responses.KIRCompareDTO
import business.datasource.network.main.responses.PlatKIRDTO
import business.datasource.network.main.responses.PreviewBADTO
import business.datasource.network.main.responses.ProfileDTO
import business.datasource.network.main.responses.QuestionDTO
import business.datasource.network.main.responses.RampcheckStartDTO
import business.datasource.network.main.responses.SendEmailBADTO
import business.datasource.network.main.responses.SubmitSignatureDTO
import business.datasource.network.main.responses.UploadChunkResponseDTO
import business.datasource.network.main.responses.UploadPetugasDTO
import business.datasource.network.main.responses.VehiclePhotoDTO
import business.datasource.network.splash.responses.ForgotRequestDTO
import common.PlatformFile
import common.platformModule
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.onUpload
import io.ktor.client.plugins.timeout
import io.ktor.client.request.forms.ChannelProvider
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
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeFully
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import okio.FileNotFoundException

class MainServiceImpl(
    private val httpClient: HttpClient
) : MainService {

    suspend fun uploadChunkStandalone(
        token: String,
        chunk: ByteArray,
        fileName: String,
        chunkIndex: Int,
        totalChunks: Int
    ): UploadChunkResponseDTO {
        val finalToken = if (token.startsWith("Bearer ")) token else "Bearer $token"

        return try {
            val response: MainGenericResponse<UploadChunkResponseDTO> = httpClient.submitFormWithBinaryData(
                url = "$BASE_URL/interioridentify",
                formData = formData {
                    append("file", chunk, Headers.build {
                        append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                        append(HttpHeaders.ContentType, ContentType.Application.OctetStream.toString())
                    })
                    append("unique_key", fileName)
                    append("chunk_index", chunkIndex)
                    append("total_chunks", totalChunks)
                }
            ) {
                headers { append(HttpHeaders.Authorization, finalToken) }
                timeout { requestTimeoutMillis = 60000 }
            }.body()

            response.result ?: UploadChunkResponseDTO() // fallback if null

        } catch (e: Exception) {
            e.printStackTrace()
            UploadChunkResponseDTO() // fallback on error
        }
    }

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
    ): MainGenericResponse<List<IdentifyDTOItem>> {
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
    ): MainGenericResponse<List<HistoryRampcheckDTOItem>> {
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
        println("Uploading Video...")
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

    override suspend fun uploadChunkFile(
        token: String,
        request: UploadChunkRequestDTO
    ): MainGenericResponse<UploadChunkResponseDTO> {

        println("DEBUG_REPO: 1. Entering uploadChunkFile...")

        if (request.file.isEmpty()) {
            println("DEBUG_REPO: ERROR - Empty file bytes!")
            return MainGenericResponse(
                status = false,
                code = "100",
                message = "File bytes are empty",
                result = null
            )
        }

        return try {
            val finalToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
            println("DEBUG_REPO: 2. Building Ktor Request...")

            val response = httpClient.submitFormWithBinaryData(
                url = "$BASE_URL/interioridentify",
                formData = formData {
                    append("file", request.file, Headers.build {
                        append(HttpHeaders.ContentDisposition, "filename=\"${request.fileName}\"")
                        append(HttpHeaders.ContentType, ContentType.Application.OctetStream.toString())
                    })
                    append("unique_key", request.uniqueKey)
                    append("chunk_index", request.chunkIndex)
                    append("total_chunks", request.totalChunks)
                }
            ) {
                headers {
                    append(HttpHeaders.Authorization, finalToken)
                }
                timeout {
                    requestTimeoutMillis = 60_000
                    connectTimeoutMillis = 60_000
                    socketTimeoutMillis = 60_000
                }
            }

            println("DEBUG_REPO: 3. Network Call Finished! Status: ${response.status}")

            val body = response.body<MainGenericResponse<UploadChunkResponseDTO>>()
            println("DEBUG_REPO: 4. Response Parsed: $body")

            body
        } catch (e: Exception) {
            println("DEBUG_REPO: 5. CRASHED: ${e.message}")
            e.printStackTrace()

            MainGenericResponse(
                status = false,
                code = "99",
                message = e.message ?: "Unknown error",
                result = null
            )
        }
    }


}