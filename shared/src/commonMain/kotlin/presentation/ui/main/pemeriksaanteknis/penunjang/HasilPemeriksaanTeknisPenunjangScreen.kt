package presentation.ui.main.pemeriksaanteknis.penunjang

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import business.constants.SECTION_BADAN
import business.constants.SECTION_BAN
import business.constants.SECTION_PENERANGAN
import business.constants.SECTION_PENGEREMAN
import business.constants.SECTION_PENGUKUR_KECEPATAN
import business.constants.SECTION_PERLENGKAPAN
import business.constants.SECTION_TANGGAP_DARURAT
import business.constants.SECTION_WIPER
import business.core.UIComponent
import business.core.UIComponentState
import business.datasource.network.main.responses.HasilTeknisDTO
import business.datasource.network.main.responses.QuestionResponse
import business.datasource.network.main.responses.SubcategoryResponse
import common.KeepScreenOn
import kotlinx.coroutines.flow.Flow
import org.koin.compose.viewmodel.koinViewModel
import presentation.component.ConditionCard
import presentation.component.ConditionTeknisPenunjang
import presentation.component.DEFAULT__BUTTON_SIZE
import presentation.component.DefaultButton
import presentation.component.DefaultScreenUI
import presentation.component.NotMatchDialog
import presentation.component.Spacer_16dp
import presentation.component.Spacer_8dp
import presentation.theme.PrimaryColor
import presentation.ui.main.home.view_model.HomeEvent
import presentation.ui.main.home.view_model.HomeState
import presentation.ui.main.pemeriksaanteknis.penunjang.viewmodel.GetResultSecondViewModel
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.ic_check_mark
import rampcheck.shared.generated.resources.ic_kemenhub

@Composable
fun HasilPemeriksaanTeknisPenunjangScreen(
    uniqueKey: String,
    state: HomeState,
    events: (HomeEvent) -> Unit,
    errors: Flow<UIComponent>,
    popup: () -> Unit,
    navigateToBeritaAcara: () -> Unit,
    navigateToCamera: () -> Unit,
    hasilViewModel: GetResultSecondViewModel = koinViewModel()
) {
    val hasilStateState = hasilViewModel.state.collectAsState()
    val hasilState = hasilStateState.value
    var showDialogError by remember { mutableStateOf(false) }

    LaunchedEffect(uniqueKey) {
        println("SCREEN_HASIL: start loadHasil($uniqueKey)")
        hasilViewModel.loadHasil(uniqueKey)
    }

    val currentStatus = hasilState.data?.data?.status?.lowercase()

    val stillWaiting = hasilState.error == null && (
                hasilState.isLoading ||
                        currentStatus == "sent" ||
                        currentStatus == "processing" ||
                        currentStatus.isNullOrBlank()
                )

    KeepScreenOn(keepOn = stillWaiting)



    LaunchedEffect(hasilState.data, hasilState.isLoading) {
        val dto = hasilState.data ?: return@LaunchedEffect
        val data = dto.data
        val response = data.response ?: emptyList()

        if (!hasilState.isLoading &&
            hasilState.error == null &&
            data.status.equals("completed", ignoreCase = true) &&
            response.isNotEmpty()
        ) {
            println("SCREEN_HASIL: apply result to HomeViewModel")
            events(
                HomeEvent.ApplyPenunjangResult(
                    apiSubcategories = response
                )
            )
        }
    }

    DefaultScreenUI(
        errors = errors,
        progressBarState = state.progressBarState,
        titleToolbar = "Pemeriksaan Teknis Penunjang",
        startIconToolbar = Icons.AutoMirrored.Filled.ArrowBack,
        onClickStartIconToolbar = { popup() },
        endIconToolbar = Res.drawable.ic_kemenhub
    ) {

        if (hasilState.error != null) {
            AlertDialog(
                onDismissRequest = { },
                confirmButton = {
                    TextButton(
                        onClick = { popup() }
                    ) {
                        Text("OK")
                    }
                },
                title = { Text("Gagal mengambil hasil") },
                text = { Text(hasilState.error ?: "") }
            )
        }

        if (hasilState.showFailedDialog) {
            AlertDialog(
                onDismissRequest = { hasilViewModel.onFailedDialogDismiss() },
                title = { Text("Gagal Identifikasi") },
                text = { Text("Proses identifikasi gagal. Silakan coba ulangi pengambilan video.") },
                confirmButton = {
                    TextButton(onClick = {
                        hasilViewModel.onFailedDialogDismiss()
                        navigateToCamera()
                    }) {
                        Text("OK")
                    }
                }
            )
        }

        if (stillWaiting) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .wrapContentSize()
                        .fillMaxWidth()
                        .widthIn(min = 300.dp, max = 420.dp),
                    shape = MaterialTheme.shapes.medium,
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth()
                            .heightIn(min = 180.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(92.dp)
                        ) {
                            CircularProgressIndicator(
                                progress = 1f,
                                strokeWidth = 10.dp,
                                modifier = Modifier
                                    .matchParentSize()
                                    .alpha(0.06f),
                                color = MaterialTheme.colorScheme.primary
                            )

                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(64.dp)
                                    .semantics { contentDescription = "Memproses" },
                                strokeWidth = 4.dp
                            )
                        }

                        Text(
                            text = "Memproses...",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = if (hasilState.statusMessage.isNotBlank())
                                hasilState.statusMessage
                            else
                                "Menunggu hasil identifikasi...",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                            textAlign = TextAlign.Center,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp)
                        )

                        AnimatedDots(modifier = Modifier.padding(top = 4.dp))

                        Spacer(modifier = Modifier.height(4.dp))

                        if (hasilState.showCancelButton) {
                            DefaultButton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(DEFAULT__BUTTON_SIZE),
                                enabled = true,
                                enableElevation = false,
                                text = "Batalkan",
                                onClick = {
                                    hasilViewModel.cancel()
                                    navigateToCamera()
                                }
                            )
                        }
                    }
                }
            }
        } else {
            HasilContent(
                navigateToBeritaAcara = navigateToBeritaAcara,
                hasil = hasilState.data ?: HasilTeknisDTO(),
                state = state,
                events = events,
                navigateToCamera = navigateToCamera
            )
        }
    }
}

