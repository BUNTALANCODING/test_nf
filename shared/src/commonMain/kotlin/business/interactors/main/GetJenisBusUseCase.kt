package business.interactors.main

import BusTypeDto
import business.constants.DataStoreKeys
import business.core.AppDataStore
import business.datasource.network.main.MainService
import presentation.ui.main.home.view_model.JenisBus

class GetJenisBusUseCase(
    private val mainService: MainService
) {
    suspend fun execute(token: String): List<JenisBus> {
        val response = mainService.getBusType(token)

        if (!response.status) {
            // kalau mau, bisa lempar exception atau handle pakai DataState
//            throw IllegalStateException(response.message)
        }

        // mapping DTO -> model UI
        return response.data.orEmpty().map { dto ->
            JenisBus(
                id = dto.id,
                nama = dto.name      // sesuaikan nama field
            )
        }
    }
}



