package presentation.component

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.ic_checked_figma
import rampcheck.shared.generated.resources.ic_info_fill
import rampcheck.shared.generated.resources.no_wifi
import business.constants.CUSTOM_TAG
import business.core.NetworkState
import business.core.ProgressBarState
import business.core.Queue
import business.core.UIComponent
import business.core.ViewSingleAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import logger.Logger
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import presentation.theme.BackgroundCustom
import presentation.theme.BgHeaderHome
import presentation.theme.PrimaryColor
import rampcheck.shared.generated.resources.blur_red
import rampcheck.shared.generated.resources.blur_yellow
import rampcheck.shared.generated.resources.ecllipse_decor
import rampcheck.shared.generated.resources.ic_kemenhub

@Composable
fun DefaultScreenUI(
    errors: Flow<UIComponent> = MutableSharedFlow(),
    progressBarState: ProgressBarState = ProgressBarState.Idle,
    networkState: NetworkState = NetworkState.Good,
    onTryAgain: () -> Unit = {},
    titleToolbar: String? = null,
    startIconToolbar: ImageVector? = null,
    endIconToolbar: DrawableResource? = null,
    onClickStartIconToolbar: () -> Unit = {},
    onClickEndIconToolbar: () -> Unit = {},
    parent: Boolean = false,
    isShowBgHeader: Boolean = false,
    isCamera: Boolean = false,
    content: @Composable () -> Unit,
) {

    val errorQueue = remember {
        mutableStateOf<Queue<UIComponent>>(Queue(mutableListOf()))
    }

    Scaffold(
        topBar = {
            if (titleToolbar != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .background(if (isCamera) Color.Black else PrimaryColor)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (startIconToolbar != null) {
                                androidx.compose.material.Icon(
                                    modifier = Modifier.size(24.dp).clickable {  onClickStartIconToolbar() },
                                    imageVector = startIconToolbar,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            } else {

                                Spacer_16dp()
                            }

                            Text(
                                titleToolbar,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.background,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }

                        if (endIconToolbar != null) {
                            Image(
                                modifier = Modifier.size(32.dp),
                                painter = painterResource(endIconToolbar),
                                contentDescription = null
                            )
                        } else {
                            Spacer_32dp()
                        }
                    }
                }
            }
        }
    ) {

        Box(
            modifier =
                if (!parent) Modifier.padding(top = it.calculateTopPadding())
                    .fillMaxSize()
                    .background(BackgroundCustom)
                else Modifier
                    .fillMaxSize()
                    /*.paint(painterResource(Res.drawable.ic_ferry), contentScale = ContentScale.Crop),*/
                    .background(BackgroundCustom),
            contentAlignment = Alignment.Center
        ) {

            if (isShowBgHeader) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .height(221.dp)
                        .background(BgHeaderHome)
                ) {
                    Image(
                        painter = painterResource(Res.drawable.ecllipse_decor),
                        contentDescription = "decoration",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Box {
                content()

                LaunchedEffect(errors) {
                    errors.collect { errors ->
                        errorQueue.appendToMessageQueue(errors)
                    }
                }

                // process the queue
                if (!errorQueue.value.isEmpty()) {
                    errorQueue.value.peek()?.let { uiComponent ->
                        if (uiComponent is UIComponent.Dialog) {
                            /*CreateUIComponentDialog(
                                title = uiComponent.alert.title,
                                description = uiComponent.alert.message,
                                status = uiComponent.status,
                                onRemoveHeadFromQueue = { errorQueue.removeHeadMessage() }
                            )*/
                            ModernAdaptiveDialog(
                                config = DialogConfig(
                                    title = uiComponent.alert.title,
                                    description = uiComponent.alert.message,
                                    icon = if (!uiComponent.status) Res.drawable.ic_info_fill else Res.drawable.ic_checked_figma,
                                    iconTint = if (!uiComponent.status) Color(0xFFF44336) else Color(0xFF4CAF50),
                                    autoDismissDelay = 3000L,
                                    showCloseButton = false,
                                    enableBackgroundDismiss = true,
                                    animationType = DialogAnimationType.SLIDE_UP
                                ),
                                onDismiss = { errorQueue.removeHeadMessage() }
                            )
                        }
                        if (uiComponent is UIComponent.ToastSimple) {
                            ShowSnackbar(
                                title = uiComponent.title,
                                snackbarVisibleState = true,
                                onDismiss = { errorQueue.removeHeadMessage() },
                                modifier = Modifier.align(Alignment.BottomCenter)
                            )
                        }
                    }
                }

                if (networkState == NetworkState.Failed && progressBarState == ProgressBarState.Idle) {
                    FailedNetworkScreen(onTryAgain = onTryAgain)
                }

                if (progressBarState is ProgressBarState.LoadingWithLogo) {
                    LoadingWithLogoScreen()
                }

                if (progressBarState is ProgressBarState.ScreenLoading || progressBarState is ProgressBarState.FullScreenLoading) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

private fun MutableState<Queue<UIComponent>>.appendToMessageQueue(uiComponent: UIComponent) {
    if (uiComponent is UIComponent.None) {
        Logger.d("$CUSTOM_TAG appendToMessageQueue:  ${uiComponent.message}")
        return
    }

    val queue = this.value
    queue.add(uiComponent)

    this.value = Queue(mutableListOf()) // force to recompose
    this.value = queue
}

private fun MutableState<Queue<UIComponent>>.removeHeadMessage() {
    if (this.value.isEmpty()) {
        Logger.d("$CUSTOM_TAG: removeHeadMessage: Nothing to remove from DialogQueue")
        return
    }
    val queue = this.value
    queue.remove() // can throw exception if empty
    this.value = Queue(mutableListOf()) // force to recompose
    this.value = queue
}

@Composable
fun <Effect : ViewSingleAction> EffectHandler(
    effectFlow: Flow<Effect>,
    onHandleEffect: (Effect) -> Unit
) {
    LaunchedEffect(Unit) {
        effectFlow.collect { effect ->
            onHandleEffect(effect)
        }
    }
}

@Composable
fun FailedNetworkScreen(onTryAgain: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(painterResource(Res.drawable.no_wifi), null)
        Spacer(modifier = Modifier.size(32.dp))
        Text(
            text = "You are currently offline, please reconnect and try again.",
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.size(16.dp))

        DefaultButton(
            text = "Try Again",
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    DEFAULT__BUTTON_SIZE
                ),
            onClick = onTryAgain
        )
    }
}

@Composable
fun LoadingWithLogoScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
/*

            Image(
                painterResource("logo.xml"),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
            Spacer_16dp()
*/

            CircularProgressIndicator()

        }
    }
}













