package presentation.ui.main.datapemeriksaan.fotopetugas

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import business.core.UIComponent
import common.toBytes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import presentation.component.DEFAULT__BUTTON_SIZE
import presentation.component.DefaultButton
import presentation.component.DefaultScreenUI
import presentation.component.Spacer_16dp
import presentation.component.Spacer_32dp
import presentation.theme.LightPurpleColor
import presentation.theme.PrimaryColor
import presentation.ui.main.home.view_model.HomeEvent
import presentation.ui.main.home.view_model.HomeState
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.ic_example_camera
import rampcheck.shared.generated.resources.ic_kemenhub

@Composable
fun VerifyFotoPetugasScreen(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    errors: Flow<UIComponent>,
    popup: () -> Unit,
    navigateToDataKIR: () -> Unit
) {

    DefaultScreenUI(
        errors = errors,
        progressBarState = state.progressBarState,
        titleToolbar = "Verifikasi Identitas",
        startIconToolbar = Icons.AutoMirrored.Filled.ArrowBack,
        onClickStartIconToolbar = { popup() },
        endIconToolbar = Res.drawable.ic_kemenhub
    ) {
        VerifyFotoPetugasContent(
            state = state,
            events = events,
            navigateToDataKIR = navigateToDataKIR,
            popup = popup

        )
    }
}

@Composable
private fun VerifyFotoPetugasContent(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    navigateToDataKIR: () -> Unit,
    popup: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),

        ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            HeaderSection()
            PhotoKTPSection(state, events)
            ButtonSectionVerify(popup = popup, state, events)
        }

    }
}

@Composable
private fun HeaderSection() {
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Foto Petugas",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

        }
    }
}

@Composable
private fun PhotoKTPSection(state: HomeState, events: (HomeEvent) -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            state.officer_image_bitmap?.let {
                Image(
                    contentDescription = null,
                    modifier = Modifier.width(223.dp).height(320.dp),
                    bitmap = it
                )
            }
//            RoundedRectangleBackground(
//                modifier = Modifier
//                    .height(255.dp)
//                    .padding(16.dp)
//
//            ) {
////                bitmapKtp?.let { image ->
////                    Image(
////                        bitmap = image,
////                        null,
////                        contentScale = ContentScale.Crop,
////                        modifier = Modifier.size(width = 355.dp, height = 255.dp)
////                    )
////                }
//            }
        }
    }
}


@Composable
private fun ButtonSectionVerify(popup: () -> Unit, state: HomeState, events: (HomeEvent) -> Unit) {

    val coroutineScope = rememberCoroutineScope()
    val isReadyToUpload = state.officer_image_bitmap != null
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer_32dp()
        DefaultButton(
            onClick = {
                popup()
            },
            modifier = Modifier.width(240.dp).height(DEFAULT__BUTTON_SIZE),
            colors = ButtonDefaults.buttonColors(
                containerColor = LightPurpleColor,
                contentColor = PrimaryColor
            ),
            text = "AMBIL ULANG",
        )

        Spacer_16dp()

        DefaultButton(
            onClick = {
                events(HomeEvent.UploadOfficerImage)
            },
            enabled = true,
            modifier = Modifier.width(240.dp).height(DEFAULT__BUTTON_SIZE),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryColor,
                contentColor = Color.White,
                disabledContainerColor = MaterialTheme.colorScheme.background,
                disabledContentColor = MaterialTheme.colorScheme.primary
            ),
            text = "SIMPAN & LANJUT",
        )
    }
}