package presentation.ui.main.beritaacara

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
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import business.core.UIComponent
import kotlinx.coroutines.flow.Flow
import org.jetbrains.compose.resources.painterResource
import presentation.component.DefaultScreenUI
import presentation.component.DefaultTextField
import presentation.component.DottedBorderBackground
import presentation.component.Spacer_16dp
import presentation.component.Spacer_4dp
import presentation.component.Spacer_8dp
import presentation.theme.PrimaryColor
import presentation.ui.main.datapemeriksaan.fotokendaraan.ButtonNextSection
import presentation.ui.main.datapemeriksaan.fotopetugas.GuideRow
import presentation.ui.main.home.view_model.HomeEvent
import presentation.ui.main.home.view_model.HomeState
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.ic_bus_guide
import rampcheck.shared.generated.resources.ic_camera
import rampcheck.shared.generated.resources.ic_card_guide
import rampcheck.shared.generated.resources.ic_guie_teknis_utama
import rampcheck.shared.generated.resources.ic_identity
import rampcheck.shared.generated.resources.ic_kemenhub
import rampcheck.shared.generated.resources.ic_location_guide
import rampcheck.shared.generated.resources.ic_tanda_tangan

@Composable
fun FormBeritaAcaraScreen(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    errors: Flow<UIComponent>,
    popup: () -> Unit,
    navigateToCameraFace: () -> Unit
) {

    DefaultScreenUI(
        errors = errors,
        progressBarState = state.progressBarState,
        titleToolbar = "Terbitkan Berita Acara",
        endIconToolbar = Res.drawable.ic_kemenhub
    ) {
        GuidePemeriksaanTeknisUtamaContent(
            state = state,
            events = events,
            navigateToCameraFace = navigateToCameraFace
        )

    }
}

@Composable
private fun GuidePemeriksaanTeknisUtamaContent(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    navigateToCameraFace: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
            TandaTanganForm()
        }
    }
}

@Composable
fun TandaTanganForm(modifier: Modifier = Modifier) {
    // List data yang merepresentasikan setiap bagian formulir
    val sections = listOf(
        "Penguji Kendaraan Bermotor",
        "Pengemudi",
        "Petugas Kemenhub"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Bagian Penguji
        Text(
            sections[0],
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
        )
        Spacer_8dp()

        // Data Penguji
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .padding(16.dp),
        ) {
            Text(
                "Gunawan", style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                "354654654", style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Normal
                )
            )
            Spacer_8dp()
            Divider(thickness = 1.dp, color = Color(0xFFE9E9E9))
            Spacer_8dp()
            Text(
                "TTD Penguji", style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Normal
                )
            )
            Spacer_8dp()
            DottedBorderBackground(modifier = Modifier.fillMaxWidth().height(160.dp)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painterResource(Res.drawable.ic_tanda_tangan),
                        null,
                        modifier = Modifier.size(24.dp)
                            .align(Alignment.CenterHorizontally),
                        contentScale = ContentScale.Fit
                    )
                    Spacer_8dp()
                    Text(
                        "Tanda Tangan",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Color(0XFF4A4A4A)
                        ),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }

        Spacer_16dp()

        // Bagian Pengemudi
        Text(
            sections[1],
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
        )
        Spacer_8dp()

        Column(
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .padding(16.dp)
        ) {
            // Input Nama Pengemudi
            DefaultTextField(
                modifier = Modifier.fillMaxWidth(),
                value = "",
                onValueChange = {},
                placeholder = "Nama Pengemudi"
            )
            Spacer_8dp()
            Divider(thickness = 1.dp, color = Color(0xFFE9E9E9))
            Spacer_8dp()

            Text("TTD Pengemudi", style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Normal
            ))
            Spacer_4dp()
            DottedBorderBackground(modifier = Modifier.fillMaxWidth().height(160.dp)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painterResource(Res.drawable.ic_tanda_tangan),
                        null,
                        modifier = Modifier.size(24.dp)
                            .align(Alignment.CenterHorizontally),
                        contentScale = ContentScale.Fit
                    )
                    Spacer_8dp()
                    Text(
                        "Tanda Tangan",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Color(0XFF4A4A4A)
                        ),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }

        Spacer_16dp()

        // Bagian Petugas Kemenhub
        Text(
            sections[2],
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
        )
        Spacer_8dp()

        Column(modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(16.dp)) {

            DefaultTextField(
                modifier = Modifier.fillMaxWidth(),
                value = "",
                onValueChange = {},
                placeholder = "Nama Petugas Kemenhub"
            )
            Spacer_8dp()
            Divider(thickness = 1.dp, color = Color(0xFFE9E9E9))
            Spacer_8dp()

            Text("TTD Petugas Kemenhub", style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Normal
            ))
            Spacer_8dp()
            DottedBorderBackground(modifier = Modifier.fillMaxWidth().height(160.dp)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painterResource(Res.drawable.ic_tanda_tangan),
                        null,
                        modifier = Modifier.size(24.dp)
                            .align(Alignment.CenterHorizontally),
                        contentScale = ContentScale.Fit
                    )
                    Spacer_8dp()
                    Text(
                        "Tanda Tangan",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Color(0XFF4A4A4A)
                        ),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}