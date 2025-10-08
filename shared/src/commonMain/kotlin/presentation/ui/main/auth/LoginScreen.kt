package presentation.ui.main.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.belum_punya_akun
import rampcheck.shared.generated.resources.daftar_disini
import rampcheck.shared.generated.resources.forget_password
import rampcheck.shared.generated.resources.ic_frame_decor
import rampcheck.shared.generated.resources.ic_logo_login
import rampcheck.shared.generated.resources.label_welcome
import rampcheck.shared.generated.resources.selamat_datang
import rampcheck.shared.generated.resources.sign_in
import business.core.UIComponent
import com.mmk.kmpnotifier.notification.NotifierManager
import kotlinx.coroutines.flow.Flow
import logger.Logger
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import presentation.component.DEFAULT__BUTTON_SIZE_EXTRA
import presentation.component.DefaultButton
import presentation.component.DefaultScreenUI
import presentation.component.PasswordTextField
import presentation.component.Spacer_16dp
import presentation.component.Spacer_32dp
import presentation.component.Spacer_4dp
import presentation.component.Spacer_8dp
import presentation.component.ValidationState
import presentation.component.noRippleClickable
import presentation.theme.PrimaryColor
import presentation.ui.main.auth.view_model.LoginEvent
import presentation.ui.main.auth.view_model.LoginState
import presentation.util.ValidationResult
import rampcheck.shared.generated.resources.ic_logo

@Composable
fun LoginScreen(
    state: LoginState,
    events: (LoginEvent) -> Unit,
    errors: Flow<UIComponent>,
    navigateToRegister: () -> Unit,
    navigateToForgot: () -> Unit
) {
    // Initialize FCM token
//    InitializeFCMToken(events)

    LaunchedEffect(Unit) {
        events(LoginEvent.ResetPasswordField)
    }

    // Memoized validation state
    val validationState by remember(state.usernameLogin, state.passwordLogin) {
        derivedStateOf {
            LoginValidationState(
                email = validateEmail(state.usernameLogin),
                password = validatePassword(state.passwordLogin)
            )
        }
    }

    DefaultScreenUI(
        errors = errors,
        progressBarState = state.progressBarState,

        ) {
        LoginContent(
            state = state,
            events = events,
            validationState = validationState,
            navigateToRegister = navigateToRegister,
            navigateToForgot = navigateToForgot
        )
    }
}

@Composable
private fun InitializeFCMToken(events: (LoginEvent) -> Unit) {
    LaunchedEffect(Unit) {
        val token = NotifierManager.getPushNotifier().getToken() ?: ""
        Logger.i("Firebase Token: $token")
        events(LoginEvent.OnUpdateFCMToken(token))
    }
}

@Composable
private fun LoginContent(
    state: LoginState,
    events: (LoginEvent) -> Unit,
    validationState: LoginValidationState,
    navigateToRegister: () -> Unit,
    navigateToForgot: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().background(Color.White)){
//        Image(
//            painter = painterResource(Res.drawable.ic_frame_decor),
//            contentDescription = "decoration",
//            modifier = Modifier.align(Alignment.BottomEnd).size(105.dp, 250.dp),
//            contentScale = ContentScale.Crop
//        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            // Header Image
            LoginHeader()

            // Login Form
            LoginFormCard(
                state = state,
                events = events,
                validationState = validationState,
                navigateToRegister = navigateToRegister,
                navigateToForgot = navigateToForgot
            )
            Spacer(modifier = Modifier.weight(1f))

            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle()) {
                        append("Dengan masuk dan daftar akun Banten Ceria, anda menyetujui\n")

                    }
                    withStyle(style = SpanStyle(color = PrimaryColor, fontWeight = FontWeight.Bold)) {
                        append("Syarat & Ketentuan ")
                    }
                    withStyle(style = SpanStyle()) {
                        append("serta ")
                    }
                    withStyle(style = SpanStyle(color = PrimaryColor, fontWeight = FontWeight.Bold)) {
                        append("Kebijakan Privasi ")
                    }
                    withStyle(style = SpanStyle()) {
                        append("yang berlaku")
                    }
                },
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
        }
    }

}

@Composable
private fun LoginHeader() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp).size(239.dp, 125.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer_16dp()
        val painter = painterResource(Res.drawable.ic_logo)
        Image(
            painter = painter,
            contentDescription = "Login Header",
            contentScale = ContentScale.Fit,
        )
    }

}

