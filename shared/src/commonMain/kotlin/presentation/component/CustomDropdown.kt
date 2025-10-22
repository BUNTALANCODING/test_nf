package presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import business.core.UIComponentState
import business.datasource.network.main.responses.GetLocationDTO
import business.datasource.network.main.responses.GetVehicleDTO
import org.jetbrains.compose.resources.painterResource
import presentation.ui.main.home.view_model.HomeEvent
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.arrow_down

@Composable
fun CustomDropdownPicker(
    options: List<GetLocationDTO>,
    modifier: Modifier = Modifier,
    value: String,
    expanded: Boolean,
    onShowDropdown: () -> Unit,
    onHideDropdown: () -> Unit,
    onValueChange : (String) -> Unit,
    onOptionSelected: (GetLocationDTO) -> Unit,

    ) {
    var rowSize by remember { mutableStateOf(Size.Zero) }
    Column(modifier = modifier.onGloballyPositioned { layoutCoordinates ->
        rowSize = layoutCoordinates.size.toSize()
    }){
        DefaultTextField(
            modifier = Modifier.fillMaxWidth().height(DEFAULT__BUTTON_SIZE).noRippleClickable {
                onShowDropdown()
            },
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            enabled = false,
            placeholder = "Pilih Lokasi",
            textStyle = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Normal
            ),
            iconEnd = {
                androidx.compose.material.Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Pilih Lokasi",
                    tint = Color.Black,
                    modifier = Modifier.size(16.dp)
                )
            },
            color = Color.White
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onHideDropdown() },
            modifier = Modifier
                .width(with(LocalDensity.current) { rowSize.width.toDp() }),
            containerColor = Color.White

        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option.rampcheckLocationName ?: "",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Normal
                            )
                        )
                    },
                    onClick = {
                        onOptionSelected(option)
                        onHideDropdown()
                    }
                )
            }
        }
    }
}

@Composable
fun VehicleDropdownPicker(
    options: List<GetVehicleDTO>,
    modifier: Modifier = Modifier,
    value: String,
    expanded: Boolean,
    onShowDropdown: () -> Unit,
    onHideDropdown: () -> Unit,
    onValueChange : (String) -> Unit,
    onOptionSelected: (GetVehicleDTO) -> Unit,

    ) {
    var rowSize by remember { mutableStateOf(Size.Zero) }
    Column(modifier = modifier.onGloballyPositioned { layoutCoordinates ->
        rowSize = layoutCoordinates.size.toSize()
    }){
        DefaultTextField(
            modifier = Modifier.fillMaxWidth().height(DEFAULT__BUTTON_SIZE).noRippleClickable {
                onShowDropdown()
            },
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            enabled = false,
            placeholder = "Pilih Nomor Kendaraan",
            textStyle = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Normal
            ),
            iconEnd = {
                androidx.compose.material.Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Pilih Nomor Kendaraan",
                    tint = Color.Black,
                    modifier = Modifier.size(16.dp)
                )
            },
            color = Color.White
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onHideDropdown() },
            modifier = Modifier
                .width(with(LocalDensity.current) { rowSize.width.toDp() }),
            containerColor = Color.White

        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option.platNumber ?: "",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Normal
                            )
                        )
                    },
                    onClick = {
                        onOptionSelected(option)
                        onHideDropdown()
                    }
                )
            }
        }
    }
}