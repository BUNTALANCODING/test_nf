package presentation.ui.main.riwayat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import business.core.UIComponent
import business.datasource.network.main.responses.HistoryRampcheckDTOItem
import kotlinx.coroutines.flow.Flow
import presentation.component.DefaultScreenUI
import presentation.component.ExaminationCard
import presentation.component.Spacer_16dp
import presentation.ui.main.riwayat.viewmodel.RiwayatEvent
import presentation.ui.main.riwayat.viewmodel.RiwayatState
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.ic_kemenhub

@Composable
fun ListRiwayatPemeriksaanScreen(
    state: RiwayatState,
    events: (RiwayatEvent) -> Unit,
    errors: Flow<UIComponent>,
    popup: () -> Unit,
    navigateToPreview: () -> Unit
) {

    DefaultScreenUI(
        errors = errors,
        progressBarState = state.progressBarState,
        titleToolbar = "Riwayat Pemeriksaan",
        startIconToolbar = Icons.AutoMirrored.Filled.ArrowBack,
        onClickStartIconToolbar = { popup() },
        endIconToolbar = Res.drawable.ic_kemenhub
    ) {
        ListRiwayatPemeriksaanContent(
            state = state,
            events = events,
            navigateToPreview = navigateToPreview
        )

    }
}

@Composable
private fun ListRiwayatPemeriksaanContent(
    state: RiwayatState,
    events: (RiwayatEvent) -> Unit,
    navigateToPreview: () -> Unit
) {

    LaunchedEffect(Unit) {
        events(RiwayatEvent.GetListRiwayat)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth()) {
//            state.dataRiwayat.historyRampcheckDTO?.let {

                RiwayatSection(state.listRiwayat ?: listOf(), { data ->
                    events(RiwayatEvent.UpdateStatusRiwayat(data))

                }, {
                    events(RiwayatEvent.OnUpdateRampcheckId(it.rampcheckId ?: 0))
                    navigateToPreview()
                }
                )
//            }
        }
    }
}

@Composable
fun RiwayatSection(list: List<HistoryRampcheckDTOItem?> = listOf(), onClickTab: (Int) -> Unit, onClick: (HistoryRampcheckDTOItem) -> Unit) {
    // 0 is "Belum Selesai", 1 is "Selesai"
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Belum Selesai", "Selesai", "Gagal")

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth()) {

            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.White,
                contentColor = Color.Black,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier
                            .tabIndicatorOffset(tabPositions[selectedTabIndex])
                            .height(3.dp)
                            .padding(horizontal = 8.dp),
                        color = Color(0xFFFF9800) // Orange color
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = {
                            selectedTabIndex = index


                            onClickTab(index + 1)
                        },
                        text = {
                            Text(
                                text = title,
                                color = if (selectedTabIndex == index) Color(0xFFFF9800) else Color.Gray,
                                fontWeight = if (selectedTabIndex == index) FontWeight.SemiBold else FontWeight.Normal
                            )
                        },
                        selectedContentColor = Color(0xFFFF9800),
                        unselectedContentColor = Color.Gray
                    )
                }
            }


            Divider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 1.dp)

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                Text(
                    text = "Daftar Pemeriksaan",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Spacer_16dp()

                // --- BAGIAN LIST ITEM ---
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if(list.isNotEmpty()){
                        items(list) { item ->
                            ExaminationCard(item = item!!, selectedTabIndex, onClick = {

                                when(selectedTabIndex){
                                    1 -> {
                                        onClick(item)
                                    }
                                }

                            })

                        }

                    } else {
                        item {
                            // Tampilan untuk tab "Selesai"
                            Text(
                                "Tidak ada data ",
                                Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                textAlign = TextAlign.Center,
                                color = Color.Gray
                            )
                        }

                    }

                }
            }
        }

    }
}

