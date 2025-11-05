package presentation.ui.main.datapemeriksaan.fotokendaraan

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import business.constants.BACK_IMAGE
import business.constants.FRONT_IMAGE
import business.constants.LEFT_IMAGE
import business.constants.NRKB_IMAGE
import business.constants.RIGHT_IMAGE
import business.core.UIComponent
import kotlinx.coroutines.flow.Flow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import presentation.component.DEFAULT__BUTTON_SIZE
import presentation.component.DefaultButton
import presentation.component.DefaultScreenUI
import presentation.component.DefaultTextField
import presentation.component.DottedBorderBackground
import presentation.component.Spacer_16dp
import presentation.component.Spacer_4dp
import presentation.component.Spacer_8dp
import presentation.component.noRippleClickable
import presentation.theme.LightPurpleColor
import presentation.theme.PrimaryColor
import presentation.ui.main.home.view_model.HomeEvent
import presentation.ui.main.home.view_model.HomeState
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.ic_camera
import rampcheck.shared.generated.resources.ic_kemenhub
import rampcheck.shared.generated.resources.ic_upload

@Composable
fun UnggahFotoKendaraanScreen(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    errors: Flow<UIComponent>,
    popup: () -> Unit,
    navigateToCamera: () -> Unit
) {

    DefaultScreenUI(
        errors = errors,
        progressBarState = state.progressBarState,
        titleToolbar = "Data Pemeriksaan",
        startIconToolbar = Icons.AutoMirrored.Filled.ArrowBack,
        onClickStartIconToolbar = { popup() },
        endIconToolbar = Res.drawable.ic_kemenhub
    ) {
        UnggahFotoKendaraanContent(
            state = state,
            events = events,
            navigateToCamera = navigateToCamera
        )

    }
}

@Composable
private fun UnggahFotoKendaraanContent(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    navigateToCamera: () -> Unit
) {

    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            HeadlineSection()
            UnggahFotoSection(state,events, navigateToCamera)
            Spacer(modifier = Modifier.weight(1f))
            ButtonNextSection("SIMPAN DAN LANJUT", state, events)

        }
    }

}

@Composable
fun HeadlineSection() {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(
            "Unggah Foto Kendaraan",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )
        Spacer_8dp()
        Text(
            "Unggah foto kendaraan dari berbagai sisi dan pastikan foto kendaraan terlihat jelas.",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Normal,
            )
        )


    }
}

@Composable
fun UnggahFotoSection(state: HomeState, events: (HomeEvent) -> Unit, navigateToCamera: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
            Column {
                Text(
                    "Tampak Depan",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer_4dp()
                DottedBorderBackground( modifier = Modifier.width(170.dp).height(120.dp).clickable {
                    events(HomeEvent.OnUpdateImageTypes(FRONT_IMAGE))
                    navigateToCamera()
                }) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if(state.frontImage != null){
                            Image(
                                contentDescription = null,
                                bitmap = state.frontImage,
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Image(
                                painterResource(Res.drawable.ic_camera),
                                null,
                                modifier = Modifier.size(24.dp)
                                    .align(Alignment.CenterHorizontally),
                                contentScale = ContentScale.Fit
                            )
                            Spacer_8dp()
                            Text(
                                "Ambil Foto",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = PrimaryColor
                                ),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
            }
            Column {
                Text(
                    "Tampak Belakang",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer_4dp()
                DottedBorderBackground( modifier = Modifier.width(170.dp).height(120.dp).noRippleClickable {
                    events(HomeEvent.OnUpdateImageTypes(BACK_IMAGE))
                    navigateToCamera()
                }) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if(state.backImage != null){
                            Image(
                                contentDescription = null,
                                bitmap = state.backImage,
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Image(
                                painterResource(Res.drawable.ic_camera),
                                null,
                                modifier = Modifier.size(24.dp)
                                    .align(Alignment.CenterHorizontally),
                                contentScale = ContentScale.Fit
                            )
                            Spacer_8dp()
                            Text(
                                "Ambil Foto",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = PrimaryColor
                                ),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
            }
        }

        Spacer_16dp()

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
            Column {
                Text(
                    "Tampak Kanan",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer_4dp()
                DottedBorderBackground( modifier = Modifier.width(170.dp).height(120.dp).noRippleClickable {
                    events(HomeEvent.OnUpdateImageTypes(RIGHT_IMAGE))
                    navigateToCamera()
                }) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if(state.rightImage != null){
                            Image(
                                contentDescription = null,
                                bitmap = state.rightImage,
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Image(
                                painterResource(Res.drawable.ic_camera),
                                null,
                                modifier = Modifier.size(24.dp)
                                    .align(Alignment.CenterHorizontally),
                                contentScale = ContentScale.Fit
                            )
                            Spacer_8dp()
                            Text(
                                "Ambil Foto",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = PrimaryColor
                                ),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
            }
            Column {
                Text(
                    "Tampak Kiri",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer_4dp()
                DottedBorderBackground( modifier = Modifier.width(170.dp).height(120.dp).noRippleClickable {
                    events(HomeEvent.OnUpdateImageTypes(LEFT_IMAGE))
                    navigateToCamera()
                }) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if(state.leftImage != null){
                            Image(
                                contentDescription = null,
                                bitmap = state.leftImage,
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Image(
                                painterResource(Res.drawable.ic_camera),
                                null,
                                modifier = Modifier.size(24.dp)
                                    .align(Alignment.CenterHorizontally),
                                contentScale = ContentScale.Fit
                            )
                            Spacer_8dp()
                            Text(
                                "Ambil Foto",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = PrimaryColor
                                ),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
            }
        }

        Spacer_16dp()
        Text(
            "Foto Plat Nomor Bus",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        Spacer_4dp()
        DottedBorderBackground( modifier = Modifier.fillMaxWidth().height(120.dp).noRippleClickable {
            events(HomeEvent.OnUpdateImageTypes(NRKB_IMAGE))
            navigateToCamera()
        }) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if(state.nrkbImage != null){
                    Image(
                        contentDescription = null,
                        bitmap = state.nrkbImage,
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painterResource(Res.drawable.ic_camera),
                        null,
                        modifier = Modifier.size(24.dp)
                            .align(Alignment.CenterHorizontally),
                        contentScale = ContentScale.Fit
                    )
                    Spacer_8dp()
                    Text(
                        "Ambil Foto",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = PrimaryColor
                        ),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }

    }
}

@Composable
fun ButtonNextSection(label: String, state: HomeState, events: (HomeEvent) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)){
        DefaultButton(
            progressBarState = state.progressBarState,
            onClick = {
                events(HomeEvent.VehiclePhoto)
            },
//            enabled = (state.frontImage != null && state.backImage != null && state.leftImage != null && state.rightImage != null && state.nrkbImage != null),
            enabled = true,
            modifier = Modifier.fillMaxWidth().height(DEFAULT__BUTTON_SIZE),
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor, contentColor = Color.White)
        )
    }
}
