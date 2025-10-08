package business.datasource.network.main.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDTO(
    @SerialName("member_id") var memberId: Int? = 0,
    @SerialName("member_type_id") var memberTypeId: Int? = 0,
    @SerialName("member_code") var memberCode: String? = "",
    @SerialName("member_name") var memberName: String? = "",
    @SerialName("member_email") var memberEmail: String? = "",
    @SerialName("member_address") var memberAddress: String? = "",
    @SerialName("member_img") var memberImg: String? = "",
    @SerialName("member_point") var memberPoint: Int? = 0,
    @SerialName("member_phone") var memberPhone: String? = "",
    @SerialName("member_referral_code") var memberReferralCode: String? = "",
    @SerialName("identity_type_id") var identityTypeId: Int? = 0,
    @SerialName("identity_number") var identityNumber: String? = "",
    @SerialName("gender_id") var genderId: Int? = 0,
    @SerialName("age") var age: Int? = 0,
    @SerialName("city_id") var cityId: Int? = 0
)
