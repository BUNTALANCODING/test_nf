package business.datasource.network.main.responses

import business.domain.main.Home
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeDTO(
    @SerialName("banner") val banners: List<BannerDTO>?,
    @SerialName("article_type") val articleType: List<ArticleTypeDTO>?,
    @SerialName("article") val article: List<ArticleDTO> = listOf(),
    @SerialName("route") var route: List<Route> = listOf(),
    @SerialName("time") var time: List<Time> = listOf(),
    @SerialName("cargo_category") var cargoCategory: List<CargoCategoryDTO> = listOf(),
    @SerialName("passenger_category") var passengerCategory: List<PassengerCategory> = listOf(),
    @SerialName("identity_type") var identityType: List<IdentityType> = listOf(),
    @SerialName("gender") var gender: List<Gender> = listOf(),
    @SerialName("city") var city: List<City> = listOf(),
    @SerialName("guide") var guide: List<Guide> = listOf(),
    @SerialName("payment_type") var paymentType: List<PaymentType> = listOf(),
    @SerialName("setting") var setting: List<Setting> = listOf()
)

@Serializable
data class Time(
    @SerialName("time_at") var timeAt: String? = null
)

@Serializable
data class IdentityType(
    @SerialName("identity_type_id") var identityTypeId: Int? = null,
    @SerialName("identity_type_name") var identityTypeName: String? = null
)

@Serializable
data class Gender(
    @SerialName("gender_id") var genderId: Int? = null,
    @SerialName("gender_name") var genderName: String? = null
)

@Serializable
data class City(
    @SerialName("city_id") var cityId: Int? = null,
    @SerialName("city_name") var cityName: String? = null
)

@Serializable
data class Setting(
    @SerialName("setting_variable") var settingVariable: String? = null,
    @SerialName("setting_value") var settingValue: String? = null
)

@Serializable
data class Guide(
    @SerialName("guide_name") var guideName: String? = null,
    @SerialName("guide_content") var guideContent: String? = null
)

fun HomeDTO.toHome() = Home(
    banners = banners?.map { it.toBanner() } ?: listOf(),
    articleType = articleType?.map { it.toArticleType() } ?: listOf(),
    article = article,
    route = route,
    time = time,
    cargoCategory = cargoCategory,
    passengerCategory = passengerCategory,
    identityType = identityType,
    city = city,
    paymentType = paymentType,
    gender = gender,
    setting = setting,
    guide = guide
)
