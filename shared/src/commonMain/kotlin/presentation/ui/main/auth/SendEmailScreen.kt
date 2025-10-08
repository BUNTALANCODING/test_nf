package presentation.ui.main.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.ic_sent_figma
import business.core.UIComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import org.jetbrains.compose.resources.painterResource
import presentation.component.DefaultScreenUI
import presentation.ui.main.auth.view_model.LoginEvent
import presentation.ui.main.auth.view_model.LoginState

@Composable
fun SendEmailScreen(
    state: LoginState,
    events: (LoginEvent) -> Unit,
    errors: Flow<UIComponent>,
    navigateToLogin: () -> Unit
) {
    DefaultScreenUI(
        errors = errors,
        progressBarState = state.progressBarState,
        parent = true
    ) {
        EmailSentScreenContent(
            username = state.usernameLogin,
            onResendEmail = { events(LoginEvent.ResendForgotPassword) },
            onFinish = navigateToLogin
        )
    }
}

@Composable
private fun EmailSentScreenContent(
    username: String,
    onResendEmail: () -> Unit,
    onFinish: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Main content
            EmailSentContent(onResendEmail = onResendEmail)
        }

        // Bottom button
        BottomActionButton(
            onClick = onFinish,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}

@Composable
private fun EmailSentContent(onResendEmail: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Success icon
        EmailSuccessIcon()

        // Title and message
        EmailSentTextContent()

        // Resend section
        ResendEmailCard(onResendEmail = onResendEmail)
    }
}

@Composable
private fun EmailSuccessIcon() {
    Card(
        modifier = Modifier.size(120.dp),
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0F7E98).copy(alpha = 0.1f)
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_sent_figma),
                contentDescription = "Email sent successfully",
                modifier = Modifier.size(64.dp)
            )
        }
    }
}

@Composable
private fun EmailSentTextContent() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Email Terkirim",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        )

        Text(
            text = "Silahkan cek email anda!\nKami telah mengirimkan kata sandi baru melalui email anda",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.Black,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        )
    }
}

@Composable
private fun ResendEmailCard(onResendEmail: () -> Unit) {
    var timeLeft by remember { mutableStateOf(0) }
    var isCountdownActive by remember { mutableStateOf(false) }

    // Countdown effect
    LaunchedEffect(isCountdownActive) {
        if (isCountdownActive && timeLeft > 0) {
            while (timeLeft > 0) {
                delay(1000)
                timeLeft--
            }
            isCountdownActive = false
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8F9FA)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Tidak menerima email?",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Black
                )
            )

            EnhancedResendButton(
                timeLeft = timeLeft,
                isCountdownActive = isCountdownActive,
                onResendClick = {
                    onResendEmail()
                    timeLeft = 30
                    isCountdownActive = true
                }
            )

            // Show countdown progress if active
            if (isCountdownActive && timeLeft > 0) {
                CountdownProgress(timeLeft = timeLeft, totalTime = 30)
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun EnhancedResendButton(
    timeLeft: Int,
    isCountdownActive: Boolean,
    onResendClick: () -> Unit
) {
    val isEnabled = !isCountdownActive || timeLeft == 0

    AnimatedContent(
        targetState = isCountdownActive && timeLeft > 0,
        transitionSpec = {
            fadeIn(animationSpec = tween(300)) with fadeOut(animationSpec = tween(300))
        }
    ) { showCountdown ->
        if (showCountdown) {
            // Countdown state
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Kirim ulang dalam",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.Gray
                    )
                )

                Text(
                    text = formatTime(timeLeft),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F7E98)
                    )
                )
            }
        } else {
            // Normal button state
            TextButton(
                onClick = onResendClick,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color(0xFF0F7E98)
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Resend",
                        modifier = Modifier.size(16.dp)
                    )

                    Text(
                        text = "KIRIM ULANG",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun CountdownProgress(timeLeft: Int, totalTime: Int) {
    val progress = (totalTime - timeLeft).toFloat() / totalTime.toFloat()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = Color(0xFF0F7E98),
            trackColor = Color(0xFF0F7E98).copy(alpha = 0.2f)
        )

        Text(
            text = "Email akan dapat dikirim ulang setelah ${formatTime(timeLeft)}",
            style = MaterialTheme.typography.bodySmall.copy(
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        )
    }
}

private fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return if (minutes > 0) {
        "$minutes:${remainingSeconds.toString().padStart(2, '0')}"
    } else {
        "${remainingSeconds}s"
    }
}

@Composable
private fun BottomActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF0F7E98),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Text(
            text = "SELESAI",
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }
}
