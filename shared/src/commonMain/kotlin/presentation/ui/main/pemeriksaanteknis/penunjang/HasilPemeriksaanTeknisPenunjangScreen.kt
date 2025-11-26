package presentation.ui.main.pemeriksaanteknis.penunjang

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
import kotlinx.coroutines.flow.Flow
import org.koin.compose.viewmodel.koinViewModel
import presentation.component.ConditionCard
import presentation.component.ConditionTeknisPenunjang
import presentation.component.ConditionTeknisUtama
import presentation.component.DEFAULT__BUTTON_SIZE
import presentation.component.DefaultButton
import presentation.component.DefaultScreenUI
import presentation.component.NotMatchDialog
import presentation.component.Spacer_8dp
import presentation.theme.PrimaryColor
import presentation.ui.main.home.view_model.HomeEvent

import presentation.ui.main.home.view_model.HomeState
import presentation.ui.main.pemeriksaanteknis.getresult.HasilTeknisViewModel
import presentation.ui.main.pemeriksaanteknis.penunjang.viewmodel.GetResultSecondViewModel
import presentation.ui.main.pemeriksaanteknis.penunjang.viewmodel.IdentifyPenunjangViewModel
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.ic_check_mark
import rampcheck.shared.generated.resources.ic_kemenhub

//@Composable
//fun HasilPemeriksaanTeknisUtamaScreen(
//    state: HomeState,
//    events: (HomeEvent) -> Unit,
//    errors: Flow<UIComponent>,
//    popup: () -> Unit,
//    navigateToBeritaAcara: () -> Unit
//) {
//
//    DefaultScreenUI(
//        errors = errors,
//        progressBarState = state.progressBarState,
//        titleToolbar = "Pemeriksaan Teknis Utama",
//        startIconToolbar = Icons.AutoMirrored.Filled.ArrowBack,
//        onClickStartIconToolbar = { popup() },
//        endIconToolbar = Res.drawable.ic_kemenhub
//    ) {
//        HasilPemeriksaanKPCadanganContent(
//            state = state,
//            events = events,
//            navigateToBeritaAcara = navigateToBeritaAcara
//        )
//
//    }
//}

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

    val stillWaiting =
        hasilState.error == null && (
                hasilState.isLoading ||
                        currentStatus == "sent" ||
                        currentStatus == "processing" ||
                        currentStatus.isNullOrBlank()
                )

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

//         🔹 Dialog error khusus hasil teknis
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
        if (stillWaiting) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer_8dp()
                    Text("Menunggu hasil identifikasi...")
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
//                item { SistemPeneranganSection(state, events) }
//                item { SistemPengeremanSection(state, events) }
//                item { BadanKendaraanSection(state, events) }
//                item { KondisiBanSection(state, events) }
//                item { PerlengkapanSection(state, events) }
//                item { PengukurKecepatanSection(state, events) }
//                item { WiperSection(state, events) }
//                item { TanggapDaruratSection(state, events) }
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


//package presentation.ui.main.pemeriksaanteknis
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ArrowBack
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import business.core.ProgressBarState
//import business.core.UIComponent
//import kotlinx.coroutines.flow.emptyFlow
//import business.datasource.network.main.responses.HasilTeknisDTO
//import business.datasource.network.main.responses.SubcategoryResponse
//import business.datasource.network.main.responses.QuestionResponse
//import kotlinx.coroutines.flow.Flow
//import org.koin.compose.viewmodel.koinViewModel
//import presentation.component.DEFAULT__BUTTON_SIZE
//import presentation.component.DefaultButton
//import presentation.component.DefaultScreenUI
//import presentation.theme.PrimaryColor
//import rampcheck.shared.generated.resources.Res
//import rampcheck.shared.generated.resources.ic_kemenhub
//
//@Composable
//fun HasilPemeriksaanTeknisUtamaScreen(
//    errors: Flow<UIComponent>,
//    popup: () -> Unit,
//    navigateToBeritaAcara: () -> Unit,
//    viewModel: HasilTeknisViewModel = koinViewModel()
//) {
//    val state by viewModel.state.collectAsState()
//
//    DefaultScreenUI(
//        errors = errors,
//        // Kalau mau, bisa ganti: if (state.isLoading) ProgressBarState.Loading else ProgressBarState.Idle
//        progressBarState = ProgressBarState.Idle,
//        titleToolbar = "Pemeriksaan Teknis Utama",
//        startIconToolbar = Icons.AutoMirrored.Filled.ArrowBack,
//        onClickStartIconToolbar = { popup() },
//        endIconToolbar = Res.drawable.ic_kemenhub
//    ) {
//        when {
//            state.isLoading -> LoadingUI()
//            state.error != null -> ErrorUI(state.error!!)
//            state.data != null -> HasilContent(
//                hasil = state.data!!,
//                navigateToBeritaAcara = navigateToBeritaAcara
//            )
//            else -> {
//                // optional: kosong / initial state
//            }
//        }
//    }
//}
//
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

//
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

//
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

                // Info file & status
                item {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Spacer(Modifier.height(8.dp))

                        // ⬇ kalau response belum ada, kasih info ke user
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

                // ⬇ Render list subcategory hanya kalau response sudah ada
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

        // kalau mau aktifkan tombol di bawah:
        // BottomButton(
        //     modifier = Modifier
        //         .align(Alignment.BottomCenter),
        //     navigateToBeritaAcara = navigateToBeritaAcara
        // )
    }

}

//
//@Composable
//private fun HeaderSection() {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//    ) {
//        Text(
//            "Hasil Pemeriksaan",
//            style = MaterialTheme.typography.labelLarge.copy(
//                fontWeight = FontWeight.Bold,
//            )
//        )
//    }
//}
//
@Composable
private fun SubcategorySection(
    sub: SubcategoryResponse,
    events: (HomeEvent) -> Unit,
    state: HomeState,
    navigateToCamera: () -> Unit
) {
    Column(Modifier.fillMaxWidth()) {

        // header kategori (mirip SECTION_PENERANGAN, dll)
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

        // isi pertanyaan + jawaban
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

//
@Composable
private fun QuestionCard(q: QuestionResponse) {
    // warna jawaban: sesuai / ada = hijau, lainnya merah
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
//
//
//
