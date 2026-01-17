package presentation.ui.main.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import common.auth.rememberAuthUiContext
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import presentation.ui.main.auth.view_model.LoginAction
import presentation.ui.main.auth.view_model.LoginViewModel
import presentation.theme.TextMuted
import navbuss.shared.generated.resources.Res
import navbuss.shared.generated.resources.ic_city_login
import navbuss.shared.generated.resources.ic_login
import presentation.component.GoogleButton


private val ButtonShape = RoundedCornerShape(16.dp)

@Composable
fun LoginScreen(
    appName: String = "Transportify",
    navigateToHome: () -> Unit
) {
    val viewModel: LoginViewModel = koinInject()
    val state by viewModel.state.collectAsState()
    val ui = rememberAuthUiContext()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(state.user) {
        if (state.user != null) {
            navigateToHome()
        }
    }

    LaunchedEffect(state.error) {
        val msg = state.error ?: return@LaunchedEffect
        snackbar.showSnackbar(msg)
        viewModel.onAction(LoginAction.ErrorShown)
    }

    val bg = Color(0xFFF3F3F3)
    val cs = MaterialTheme.colorScheme

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        containerColor = bg
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(bg)
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_city_login),
                contentDescription = "City",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(190.dp),
                alpha = 0.65f
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 210.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.ic_login),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(200.dp)
                )

                Spacer(Modifier.height(28.dp))

                GoogleButton(
                    onClick = {
                        if (!state.isLoading) {
                            viewModel.onAction(LoginAction.Google(ui))
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 420.dp)
                        .height(54.dp)
                )

                if (state.isLoading) {
                    Spacer(Modifier.height(12.dp))
                    CircularProgressIndicator()
                }

                Spacer(Modifier.height(10.dp))
                Text(
                    text = "atau",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted
                )
                Spacer(Modifier.height(10.dp))

                Button(
                    onClick = navigateToHome,
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 420.dp)
                        .height(54.dp),
                    shape = ButtonShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = cs.primary,
                        contentColor = Color.White
                    )
                ) {
                    Text("Akses tanpa Akun", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
