package business.datasource.network.main.responses

import business.domain.main.Banner
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BannerDTO(
    @SerialName("banner_name") val banner: String?,
    @SerialName("banner_code") val bannerCode: String?,
    @SerialName("banner_id") val id: Int?
)

fun BannerDTO.toBanner() = Banner(banner = banner ?: "", bannerCode = bannerCode ?: "", id = id ?: 0)