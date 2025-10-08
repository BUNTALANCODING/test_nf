package presentation.ui.main.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import business.core.ProgressBarState
import business.core.UIComponent
import kotlinx.coroutines.flow.Flow
import presentation.component.DEFAULT__BUTTON_SIZE_EXTRA
import presentation.component.DefaultButton
import presentation.component.DefaultScreenUI
import presentation.component.OtpInput
import presentation.component.PasswordTextField
import presentation.component.Spacer_16dp
import presentation.component.Spacer_32dp
import presentation.component.Spacer_4dp
import presentation.component.Spacer_8dp
import presentation.theme.BackgroundColor
import presentation.theme.PrimaryColor
import presentation.theme.iconBg
import presentation.ui.main.auth.view_model.LoginEvent
import presentation.ui.main.auth.view_model.LoginState

@Composable
fun RegisterScreen(
    state: LoginState,
    events: (LoginEvent) -> Unit,
    errors: Flow<UIComponent>,
    navigateToLogin: () -> Unit,
    navigateToSuccess: () -> Unit,
    popUp: () -> Unit
) {
    var currentStep by rememberSaveable { mutableStateOf(1) }

    LaunchedEffect(Unit) {
        events(LoginEvent.ResetPasswordField)
    }

    LaunchedEffect(state.isSuccessRegister) {
        if (!state.isSuccessRegister) {
            currentStep = 1
        }
    }

    DefaultScreenUI(
        errors = errors,
        progressBarState = state.progressBarState,
        parent = true
    ) {
        RegisterContent(
            currentStep = currentStep,
            state = state,
            events = events,
            onStepChange = { currentStep = it },
            navigateToLogin = navigateToLogin,
            navigateToSuccess = navigateToSuccess
        )
    }
}

@Composable
private fun RegisterContent(
    currentStep: Int,
    state: LoginState,
    events: (LoginEvent) -> Unit,
    onStepChange: (Int) -> Unit,
    navigateToLogin: () -> Unit,
    navigateToSuccess: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy((-20).dp),
    ) {
        //Step Indicator
        StepIndicator(currentStep = currentStep)
        // Registration Form
        RegistrationForm(
            currentStep = currentStep,
            state = state,
            events = events,
            onStepChange = onStepChange,
            navigateToLogin = navigateToLogin,
            navigateToSuccess = navigateToSuccess
        )
    }
}

