package presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.ic_checked_figma
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralAlertDialog(
    title: String,
    message: String = "",
    isShowIcon: Boolean = true,
    isShowButton:Boolean = false,
    isShowDescription:Boolean = false,
    iconImage: DrawableResource = Res.drawable.ic_checked_figma,
    positiveButtonText: String = "",
    negativeButtonText: String = "",
    onPositiveClick: () -> Unit,
    onNegativeClick: () -> Unit,
    onDismissRequest: () -> Unit,
) {

    CustomAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .fillMaxWidth().background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp).verticalScroll(rememberScrollState()),
        ) {

            if (isShowIcon) {
                Image(
                    painterResource(iconImage),
                    null,
                    modifier = Modifier.size(width = 66.dp, height = 66.dp)
                        .align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.Fit
                )

                Spacer_24dp()
            }

            Text(
                title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            if(isShowDescription){
                Spacer_8dp()
                Text(
                    message,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            if(isShowButton){
                Spacer_32dp()
                Row(modifier = Modifier.fillMaxWidth()) {

                    DefaultButton(
                        modifier = Modifier.weight(1f).height(DEFAULT__BUTTON_SIZE),
                        text = positiveButtonText,
                        onClick = {
                            onPositiveClick()
                            onDismissRequest()
                        }
                    )
                    Spacer_8dp()
                    DefaultButton(
                        modifier = Modifier.weight(1f).height(DEFAULT__BUTTON_SIZE),
                        text = negativeButtonText,
                        onClick = {
                            onNegativeClick()
                            onDismissRequest()
                        }
                    )
                }
            }

        }

    }

}
