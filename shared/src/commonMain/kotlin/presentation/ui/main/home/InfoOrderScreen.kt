package presentation.ui.main.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import be.digitalia.compose.htmlconverter.htmlToAnnotatedString
import business.core.UIComponent
import kotlinx.coroutines.flow.Flow
import presentation.component.DefaultScreenUI
import presentation.ui.main.home.view_model.HomeEvent
import presentation.ui.main.home.view_model.HomeState

@Composable
fun InfoOrderScreen(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    errors: Flow<UIComponent>,
    popup: () -> Unit
) {

    DefaultScreenUI(
        errors = errors,
        progressBarState = state.progressBarState,
        networkState = state.networkState,
        titleToolbar = "Cara Memesan Tiket",
        startIconToolbar = Icons.AutoMirrored.Filled.ArrowBack,
        onClickStartIconToolbar = { popup() },
        onTryAgain = {  }
    ) {

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            Text(text = remember(state.home.guide[0].guideContent.toString()) { htmlToAnnotatedString(state.home.guide[0].guideContent.toString()) })
        }
    }
}