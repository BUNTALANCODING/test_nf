package presentation.ui.main.beritaacara

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import business.core.UIComponent
import business.core.UIComponentState
import common.rememberPdfDownloader
import common.toBase64
import common.toBytes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import presentation.component.AlertDialog
import presentation.component.DEFAULT__BUTTON_SIZE
import presentation.component.DefaultButton
import presentation.component.DefaultScreenUI
import presentation.component.DefaultTextField
import presentation.component.DottedBorderBackground
import presentation.component.SignaturePad
import presentation.component.Spacer_16dp
import presentation.component.Spacer_4dp
import presentation.component.Spacer_8dp
import presentation.theme.BackgroundColor
import presentation.theme.PrimaryColor
import presentation.ui.main.auth.view_model.LoginState
import presentation.ui.main.datapemeriksaan.fotokendaraan.ButtonNextSection
import presentation.ui.main.datapemeriksaan.fotopetugas.GuideRow
import presentation.ui.main.home.view_model.HomeEvent
import presentation.ui.main.home.view_model.HomeState
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.ic_bus_guide
import rampcheck.shared.generated.resources.ic_camera
import rampcheck.shared.generated.resources.ic_card_guide
import rampcheck.shared.generated.resources.ic_check
import rampcheck.shared.generated.resources.ic_checked_figma
import rampcheck.shared.generated.resources.ic_guie_teknis_utama
import rampcheck.shared.generated.resources.ic_identity
import rampcheck.shared.generated.resources.ic_kemenhub
import rampcheck.shared.generated.resources.ic_location_guide
import rampcheck.shared.generated.resources.ic_tanda_tangan

@Composable
fun FormBeritaAcaraScreen(
    state: HomeState,
    loginState: LoginState,
    events: (HomeEvent) -> Unit,
    errors: Flow<UIComponent>,
    popup: () -> Unit,
    navigateToPengemudi: () -> Unit,
    navigateToKemenhub: () -> Unit,
    navigateToPreviewBA: () -> Unit,
//    navigateToHome: () -> Unit,
) {

    DefaultScreenUI(
        errors = errors,
        progressBarState = state.progressBarState,
        titleToolbar = "Terbitkan Berita Acara",
        endIconToolbar = Res.drawable.ic_kemenhub
    ) {
        FormBeritaAcaraContent(
            state = state,
            events = events,
            navigateToPengemudi = navigateToPengemudi,
            navigateToKemenhub = navigateToKemenhub,
            navigateToPreviewBA = navigateToPreviewBA,
            loginState = loginState
        )

    }
}