@Composable
fun StepIndicator(
    currentStep: Int,
    totalSteps: Int = 4
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .background(iconBg),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = "Langkah $currentStep dari $totalSteps",
            style = MaterialTheme.typography.labelMedium,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun RegistrationForm(
    currentStep: Int,
    state: LoginState,
    events: (LoginEvent) -> Unit,
    onStepChange: (Int) -> Unit,
    navigateToLogin: () -> Unit,
    navigateToSuccess: () -> Unit
) {
    when (currentStep) {
        1 -> InputNumberStep(
            state = state,
            events = events,
            onNext = { onStepChange(2) },
            onAlreadyHaveAccount = navigateToLogin
        )

        2 -> OTPInput(
            state = state,
            events = events,
            onBack = { onStepChange(1) },
            onNext = { onStepChange(3) },
            onAlreadyHaveAccount = navigateToLogin
        )

        3 -> IdentityInfo(
            state = state,
            events = events,
            onBack = { onStepChange(2) },
            onSubmit = { onStepChange(4) },
            onAlreadyHaveAccount = navigateToLogin
        )
        4 -> PasswordStep(
            state = state,
            events = events,
            onBack = { onStepChange(1) },
            onSubmit = navigateToSuccess,
            onAlreadyHaveAccount = navigateToLogin
        )
    }
}

// Data classes for validation
@Stable
data class ValidationState(
    val isValid: Boolean,
    val errorMessage: String? = null
)

@Stable
data class PersonalInfoValidation(
//    val name: ValidationState,
//    val email: ValidationState,
    val phone: ValidationState
) {
    val isAllValid: Boolean
        get() = phone.isValid
}

@Stable
data class PasswordValidation(
    val password: ValidationState,
    val confirmPassword: ValidationState
) {
    val isAllValid: Boolean
        get() = password.isValid && confirmPassword.isValid
}

// Validation functions
//private fun validateName(name: String): ValidationState {
//    val namePattern = Regex("^[\\p{L} .'-]+\$")
//    return when {
//        name.isBlank() -> ValidationState(false, "Masukkan nama valid")
//        !name.matches(namePattern) -> ValidationState(
//            false,
//            "Nama hanya boleh mengandung huruf dan spasi"
//        )
//
//        else -> ValidationState(true)
//    }
//}
//
//private fun validateEmail(email: String): ValidationState {
//    return when {
//        email.isBlank() -> ValidationState(false, "Email tidak boleh kosong")
//        !email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$")) ->
//            ValidationState(false, "Email tidak valid")
//
//        else -> ValidationState(true)
//    }
//}

private fun validatePhone(phone: String): ValidationState {
    val phonePattern = Regex("^\\+?[0-9]{10,13}\$")
    return when {
        phone.isBlank() -> ValidationState(false, "Masukkan nomor telepon yang valid")
        !phone.matches(phonePattern) -> ValidationState(false, "Nomor telepon tidak valid")
        else -> ValidationState(true)
    }
}

private fun validatePassword(password: String): ValidationState {
    return when {
        password.isBlank() -> ValidationState(false, "Password tidak boleh kosong")
        password.length < 6 -> ValidationState(false, "Password minimal 6 karakter")
        else -> ValidationState(true)
    }
}

private fun validateConfirmPassword(password: String, confirmPassword: String): ValidationState {
    return when {
        confirmPassword.isBlank() -> ValidationState(
            false,
            "Konfirmasi password tidak boleh kosong"
        )

        confirmPassword != password -> ValidationState(false, "Password tidak cocok")
        else -> ValidationState(true)
    }
}

@Composable
private fun InputNumberStep(
    state: LoginState,
    events: (LoginEvent) -> Unit,
    onNext: () -> Unit,
    onAlreadyHaveAccount: () -> Unit
) {
    // Memoized validation state
    val validationState by remember(state.nameRegister, state.usernameLogin, state.phoneRegister) {
        derivedStateOf {
            PersonalInfoValidation(
//                name = validateName(state.nameRegister),
//                email = validateEmail(state.usernameLogin),
                phone = validatePhone(state.phoneRegister)
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
//        Column(modifier = Modifier.fillMaxWidth().height(36.dp).padding(16.dp).background(iconBg)) {
//            Text(
//                "Langkah 1 dari 4",
//                style = MaterialTheme.typography.labelMedium,
//            )
//        }

        Spacer_16dp()

        Text(
            "Daftar Akun",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer_8dp()
        Text(
            "Masukkan nomor telepon yang masih aktif, kami akan mengirim Kode OTP melalui whatsapp",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Normal
            )
        )

        Spacer_32dp()

        PersonalInfoFields(
            state = state,
            events = events,
            validationState = validationState
        )

        Spacer_32dp()

        ActionButton(
            text = "LANJUT",
            enabled = true,
            progressBarState = state.progressBarState,
            onClick = onNext
        )

        Spacer_32dp()

        LoginPrompt(onLoginClick = onAlreadyHaveAccount)

        Spacer_32dp()
    }
}

@Composable
private fun PersonalInfoFields(
    state: LoginState,
    events: (LoginEvent) -> Unit,
    validationState: PersonalInfoValidation
) {
    Column(horizontalAlignment = Alignment.Start) {
        // Phone Field
        ValidatedTextField(
            value = state.phoneRegister,
            onValueChange = {
                if (it.length <= 13) {
                    events(LoginEvent.OnUpdatePhoneRegister(it))
                }
            },
            placeholder = "Nomor Telepon",
            validation = validationState.phone,
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
        )
    }
}

@Composable
private fun OTPInput(
    state: LoginState,
    events: (LoginEvent) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit,
    onAlreadyHaveAccount: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Spacer_16dp()
        Text(
            "Masukkan Kode OTP",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
            ),
            modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
        )
        Spacer_16dp()
        Text(
            "Kami telah mengirimkan Kode OTP melalui whatsapp ke nomor ",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Normal
            ),
        )
        Spacer_16dp()

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        OtpInput(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onOtpEntered = { dataOtp ->
//            paramValue = dataOtp
            },
            clearOtpTrigger = false
        )
        Spacer_16dp()
        ActionButton(
            text = "Verifikasi",
            enabled = true,
            progressBarState = state.progressBarState,
            onClick = onNext
        )
        Spacer_16dp()
        Text(
            "Belum mendapatkan Kode?",
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold
            ),
        )

        Spacer_8dp()

        Row {
            Text(
                text = "Kirim ulang dalam",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                ),
            )
            Spacer_8dp()
            Text(
                text = "00:59",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                ),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
        Spacer_16dp()
        Text(
            text = "atau",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
            ),
        )
        Spacer_16dp()
        Text(
            text = "Ganti Nomor Telepon",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = PrimaryColor
            ),
        )
    }
}

