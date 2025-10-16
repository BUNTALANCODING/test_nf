package business.datasource.network.main

import business.constants.BASE_URL
import business.datasource.network.common.MainGenericResponse

import business.datasource.network.main.responses.GetLocationDTO
import business.datasource.network.main.responses.ProfileDTO
import business.datasource.network.splash.responses.ForgotRequestDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.FormPart
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.http.takeFrom
import io.ktor.util.InternalAPI
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeFully

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

    @OptIn(InternalAPI::class)
    override suspend fun updateProfile(
        token: String,
        name: String,
        age: String,
        image: ByteArray?
    ): MainGenericResponse<Boolean> {
        return httpClient.post {
            url {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                takeFrom(BASE_URL)
                encodedPath += MainService.PROFILE
            }
            // contentType(ContentType.MultiPart.FormData)
            /*body = formData {
                append("name", name)
                if (image != null) {
                    append("image", image)
                }
                append("age", age)
            }*/

            body = MultiPartFormDataContent(
                formData {
                    append("name", name)
                    append("age", age)
                    this.append(FormPart("image", "image.jpg"))
                    this.appendInput(
                        key = "image",
                        headers = Headers.build {
                            append(
                                HttpHeaders.ContentDisposition,
                                "filename=image.jpg"
                            )
                        },
                    ) {
                        buildPacket {
                            if (image != null) {
                                writeFully(image)
                            }
                        }
                    }
                }
            )

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