package presentation.ui.main.pemeriksaanadministrasi.kartuuji

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.RadioButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import business.constants.CARD_NOT_AVAILABLE
import business.constants.SECTION_KARTU_UJI
import business.core.UIComponent
import business.datasource.network.main.request.AnswersItem
import common.toBase64
import common.toBytes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.painterResource
import presentation.component.ConditionCard
import presentation.component.ConditionKartuUjiCard
import presentation.component.DEFAULT__BUTTON_SIZE
import presentation.component.DefaultButton
import presentation.component.DefaultScreenUI
import presentation.component.Spacer_16dp
import presentation.component.Spacer_48dp
import presentation.component.Spacer_8dp
import presentation.theme.PrimaryColor
import presentation.ui.main.datapemeriksaan.kir.ButtonNextSection
import presentation.ui.main.home.view_model.HomeEvent
import presentation.ui.main.home.view_model.HomeState
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.ic_identity
import rampcheck.shared.generated.resources.ic_kartu_not_available
import rampcheck.shared.generated.resources.ic_kemenhub
import rampcheck.shared.generated.resources.kartu_uji

@Composable
fun HasilPemeriksaanKartuUjiScreen(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    errors: Flow<UIComponent>,
    popup: () -> Unit,
    navigateToCameraNegative: () -> Unit
) {

    DefaultScreenUI(
        errors = errors,
        progressBarState = state.progressBarState,
        titleToolbar = "Pemeriksaan Administrasi",
        startIconToolbar = Icons.AutoMirrored.Filled.ArrowBack,
        onClickStartIconToolbar = { popup() },
        endIconToolbar = Res.drawable.ic_kemenhub
    ) {
        PemeriksaanKartuUjiContent(
            state = state,
            events = events,
            navigateToCameraNegative = navigateToCameraNegative
        )
    }
}

@Composable
private fun PemeriksaanKartuUjiContent(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    navigateToCameraNegative: () -> Unit
) {
    var base64 by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(state.tidakSesuaiBitmap) {
        if (state.tidakSesuaiBitmap != null) {
            coroutineScope.launch {
                base64 = withContext(Dispatchers.Default) {
                    state.tidakSesuaiBitmap.toBytes().toBase64()
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            HeaderSection(state)
            CardSection(state, events, navigateToCameraNegative = navigateToCameraNegative)
            Spacer(Modifier.weight(1f))
            ButtonNextSection("LANJUT", state, onClick = {

                if (state.availableCard == CARD_NOT_AVAILABLE) {
                    events(HomeEvent.NegativeAnswerUji)
                } else {
                    events(HomeEvent.SubmitQuestion)
                }
            })
        }
    }
}

@Composable
private fun CardSection(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    navigateToCameraNegative: () -> Unit
) {
    if (state.availableCard == CARD_NOT_AVAILABLE) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(
                "Hasil Pemeriksaan",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                )
            )
            Spacer_16dp()
            Text(
                "Kartu Uji/STUK",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Normal,
                )
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
                    state.keteranganKartuTidakAda ?: "",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }
    } else {
        val items = state.listIdentifyKartuUji
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Text(
                "Hasil Pemeriksaan",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                )
            )
            Spacer_16dp()
            items.forEach { item ->
                ConditionKartuUjiCard(
                    item = item, events = events, state = state,
                    value = state.tidakSesuai,
                    onValueChange = {
                        events(HomeEvent.OnUpdateTidakSesuai(it))
                        events(
                            HomeEvent.OnUpdateListSubmitQuestion(
                                listOf(
                                    AnswersItem(
                                        answerId = state.identifyKartuUji.answerId,
                                        answerCondition = state.tidakSesuai,
                                        answerFile = state.tidakSesuaiBase64,
                                        answerOptionId = state.selectionKartuUji,
                                        questionId = state.identifyKartuUji.questionId
                                    )
                                )
                            )
                        )
                    },
                    onClickCamera = {
                        navigateToCameraNegative()
                    }
                )
                Spacer_8dp()
            }

        }
    }

}


@Composable
private fun HeaderSection(state: HomeState) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer_16dp()
            Column(
                modifier = Modifier.fillMaxWidth().height(120.dp).background(Color(0xFFE5E5E5)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (state.availableCard == CARD_NOT_AVAILABLE) {
                    Image(
                        painter = painterResource(Res.drawable.ic_kartu_not_available),
                        modifier = Modifier.height(110.dp),
                        contentDescription = null
                    )
                } else {
                    state.kartuUjiBitmap?.let {
                        Image(
                            contentDescription = null,
                            modifier = Modifier.width(223.dp).height(320.dp),
                            bitmap = it
                        )
                    }
                }

            }
            Spacer_8dp()
            Text(
                "KARTU UJI/STUK",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}


