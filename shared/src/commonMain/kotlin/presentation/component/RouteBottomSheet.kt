package presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import business.core.UIComponentState
import business.datasource.network.main.responses.Route
import presentation.ui.main.home.view_model.HomeEvent
import presentation.ui.main.home.view_model.HomeState

@Composable
fun RouteBottomSheet(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    val recent = remember { state.home.route }
    //val popular = remember { listOf(state.home.route[2]) }

    var search by remember { mutableStateOf("") }
    val filtered = remember(search) {
        if (search.isBlank()) state.home.route else
            state.home.route.filter {
                it.originPortName!!.contains(
                    search,
                    ignoreCase = true
                ) || it.destinationPortName!!.contains(search, ignoreCase = true)
            }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            placeholder = { Text("Cari pelabuhan atau kota") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
        )

        Spacer_16dp()

        LazyColumn {
            if (search.isBlank()) {
                items(recent) { harbor ->
                    FerryTerminalRow(harbor = harbor, typeRoute = state.typeRoute, onClick = {

                    })
                }
            } else {
                items(filtered) { harbor ->
                    FerryTerminalRow(harbor = harbor, typeRoute = state.typeRoute, onClick = {

                    })
                }
            }
        }
    }
}

@Composable
fun FerryTerminalRow(harbor: Route, typeRoute: String, onClick: (Route) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(harbor) }
            .padding(vertical = 8.dp)
    ) {
        when (typeRoute) {
            "origin" -> {
                Text(
                    harbor.originPortName ?: "-",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    harbor.originPortDescription ?: "-",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            "destination" -> {
                Text(
                    harbor.destinationPortName ?: "-",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    harbor.destinationPortDescription ?: "-",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}