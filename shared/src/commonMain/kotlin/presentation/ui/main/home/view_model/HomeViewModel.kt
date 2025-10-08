package presentation.ui.main.home.view_model

import business.core.BaseViewModel
import business.core.UIComponentState
import business.datasource.network.main.request.UpdateDeviceTokenRequestDTO
import business.interactors.main.CheckStatusUseCase
import business.interactors.main.CheckoutUseCase
import business.interactors.main.GetAvailableFerryUseCase
import business.interactors.main.GetProfileUseCase
import business.interactors.main.HomeUseCase
import business.interactors.main.PaymentUseCase
import business.interactors.main.UpdateDeviceTokenUseCase
import business.interactors.main.UpdateProfileUseCase
import business.interactors.splash.CheckTokenUseCase
import common.ImageSaveShare

class HomeViewModel(
    private val homeUseCase: HomeUseCase,
    private val ferryUseCase: GetAvailableFerryUseCase,
    private val profileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val checkoutUseCase: CheckoutUseCase,
    private val paymentUseCase: PaymentUseCase,
    private val checkTokenUseCase: CheckTokenUseCase,
    private val checkStatusUseCase: CheckStatusUseCase,
    private val updateDeviceTokenUseCase: UpdateDeviceTokenUseCase,
    private val imageSaver: ImageSaveShare
) : BaseViewModel<HomeEvent, HomeState, HomeAction>() {

    override fun setInitialState() = HomeState()

    override fun onTriggerEvent(event: HomeEvent) {
        when (event) {

            is HomeEvent.GetHomeContent -> {
//                getHome()
            }


            is HomeEvent.OnUpdateCityCode -> {
                onUpdateCityCode(event.value)
            }

            is HomeEvent.OnValidateField -> {

            }



            is HomeEvent.OnUpdateTokenFCM -> {
                updateTokenFCM(event.value)
            }

            is HomeEvent.OnUpdateNetworkState -> {

            }

            is HomeEvent.OnUpdateLastCode -> {
                onUpdateLastCode(event.value)
            }
            is HomeEvent.OnUpdateMiddleCode -> {
                onUpdateMiddleCode(event.value)
            }
            is HomeEvent.OnUpdateNoRangka -> {
                onUpdateNoRangka(event.value)
            }

            is HomeEvent.OnUpdateSelectedOption -> {
                onUpdateSelectedOption(event.value)
            }

            is HomeEvent.OnUpdateSelectedTab -> {
                onUpdateSelectedTab(event.value)
            }

            is HomeEvent.OnUpdateSelectedTabListrik -> {
                onUpdateSelectedTabListrik(event.value)
            }

            is HomeEvent.OnUpdateSelectedMethod -> {
                onUpdateSelectedMethod(event.value)
            }

            is HomeEvent.OnUpdateNik -> {
                onUpdateNik(event.value)
            }
            is HomeEvent.OnUpdatePin -> {
                onUpdatePin(event.value)
            }

            is HomeEvent.OnUpdateSearch -> {
                onUpdateSearch(event.value)
            }

            is HomeEvent.OnUpdateNamaLengkap -> {
                onUpdateNamaLengkap(event.value)
            }
            is HomeEvent.OnShowDialogPajak -> {
                onShowDialogPajak(event.value)
            }

            is HomeEvent.OnUpdateClearTrigger -> {
                onUpdateClearTrigger(event.value)
            }

            is HomeEvent.OnUpdateTanggalPemeriksaan -> {
                onUpdateTanggalPemeriksaan(event.value)
            }

            is HomeEvent.OnUpdateSelectedVehicle -> {
                onUpdateSelectedVehicle(event.value)
            }
        }
    }

    private fun onUpdateCityCode(value: String) {
        setState { copy(cityCodeValue = value) }
    }
    private fun onUpdateMiddleCode(value: String) {
        setState { copy(middleCodeValue = value) }
    }
    private fun onUpdateLastCode(value: String) {
        setState { copy(lastCodeValue = value) }
    }
    private fun onUpdateNoRangka(value: String) {
        setState { copy(noRangka = value) }
    }
    private fun onUpdateSelectedOption(value: Int) {
        setState { copy(selectedOption = value) }
    }
    private fun onUpdateSelectedTab(value: Int) {
        setState { copy(selectedTab = value) }
    }
    private fun onUpdateTanggalPemeriksaan(value: String) {
        setState { copy(tanggalPemeriksaan = value) }
    }
    private fun onUpdateSelectedTabListrik(value: Int) {
        setState { copy(selectedTabListrik = value) }
    }
    private fun onUpdateSelectedMethod(value: Pair<Int,Int>) {
        setState { copy(selectedMethod = value) }
    }
    private fun onShowDialogPajak(value: UIComponentState) {
        setState { copy(showDialogPajak = value) }
    }
    private fun onUpdatePin(value: String) {
        setState { copy(pin = value) }
    }
    private fun onUpdateNik(value: String) {
        setState { copy(nikValue = value) }
    }
    private fun onUpdateSearch(value: String) {
        setState { copy(searchValue = value) }
    }
    private fun onUpdateNamaLengkap(value: String) {
        setState { copy(namaLengkap = value) }
    }
    private fun onUpdateClearTrigger(value: Boolean){
        setState { copy(clearTrigger = value) }
    }
    private fun onUpdateSelectedVehicle(value: String){
        setState { copy(selectedVehicle = value) }
    }

    private fun updateTokenFCM(token: String) {
        executeUseCase(
            updateDeviceTokenUseCase.execute(
                UpdateDeviceTokenRequestDTO(
                    token = token
                )
            ), onSuccess = { data, status ->
                data?.let {
                    //setState { copy(updateTokenFCM = it) }
                }
            }, onLoading = {
                setState { copy(progressBarState = it) }
            }, onNetworkStatus = {
                setEvent(HomeEvent.OnUpdateNetworkState(it))
            }
        )
    }



    private fun onErrorDialogState(value: UIComponentState) {
        setState {
            copy(errorDialogState = value)
        }
    }



}