@Composable
private fun FormBeritaAcaraContent(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    loginState: LoginState,
    navigateToPengemudi: () -> Unit,
    navigateToKemenhub: () -> Unit,
    navigateToPreviewBA: () -> Unit,
) {

    LaunchedEffect(Unit){
        events(HomeEvent.SetStateValue)

    }
    val scope = rememberCoroutineScope()

    val downloader = rememberPdfDownloader()

    val pdfUrl = state.urlPreviewBA
    if (state.showDialogTandaTanganPenguji == UIComponentState.Show) {
        Dialog(onDismissRequest = { events(HomeEvent.OnShowDialogTandaTanganPenguji(UIComponentState.Hide)) }) {
            SignaturePad(
                onSave = { bitmap ->
                    events(HomeEvent.OnUpdateTTDPenguji(bitmap.toBytes().toBase64()))
                    events(HomeEvent.OnUpdateTTDPengujiBitmap(bitmap))
                    events(HomeEvent.OnShowDialogTandaTanganPenguji(UIComponentState.Hide))
                },
                onClose = { events(HomeEvent.OnShowDialogTandaTanganPenguji(UIComponentState.Hide)) }
            )
        }
    }
    if (state.showDialogTandaTanganPengemudi == UIComponentState.Show) {
        Dialog(onDismissRequest = {
            events(
                HomeEvent.OnShowDialogTandaTanganPengemudi(
                    UIComponentState.Hide
                )
            )
        }) {
            SignaturePad(
                onSave = { bitmap ->
                    events(HomeEvent.OnUpdateTTDPengemudi(bitmap.toBytes().toBase64()))
                    events(HomeEvent.OnUpdateTTDPengemudiBitmap(bitmap))
                    events(HomeEvent.OnShowDialogTandaTanganPengemudi(UIComponentState.Hide))
                },
                onClose = { events(HomeEvent.OnShowDialogTandaTanganPengemudi(UIComponentState.Hide)) }
            )
        }
    }

    if (state.showDialogTandaTanganKemenhub == UIComponentState.Show) {
        Dialog(onDismissRequest = {
            events(
                HomeEvent.OnShowDialogTandaTanganKemenhub(
                    UIComponentState.Hide
                )
            )
        }) {
            SignaturePad(
                onSave = { bitmap ->
                    events(HomeEvent.OnUpdateTTDKemenhub(bitmap.toBytes().toBase64()))
                    events(HomeEvent.OnUpdateTTDKemenhubBitmap(bitmap))
                    events(HomeEvent.OnShowDialogTandaTanganKemenhub(UIComponentState.Hide))
                },
                onClose = { events(HomeEvent.OnShowDialogTandaTanganKemenhub(UIComponentState.Hide)) }
            )
        }
    }
    if (state.showDialogSuccessSubmitSignature == UIComponentState.Show) {
        AlertDialog(
            iconRes = Res.drawable.ic_check,
            title = "Berita Acara Berhasil Diterbitkan",
            subtitle = "Anda dapat mengakses kembali hasil Berita Acara di menu Riwayat Pemeriksaan",
            isButtonVertical = true,
            isButtonVisible = true,
            onDismissRequest = {
                events(HomeEvent.OnShowDialogSubmitSignature(UIComponentState.Hide))
            },
            positiveLabel = "UNDUH DAN SELESAI",
            negativeLabel = "LIHAT PREVIEW",
            onClickPositive = {
                val cleanUrl = normalizePdfUrl(pdfUrl)

                if (cleanUrl.isNotBlank()) {
                    scope.launch {
                        downloader.download(
                            url = cleanUrl,
                            fileName = "Laporan_${state.rampcheckId}.pdf"
                        )
                    }
                    events(HomeEvent.OnShowDialogSubmitSignature(UIComponentState.Hide))
                } else {
                }
            },
            onClickNegative = navigateToPreviewBA
        )
    }


    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
            TandaTanganForm(state = state, events = events, loginState = loginState, navigateToKemenhub = navigateToKemenhub, navigateToPengemudi = navigateToPengemudi)
            Spacer(modifier = Modifier.weight(1f))
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                DefaultButton(
                    onClick = {
                        events(HomeEvent.SubmitSignature)
                    },
                    modifier = Modifier.fillMaxWidth().height(DEFAULT__BUTTON_SIZE),
                    text = "SIMPAN",
                    enabled = (state.officerName.isNotEmpty() && state.officerNip.isNotEmpty() && state.driverName.isNotEmpty() && state.kemenhubName.isNotEmpty() && state.nipKemenhub.isNotEmpty() && state.ttdPenguji!!.isNotEmpty() && state.ttdPengemudi!!.isNotEmpty() && state.ttdKemenhub!!.isNotEmpty()),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryColor,
                        contentColor = Color.White
                    )
                )
            }
        }
    }
}

