package business.datasource.network.main.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequestDTO(
    @SerialName("name") var name: String? = null,
    @SerialName("address") var address: String? = null,
    @SerialName("phone") var phone: String? = null,
    @SerialName("email") var email: String? = null,
    @SerialName("identity_type_id") var identityTypeId: Int? = null,
    @SerialName("identity_number") var identityNumber: String? = null,
    @SerialName("gender_id") var genderId: Int? = null,
    @SerialName("age") var age: Int? = null,
    @SerialName("city_id") var cityId: Int? = null
)
