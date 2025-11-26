package presentation.ui.main.home.view_model

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.viewModelScope
import business.constants.AUTHORIZATION_BEARER_TOKEN
import business.constants.DataStoreKeys
import business.core.AppDataStore
import business.core.BaseViewModel
import business.core.UIComponentState
import business.datasource.network.main.request.AnswersItem
import business.datasource.network.main.request.CheckQRRequestDTO
import business.datasource.network.main.request.IdentifyRequestDTO
import business.datasource.network.main.request.NegativeAnswerRequestDTO
import business.datasource.network.main.request.PlatKIRRequestDTO
import business.datasource.network.main.request.PreviewBARequestDTO
import business.datasource.network.main.request.RampcheckStartRequestDTO
import business.datasource.network.main.request.SubmitQuestionsRequestDTO
import business.datasource.network.main.request.SubmitSignatureRequestDTO
import business.datasource.network.main.request.UploadPetugasRequestDTO
import business.datasource.network.main.request.VehiclePhotoRequestDTO
import business.datasource.network.main.responses.ItemsItemLoadCard
import business.datasource.network.main.responses.SubcategoryResponse
import business.interactors.main.CheckQRUseCase
import business.interactors.main.GetLocationUseCase
import business.interactors.main.GetVehicleUseCase
import business.interactors.main.IdentifyUseCase
import business.interactors.main.LoadCardUseCase
import business.interactors.main.LogoutUseCase
import business.interactors.main.NegativeAnswerUseCase
import business.interactors.main.PlatKIRUseCase
import business.interactors.main.PreviewBAUseCase
import business.interactors.main.RampcheckStartUseCase
import business.interactors.main.SubmitQuestionUseCase
import business.interactors.main.SubmitSignatureUseCase
import business.interactors.main.UploadPetugasUseCase
import business.interactors.main.VehiclePhotoUseCase
import common.toBase64
import common.toBytes
import kotlinx.coroutines.launch
import presentation.util.BackgroundScheduler