@Composable
private fun AnimatedDots(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition()
    val a1 by transition.animateFloat(
        initialValue = 0.25f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(0)
        )
    )
    val a2 by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(200)
        )
    )
    val a3 by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(400)
        )
    )

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(modifier = Modifier.size(6.dp).alpha(a1))
        Spacer(modifier = Modifier.width(6.dp))
        Box(modifier = Modifier.size(6.dp).alpha(a2))
        Spacer(modifier = Modifier.width(6.dp))
        Box(modifier = Modifier.size(6.dp).alpha(a3))
    }
}


@Composable
private fun HasilPemeriksaanKPCadanganContent(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    navigateToBeritaAcara: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                item { HeaderSection() }
            }
        }



        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.White)
                .padding(16.dp)
        ) {
            DefaultButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(DEFAULT__BUTTON_SIZE),
                enabled = true,
                enableElevation = false,
                text = "SIMPAN",
                onClick = {
                    navigateToBeritaAcara()
                }
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
        ) {
            Text(
                "Hasil Pemeriksaan",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                )
            )
        }
    }
}


@Composable
private fun SistemPeneranganSection(state: HomeState, events: (HomeEvent) -> Unit) {
    val SECTION_ID = SECTION_PENERANGAN
    val items = state.technicalConditions.filter { it.section == SECTION_ID }

    Column(Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth().background(Color(0xFFF3E9FF))) {
            Text(
                SECTION_ID,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor
                ),
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            items.forEach { item ->
                ConditionCard(item = item, events = events)
                Spacer_8dp()
            }
        }
    }
}

@Composable
private fun SistemPengeremanSection(state: HomeState, events: (HomeEvent) -> Unit) {
    val SECTION_ID = SECTION_PENGEREMAN
    val items = state.technicalConditions.filter { it.section == SECTION_ID }

    Column(Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth().background(Color(0xFFF3E9FF))) {
            Text(
                SECTION_ID,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor
                ),
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            items.forEach { item ->
                ConditionCard(item = item, events = events)
                Spacer_8dp()
            }
        }
    }
}

@Composable
private fun BadanKendaraanSection(state: HomeState, events: (HomeEvent) -> Unit) {
    val SECTION_ID = SECTION_BADAN
    val items = state.technicalConditions.filter { it.section == SECTION_ID }

    Column(Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth().background(Color(0xFFF3E9FF))) {
            Text(
                SECTION_ID,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor
                ),
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            items.forEach { item ->
                ConditionCard(item = item, events = events)
                Spacer_8dp()
            }
        }
    }
}

@Composable
private fun KondisiBanSection(state: HomeState, events: (HomeEvent) -> Unit) {
    val SECTION_ID = SECTION_BAN
    val items = state.technicalConditions.filter { it.section == SECTION_ID }

    Column(Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth().background(Color(0xFFF3E9FF))) {
            Text(
                SECTION_ID,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor
                ),
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            items.forEach { item ->
                ConditionCard(item = item, events = events)
                Spacer_8dp()
            }
        }
    }
}

@Composable
private fun PerlengkapanSection(state: HomeState, events: (HomeEvent) -> Unit) {
    val SECTION_ID = SECTION_PERLENGKAPAN
    val items = state.technicalConditions.filter { it.section == SECTION_ID }

    Column(Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth().background(Color(0xFFF3E9FF))) {
            Text(
                SECTION_ID,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor
                ),
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            items.forEach { item ->
                ConditionCard(item = item, events = events)
                Spacer_8dp()
            }
        }
    }
}

