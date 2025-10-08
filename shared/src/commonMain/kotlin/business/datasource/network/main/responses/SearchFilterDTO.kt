package business.datasource.network.main.responses

import business.domain.main.SearchFilter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchFilterDTO(
    @SerialName("categories") val categories: List<CargoCategoryDTO> = listOf(),
    @SerialName("min_price") val minPrice: Int?,
    @SerialName("max_price") val maxPrice: Int?,
)

fun SearchFilterDTO.toSearchFilter() = SearchFilter(
    categories = categories,
    minPrice = minPrice ?: 0,
    maxPrice = maxPrice ?: 0,
)