@Composable
private fun LoginFormCard(
    state: LoginState,
    events: (LoginEvent) -> Unit,
    validationState: LoginValidationState,
    navigateToRegister: () -> Unit,
    navigateToForgot: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Spacer_16dp()

        // Welcome Section
        WelcomeSection()

        Spacer_32dp()

        // Input Fields
        LoginInputFields(
            state = state,
            events = events,
            validationState = validationState
        )

        Spacer_16dp()

        // Forgot Password Link
        ForgotPasswordLink(onClick = navigateToForgot)

        Spacer_16dp()

        // Login Button
        LoginButton(
            state = state,
            events = events,
            isEnabled = validationState.isAllValid
        )

        Spacer_16dp()

        // Register Prompt
        RegisterPrompt(onClick = navigateToRegister)

        Spacer_32dp()
    }
}

@Composable
private fun WelcomeSection() {
    Text(
        stringResource(Res.string.selamat_datang),
        style = MaterialTheme.typography.headlineSmall.copy(
            textAlign = TextAlign.Start
        )
    )
    Spacer_8dp()
    Text(
        stringResource(Res.string.label_welcome),
        style = MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.Normal
        )
    )
}

@Composable
private fun LoginInputFields(
    state: LoginState,
    events: (LoginEvent) -> Unit,
    validationState: LoginValidationState
) {
    Column(horizontalAlignment = Alignment.Start) {
        // Email Field
        ValidatedEmailField(
            value = state.usernameLogin,
            onValueChange = {
                if (it.length < 32) {
                    events(LoginEvent.OnUpdateUsernameLogin(it))
                }
            },
            validation = validationState.email,
        )

        Spacer_8dp()

        // Password Field
        ValidatedPasswordField(
            value = state.passwordLogin,
            onValueChange = { events(LoginEvent.OnUpdatePasswordLogin(it)) },
            validation = validationState.password
        )
    }
}

@Composable
private fun ValidatedEmailField(
    value: String,
    onValueChange: (String) -> Unit,
    validation: ValidationResult
) {
    Column {
        Text(
            "Nomor Telepon",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer_8dp()
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            singleLine = true,
            //isError = !validation.isValid && value.isNotEmpty(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email,
            ),
        )

        AnimatedVisibility(visible = !validation.isValid && value.isNotEmpty()) {
            Text(
                validation.errorMessage,
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
    validation: ValidationResult
) {
    Column {
        PasswordTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
        )

        AnimatedVisibility(visible = !validation.isValid && value.isNotEmpty()) {
            Text(
                validation.errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun ForgotPasswordLink(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            stringResource(Res.string.forget_password),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = PrimaryColor,
        )
    }
}

@Composable
private fun LoginButton(
    state: LoginState,
    events: (LoginEvent) -> Unit,
    isEnabled: Boolean
) {
    DefaultButton(
//        progressBarState = state.progressBarState,
        text = stringResource(Res.string.sign_in),
        modifier = Modifier
            .fillMaxWidth()
            .height(DEFAULT__BUTTON_SIZE_EXTRA),
        onClick = {
            if (isEnabled) {
                events(LoginEvent.Login)
            }
        },
        enabled = isEnabled,
        shape = RoundedCornerShape(10.dp)
    )
}

@Composable
private fun RegisterPrompt(onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(Res.string.belum_punya_akun),
                style = MaterialTheme.typography.labelMedium
            )
            Spacer_4dp()
            Text(
                modifier = Modifier.clickable { onClick() },
                text = stringResource(Res.string.daftar_disini),
                style = MaterialTheme.typography.labelMedium,
                color = PrimaryColor,
            )
        }
    }

}
//
@Stable
private data class LoginValidationState(
    val email: ValidationResult,
    val password: ValidationResult
) {
    val isAllValid: Boolean
        get() = email.isValid && password.isValid
}

// Validation functions
private fun validateEmail(email: String): ValidationResult {
    return when {
        email.isBlank() -> ValidationResult(false, "Email tidak boleh kosong")
        !email.matches(Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) ->
            ValidationResult(false, "Email tidak valid")

        else -> ValidationResult(true)
    }
}

private fun validatePassword(password: String): ValidationResult {
    return when {
        password.isBlank() -> ValidationResult(false, "Kata Sandi tidak boleh kosong")
        password.length < 8 -> ValidationResult(false, "Kata Sandi minimal 8 karakter")
        else -> ValidationResult(true)
    }
}