@Composable
private fun PengukurKecepatanSection(state: HomeState, events: (HomeEvent) -> Unit) {
    val SECTION_ID = SECTION_PENGUKUR_KECEPATAN
    val items = state.technicalConditions.filter { it.section == SECTION_ID }

    Column(Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth().background(Color(0xFFF3E9FF))) {
            Text(
                SECTION_ID,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor
                ),
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            items.forEach { item ->
                ConditionCard(item = item, events = events)
                Spacer_8dp()
            }
        }
    }
}

@Composable
private fun WiperSection(state: HomeState, events: (HomeEvent) -> Unit) {
    val SECTION_ID = SECTION_WIPER
    val items = state.technicalConditions.filter { it.section == SECTION_ID }

    Column(Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth().background(Color(0xFFF3E9FF))) {
            Text(
                SECTION_ID,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor
                ),
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            items.forEach { item ->
                ConditionCard(item = item, events = events)
                Spacer_8dp()
            }
        }
    }
}

@Composable
private fun TanggapDaruratSection(state: HomeState, events: (HomeEvent) -> Unit) {
    val SECTION_ID = SECTION_TANGGAP_DARURAT
    val items = state.technicalConditions.filter { it.section == SECTION_ID }

    Column(Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth().background(Color(0xFFF3E9FF))) {
            Text(
                SECTION_ID,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor
                ),
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            items.forEach { item ->
                ConditionCard(item = item, events = events)
                Spacer_8dp()
            }
        }
    }
}


@Composable
private fun LoadingUI() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(Modifier.height(8.dp))
            Text("Mengambil hasil pemeriksaan...")
        }
    }
}

@Composable
private fun ErrorUI(msg: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("Terjadi kesalahan: $msg")
    }
}

@Composable
private fun HasilContent(
    hasil: HasilTeknisDTO,
    state: HomeState,
    events: (HomeEvent) -> Unit,
    navigateToBeritaAcara: () -> Unit,
    navigateToCamera: () -> Unit,
) {
    if (state.successTeknisPenunjang == UIComponentState.Show) {
        NotMatchDialog(
            iconRes = Res.drawable.ic_check_mark,
            title = "Pemeriksaan Selesai",
            subtitle = "Semua tahapan sudah selesai diperiksa. Lanjutkan untuk membuat Berita Acara",
            isButtonVisible = true,
            positiveLabel = "BUAT BERITA ACARA",
            onClickPositive = {
                events(HomeEvent.OnSuccessTeknisPenunjang(UIComponentState.Hide))
                navigateToBeritaAcara()
            },
            onDismissRequest = {
            }
        )
    }
    val data = hasil.data

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item { HeaderSection() }

                item {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Spacer(Modifier.height(8.dp))

                        if (data.response.isNullOrEmpty()) {
                            Text(
                                "Masih dalam proses identifikasi, silakan tunggu...",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Gray
                                )
                            )
                        }
                    }
                }

                if (!data.response.isNullOrEmpty()) {
                    items(data.response!!) { sub ->
                        SubcategorySection(
                            sub,
                            events = events,
                            state = state,
                            navigateToCamera = navigateToCamera
                        )
                    }
                }
            }

        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.White)
                .padding(16.dp)
        ) {
            DefaultButton(
                progressBarState = state.progressBarState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(DEFAULT__BUTTON_SIZE),
                enabled = true,
                enableElevation = false,
                text = "SIMPAN",
                onClick = {
                    events(HomeEvent.SubmitQuestionTeknisPenunjang)
                }
            )
        }
    }

}

@Composable
private fun SubcategorySection(
    sub: SubcategoryResponse,
    events: (HomeEvent) -> Unit,
    state: HomeState,
    navigateToCamera: () -> Unit
) {
    Column(Modifier.fillMaxWidth()) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF3E9FF))
        ) {
            Text(
                sub.subcategory_name ?: "-",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor
                ),
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            sub.questions.forEach { q ->
                ConditionTeknisPenunjang(
                    item = q,
                    events = events,
                    state = state,
                    onClickCamera = {
                        events(HomeEvent.OnSetActiveQuestionPenunjang(it))
                        navigateToCamera()
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun QuestionCard(q: QuestionResponse) {
    val isOk = q.answer_name == "Sesuai" || q.answer_name == "Ada"
    val statusColor = if (isOk) Color(0xFF1B5E20) else Color(0xFFB71C1C)

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = q.question_name ?: "-",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = q.answer_name ?: "-",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = statusColor
                )
            )
        }
    }
}

@Composable
private fun BottomButton(
    modifier: Modifier = Modifier,
    navigateToBeritaAcara: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        DefaultButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(DEFAULT__BUTTON_SIZE),
            enabled = true,
            enableElevation = false,
            text = "SIMPAN",
            onClick = navigateToBeritaAcara
        )
    }
}
