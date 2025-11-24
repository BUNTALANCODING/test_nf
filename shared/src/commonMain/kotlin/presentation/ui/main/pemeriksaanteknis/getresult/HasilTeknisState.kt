// HasilTeknisState.kt
package presentation.ui.main.pemeriksaanteknis

import business.datasource.network.main.responses.HasilTeknisDTO

data class HasilTeknisState(
    val isLoading: Boolean = false,
    val data: HasilTeknisDTO? = null,
    val error: String? = null
)