class HomeViewModel(
    private val getLocationUseCase: GetLocationUseCase,
    private val rampcheckStartUseCase: RampcheckStartUseCase,
    private val uploadPetugasUseCase: UploadPetugasUseCase,
    private val checkQRUseCase: CheckQRUseCase,
    private val getVehicleUseCase: GetVehicleUseCase,
    private val platKIRUseCase: PlatKIRUseCase,
    private val vehiclePhotoUseCase: VehiclePhotoUseCase,
    private val appDataStoreManager: AppDataStore,
    private val submitSignatureUseCase: SubmitSignatureUseCase,
    private val previewBAUseCase: PreviewBAUseCase,
    private val negativeAnswerUseCase: NegativeAnswerUseCase,
    private val identifyUseCase: IdentifyUseCase,
    private val submitQuestionUseCase: SubmitQuestionUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val loadCardUseCase: LoadCardUseCase,
    private val scheduler: BackgroundScheduler
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

            is HomeEvent.SetStateValue -> {
                setStateValue()
            }

            is HomeEvent.PreviewBA -> {
                previewBA()
            }

            is HomeEvent.SubmitSignature -> {
                submitSignature()
            }

            is HomeEvent.LoadCard -> {
                loadCard()
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

            is HomeEvent.NegativeAnswerUji -> {
                negativeAnswerKartuUji()
            }

            is HomeEvent.NegativeAnswerKPReguler -> {
                negativeAnswerKPReguler()
            }

            is HomeEvent.NegativeAnswerKPCadangan -> {
                negativeAnswerKPCadangan()
            }

            is HomeEvent.NegativeAnswerSIM -> {
                negativeAnswerSIM()
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

            is HomeEvent.OnShowDialogKartuTidakAda -> {
                onShowDialogKartuTidakAda(event.value)
            }

            is HomeEvent.OnUpdateKeteranganKartuTidakAda -> {
                onUpdateKeteranganKartuTidakAda(event.value)
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

            is HomeEvent.OnUpdateTidakSesuai -> {
                onUpdateTidakSesuai(event.value)
            }

            is HomeEvent.OnUpdateTidakSesuaiBitmap -> {
                onUpdateTidakSesuaiBitmap(event.value)
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

            is HomeEvent.UploadVideo -> {
                startVideoUpload(event.value)
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

            is HomeEvent.OnShowDialogTandaTanganKemenhub -> {
                onShowDialogTandaTanganKemenhub(event.value)
            }

            is HomeEvent.OnShowDialogTandaTanganPengemudi -> {
                onShowDialogTandaTanganPengemudi(event.value)
            }

            is HomeEvent.OnShowDialogTandaTanganPenguji -> {
                onShowDialogTandaTanganPenguji(event.value)
            }

            is HomeEvent.OnUpdateTTDKemenhub -> {
                onUpdateTTDKemenhub(event.value)
            }

            is HomeEvent.OnUpdateTTDPengemudi -> {
                onUpdateTTDPengemudi(event.value)
            }

            is HomeEvent.OnUpdateTTDPenguji -> {
                onUpdateTTDPenguji(event.value)
            }

            is HomeEvent.OnUpdateTTDKemenhubBitmap -> {
                onUpdateTTDKemenhubBitmap(event.value)
            }

            is HomeEvent.OnUpdateTTDPengemudiBitmap -> {
                onUpdateTTDPengemudiBitmap(event.value)
            }

            is HomeEvent.OnUpdateTTDPengujiBitmap -> {
                onUpdateTTDPengujiBitmap(event.value)
            }

            is HomeEvent.OnShowDialogSubmitSignature -> {
                onShowDialogSubmitSignature(event.value)
            }

            is HomeEvent.OnUpdateDriverName -> {
                onUpdateDriverName(event.value)
            }

            is HomeEvent.OnUpdateKemenhubNIP -> {
                onUpdateKemenhubNIP(event.value)
            }

            is HomeEvent.OnUpdateKemenhubName -> {
                onUpdateKemenhubName(event.value)
            }

            is HomeEvent.OnUpdateOfficerNIP -> {
                onUpdateOfficerNIP(event.value)
            }

            is HomeEvent.OnUpdateOfficerName -> {
                onUpdateOfficerName(event.value)
            }

            is HomeEvent.OnUpdateRampcheckId -> {
                onUpdateRampcheckId(event.value)
            }

            is HomeEvent.OnUpdateSelectionKartuUji -> {
                onUpdateSelectionKartuUji(event.value)
            }

            is HomeEvent.OnUpdateTypeCard -> {
                onUpdateTypeCard(event.value)
            }

            is HomeEvent.OnUpdateCardAvailable -> {
                onUpdateAvailableCard(event.value)
            }

            is HomeEvent.IdentifyKartuUji -> {
                identifyKartuUji()
            }

            is HomeEvent.IdentifySIM -> {
                identifySIM()
            }

            is HomeEvent.SubmitQuestion -> {
                submitQuestionKartuUji()
            }

            is HomeEvent.SubmitQuestionSIM -> {
                submitQuestionSIM()
            }
            is HomeEvent.SubmitQuestionTeknisUtama -> {
                submitQuestionTeknisUtama()
            }

            is HomeEvent.SubmitQuestionTeknisPenunjang -> {
                submitQuestionTeknisPenunjang()
            }

            is HomeEvent.ListSubmitQuestionKpReguler -> {
                listsubmitQuestionKpReguler(event.value)
            }

            is HomeEvent.ListSubmitQuestionKpCadangan -> {
                listsubmitQuestionKpCadangan(event.value)
            }

            is HomeEvent.SubmitQuestionKp -> {
                submitQuestionKp(event.value)
            }

            is HomeEvent.OnUpdateKartuUjiBase64 -> {
                onUpdateKartuUjiBase64(event.value)
            }

            is HomeEvent.OnUpdateKartuUjiImageBitmap -> {
                onUpdateKartuUjiBitmap(event.value)
            }

            is HomeEvent.OnUpdateSimPengemudiBase64 -> {
                onUpdateSimPengemudiBase64(event.value)
            }

            is HomeEvent.OnUpdateSimPengemudiImageBitmap -> {
                onUpdateSimPengemudiImageBitmap(event.value)
            }

            is HomeEvent.OnUpdateTidakSesuaiBase64 -> {
                onUpdateTidakSesuaiBase64(event.value)
            }

            is HomeEvent.OnUpdateListSubmitQuestion -> {
                onUpdateListSubmitQuestion(event.value)
            }

            is HomeEvent.OnShowDialogNotMatch -> {
                onShowDialogNotMatch(event.value)
            }

            is HomeEvent.OnSetActiveQuestion -> {
                setState {
                    copy(activeQuestionId = event.questionId)
                }
            }
            is HomeEvent.OnSetActiveQuestionPenunjang -> {
                setState {
                    copy(activeQuestionIdPenunjang = event.questionId)
                }
            }

            is HomeEvent.OnUpdateTidakSesuaiListBitmap -> {
                setState {
                    copy(
                        bitmapTidakSesuaiMap =
                            bitmapTidakSesuaiMap.toMutableMap().apply {
                                put(event.questionId, event.bitmap)
                            }
                    )
                }
            }

            is HomeEvent.OnUpdateTidakSesuaiListBitmapPenunjang -> {
                setState {
                    copy(
                        bitmapTidakSesuaiPenunjangMap =
                            bitmapTidakSesuaiPenunjangMap.toMutableMap().apply {
                                put(event.questionId, event.bitmap)
                            }
                    )
                }
            }



            is HomeEvent.ApplyTeknisResultFromApi -> {

                if (state.value.answers.isNotEmpty()) return

                val answers = event.apiSubcategories.flatMap { sub ->
                    sub.questions.map { q ->
                        AnswersItem(
                            questionId = q.question_id ?: 0,
                            answerId = q.answer_id
                        )
                    }
                }

                val selection = event.apiSubcategories
                    .flatMap { it.questions }
                    .associate { q ->
                        (q.question_id ?: 0) to 0
                    }

                setState {
                    copy(
                        answers = answers,
                        selectionMap = selection
                    )
                }
//                applyTeknisResultFromApi(event.apiSubcategories)
            }

            is HomeEvent.ApplyPenunjangResult -> {

                if (state.value.answersPenunjang.isNotEmpty()) return

                val answersPenunjang = event.apiSubcategories.flatMap { sub ->
                    sub.questions.map { q ->
                        AnswersItem(
                            questionId = q.question_id ?: 0,
                            answerId = q.answer_id
                        )
                    }
                }

                val selection = event.apiSubcategories
                    .flatMap { it.questions }
                    .associate { q ->
                        (q.question_id ?: 0) to 0
                    }

                setState {
                    copy(
                        answersPenunjang = answersPenunjang,
                        selectionMapPenunjang = selection
                    )
                }
//                applyTeknisResultFromApi(event.apiSubcategories)
            }

            is HomeEvent.OnSaveImage -> {
                val updated = state.value.answers.map {
                    if (it.questionId == event.questionId) {
                        it.copy(answerFile = event.base64)
                    } else it
                }

                setState {
                    copy(
                        answers = updated,
                        currentCameraQuestionId = null
                    )
                }
            }

            is HomeEvent.OnSaveImagePenunjang -> {
                val updated = state.value.answersPenunjang.map {
                    if (it.questionId == event.questionId) {
                        it.copy(answerFile = event.base64)
                    } else it
                }

                setState {
                    copy(
                        answersPenunjang = updated,
                        currentCameraQuestionIdPenunjang = null
                    )
                }
            }

            is HomeEvent.Logout -> {
                logout()
            }

            is HomeEvent.OnUpdateKeteranganKPReguler -> {
                onUpdateKeteranganKPReguler(event.value)
            }

            is HomeEvent.OnUpdateKeteranganKPCadangan -> {
                onUpdateKeteranganKPCadangan(event.value)
            }

            is HomeEvent.OnUpdateSelectedOptionKPReguler -> {
                onUpdateSelectedOptionKPReguler(event.id)
            }

            is HomeEvent.OnUpdateSelectedOptionKPCadangan -> {
                onUpdateSelectedOptionKPCadangan(event.id)
            }

            is HomeEvent.OnUpdateImageKPReguler -> {
                onUpdateImageKPReguler(event.bitmap)
            }

            is HomeEvent.OnUpdateImageKPCadangan -> {
                onUpdateImageKPCadangan(event.bitmap)
            }

            is HomeEvent.OnUpdateImageKPRegulerBase64 -> {
                onUpdateImageKPRegulerBase64(event.value)
            }

            is HomeEvent.OnUpdateImageKPCadanganBase64 -> {
                onUpdateImageKPCadanganBase64(event.value)
            }

            is HomeEvent.OnUpdateKpType -> {
                onUpdateKpType(event.value)
            }

            is HomeEvent.OnUpdateSelectionSIM -> {
                onUpdateSelectionSIM(event.value)
            }

            is HomeEvent.OnShowDialogSuccessAdministrasi -> {
                onShowDialogSuccessAdministrasi(event.value)
            }

            is HomeEvent.OnSuccessTeknisUtama -> {
                onSuccessTeknisUtama(event.value)
            }

            is HomeEvent.OnSuccessTeknisPenunjang -> {
                onSuccessTeknisPenunjang(event.value)
            }

            is HomeEvent.OnUpdateSelection -> {
                val newSelection = state.value.selectionMap.toMutableMap()
                newSelection[event.questionId] = event.selection

                val updatedAnswers = state.value.answers.map {
                    if (it.questionId == event.questionId) {
                        it.copy(
                            answerOptionId = event.selection
                        )
                    } else it
                }

                setState {
                    copy(
                        selectionMap = newSelection,
                        answers = updatedAnswers
                    )
                }
            }

            is HomeEvent.OnUpdateCondition -> {
                val updatedAnswers = state.value.answers.map {
                    if (it.questionId == event.questionId) {
                        it.copy(answerCondition = event.value)
                    } else it
                }

                setState { copy(answers = updatedAnswers) }
            }

            is HomeEvent.OnUpdateImage -> {
                val updatedAnswers = state.value.answers.map {
                    if (it.questionId == event.questionId) {
                        it.copy(answerFile = event.base64)
                    } else it
                }

                setState { copy(answers = updatedAnswers) }
            }
            is HomeEvent.OnUpdateSelectionPenunjang -> {
                val newSelection = state.value.selectionMapPenunjang.toMutableMap()
                newSelection[event.questionId] = event.selection

                val updatedAnswers = state.value.answersPenunjang.map {
                    if (it.questionId == event.questionId) {
                        it.copy(
                            answerOptionId = event.selection
                        )
                    } else it
                }

                setState {
                    copy(
                        selectionMapPenunjang = newSelection,
                        answersPenunjang = updatedAnswers
                    )
                }
            }
            is HomeEvent.OnUpdateConditionPenunjang -> {
                val updatedAnswers = state.value.answersPenunjang.map {
                    if (it.questionId == event.questionId) {
                        it.copy(answerCondition = event.value)
                    } else it
                }

                setState { copy(answersPenunjang = updatedAnswers) }
            }


            else -> {}
        }


    }

    private fun updateAnswer(
        list: List<AnswersItem>,
        questionId: Int,
        transform: (AnswersItem) -> AnswersItem
    ): List<AnswersItem> {
        val existing = list.find { it.questionId == questionId }

        return if (existing != null) {
            list.map {
                if (it.questionId == questionId) transform(it) else it
            }
        } else list
    }

    private fun loadCard() {
        executeUseCase(
            loadCardUseCase.execute(
                params = Unit
            ),
            onSuccess = { items, status, code ->
                // items = List<ItemsItemLoadCard>?

                items?.let {
                    setState {
                        copy(
                            listLoadCard = it
                        )
                    }
                }
            },
            onLoading = {
                setState { copy(progressBarState = it) }
            }
        )
    }


    private fun applyTeknisResultFromApi(apiSubcategories: List<SubcategoryResponse>) {
        setState {
            val updatedList = technicalConditions.map { cond ->

                val apiSub = apiSubcategories.firstOrNull { sub ->
                    sub.subcategory_name.equals(cond.section, ignoreCase = true)
                }

                if (apiSub == null) {
                    return@map cond
                }

                val normalizedTitle = cond.title.substringBefore(":").trim()

                val apiQuestion = apiSub.questions.firstOrNull { q ->
                    q.question_name.equals(normalizedTitle, ignoreCase = true)
                }

                if (apiQuestion == null) {
                    return@map cond
                }

                val selectionFromApi = mapAnswerNameToSelection(apiQuestion.answer_name ?: "-")

                cond.copy(
                    selection = selectionFromApi
                )
            }

            copy(technicalConditions = updatedList)
        }
    }

    private fun mapAnswerNameToSelection(answerName: String): Int {
        val lower = answerName.lowercase()

        return when {
            lower.contains("sesuai") ||
                    (lower.contains("ada") && !lower.contains("tidak")) ||
                    (lower.contains("baik") && !lower.contains("tidak")) ||
                    (lower.contains("berfungsi") && !lower.contains("tidak")) ||
                    (lower.contains("laik") && !lower.contains("tidak")) ||
                    lower.contains("berlaku") ||
                    (lower.contains("menyala") && !lower.contains("tidak")) -> 1

            lower.contains("tidak") -> 2

            else -> 0
        }
    }


    private fun onUpdateCityCode(value: String) {
        setState { copy(cityCodeValue = value) }
    }

    private fun onUpdateTidakSesuai(value: String) {
        setState { copy(tidakSesuai = value) }
    }

    private fun onUpdateTidakSesuaiBitmap(value: ImageBitmap) {
        setState { copy(tidakSesuaiBitmap = value) }
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

    private fun negativeAnswerKartuUji() {
        executeUseCase(
            negativeAnswerUseCase.execute(
                params = NegativeAnswerRequestDTO(
                    type = state.value.typeCard,
                    condition = state.value.keteranganKartuTidakAda,
                    step = "4"
                ),
            ),
            onSuccess = { data, status, code ->
                status?.let { s ->
                    if (s) {
                        setState {
                            copy(
                                typeCard = "",
                                keteranganKartuTidakAda = ""
                            )
                        }
                        setAction { HomeAction.Navigation.NavigateToKPReguler }
                    }
                }
            },
            onLoading = {
                setState { copy(progressBarState = it) }
            },
        )
    }

    private fun negativeAnswerKPReguler() {
        executeUseCase(
            negativeAnswerUseCase.execute(
                params = NegativeAnswerRequestDTO(
                    type = state.value.typeCard,
                    condition = state.value.keteranganKartuTidakAda,
                    step = "5"
                ),
            ),
            onSuccess = { data, status, code ->
                status?.let {
                    setState {
                        copy(
                            typeCard = "",
                            keteranganKartuTidakAda = ""
                        )
                    }
                    setAction { HomeAction.Navigation.NavigateToKPCadangan }
                }
            },
            onLoading = {
                setState { copy(progressBarState = it) }
            },
        )
    }

    private fun negativeAnswerKPCadangan() {
        executeUseCase(
            negativeAnswerUseCase.execute(
                params = NegativeAnswerRequestDTO(
                    type = state.value.typeCard,
                    condition = state.value.keteranganKartuTidakAda,
                    step = "7"
                ),
            ),
            onSuccess = { data, status, code ->
                status?.let {
                    setState {
                        copy(
                            typeCard = "",
                            keteranganKartuTidakAda = ""
                        )
                    }
                    setAction { HomeAction.Navigation.NavigateToSIMPengemudi }
                }
            },
            onLoading = {
                setState { copy(progressBarState = it) }
            },
        )
    }

    private fun negativeAnswerSIM() {
        executeUseCase(
            negativeAnswerUseCase.execute(
                params = NegativeAnswerRequestDTO(
                    type = state.value.typeCard,
                    condition = state.value.keteranganKartuTidakAda,
                    step = "6"
                ),
            ),
            onSuccess = { data, status, code ->
                status?.let {
                    setState {
                        copy(
                            typeCard = "",
                            keteranganKartuTidakAda = ""
                        )
                    }
                    setAction { HomeAction.Navigation.NavigateToTeknisUtama }
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
                    if (s) {
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

    private fun setStateValue() {
        viewModelScope.launch {
            val officerName = appDataStoreManager.readValue(DataStoreKeys.OFFICER_NAME)
            val officerNIP = appDataStoreManager.readValue(DataStoreKeys.OFFICER_NIP)
            setState {
                copy(officerName = officerName ?: "")
            }
            setState {
                copy(officerNip = officerNIP ?: "")
            }

        }
    }

    private fun submitSignature() {

        executeUseCase(
            submitSignatureUseCase.execute(

                params = SubmitSignatureRequestDTO(
                    rampcheckOfficerNip = state.value.nipKemenhub,
                    rampcheckOfficerName = state.value.officerName,
                    rampcheckOfficerSignature = state.value.ttdPenguji,
                    driverName = state.value.driverName,
                    driverSignature = state.value.ttdPengemudi,
                    kemenhubNip = state.value.nipKemenhub,
                    kemenhubName = state.value.kemenhubName,
                    kemenhubSignature = state.value.ttdKemenhub,
                    step = "9"
                ),
            ),
            onSuccess = { data, status, code ->
                status?.let { s ->
                    if (s) {
                        data.let {
                            setState {
                                copy(
                                    showDialogSuccessSubmitSignature = UIComponentState.Show,
                                    rampcheckId = data?.rampcheckId!!.toInt()
                                )
                            }
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
                    if (s) {
                        data?.let { dataHasil ->
                            if (dataHasil.match == true) {
                                setState {
                                    copy(
                                        dataHasilEKIR = dataHasil,
                                    )
                                }
                                setAction {
                                    HomeAction.Navigation.NavigateToResultScreen
                                }
                            } else {
                                setState { copy(showDialogNotMatch = UIComponentState.Show) }
                            }

                        }

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

    private fun identifyKartuUji() {
        executeUseCase(
            identifyUseCase.execute(
                params = IdentifyRequestDTO(
                    questionName = "kartu uji/stuk",
                    imageBase64 = state.value.kartuUjiBase64,
                    step = "4"
                )
            ),
            onSuccess = { data, status, code ->
                status?.let { s ->
                    if (s) {
                        data?.let { listIdentify ->
                            val identifyDTOItem = listIdentify[0]
                            setState {
                                copy(
                                    listIdentifyKartuUji = listIdentify,
                                    identifyKartuUji = identifyDTOItem
                                )
                            }
                        }
                        setAction { HomeAction.Navigation.NavigateHasilKartuUji }
                    } else {
                        setAction { HomeAction.Navigation.NavigateToBack }
                    }

                }

            },
            onLoading = {
                setState { copy(progressBarState = it) }
            },
        )
    }

    private fun identifySIM() {
        executeUseCase(
            identifyUseCase.execute(
                params = IdentifyRequestDTO(
                    questionName = "sim pengemudi",
                    imageBase64 = state.value.simPengemudiBase64,
                    step = "6"
                )
            ),
            onSuccess = { data, status, code ->
                status?.let { s ->
                    if (s) {
                        data?.let { listIdentify ->
                            val identifyDTOItem = listIdentify[0]
                            setState {
                                copy(
                                    listIdentifySIM = listIdentify,
                                    identifySIM = identifyDTOItem
                                )
                            }
                        }
                        setAction { HomeAction.Navigation.NavigateHasilSIMPengemudi }
                    } else {
                        setAction { HomeAction.Navigation.NavigateToBack }
                    }

                }
            },
            onLoading = {
                setState { copy(progressBarState = it) }
            },
        )
    }

    private fun submitQuestionKartuUji() {
        executeUseCase(
            submitQuestionUseCase.execute(
                params = SubmitQuestionsRequestDTO(
                    step = "4",
                    answers = state.value.listSubmitQuestion,
                )
            ),
            onSuccess = { data, status, code ->
                status?.let { s ->
                    if (s) {
                        setState {
                            copy(
                                listSubmitQuestion = listOf(),
                                tidakSesuai = "",
                                tidakSesuaiBitmap = null,
                                tidakSesuaiBase64 = ""
                            )
                        }
                        setAction { HomeAction.Navigation.NavigateToKPReguler }
                    }

                }
            },
            onLoading = {
                setState { copy(progressBarState = it) }
            },
        )
    }

    private fun submitQuestionSIM() {
        executeUseCase(
            submitQuestionUseCase.execute(
                params = SubmitQuestionsRequestDTO(
                    step = "6",
                    answers = state.value.listSubmitQuestion,
                )
            ),
            onSuccess = { data, status, code ->
                status?.let { s ->
                    if (s) {
                        setState {
                            copy(
                                listSubmitQuestion = listOf(),
                                showDialogSuccessAdministrasi = UIComponentState.Show,
                                tidakSesuai = "",
                                tidakSesuaiBitmap = null,
                                tidakSesuaiBase64 = ""
                            )
                        }
                    }
                }
            },
            onLoading = {
                setState { copy(progressBarState = it) }
            },
        )
    }

    private fun submitQuestionTeknisUtama() {
        executeUseCase(
            submitQuestionUseCase.execute(
                params = SubmitQuestionsRequestDTO(
                    step = "7",
                    answers = state.value.answers,
                )
            ),
            onSuccess = { data, status, code ->
                status?.let { s ->
                    if (s) {
                        setState {
                            copy(
                                answers = listOf(),
                                successTeknisUtama = UIComponentState.Show
                            )
                        }
//                        setAction { HomeAction.Navigation.NavigateToBeritaAcara }
                    }
                }
            },
            onLoading = {
                setState { copy(progressBarState = it) }
            },
        )
    }
    private fun submitQuestionTeknisPenunjang() {
        executeUseCase(
            submitQuestionUseCase.execute(
                params = SubmitQuestionsRequestDTO(
                    step = "8",
                    answers = state.value.answersPenunjang,
                )
            ),
            onSuccess = { data, status, code ->
                status?.let { s ->
                    if (s) {
                        setState {
                            copy(
                                answersPenunjang = listOf(),
                                successTeknisPenunjang = UIComponentState.Show
                            )
                        }
//                        setAction { HomeAction.Navigation.NavigateToBeritaAcara }
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
                    nrkbImage = state.value.nrkbImage?.toBytes()?.toBase64(),
                    step = "3"
                )
            ),
            onSuccess = { data, status, code ->
                status?.let { s ->
                    if (s) {
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
                    officerImage = state.value.officer_image_bitmap?.toBytes()?.toBase64(),
                    step = "1"
                )
            ),
            onSuccess = { data, status, code ->
                status?.let { s ->
                    if (s) {
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
                    kirImage = state.value.kirImage?.toBytes()?.toBase64(),
                    step = "2"
                )
            ),
            onSuccess = { data, status, code ->
                status?.let { s ->
                    if (s) {
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

    private fun previewBA() {
        executeUseCase(
            previewBAUseCase.execute(
                params = PreviewBARequestDTO(
                    rampcheckId = state.value.rampcheckId
                )
            ),
            onSuccess = { data, status, code ->
                data?.let {
                    setState {
                        copy(urlPreviewBA = data.file_url ?: "")
                    }
                }
            },
            onLoading = {
                setState { copy(progressBarState = it) }
            },
        )
    }

    private fun logout() {
        executeUseCase(logoutUseCase.execute(Unit), onSuccess = { data, status, code ->
            data?.let {
                setAction { HomeAction.Navigation.Logout }
            }
        }, onLoading = {
            setState { copy(progressBarState = it) }
        })
    }

    private fun onUpdateLastCode(value: String) {
        setState { copy(lastCodeValue = value) }
    }

    private fun onShowDialogKartuTidakAda(value: UIComponentState) {
        setState { copy(showDialogKartuTidakAda = value) }
    }

    private fun onUpdateKeteranganKartuTidakAda(value: String) {
        setState { copy(keteranganKartuTidakAda = value) }
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

    private fun onUpdateSelectionSIM(value: Int) {
        setState { copy(selectionSIM = value) }
    }

    private fun onShowDialogSuccessAdministrasi(value: UIComponentState) {
        setState { copy(showDialogSuccessAdministrasi = value) }
    }

    private fun onSuccessTeknisUtama(value: UIComponentState) {
        setState { copy(successTeknisUtama = value) }
    }

    private fun onSuccessTeknisPenunjang(value: UIComponentState) {
        setState { copy(successTeknisPenunjang = value) }
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

    private fun onUpdateSelectedMethod(value: Pair<Int, Int>) {
        setState { copy(selectedMethod = value) }
    }

    private fun onShowDialogPajak(value: UIComponentState) {
        setState { copy(showDialogPajak = value) }
    }

    private fun onUpdatePin(value: String) {
        setState { copy(pin = value) }
    }

    private fun onUpdateKartuUjiBase64(value: String) {
        setState { copy(kartuUjiBase64 = value) }
    }

    private fun onUpdateKartuUjiBitmap(value: ImageBitmap) {
        setState { copy(kartuUjiBitmap = value) }
    }

    private fun onUpdateSimPengemudiBase64(value: String) {
        setState { copy(simPengemudiBase64 = value) }
    }


    private fun onUpdateTidakSesuaiBase64(value: String) {
        setState { copy(tidakSesuaiBase64 = value) }
    }

    private fun onUpdateListSubmitQuestion(value: List<AnswersItem>) {
        setState { copy(listSubmitQuestion = value) }
    }

    private fun onUpdateSimPengemudiImageBitmap(value: ImageBitmap) {
        setState { copy(simPengemudiBitmap = value) }
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

    private fun onUpdateClearTrigger(value: Boolean) {
        setState { copy(clearTrigger = value) }
    }

    private fun onUpdateSelectedPlatNumber(value: String) {
        setState { copy(selectedPlatNumber = value) }
    }

    private fun onShowDialogDatePicker(value: UIComponentState) {
        setState { copy(showDialogDatePicker = value) }
    }

    private fun onShowDialogNotMatch(value: UIComponentState) {
        setState { copy(showDialogNotMatch = value) }
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

    private fun onUpdateImageTypes(value: String) {
        setState { copy(imageTypes = value) }
    }

    private fun onShowDialogTandaTanganPengemudi(value: UIComponentState) {
        setState { copy(showDialogTandaTanganPengemudi = value) }
    }

    private fun onShowDialogTandaTanganPenguji(value: UIComponentState) {
        setState { copy(showDialogTandaTanganPenguji = value) }
    }

    private fun onShowDialogTandaTanganKemenhub(value: UIComponentState) {
        setState { copy(showDialogTandaTanganKemenhub = value) }
    }

    private fun onUpdateTTDPenguji(value: String) {
        setState { copy(ttdPenguji = value) }
    }

    private fun onUpdateTTDPengemudi(value: String) {
        setState { copy(ttdPengemudi = value) }
    }

    private fun onUpdateTTDKemenhub(value: String) {
        setState { copy(ttdKemenhub = value) }
    }

    private fun onUpdateTTDPengujiBitmap(value: ImageBitmap) {
        setState { copy(ttdPengujiBitmap = value) }
    }

    private fun onUpdateTTDPengemudiBitmap(value: ImageBitmap) {
        setState { copy(ttdPengemudiBitmap = value) }
    }

    private fun onUpdateTTDKemenhubBitmap(value: ImageBitmap) {
        setState { copy(ttdKemenhubBitmap = value) }
    }

    private fun onUpdateDriverName(value: String) {
        setState { copy(driverName = value) }
    }

    private fun onUpdateKemenhubName(value: String) {
        setState { copy(kemenhubName = value) }
    }

    private fun onUpdateKemenhubNIP(value: String) {
        setState { copy(nipKemenhub = value) }
    }

    private fun onUpdateOfficerName(value: String) {
        setState { copy(officerName = value) }
    }

    private fun onUpdateOfficerNIP(value: String) {
        setState { copy(officerNip = value) }
    }

    private fun onUpdateRampcheckId(value: Int) {
        setState { copy(rampcheckId = value) }
    }

    private fun onShowDialogSubmitSignature(value: UIComponentState) {
        setState { copy(showDialogSuccessSubmitSignature = value) }
    }

    private fun onUpdateSelectionKartuUji(value: Int) {
        setState { copy(selectionKartuUji = value) }
    }

    private fun onUpdateFilePath(value: String) {
        setState { copy(filePath = value) }
    }

    private fun onUpdateOfficerByteArray(value: ByteArray) {
        setState { copy(officer_image = value) }
    }

    private fun onUpdateTypeCard(value: String) {
        setState { copy(typeCard = value) }
    }

    private fun onUpdateAvailableCard(value: Int) {
        setState { copy(availableCard = value) }
    }

    fun startVideoUpload(filePath: String) = viewModelScope.launch {

        val token = appDataStoreManager.readValue(AUTHORIZATION_BEARER_TOKEN) ?: run {

            return@launch
        }


        scheduler.enqueueVideoUpload(filePath, token)

        // 3. Update UI (Opsional)
        // setState { copy(status = "Video sedang diunggah di latar belakang") }
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

    private fun onUpdateSelectedOptionKPReguler(id: Int) {
        setState { copy(selectedOptionKPReguler = id) }
    }

    private fun onUpdateSelectedOptionKPCadangan(id: Int) {
        setState { copy(selectedOptionKPCadangan = id) }
    }

    private fun onUpdateKeteranganKPReguler(value: String) {
        setState { copy(keteranganKPReguler = value) }
    }

    private fun onUpdateKeteranganKPCadangan(value: String) {
        setState { copy(keteranganKPCadangan = value) }
    }

    private fun onUpdateImageKPReguler(bitmap: ImageBitmap) {
        setState { copy(imageKPReguler = bitmap) }
    }

    private fun onUpdateImageKPCadangan(bitmap: ImageBitmap) {
        setState { copy(imageKPCadangan = bitmap) }
    }

    private fun onUpdateImageKPRegulerBase64(value: String) {
        setState { copy(imageKPRegulerBase64 = value) }
    }

    private fun onUpdateImageKPCadanganBase64(value: String) {
        setState { copy(imageKPCadanganBase64 = value) }
    }

    private fun onUpdateKpType(value: Int) {
        setState { copy(kpType = value) }
    }

    private fun listsubmitQuestionKpReguler(value: AnswersItem) {
        setState { copy(listSubmitQuestionKPReguler = value) }
    }

    private fun listsubmitQuestionKpCadangan(value: AnswersItem) {
        setState { copy(listSubmitQuestionKPCadangan = value) }
    }

    private fun submitQuestionKp(listAnswer: List<AnswersItem>) {
        executeUseCase(
            submitQuestionUseCase.execute(
                params = SubmitQuestionsRequestDTO(
                    step = "5",
                    answers = listAnswer
                )
            ),
            onSuccess = { data, status, code ->
                if (status == true) {
                    setState {
                        copy(
                            submitQuestionKp = emptyList() // Clear setelah submit
                        )
                    }
                    setAction { HomeAction.Navigation.NavigateToSIMPengemudi }
                }
            },
            onLoading = {
                setState { copy(progressBarState = it) }
            },
        )
    }


}