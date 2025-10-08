package presentation.ui.main.home


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import business.core.UIComponent
import kotlinx.coroutines.flow.Flow
import org.jetbrains.compose.resources.painterResource
import presentation.component.DefaultScreenUI
import presentation.component.Spacer_4dp
import presentation.component.Spacer_8dp
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.ic_notification

@Composable
fun NotificationScreen(
    errors: Flow<UIComponent>,
    popup: () -> Unit,
    navigateToVerify: () -> Unit
) {

    DefaultScreenUI(
        errors = errors,
        titleToolbar = "Notification",
        onClickStartIconToolbar = { popup() },
    ) {
        NotificationContent(

            navigateToVerify = navigateToVerify
        )

    }
}

@Composable
private fun NotificationContent(
    navigateToVerify: () -> Unit
) {
    val notifItems = listOf(
        NotifikasiItem(
            title = "Isi Saldo Samsat Ceria Berhasil",
            subtitle = "Saldo Anda berhasil ditambahkan, Terima kasih telah menggunakan Samsat Ceria",
            date = "2 Hari yang lalu"
        ),
        NotifikasiItem(
            title = "Pengesahan berhasil",
            subtitle = "Terima kasih",
            date = "2 Hari yang lalu"
        ),
        NotifikasiItem(
            title = "Pengesahan berhasil",
            subtitle = "Terima kasih",
            date = "2 Hari yang lalu"
        ),

        )
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            itemsIndexed(notifItems) { index, item ->
                NotificationList(
                    item = item
                )
            }
        }
    }
}


@Composable
fun NotificationList(item: NotifikasiItem) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(Res.drawable.ic_notification),
            contentDescription = null
        )
        Spacer_8dp()
        Column(Modifier.fillMaxWidth()) {
            Text(
                item.title,
                style = MaterialTheme.typography.labelMedium
            )
            Spacer_4dp()
            Text(
                item.subtitle,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = Color.Gray
                )
            )
            Spacer_8dp()
            Text(
                item.date,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Normal
                )
            )

        }
    }
    Spacer_8dp()
    Divider(color = Color.LightGray, thickness = 1.dp)
    Spacer_8dp()

}

data class NotifikasiItem(
    val title: String,
    val subtitle: String,
    val date: String
)
