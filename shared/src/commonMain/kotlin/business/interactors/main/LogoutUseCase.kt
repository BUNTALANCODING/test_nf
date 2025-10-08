package business.interactors.main

import business.constants.DataStoreKeys
import business.core.AppDataStore
import business.core.BaseDataStoreUseCase
import business.core.ProgressBarState
import com.mmk.kmpnotifier.notification.NotifierManager

class LogoutUseCase(
    private val appDataStoreManager: AppDataStore,
) : BaseDataStoreUseCase<Unit, Boolean>(appDataStoreManager) {

    override suspend fun run(params: Unit): Boolean {
        appDataStoreManager.setValue(
            DataStoreKeys.TOKEN,
            ""
        )
        appDataStoreManager.setValue(
            DataStoreKeys.FCMTOKEN,
            ""
        )
        NotifierManager.getPushNotifier().deleteMyToken()
        return true
    }

    override val progressBarState = ProgressBarState.ButtonLoading
}