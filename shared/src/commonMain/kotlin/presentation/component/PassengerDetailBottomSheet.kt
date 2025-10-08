package presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import business.core.UIComponentState
import business.datasource.network.main.request.DataPassengerCO
import business.datasource.network.main.responses.City
import business.datasource.network.main.responses.Gender
import business.datasource.network.main.responses.IdentityType
import presentation.ui.main.home.view_model.HomeEvent
import presentation.ui.main.home.view_model.HomeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PassengerDetailBottomSheet(
    id: Int,
    index: Int,
    state: HomeState,
    events: (HomeEvent) -> Unit,
    onSelected: (DataPassengerCO) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Consolidated validation state
    val validationState = remember { ValidationState() }

    val tempPass = remember(state.selectedPassengerCO, id) {
        state.selectedPassengerCO.find { id == it.passengerCategoryId }
    }

    val isAdultPassenger = tempPass?.passengerCategoryId == 4

    // Pre-computed values
    val tempData = remember(tempPass, index) {
        tempPass?.data?.getOrNull(index)
    }

    val tempGender = remember(state.home.gender, tempData) {
        state.home.gender.find { it.genderId == tempData?.genderId }
    }

    val tempCity = remember(state.home.city, tempData) {
        state.home.city.find { it.cityId == tempData?.cityId }
    }

    val tempIdentityType = remember(state.home.identityType, tempData) {
        state.home.identityType.find { it.identityTypeId == tempData?.identityTypeId }
    }

    // Validation logic
    val validator = remember { PassengerValidator() }

    // Single LaunchedEffect for all validations
    LaunchedEffect(
        state.selectedName,
        state.selectedAge,
        state.selectedCity,
        state.selectedBirthday,
        state.selectedGender,
        state.selectedIdentity,
        state.selectedIdentityType,
        isAdultPassenger
    ) {
        validationState.updateValidation(
            validator.validateAll(
                name = state.selectedName,
                age = state.selectedAge,
                city = state.selectedCity,
                birthday = state.selectedBirthday,
                gender = state.selectedGender,
                identity = state.selectedIdentity,
                identityType = state.selectedIdentityType,
                isAdultPassenger = isAdultPassenger
            )
        )
    }

    // Initialize form data once
    LaunchedEffect(tempData) {
        tempData?.let { data ->

        }
    }

    ModalBottomSheet(
        onDismissRequest = { },
        containerColor = MaterialTheme.colorScheme.background,
        sheetState = sheetState,
        dragHandle = null,
    ) {
        PassengerDetailContent(
            state = state,
            events = events,
            validationState = validationState,
            isAdultPassenger = isAdultPassenger,
            onSave = { onSelected(createPassengerData(state)) }
        )
    }
}

