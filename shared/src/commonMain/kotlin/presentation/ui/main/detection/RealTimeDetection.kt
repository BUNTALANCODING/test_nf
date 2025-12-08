package presentation.ui.main.detection

import androidx.compose.runtime.Composable
import business.core.UIComponent
import common.detectcam.RealTimeCameraView
import kotlinx.coroutines.flow.Flow
import presentation.component.DefaultScreenUI
import presentation.ui.main.home.view_model.HomeEvent
import presentation.ui.main.home.view_model.HomeState

@Composable
fun RealTimeDetectionScreen(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    errors: Flow<UIComponent>,
    popup: () -> Unit,
    navigateToRealTimeCam: () -> Unit
) {
    DefaultScreenUI(
        errors = errors,
        progressBarState = state.progressBarState,
    ) {
        RealTimeDetectionContent(
            state = state,
            events = events,
            errors = errors,
            popup = popup,
            navigateToRealTimeCam = navigateToRealTimeCam
        )
    }
}

@Composable
fun RealTimeDetectionContent(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    errors: Flow<UIComponent>,
    popup: () -> Unit,
    navigateToRealTimeCam: () -> Unit
) {
    RealTimeCameraView()
}
