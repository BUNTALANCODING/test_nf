package business.datasource.network.main

import business.constants.BASE_URL
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
import business.datasource.network.main.responses.HistoryDTO
import business.datasource.network.main.responses.HomeDTO
import business.datasource.network.main.responses.NotificationDTO
import business.datasource.network.main.responses.PaymentDTO
import business.datasource.network.main.responses.PrivacyPolicyDTO
import business.datasource.network.main.responses.ProfileDTO
import business.datasource.network.main.responses.TransactionDTO
import business.datasource.network.splash.responses.ForgotRequestDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.FormPart
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
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
    override suspend fun getNotifications(token: String): MainGenericResponse<List<NotificationDTO>> {
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

    override suspend fun home(token: String): MainGenericResponse<HomeDTO> {
        return httpClient.post {
            url {
                /*headers {
                    append(HttpHeaders.Authorization, token)
                }*/
                takeFrom(BASE_URL)
                encodedPath += MainService.HOME
            }
            contentType(ContentType.Application.Json)
        }.body()
    }

    override suspend fun getFerry(
        token: String,
        requestDTO: FerryRequestDTO
    ): MainGenericResponse<List<FerryDTO>> {
        return httpClient.post {
            url {
                /*headers {
                    append(HttpHeaders.Authorization, token)
                }*/
                takeFrom(BASE_URL)
                encodedPath += MainService.GET_FERRY
            }
            contentType(ContentType.Application.Json)
            setBody(requestDTO)
        }.body()
    }

    override suspend fun checkout(
        token: String,
        requestDTO: CheckoutRequestDTO
    ): MainGenericResponse<CheckoutDTO> {
        return httpClient.post {
            url {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                takeFrom(BASE_URL)
                encodedPath += MainService.CHECKOUT
            }
            contentType(ContentType.Application.Json)
            setBody(requestDTO)
        }.body()
    }

    override suspend fun updateProfile(
        token: String,
        requestDTO: UpdateProfileRequestDTO
    ): MainGenericResponse<Boolean> {
        return httpClient.post {
            url {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                takeFrom(BASE_URL)
                encodedPath += MainService.UPDATE_PROFILE
            }
            contentType(ContentType.Application.Json)
            setBody(requestDTO)
        }.body()
    }

    override suspend fun uploadFile(
        token: String,
        requestDTO: UploadFileRequestDTO
    ): MainGenericResponse<String> {
        return httpClient.post {
            url {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                takeFrom(BASE_URL)
                encodedPath += MainService.UPLOAD_FILE
            }
            contentType(ContentType.Application.Json)
            setBody(requestDTO)
        }.body()
    }

    override suspend fun getPrivacy(token: String): MainGenericResponse<PrivacyPolicyDTO> {
        return httpClient.post {
            url {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                takeFrom(BASE_URL)
                encodedPath += MainService.PRIVACY
            }
            contentType(ContentType.Application.Json)
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

    override suspend fun readNotification(
        token: String,
        requestDTO: NotificationRequestDTO
    ): MainGenericResponse<String> {
        return httpClient.post {
            url {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                takeFrom(BASE_URL)
                encodedPath += MainService.READ_NOTIFICATION
            }
            contentType(ContentType.Application.Json)
            setBody(requestDTO)
        }.body()
    }

    override suspend fun updateDeviceToken(
        token: String,
        requestDTO: UpdateDeviceTokenRequestDTO
    ): MainGenericResponse<String> {
        return httpClient.post {
            url {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                takeFrom(BASE_URL)
                encodedPath += MainService.UPDATE_DEVICE_TOKEN
            }
            contentType(ContentType.Application.Json)
            setBody(requestDTO)
        }.body()
    }

    override suspend fun payment(
        token: String,
        requestDTO: PaymentRequestDTO
    ): MainGenericResponse<PaymentDTO> {
        return httpClient.post {
            url {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                takeFrom(BASE_URL)
                encodedPath += MainService.PAYMENT
            }
            contentType(ContentType.Application.Json)
            setBody(requestDTO)
        }.body()
    }

    override suspend fun checkStatus(
        token: String,
        requestDTO: CheckStatusRequestDTO
    ): MainGenericResponse<CheckStatusDTO> {
        return httpClient.post {
            url {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                takeFrom(BASE_URL)
                encodedPath += MainService.CHECK_STATUS
            }
            contentType(ContentType.Application.Json)
            setBody(requestDTO)
        }.body()
    }

    override suspend fun getTransaction(
        token: String,
        requestDTO: TransactionRequestDTO
    ): MainGenericResponse<List<TransactionDTO>> {
        return httpClient.post {
            url {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                takeFrom(BASE_URL)
                encodedPath += MainService.TRANSACTION
            }
            contentType(ContentType.Application.Json)
            setBody(requestDTO)
        }.body()
    }

    override suspend fun getDetailTransaction(
        token: String,
        requestDTO: DetailTransactionRequestDTO
    ): MainGenericResponse<DetailTransaction> {
        return httpClient.post {
            url {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                takeFrom(BASE_URL)
                encodedPath += MainService.TRANSACTION_DETAIL
            }
            contentType(ContentType.Application.Json)
            setBody(requestDTO)
        }.body()
    }

    override suspend fun changePassword(
        token: String,
        requestDTO: ChangePasswordRequestDTO
    ): MainGenericResponse<String> {
        return httpClient.post {
            url {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                takeFrom(BASE_URL)
                encodedPath += MainService.CHANGE_PASSWORD
            }
            contentType(ContentType.Application.Json)
            setBody(requestDTO)
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