// Extracted content composable
@Composable
private fun PassengerDetailContent(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    validationState: ValidationState,
    isAdultPassenger: Boolean,
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        PassengerDetailHeader()

        Spacer_16dp()

        // Gender Selection
        RadioButtonGender(state, events, if (isAdultPassenger) 4 else 1)

        Spacer_24dp()

        // Name Field
        PassengerNameField(
            value = state.selectedName,
            onValueChange = {  },
            isAdultPassenger = isAdultPassenger,
            validationState = validationState
        )

        Spacer_16dp()

        // Age or Birthday Field
        if (isAdultPassenger) {
            PassengerAgeField(
                value = state.selectedAge,
                onValueChange = { },
                validationState = validationState
            )
        } else {
            PassengerBirthdayField(
                value = state.selectedBirthday,
                onDatePickerClick = { },
                validationState = validationState
            )
        }

        Spacer_16dp()

        // City Field
        PassengerCityField(
            selectedCity = state.selectedCity,
            onCityClick = { }
        )

        // Identity Section (Adult only)
        if (isAdultPassenger) {
            Spacer_16dp()
            PassengerIdentitySection(
                state = state,
                events = events,
                validationState = validationState
            )
        }

        Spacer_24dp()

        // Save Button
        DefaultButton(
            text = "Simpan",
            enabled = validationState.isFormValid,
            onClick = onSave,
            modifier = Modifier.fillMaxWidth().size(DEFAULT__BUTTON_SIZE)
        )

        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun RadioButtonGender(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    typePassenger: Int = 0
) {
    val genders = remember { mutableStateListOf<Gender>() }
    LaunchedEffect(typePassenger) {
        if (typePassenger == 4) {
            val genders1 = listOf(
                Gender(
                    genderId = 1,
                    genderName = "Tuan"
                ),
                Gender(
                    genderId = 2,
                    genderName = "Nyonya"
                ),
                Gender(
                    genderId = 3,
                    genderName = "Nona"
                )
            )
            genders.addAll(genders1)
        } else {
            val genders2 = listOf(
                Gender(
                    genderId = 1,
                    genderName = "Tuan"
                ),
                Gender(
                    genderId = 3,
                    genderName = "Nona"
                )
            )
            genders.addAll(genders2)
        }
    }

    val selectedId = state.selectedGender.genderId ?: 1

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        genders.forEachIndexed { index, g ->
            val interactionSource = remember { MutableInteractionSource() }

            Row(
                Modifier.padding(end = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                    RadioButton(
                        modifier = Modifier.padding(horizontal = 0.dp),
                        selected = (g.genderId == selectedId),
                        onClick = {

                        },
                        interactionSource = interactionSource
                    )
                }
                Spacer_8dp()
                Text(
                    text = g.genderName.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
private fun PassengerDetailHeader() {
    Box(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Box(
            Modifier
                .align(Alignment.TopCenter)
                .width(36.dp)
                .height(4.dp)
                .background(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    shape = RoundedCornerShape(2.dp)
                )
        )
    }

    Spacer_16dp()

    Text(
        "Detail Penumpang",
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
    )

    Spacer(Modifier.height(12.dp))

    Text(
        "Info Penumpang",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
    )
}

@Composable
private fun PassengerNameField(
    value: String,
    onValueChange: (String) -> Unit,
    isAdultPassenger: Boolean,
    validationState: ValidationState
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                if (isAdultPassenger) "Nama Lengkap Sesuai KTP/Paspor"
                else "Nama Lengkap"
            )
        },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        ),
        singleLine = true,
        isError = !validationState.isNameValid && validationState.nameError != null,
    )

    AnimatedVisibility(visible = validationState.nameError != null) {
        Text(
            validationState.nameError ?: "",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun PassengerAgeField(
    value: String,
    onValueChange: (String) -> Unit,
    validationState: ValidationState
) {
    OutlinedTextField(
        value = if (value == "0") "" else value,
        onValueChange = { input ->
            val filtered = input.filter { it.isDigit() }
            onValueChange(filtered)
        },
        label = { Text("Usia") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        isError = !validationState.isAgeValid && validationState.ageError != null,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        )
    )

    AnimatedVisibility(visible = validationState.ageError != null) {
        Text(
            validationState.ageError ?: "",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun PassengerBirthdayField(
    value: String,
    onDatePickerClick: () -> Unit,
    validationState: ValidationState
) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        label = { Text("Tanggal Lahir") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        readOnly = true,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = null
            )
        },
        isError = !validationState.isBirthdayValid && validationState.birthdayError != null,
        interactionSource = remember { MutableInteractionSource() }
            .also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            onDatePickerClick()
                        }
                    }
                }
            }
    )

    AnimatedVisibility(visible = validationState.birthdayError != null) {
        Text(
            validationState.birthdayError ?: "",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun PassengerCityField(
    selectedCity: City, // Replace with your actual city data type
    onCityClick: () -> Unit
) {
    OutlinedTextField(
        value = selectedCity.cityName ?: "Pilih Kota Domisili",
        onValueChange = {},
        label = { Text("Kota Domisili") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        readOnly = true,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        },
        isError = selectedCity.cityName.isNullOrEmpty(),
        interactionSource = remember { MutableInteractionSource() }
            .also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            onCityClick()
                        }
                    }
                }
            }
    )

    AnimatedVisibility(visible = selectedCity.cityName.isNullOrEmpty()) {
        Text(
            "Pilih kota domisilimu dulu, ya",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun PassengerIdentitySection(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    validationState: ValidationState
) {
    Text(
        "Info Identitas",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
    )

    Spacer_16dp()

    // Identity Type Picker
    OutlinedTextField(
        value = state.selectedIdentityType.identityTypeName.toString(),
        onValueChange = {},
        label = { Text("Tipe Identitas") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        readOnly = true,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        },
        interactionSource = remember { MutableInteractionSource() }
            .also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {

                        }
                    }
                }
            }
    )

    Spacer_16dp()

    // Identity Number Field
    val isKTP = state.selectedIdentityType.identityTypeId == 1
    val maxLen = if (isKTP) 16 else 32

    OutlinedTextField(
        value = state.selectedIdentity,
        onValueChange = { raw ->
            val cleaned = if (isKTP) {
                raw.filter { it.isDigit() }.take(maxLen)
            } else {
                raw.uppercase().filter { it.isLetterOrDigit() }.take(maxLen)
            }
        },
        label = { Text("Nomor Identitas") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        isError = !validationState.isIdentityValid && validationState.identityError != null,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = if (isKTP) KeyboardType.Number else KeyboardType.Ascii,
        ),
    )

    AnimatedVisibility(visible = validationState.identityError != null) {
        Text(
            validationState.identityError ?: "",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error
        )
    }
}

