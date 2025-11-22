package presentation.ui.main.pemeriksaanadministrasi.kpreguler

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import business.constants.CARD_NOT_AVAILABLE
import business.constants.KP_REGULER_TYPE
import business.core.UIComponent
import business.core.UIComponentState
import kotlinx.coroutines.flow.Flow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import presentation.component.ButtonVerticalSection
import presentation.component.DefaultScreenUI
import presentation.component.DefaultTextField
import presentation.component.DottedBorderBackground
import presentation.component.KartuTidakAdaDialog
import presentation.component.Spacer_48dp
import presentation.component.Spacer_8dp
import presentation.ui.main.home.view_model.HomeEvent
import presentation.ui.main.home.view_model.HomeState
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.ic_camera
import rampcheck.shared.generated.resources.ic_identity
import rampcheck.shared.generated.resources.ic_kemenhub

@Composable
fun PemeriksaanKPRegularScreen(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    errors: Flow<UIComponent>,
    popup: () -> Unit,
    navigateToCameraKPReguler: () -> Unit,
    navigateToHasilKPReguler: () -> Unit
) {

    DefaultScreenUI(
        errors = errors,
        progressBarState = state.progressBarState,
        titleToolbar = "Pemeriksaan Administrasi",
        startIconToolbar = Icons.AutoMirrored.Filled.ArrowBack,
        onClickStartIconToolbar = { popup() },
        endIconToolbar = Res.drawable.ic_kemenhub
    ) {
        PemeriksaanKPRegularContent(
            state = state,
            events = events,
            navigateToCameraKPReguler = navigateToCameraKPReguler,
            navigateToHasilKPReguler = navigateToHasilKPReguler,
        )

    }
}

@Composable
private fun PemeriksaanKPRegularContent(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    navigateToCameraKPReguler: () -> Unit,
    navigateToHasilKPReguler: () -> Unit
) {
    if (state.showDialogKartuTidakAda == UIComponentState.Show) {
        KartuTidakAdaDialog(
            keterangan = state.keteranganKartuTidakAda ?: "",
            onKeteranganChange = { events(HomeEvent.OnUpdateKeteranganKartuTidakAda(it)) },
            onDismiss = {
                events(HomeEvent.OnShowDialogKartuTidakAda(UIComponentState.Hide))
            },
            onSimpan = {
                events(HomeEvent.OnShowDialogKartuTidakAda(UIComponentState.Hide))
                events(HomeEvent.OnUpdateTypeCard(KP_REGULER_TYPE))
                events(HomeEvent.OnUpdateCardAvailable(CARD_NOT_AVAILABLE))
                navigateToHasilKPReguler()
            },
            enableSimpan = state.keteranganKartuTidakAda != null
        )
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            HeaderSection()
            ButtonVerticalSection(
                positiveButtonLabel = "AMBIL FOTO",
                negativeButtonLabel = "KARTU TIDAK ADA",
                positiveButtonClick = navigateToCameraKPReguler,
                negativeButtonClick = { events(HomeEvent.OnShowDialogKartuTidakAda(UIComponentState.Show)) }
            )
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
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer_48dp()
            Image(
                painter = painterResource(Res.drawable.ic_identity),
                contentDescription = "wajah"
            )
            Spacer_8dp()
            Text(
                "KP Reguler",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

data class KpOption(
    val id: Int,
    val label: String
)

@Composable
fun KpRegulerSection(
    selectedId: Int,
    onOptionSelected: (KpOption) -> Unit,
    onTakePhoto: () -> Unit
) {
    val kpOptions = listOf(
        KpOption(1, "Ada, Berlaku"),
        KpOption(2, "Tidak Berlaku"),
        KpOption(3, "Tidak Ada"),
        KpOption(4, "Tidak Sesuai Fisik")
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            // HEADER ORANGE
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF39C12))
                    .padding(10.dp)
            ) {
                Text(
                    text = "KP Reguler",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Column {
                // Radio Options
                Column(modifier = Modifier.background(Color.White)) {
                    kpOptions.forEach { option ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = selectedId == option.id,
                                onClick = { onOptionSelected(option) }
                            )

                            // ✅ MUNCUL HANYA JIKA ID = 1 (Ada, Berlaku)
                            AnimatedVisibility(
                                visible = selectedId == 1,
                                enter = expandVertically(expandFrom = Alignment.Top),
                                exit = shrinkVertically(shrinkTowards = Alignment.Top)
                            ) {

                                Column(Modifier.fillMaxWidth().padding(top = 16.dp)) {

                                    Spacer_8dp()


                                    DottedBorderBackground(
                                        bgColor = Color(0XFFF4F4F4),
                                        modifier = Modifier.height(70.dp).width(100.dp)
                                            .clickable { onTakePhoto() }) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize(),
                                            verticalArrangement = Arrangement.Center,
                                        ) {
                                            androidx.compose.material.Icon(
                                                imageVector = vectorResource(Res.drawable.ic_camera),
                                                modifier = Modifier.size(24.dp)
                                                    .align(Alignment.CenterHorizontally),
                                                contentDescription = null,
                                                tint = Color.Black
                                            )
                                            Spacer_8dp()
                                            Text(
                                                "Foto",
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    color = Color.Black
                                                ),
                                                modifier = Modifier.align(Alignment.CenterHorizontally)
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = option.label)
                        }
                    }
                }
            }
        }
    }
}
