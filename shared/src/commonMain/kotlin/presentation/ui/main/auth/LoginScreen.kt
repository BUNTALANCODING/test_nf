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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
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
import presentation.component.DefaultTextField
import presentation.component.PasswordTextField
import presentation.component.Spacer_16dp
import presentation.component.Spacer_32dp
import presentation.component.Spacer_4dp
import presentation.component.Spacer_8dp
import presentation.component.ValidationState
import presentation.component.noRippleClickable
import presentation.theme.PrimaryColor
import presentation.theme.yellowBackground
import presentation.ui.main.auth.view_model.LoginEvent
import presentation.ui.main.auth.view_model.LoginState
import presentation.util.ValidationResult
import rampcheck.shared.generated.resources.ic_kemenhub
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
    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
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
                navigateToForgot = navigateToForgot,
                modifier = Modifier.weight(1f)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            )
        }
    }

}

@Composable
private fun LoginHeader() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer_16dp()
        val painter = painterResource(Res.drawable.ic_kemenhub)
        Image(
            painter = painter,
            contentDescription = "Login Header",
            contentScale = ContentScale.Fit,
            modifier = Modifier.width(121.dp).height(140.dp)
        )
        Spacer_16dp()
        Text(
            text = "RAMPCHECK KEMENHUB",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
            )
        )
    }

}

@Composable
private fun LoginFormCard(
    state: LoginState,
    events: (LoginEvent) -> Unit,
    validationState: LoginValidationState,
    navigateToRegister: () -> Unit,
    navigateToForgot: () -> Unit,
    modifier: Modifier
) {
    Spacer_32dp()
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        color = PrimaryColor
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer_16dp()
                Text(
                    "Masuk Dengan Akun Anda",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    ),
                )
                Spacer_16dp()
                // Input Fields
                LoginInputFields(
                    state = state,
                    events = events,
                    validationState = validationState
                )

                Spacer_16dp()


                Spacer_16dp()

                // Login Button
                LoginButton(
                    state = state,
                    events = events,
                    isEnabled = validationState.isAllValid
                )

                Spacer_16dp()
            }

        }
    }
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
            "Email",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer_8dp()
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text("Masukkan Email") },
            modifier = Modifier
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            singleLine = true,
            //isError = !validation.isValid && value.isNotEmpty(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email,
            ),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedIndicatorColor = Color.White,
                focusedIndicatorColor = Color.White
            )
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
        shape = RoundedCornerShape(10.dp),
        colors =  ButtonDefaults.buttonColors(
            containerColor = yellowBackground, contentColor = Color.White, disabledContainerColor = Color.LightGray, disabledContentColor = Color.Gray
        )
    )
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
        email.isBlank() -> ValidationResult(false, "Username tidak boleh kosong")
        !email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$")) ->
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