// Validation state class
@Stable
class ValidationState {
    var isNameValid by mutableStateOf(false)
    var nameError by mutableStateOf<String?>(null)

    var isAgeValid by mutableStateOf(false)
    var ageError by mutableStateOf<String?>(null)

    var isBirthdayValid by mutableStateOf(true)
    var birthdayError by mutableStateOf<String?>(null)

    var isCityValid by mutableStateOf(false)
    var isGenderValid by mutableStateOf(false)

    var isIdentityValid by mutableStateOf(true)
    var identityError by mutableStateOf<String?>(null)

    val isFormValid: Boolean
        get() = isNameValid && isAgeValid && isBirthdayValid &&
                isCityValid && isGenderValid && isIdentityValid

    fun updateValidation(validation: ValidationResult) {
        isNameValid = validation.isNameValid
        nameError = validation.nameError
        isAgeValid = validation.isAgeValid
        ageError = validation.ageError
        isBirthdayValid = validation.isBirthdayValid
        birthdayError = validation.birthdayError
        isCityValid = validation.isCityValid
        isGenderValid = validation.isGenderValid
        isIdentityValid = validation.isIdentityValid
        identityError = validation.identityError
    }
}

// Validation logic class
class PassengerValidator {
    fun validateAll(
        name: String,
        age: String,
        city: City, // Replace with your actual type
        birthday: String,
        gender: Gender, // Replace with your actual type
        identity: String,
        identityType: IdentityType, // Replace with your actual type
        isAdultPassenger: Boolean
    ): ValidationResult {
        return ValidationResult(
            isNameValid = validateName(name).first,
            nameError = validateName(name).second,
            isAgeValid = if (isAdultPassenger) validateAge(age).first else true,
            ageError = if (isAdultPassenger) validateAge(age).second else null,
            isBirthdayValid = if (!isAdultPassenger) validateBirthday(birthday).first else true,
            birthdayError = if (!isAdultPassenger) validateBirthday(birthday).second else null,
            isCityValid = city.cityId != null && city.cityId != 0,
            isGenderValid = gender.genderId != null && gender.genderId != 0,
            isIdentityValid = if (isAdultPassenger) validateIdentity(identity, identityType).first else true,
            identityError = if (isAdultPassenger) validateIdentity(identity, identityType).second else null
        )
    }

    private fun validateName(name: String): Pair<Boolean, String?> {
        return when {
            name.isBlank() -> false to "Isi nama penumpang dulu, ya"
            name.length > 64 -> false to "Nama penumpang maksimal 64 huruf"
            else -> true to null
        }
    }

    private fun validateAge(age: String): Pair<Boolean, String?> {
        return when {
            age.isBlank() -> false to "Isi usia penumpang dulu, ya"
            age.length > 3 -> false to "Usia maksimal 3 digit"
            else -> true to null
        }
    }

    private fun validateBirthday(birthday: String): Pair<Boolean, String?> {
        return when {
            birthday.isBlank() -> false to "Isi tanggal lahir penumpang dulu, ya"
            else -> true to null
        }
    }

    private fun validateIdentity(identity: String, identityType: IdentityType): Pair<Boolean, String?> {
        val isKTP = identityType.identityTypeId == 1
        return when {
            identity.isBlank() -> false to "Isi no identitas penumpang dulu, ya"
            isKTP && identity.length in 1..15 -> false to "NIK harus 16 digit"
            isKTP && identity.length > 16 -> false to "No identitas maksimal 16 digit"
            else -> true to null
        }
    }
}

data class ValidationResult(
    val isNameValid: Boolean,
    val nameError: String?,
    val isAgeValid: Boolean,
    val ageError: String?,
    val isBirthdayValid: Boolean,
    val birthdayError: String?,
    val isCityValid: Boolean,
    val isGenderValid: Boolean,
    val isIdentityValid: Boolean,
    val identityError: String?
)

// Helper function
private fun createPassengerData(state: HomeState): DataPassengerCO {
    return DataPassengerCO(
        identityTypeId = state.selectedIdentityType.identityTypeId,
        identityNumber = state.selectedIdentity,
        passengerName = state.selectedName,
        birthday = state.selectedBirthday,
        age = if (state.selectedAge.isNotEmpty()) state.selectedAge.toInt() else 0,
        cityId = state.selectedCity.cityId,
        genderId = state.selectedGender.genderId
    )
}