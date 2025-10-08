package business.interactors.splash

import business.constants.DataStoreKeys
import business.core.AppDataStore
import business.core.BaseDataStoreUseCase
import business.core.ProgressBarState

class CheckTokenUseCase(
    private val appDataStoreManager: AppDataStore,
) : BaseDataStoreUseCase<Unit, Boolean>(appDataStoreManager) {

    override suspend fun run(params: Unit) =
        (appDataStoreManager.readValue(DataStoreKeys.TOKEN) ?: "").isNotEmpty()

    override val progressBarState = ProgressBarState.ButtonLoading
}