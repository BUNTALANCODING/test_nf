package presentation.ui.main.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.ic_tax_figma
import rampcheck.shared.generated.resources.image_auth
import business.core.ProgressBarState
import business.core.UIComponent
import kotlinx.coroutines.flow.Flow
import org.jetbrains.compose.resources.painterResource
import presentation.component.DEFAULT__BUTTON_SIZE_EXTRA
import presentation.component.DefaultButton
import presentation.component.DefaultScreenUI
import presentation.component.Spacer_16dp
import presentation.component.Spacer_32dp
import presentation.theme.PrimaryLighterColor
import presentation.theme.gradientBackground
import presentation.theme.gradientSamsatCeriaVertical
import presentation.ui.main.auth.view_model.LoginEvent
import presentation.ui.main.auth.view_model.LoginState
import rampcheck.shared.generated.resources.blur_red
import rampcheck.shared.generated.resources.blur_yellow

@Composable
fun SuccessRegisterScreen(
    state: LoginState,
    events: (LoginEvent) -> Unit,
    errors: Flow<UIComponent>,
    navigateToAturPin: () -> Unit
) {
    DefaultScreenUI(
        errors = errors,
        progressBarState = state.progressBarState,
        parent = true
    ) {
        SuccessScreenContent(
            progressBarState = state.progressBarState,
            onFinishClick = navigateToAturPin
        )
    }
}

@Composable
private fun SuccessScreenContent(
    progressBarState: ProgressBarState,
    onFinishClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientSamsatCeriaVertical)
    ) {
        Image(
            painter = painterResource(Res.drawable.blur_yellow),
            contentDescription = "decoration",
            modifier = Modifier.align(Alignment.TopEnd). blur(187.dp),
            contentScale = ContentScale.Crop
        )

        Image(
            painter = painterResource(Res.drawable.blur_red),
            contentDescription = "decoration",
            modifier = Modifier.align(Alignment.BottomStart). blur(187.dp),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))


            Image(
                painter = painterResource(Res.drawable.ic_tax_figma),
                contentDescription = null,
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Selamat!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Registrasi Akun Kamu Berhasil",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))


            Text(
                text = "Untuk mengamankan akun kamu dan memudahkan transaksi, atur dulu PIN Ceria Kamu ya!",
                style = MaterialTheme.typography.labelMedium,
                color = Color.DarkGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // === Tombol Atur PIN Ceria ===
            Button(
                onClick = {
                    onFinishClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),

            ) {
                Text(
                    text = "ATUR PIN CERIA",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
//        // Background Image
//        HeaderImage()
//
//        // Overlaid Content
//        OverlaidContent(
//            progressBarState = progressBarState,
//            onFinishClick = onFinishClick
//        )
    }
}

@Composable
private fun HeaderImage() {
    Image(
        painter = painterResource(Res.drawable.image_auth),
        contentDescription = "Success Registration Background",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .fillMaxWidth()
            .height(450.dp)
    )
}

@Composable
private fun OverlaidContent(
    progressBarState: ProgressBarState,
    onFinishClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = PrimaryLighterColor
        ) {
            ContentBody(
                progressBarState = progressBarState,
                onFinishClick = onFinishClick
            )
        }
    }
}

@Composable
private fun ContentBody(
    progressBarState: ProgressBarState,
    onFinishClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer_32dp()

        // Success Icon (optional enhancement)
        SuccessIcon()

        Spacer_16dp()

        // Success Text
        SuccessText()

        Spacer_32dp()

        // Finish Button
        ActionButton(
            progressBarState = progressBarState,
            onClick = onFinishClick
        )

        Spacer_16dp()
    }
}

@Composable
private fun SuccessIcon() {
    // Optional: Add a success icon for better UX
    Icon(
        imageVector = Icons.Default.CheckCircle,
        contentDescription = "Success",
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(64.dp)
    )
}

@Composable
private fun SuccessText() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Selamat!!",
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Registrasi Akunmu Berhasil",
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ActionButton(
    progressBarState: ProgressBarState,
    onClick: () -> Unit
) {
    DefaultButton(
//        progressBarState = progressBarState,
        text = "SELESAI",
        modifier = Modifier
            .fillMaxWidth()
            .height(DEFAULT__BUTTON_SIZE_EXTRA),
        onClick = onClick,
        shape = RoundedCornerShape(10.dp)
    )
}
