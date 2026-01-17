package presentation.ui.main.home.view_model

import presentation.ui.main.home.BusArrivalUi
import presentation.ui.main.home.HalteTerdekatUi

data class HomeState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val halteTerdekat: HalteTerdekatUi? = null,
    val busArrivals: List<BusArrivalUi> = emptyList()
)
