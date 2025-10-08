package presentation.ui.main.auth.pin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import business.core.ProgressBarState
import business.core.UIComponent
import kotlinx.coroutines.flow.Flow
import presentation.component.DefaultScreenUI
import presentation.component.PinDot
import presentation.component.Spacer_32dp
import presentation.component.Spacer_64dp
import presentation.ui.main.auth.view_model.LoginEvent
import presentation.ui.main.auth.view_model.LoginState

@Composable
fun KonfirmasiPinScreen(
    state: LoginState,
    events: (LoginEvent) -> Unit,
    errors: Flow<UIComponent>,
    navigateToSuccess: () -> Unit,
    popup: () -> Unit
) {
    var pin by remember { mutableStateOf("") }
    DefaultScreenUI(
        errors = errors,
        progressBarState = state.progressBarState,
        parent = true,
        titleToolbar = "Konfirmasi Pin",
        startIconToolbar = Icons.AutoMirrored.Filled.ArrowBack,
        onClickStartIconToolbar = popup
    ) {
        KonfirmasiPinContent(
            progressBarState = state.progressBarState,
            navigateToSuccess = navigateToSuccess
        )
    }
}

@Composable
private fun KonfirmasiPinContent(
    navigateToSuccess: () -> Unit,
    progressBarState: ProgressBarState,
) {
    val maxLength = 6
    val focusRequester = remember { FocusRequester() }
    var pin by remember { mutableStateOf("") }
    Spacer_32dp()
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Konfirmasi PIN", style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )

        Spacer_64dp()

        // Area PIN yang dapat diklik
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { focusRequester.requestFocus() }, // Meminta fokus saat diklik
            horizontalArrangement = Arrangement.Center
        ) {
            for (i in 0 until maxLength) {
                PinDot(isActive = i < pin.length)
            }
        }

        // TextField yang tersembunyi, fokusnya akan diminta saat area di atas diklik
        TextField(
            value = pin,
            onValueChange = { newValue ->
                if (newValue.length <= maxLength) {
                    pin = newValue
                    // Optional: Call a function when PIN is complete
                    if (newValue.length == maxLength) {

                        println("PIN Lengkap: $newValue")
                        navigateToSuccess()
                    }
                }
            },
            modifier = Modifier
                .size(60.dp)
                .focusRequester(focusRequester),
            // Atur warna menjadi transparan penuh
            colors = TextFieldDefaults.colors(
                unfocusedLabelColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedLabelColor = Color.Transparent,
                focusedTextColor = Color.Transparent,
                unfocusedTextColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            visualTransformation = PasswordVisualTransformation()
        )
    }
}

