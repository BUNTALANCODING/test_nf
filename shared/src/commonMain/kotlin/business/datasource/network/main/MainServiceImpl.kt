package business.datasource.network.main

import business.constants.BASE_URL
import business.datasource.network.common.MainGenericResponse
import business.datasource.network.main.request.RampcheckStartRequestDTO

import business.datasource.network.main.responses.GetLocationDTO
import business.datasource.network.main.responses.ProfileDTO
import business.datasource.network.main.responses.RampcheckStartDTO
import business.datasource.network.main.responses.UploadPetugasDTO
import business.datasource.network.splash.responses.ForgotRequestDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.http.takeFrom
import kotlinx.datetime.Clock

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


//    @OptIn(InternalAPI::class)
//    override suspend fun uploadFotoPetugas(
//        token: String,
//        officer_image: ByteArray?
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
//                            key = "officer_image",
//                            headers = Headers.build {
//                                append(HttpHeaders.ContentDisposition, "form-data; name=\"officer_image\"; filename=\"image.png\"")
//                                append(HttpHeaders.ContentType, "image/png")
//                            }
//                        ) {
//                            buildPacket {
//                                officer_image?.let { writeFully(it) }
//                            }
//                        }
//                    }
//                )
//            )
//
//        }.body()
//    }

    override suspend fun uploadFotoPetugas(
        token: String,
        officerImage: ByteArray?,
    ): MainGenericResponse<UploadPetugasDTO> {
        val timestamp = Clock.System.now()
        val fileName = "image_${timestamp}.png"
        return httpClient.submitFormWithBinaryData(
            formData = formData {
                if (officerImage != null) {
                    append(
                        "officer_image",
                        officerImage,
                        Headers.build {
                            append(HttpHeaders.ContentType, "image/jpeg")
                            append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                        }
                    )
                }
            }
        ) {
            url {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                takeFrom(BASE_URL)
                encodedPath += MainService.OFFICER_IMAGE
            }
        }.body()
    }

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
}