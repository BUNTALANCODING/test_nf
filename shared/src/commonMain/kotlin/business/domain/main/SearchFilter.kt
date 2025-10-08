package business.domain.main

import business.datasource.network.main.responses.CargoCategoryDTO

data class SearchFilter(
    val categories: List<CargoCategoryDTO> = listOf(),
    val minPrice: Int = 0,
    val maxPrice: Int = 10,
)