@Composable
private fun IdentityInfo(
    state: LoginState,
    events: (LoginEvent) -> Unit,
    onBack: () -> Unit,
    onSubmit: () -> Unit,
    onAlreadyHaveAccount: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp)
        ) {
            Spacer_16dp()
            Text(
                "Lengkapi Data Diri",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Isi dan lengkapi data diri kamu ya!",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
            )

            Spacer_16dp()
            //NIK
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Gray)) {
                        append("NIK e-KTP")
                    }
                    withStyle(style = SpanStyle(color = Color.Red)) {
                        append("*")
                    }
                },
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer_8dp()
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("NIK e-KTP*") },
                placeholder = { Text("Masukkan NIK e-KTP") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            // Nama Lengkap
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Gray)) {
                        append("Nama Lengkap")
                    }
                    withStyle(style = SpanStyle(color = Color.Red)) {
                        append("*")
                    }
                },
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer_8dp()
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Nama Lengkap*") },
                placeholder = { Text("Masukkan Nama Lengkap") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            // Email
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Gray)) {
                        append("Email")
                    }
                    withStyle(style = SpanStyle(color = Color.Red)) {
                        append("*")
                    }
                },
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer_8dp()
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Email*") },
                placeholder = { Text("Masukkan Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            // Tanggal Lahir
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Gray)) {
                        append("Tanggal Lahir")
                    }
                    withStyle(style = SpanStyle(color = Color.Red)) {
                        append("*")
                    }
                },
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer_8dp()
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Tanggal Lahir*") },
                placeholder = { Text("Pilih Tanggal Lahir") },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(16.dp))

            // Jenis Kelamin
            Text(
                "Jenis Kelamin",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { /* pilih laki2 */ }
                ) {
                    RadioButton(selected = true, onClick = { })
                    Text("Laki-laki")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { /* pilih perempuan */ }
                ) {
                    RadioButton(selected = false, onClick = { })
                    Text("Perempuan")
                }
            }
            ActionButton(
                text = "LANJUT",
                enabled = true,
                progressBarState = state.progressBarState,
                onClick = onSubmit
            )
        }

    }
}


@Composable
private fun PasswordStep(
    state: LoginState,
    events: (LoginEvent) -> Unit,
    onBack: () -> Unit,
    onSubmit: () -> Unit,
    onAlreadyHaveAccount: () -> Unit
) {
    // Memoized validation state
    val validationState by remember(state.passwordLogin, state.confirmPasswordRegister) {
        derivedStateOf {
            PasswordValidation(
                password = validatePassword(state.passwordLogin),
                confirmPassword = validateConfirmPassword(
                    state.passwordLogin,
                    state.confirmPasswordRegister
                )
            )
        }
    }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.Start) {
        Spacer_16dp()
        Text(
            "Buat Kata Sandi",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer_8dp()
        Text("Pendaftaran akun kamu hampir selesai!", style = MaterialTheme.typography.labelMedium)
        Spacer_32dp()
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            PasswordFields(
                state = state,
                events = events,
                validationState = validationState
            )

            Spacer_32dp()

            ActionButton(
                text = "DAFTAR SEKARANG",
                enabled = true,
                progressBarState = state.progressBarState,
                onClick = onSubmit
            )

            Spacer_16dp()

        }
    }

}

@Composable
private fun PasswordFields(
    state: LoginState,
    events: (LoginEvent) -> Unit,
    validationState: PasswordValidation
) {
    Column(horizontalAlignment = Alignment.Start) {
        // Password Field
        ValidatedPasswordField(
            value = state.passwordLogin,
            onValueChange = { events(LoginEvent.OnUpdatePasswordLogin(it)) },
            title = "Kata Sandi",
            hint = "Kata Sandi",
            validation = validationState.password
        )

        Spacer_8dp()

        // Confirm Password Field
        ValidatedPasswordField(
            value = state.confirmPasswordRegister,
            onValueChange = { events(LoginEvent.OnUpdateConfirmPasswordRegister(it)) },
            title = "Konfirmasi Kata Sandi",
            hint = "Konfirmasi Kata Sandi",
            validation = validationState.confirmPassword
        )
    }
}

@Composable
private fun ValidatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    validation: ValidationState,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, style = MaterialTheme.typography.labelMedium) },
            modifier = Modifier
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            singleLine = true,
            //isError = !validation.isValid && value.isNotEmpty(),
            keyboardOptions = KeyboardOptions(
                imeAction = imeAction,
                keyboardType = keyboardType,
            ),
        )

        AnimatedVisibility(visible = !validation.isValid && value.isNotEmpty()) {
            Text(
                validation.errorMessage ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun ValidatedPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    title: String,
    validation: ValidationState
) {
    Column {
        PasswordTextField(
            value = value,
            hint = hint,
            onValueChange = onValueChange,
            title = title,
            modifier = Modifier
                .fillMaxWidth()
        )

        AnimatedVisibility(visible = !validation.isValid && value.isNotEmpty()) {
            Text(
                validation.errorMessage ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun ActionButton(
    text: String,
    enabled: Boolean,
    progressBarState: ProgressBarState,
    onClick: () -> Unit
) {
    DefaultButton(
//        progressBarState = progressBarState,
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .height(DEFAULT__BUTTON_SIZE_EXTRA),
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(10.dp)
    )
}

@Composable
private fun LoginPrompt(onLoginClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Sudah punya akun?",
                style = MaterialTheme.typography.labelMedium
            )
            Spacer_4dp()
            Text(
                modifier = Modifier.clickable { onLoginClick() },
                text = "Masuk disini",
                style = MaterialTheme.typography.labelMedium,
                color = PrimaryColor,
            )
        }
    }
}


