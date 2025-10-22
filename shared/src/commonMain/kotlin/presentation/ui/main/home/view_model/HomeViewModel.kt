package presentation.ui.main.home.view_model

import androidx.compose.ui.graphics.ImageBitmap
import business.core.BaseViewModel
import business.core.ProgressBarState
import business.core.UIComponentState
import business.datasource.network.main.request.CheckQRRequestDTO
import business.datasource.network.main.request.PlatKIRRequestDTO
import business.datasource.network.main.request.RampcheckStartRequestDTO
import business.datasource.network.main.request.UploadPetugasRequestDTO
import business.datasource.network.main.request.VehiclePhotoRequestDTO
import business.datasource.network.main.responses.CheckQRDTO
import business.datasource.network.main.responses.GetLocationDTO
import business.interactors.main.CheckQRUseCase
import business.interactors.main.GetLocationUseCase
import business.interactors.main.GetProfileUseCase
import business.interactors.main.GetVehicleUseCase
import business.interactors.main.PlatKIRUseCase
import business.interactors.main.RampcheckStartUseCase
import business.interactors.main.UploadPetugasUseCase
import business.interactors.main.VehiclePhotoUseCase
import business.interactors.splash.CheckTokenUseCase
import business.interactors.splash.LoginUseCase
import coil3.Bitmap
import common.ImageSaveShare
import common.toBase64
import common.toBytes
import presentation.ui.main.auth.view_model.LoginAction

