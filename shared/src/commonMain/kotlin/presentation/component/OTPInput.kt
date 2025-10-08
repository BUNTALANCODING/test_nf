package presentation.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import presentation.theme.ColorPrimary300
import presentation.theme.GrayButtonColor

@Composable
fun OtpInput(
    modifier: Modifier = Modifier,
    otpLength: Int = 6,
    unfocusColor: Color = GrayButtonColor.copy(0.20F),
    focusedColor: Color = ColorPrimary300,
    onOtpEntered: (String) -> Unit,
    clearOtpTrigger: Boolean
) {
    var otpValues by remember { mutableStateOf(List(otpLength) { "" }) }
    val focusRequesters = List(otpLength) { FocusRequester() }

    LaunchedEffect(clearOtpTrigger) {
        if (clearOtpTrigger) {
            otpValues = List(otpLength) { "" }
        }
    }

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        otpValues.forEachIndexed { index, value ->
            var isFocused by remember { mutableStateOf(false) }
            TextField(
                value = value,
                onValueChange = { newValue ->
                    if (newValue.length <= 1 && !newValue.any { it.isLetter() }) {
                        val updatedOtp = otpValues.toMutableList()
                        updatedOtp[index] = newValue
                        otpValues = updatedOtp

                        if (newValue.isNotEmpty() && index < otpLength - 1) {
                            focusRequesters[index + 1].requestFocus()
                        }

                        if (updatedOtp.joinToString("").length == otpLength) {
                            onOtpEntered(updatedOtp.joinToString(""))
                        }
                    }
                },
                modifier = Modifier
                    .width(45.dp)
                    .height(60.dp)
                    .border(
                        1.dp,
                        if (isFocused) focusedColor else unfocusColor,
                        RoundedCornerShape(8.dp)
                    )
                    .onFocusChanged { isFocused = it.isFocused }
                    .focusRequester(focusRequesters[index])
                    .onKeyEvent {
                        if (it.key == Key.Backspace && it.type == KeyEventType.KeyDown &&
                            value.isEmpty() && index > 0
                        ) {
                            focusRequesters[index - 1].requestFocus()
                        }
                        false
                    },
                textStyle = TextStyle(fontSize = 20.sp, textAlign = TextAlign.Center),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                )
            )
        }
    }
}