@Composable
fun TandaTanganForm(modifier: Modifier = Modifier, state: HomeState, events: (HomeEvent) -> Unit, loginState: LoginState, navigateToKemenhub: () -> Unit, navigateToPengemudi: () -> Unit) {
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

        Text(
            sections[0],
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
        )
        Spacer_8dp()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .padding(16.dp),
        ) {

            Text(
                state.officerName ?: "", style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                state.officerNip ?: "", style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Normal
                )
            )
            Spacer_8dp()
            Divider(thickness = 1.dp, color = Color(0xFFE9E9E9))
            Spacer_8dp()
            Text(
                "TTD Penguji Kendaraan Bermotor", style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Normal
                )
            )
            Spacer_8dp()
            if (state.ttdPengujiBitmap != null) {
                Box(modifier = Modifier.fillMaxWidth().height(160.dp).background(BackgroundColor).clickable {
                    events(
                        HomeEvent.OnShowDialogTandaTanganPenguji(
                            UIComponentState.Show
                        )
                    )
                }){
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Image(
                            bitmap = state.ttdPengujiBitmap,
                            contentDescription = null,
                        )
                    }
                }

            } else {
                DottedBorderBackground(
                    modifier = Modifier.fillMaxWidth().height(160.dp)
                        .clickable {
                            events(
                                HomeEvent.OnShowDialogTandaTanganPenguji(
                                    UIComponentState.Show
                                )
                            )
                        }) {

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

        Spacer_16dp()

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
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .clickable {
                        navigateToPengemudi()
                    }
                    .height(48.dp)
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .border(1.dp, Color.LightGray, shape = RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    if (state.driverName.isNotEmpty()){
                        Text(
                            text = state.driverName,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp
                            ),
                            color = Color.Black
                        )
                    } else{
                        Text(
                            text = "Nama Pengemudi",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal),
                            color = Color.Gray
                        )
                    }

                }
            }
            Spacer_8dp()
            Divider(thickness = 1.dp, color = Color(0xFFE9E9E9))
            Spacer_8dp()

            Text(
                "TTD Pengemudi", style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Normal
                )
            )
            Spacer_4dp()
            if (state.ttdPengemudiBitmap != null) {
                Box(modifier = Modifier.fillMaxWidth().height(160.dp).background(BackgroundColor).clickable {
                    events(
                        HomeEvent.OnShowDialogTandaTanganPengemudi(
                            UIComponentState.Show
                        )
                    )
                }){
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Image(
                            bitmap = state.ttdPengemudiBitmap,
                            contentDescription = null,
                        )
                    }
                }

            } else {
                DottedBorderBackground(
                    modifier = Modifier.fillMaxWidth().height(160.dp)
                        .clickable {
                            events(
                                HomeEvent.OnShowDialogTandaTanganPengemudi(
                                    UIComponentState.Show
                                )
                            )
                        }) {

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

        Spacer_16dp()

        Text(
            sections[2],
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
        )
        Spacer_8dp()

        Column(
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .padding(16.dp)
        ) {

            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .clickable {
                        navigateToKemenhub()
                    }
                    .height(48.dp)
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .border(1.dp, Color.LightGray, shape = RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    if (state.kemenhubName.isNotEmpty()){
                        Text(
                            text = state.kemenhubName,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            ),
                            color = Color.Black
                        )
                        Text(
                            text = state.nipKemenhub,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp
                            ),
                            color = Color.Gray
                        )
                    } else{
                        Text(
                            text = "Nama Petugas Kemenhub",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal),
                            color = Color.Gray
                        )
                    }

                }
            }
            Spacer_8dp()
            Divider(thickness = 1.dp, color = Color(0xFFE9E9E9))
            Spacer_8dp()

            Text(
                "TTD Petugas Kemenhub", style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Normal
                )
            )
            Spacer_8dp()

            if (state.ttdKemenhubBitmap != null) {
                Box(modifier = Modifier.fillMaxWidth().height(160.dp).background(BackgroundColor).clickable {
                    events(
                        HomeEvent.OnShowDialogTandaTanganKemenhub(
                            UIComponentState.Show
                        )
                    )
                }){
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Image(
                        bitmap = state.ttdKemenhubBitmap,
                        contentDescription = null,
                        )
                    }

                }

            } else {
                DottedBorderBackground(
                    modifier = Modifier.fillMaxWidth().height(160.dp)
                        .clickable {
                            events(
                                HomeEvent.OnShowDialogTandaTanganKemenhub(
                                    UIComponentState.Show
                                )
                            )
                        }) {

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
}