class HomeViewModel(
    private val getLocationUseCase: GetLocationUseCase,
    private val rampcheckStartUseCase: RampcheckStartUseCase,
    private val uploadPetugasUseCase: UploadPetugasUseCase,
    private val checkQRUseCase: CheckQRUseCase,
    private val getVehicleUseCase: GetVehicleUseCase,
    private val platKIRUseCase: PlatKIRUseCase,
    private val vehiclePhotoUseCase: VehiclePhotoUseCase
) : BaseViewModel<HomeEvent, HomeState, HomeAction>() {

    override fun setInitialState() = HomeState()

    override fun onTriggerEvent(event: HomeEvent) {
        when (event) {

            is HomeEvent.GetHomeContent -> {
//                getHome()
            }
            is HomeEvent.RampcheckStart -> {
                rampcheckStart()
            }
            is HomeEvent.UploadOfficerImage -> {
                uploadFotoPetugas()
            }
            is HomeEvent.CheckQR -> {
                checkQR()
            }

            is HomeEvent.GetVehicle -> {
                getVehicle()
            }

            is HomeEvent.PlatKIR -> {
                platKIR()
            }

            is HomeEvent.VehiclePhoto -> {
                vehiclePhoto()
            }

            is HomeEvent.OnUpdateCityCode -> {
                onUpdateCityCode(event.value)
            }

            is HomeEvent.OnValidateField -> {

            }
            is HomeEvent.OnUpdateQrUrl -> {
                onUpdateQrUrl(event.value)
            }

            is HomeEvent.GetLocation -> {
                getLocation()
            }

            is HomeEvent.OnUpdateTokenFCM -> {
//                updateTokenFCM(event.value)
            }

            is HomeEvent.OnUpdateNetworkState -> {

            }

            is HomeEvent.OnUpdateLastCode -> {
                onUpdateLastCode(event.value)
            }
            is HomeEvent.OnUpdateMiddleCode -> {
                onUpdateMiddleCode(event.value)
            }
            is HomeEvent.OnUpdateConditionSelection -> {
                onUpdateConditionSelection(event.cardId, event.selection)
            }
            is HomeEvent.OnUpdateListLocation -> {
//                onUpdateListLocation(event.value)
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

            is HomeEvent.OnUpdateLatitude -> {
                onUpdateLatitude(event.value)
            }

            is HomeEvent.OnUpdateLongitude -> {
                onUpdateLongitude(event.value)
            }

            is HomeEvent.OnLocationTrigger -> {
                onLocationTrigger(event.value)
            }

            is HomeEvent.OnUpdateStatusMessage -> {
                onUpdateStatusMessage(event.value)
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
            is HomeEvent.OnShowDialogDatePicker -> {
                onShowDialogDatePicker(event.value)
            }

            is HomeEvent.OnUpdateTanggalPemeriksaan -> {
                onUpdateTanggalPemeriksaan(event.value)
            }

            is HomeEvent.OnUpdateLocation -> {
                onUpdateLocation(event.value)
            }
            is HomeEvent.OnUpdateLocationId -> {
                onUpdateLocationId(event.value)
            }

            is HomeEvent.OnUpdateVehiclePlatNumber -> {
                onUpdateSelectedPlatNumber(event.value)
            }


            is HomeEvent.OnShowDialogLocation -> {
                onShowDialogLocation(event.value)
            }

            is HomeEvent.OnUpdateOfficerImage -> {
                onUpdateOfficerByteArray(event.value)
            }
            is HomeEvent.OnUpdateOfficerImageImageBitmap -> {
                onUpdateOfficerImageBitmap(event.value)
            }

            is HomeEvent.OnShowDropdownVehiclePicker -> {
                onShowDropdownVehicle(event.value)
            }

            is HomeEvent.OnUpdateKIRImageBitmap -> {
                onUpdateKIRImageBitmap(event.value)
            }

            is HomeEvent.OnUpdateBackImageBitmap -> {
                onUpdateBackImageBitmap(event.value)
            }
            is HomeEvent.OnUpdateFrontImageBitmap -> {
                onUpdateFrontImageBitmap(event.value)
            }
            is HomeEvent.OnUpdateImageTypes -> {
                onUpdateImageTypes(event.value)
            }
            is HomeEvent.OnUpdateLeftImageBitmap -> {
                onUpdateLeftImageBitmap(event.value)
            }
            is HomeEvent.OnUpdateNrkbImageBitmap -> {
                onUpdateNrkbImageBitmap(event.value)
            }
            is HomeEvent.OnUpdateRightImageBitmap -> {
                onUpdateRightImageBitmap(event.value)
            }
        }
    }

    private fun onUpdateCityCode(value: String) {
        setState { copy(cityCodeValue = value) }
    }
    private fun onUpdateMiddleCode(value: String) {
        setState { copy(middleCodeValue = value) }
    }
    private fun onUpdateConditionSelection(cardId: String, selection: Int) {
        setState {
            val currentList = technicalConditions

            val updatedList = currentList.map { item ->
                if (item.id == cardId) {
                    item.copy(selection = selection)
                } else {
                    item
                }
            }
            copy(technicalConditions = updatedList)
        }
    }
    private fun getLocation() {
        executeUseCase(
            getLocationUseCase.execute(
                params = Unit,
            ),
            onSuccess = { data, status, code ->
                data?.let { locationList ->
                    setState {
                        copy(
                            selectedLocationList = locationList,
                        )
                    }

                }
            },
            onLoading = {
                setState { copy(progressBarState = it) }
            },
        )
    }
    private fun rampcheckStart() {
        executeUseCase(
            rampcheckStartUseCase.execute(
                params = RampcheckStartRequestDTO(
                    inspectionDate = state.value.tanggalPemeriksaan,
                    rampcheckLocationId = state.value.locationId,
                    latitude = state.value.latitude,
                    longitude = state.value.longitude
                ),
            ),
            onSuccess = { data, status, code ->
                status?.let { s ->
                    if(s){
                        setAction {
                            HomeAction.Navigation.NavigateToGuide
                        }
                    }
                }
            },
            onLoading = {
                setState { copy(progressBarState = it) }
            },
        )
    }

    private fun checkQR() {
        executeUseCase(
            checkQRUseCase.execute(
                params = CheckQRRequestDTO(
                    qrUrl = state.value.qrUrl
                ),
            ),
            onSuccess = { data, status, code ->
                status?.let { s ->
                    if(s){
                        setAction {
                            HomeAction.Navigation.NavigateToResultScreen
                        }
                    }
                }
                data?.let { dataHasil ->
                    setState {
                        copy(
                            dataHasilEKIR = dataHasil,
                        )
                    }

                }
            },
            onLoading = {
                setState { copy(progressBarState = it) }
            },
        )
    }

    private fun getVehicle() {
        executeUseCase(
            getVehicleUseCase.execute(
                params = Unit
            ),
            onSuccess = { data, status, code ->
                data?.let { listVehicle ->
                    setState {
                        copy(
                            listVehicle = listVehicle,
                        )
                    }

                }
            },
            onLoading = {
                setState { copy(progressBarState = it) }
            },
        )
    }

    private fun vehiclePhoto() {
        executeUseCase(
            vehiclePhotoUseCase.execute(
                params = VehiclePhotoRequestDTO(
                    frontImage = state.value.frontImage?.toBytes()?.toBase64(),
                    backImage = state.value.backImage?.toBytes()?.toBase64(),
                    rightImage = state.value.rightImage?.toBytes()?.toBase64(),
                    leftImage = state.value.leftImage?.toBytes()?.toBase64(),
                    nrkbImage = state.value.nrkbImage?.toBytes()?.toBase64()
                )
            ),
            onSuccess = { data, status, code ->
                status?.let {s ->
                    if (s){
                        setAction {
                            HomeAction.Navigation.NavigateToPemeriksaanAdministrasi
                        }
                    }
                }
            },
            onLoading = {
                setState { copy(progressBarState = it) }
            },
        )
    }

    private fun uploadFotoPetugas() {
        executeUseCase(
            uploadPetugasUseCase.execute(
                params = UploadPetugasRequestDTO(
                    officerImage = state.value.officer_image_bitmap?.toBytes()?.toBase64()
                )
            ),
            onSuccess = { data, status, code ->
                status?.let { s ->
                    if(s){
                        setAction {
                            HomeAction.Navigation.NavigateToKIR
                        }
                    }
                }
            },
            onLoading = {
                setState { copy(progressBarState = it) }
            },
        )
    }

    private fun platKIR() {
        executeUseCase(
            platKIRUseCase.execute(
                params = PlatKIRRequestDTO(
                    platNumber = state.value.selectedPlatNumber,
                    kirImage = state.value.kirImage?.toBytes()?.toBase64()
                )
            ),
            onSuccess = { data, status, code ->
                status?.let { s ->
                    if(s){
                        setAction {
                            HomeAction.Navigation.NavigateToQRKIR
                        }
                    }
                }
            },
            onLoading = {
                setState { copy(progressBarState = it) }
            },
        )
    }
    private fun onUpdateLastCode(value: String) {
        setState { copy(lastCodeValue = value) }
    }

    private fun onUpdateQrUrl(value: String) {
        setState { copy(qrUrl = value) }
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
    private fun onUpdateLocation(value: String) {
        setState { copy(location = value) }
    }
    private fun onUpdateLocationId(value: String) {
        setState { copy(locationId = value) }
    }
    private fun onUpdateLatitude(value: String) {
        setState { copy(latitude = value) }
    }
    private fun onUpdateLongitude(value: String) {
        setState { copy(longitude = value) }
    }
    private fun onLocationTrigger(value: Boolean) {
        setState { copy(locationTrigger = value) }
    }
    private fun onUpdateStatusMessage(value: String) {
        setState { copy(statusMessage = value) }
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
    private fun onUpdateSelectedPlatNumber(value: String){
        setState { copy(selectedPlatNumber = value) }
    }

    private fun onShowDialogDatePicker(value: UIComponentState) {
        setState { copy(showDialogDatePicker = value) }
    }

    private fun onShowDialogLocation(value: UIComponentState) {
        setState { copy(showDialogLocation = value) }
    }

    private fun onShowDropdownVehicle(value: UIComponentState) {
        setState { copy(showDropdownVehicle = value) }
    }

    private fun onUpdateOfficerImageBitmap(value: ImageBitmap) {
        setState { copy(officer_image_bitmap = value) }
    }

    private fun onUpdateKIRImageBitmap(value: ImageBitmap) {
        setState { copy(kirImage = value) }
    }

    private fun onUpdateFrontImageBitmap(value: ImageBitmap) {
        setState { copy(frontImage = value) }
    }

    private fun onUpdateBackImageBitmap(value: ImageBitmap) {
        setState { copy(backImage = value) }
    }
    private fun onUpdateLeftImageBitmap(value: ImageBitmap) {
        setState { copy(leftImage = value) }
    }
    private fun onUpdateRightImageBitmap(value: ImageBitmap) {
        setState { copy(rightImage = value) }
    }
    private fun onUpdateNrkbImageBitmap(value: ImageBitmap) {
        setState { copy(nrkbImage = value) }
    }

    private fun onUpdateImageTypes(value: String){
        setState { copy(imageTypes = value) }
    }

    private fun onUpdateOfficerByteArray(value: ByteArray) {
        setState { copy(officer_image = value) }
    }

//    private fun updateTokenFCM(token: String) {
//        executeUseCase(
//            updateDeviceTokenUseCase.execute(
//                UpdateDeviceTokenRequestDTO(
//                    token = token
//                )
//            ), onSuccess = { data, status ->
//                data?.let {
//                    //setState { copy(updateTokenFCM = it) }
//                }
//            }, onLoading = {
//                setState { copy(progressBarState = it) }
//            }, onNetworkStatus = {
//                setEvent(HomeEvent.OnUpdateNetworkState(it))
//            }
//        )
//    }



    private fun onErrorDialogState(value: UIComponentState) {
        setState {
            copy(errorDialogState = value)
        }
    }



}