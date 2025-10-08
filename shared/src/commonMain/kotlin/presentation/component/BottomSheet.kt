package presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import business.datasource.network.main.request.Passenger
import business.datasource.network.main.responses.CargoCategoryDTO
import business.datasource.network.main.responses.City
import business.datasource.network.main.responses.Gender
import business.datasource.network.main.responses.IdentityType
import business.datasource.network.main.responses.PassengerCategory
import business.domain.main.Country
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import presentation.theme.BorderOptionColor
import presentation.theme.ButtonPrimaryColor
import presentation.ui.main.home.view_model.HomeEvent
import presentation.ui.main.home.view_model.HomeState
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.ic_car_milik_orang_lain
import rampcheck.shared.generated.resources.ic_car_milik_pribadi
import rampcheck.shared.generated.resources.ic_checked_figma
import rampcheck.shared.generated.resources.ic_jne

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CekPajakBottomDialog(
    onDismiss: () -> Unit,
    navigateToDaftarKendaraan: () -> Unit,
    navigateToMilikOrangLain: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background,
        sheetState = sheetState,
        dragHandle = null
    ) {

        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Cek Pajak Kendaraan",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }


        Spacer_8dp()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            DialogItem(
                iconRes = Res.drawable.ic_car_milik_pribadi,
                title = "Milik Pribadi",
                subtitle = "Lihat detail pajak dan perpanjang STNK milik Anda",
                onClick = {
                    onDismiss()
                    navigateToDaftarKendaraan()
                }
            )

            Spacer_12dp()

            DialogItem(
                iconRes = Res.drawable.ic_car_milik_orang_lain,
                title = "Milik Orang Lain",
                subtitle = "Bantu cek pajak kendaraan milik orang lain",
                onClick = {
                    onDismiss()
                    navigateToMilikOrangLain()
                }
            )
            Spacer_16dp()

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryBottomDialog(
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background,
        sheetState = sheetState,
        dragHandle = null
    ) {

        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Pilih Opsi Pengiriman",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }


        Spacer_8dp()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            DialogItem(
                iconRes = Res.drawable.ic_jne,
                title = "JNE Reguler (3-5 Hari Kerja)",
                subtitle = "Rp. 15.000",
                onClick = {
                    onDismiss()
                }
            )

            Spacer_12dp()

            DialogItem(
                iconRes = Res.drawable.ic_jne,
                title = "JNE Instan(1-2 Hari Kerja)",
                subtitle = "Rp. 25.000",
                onClick = {}
            )
            Spacer_16dp()

        }

    }
}


@Composable
fun DialogItem(
    iconRes: DrawableResource,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = BorderOptionColor, RoundedCornerShape(10.dp))
            .background(Color.White)
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = null,
        )
        Spacer_12dp()
        Column {
            Text(
                title,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                subtitle,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketTypeBottomSheet(
    ticketType: List<CargoCategoryDTO>,
    selectedType: String = "",
    onSelect: (CargoCategoryDTO) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background,
        sheetState = sheetState,
        dragHandle = null,
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer_32dp()

        IconButton(onClick = onDismiss) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer_8dp()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {

            Text(
                "Pilih Tipe Tiket",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(end = 48.dp)
            )

            Spacer_8dp()

            ticketType.forEach { type ->
                TicketTypeRow(
                    type = type,
                    isSelected = type.cargoCategoryName == selectedType,
                    onClick = { onSelect(type) }
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            }
        }
    }
}

@Composable
fun TicketTypeRow(
    type: CargoCategoryDTO,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        /*MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)*/
        Color.Transparent
    } else {
        Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(backgroundColor, shape = RoundedCornerShape(8.dp))
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberCustomImagePainter(type.cargoCategoryImg),
            contentDescription = null,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Fit,
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                type.cargoCategoryName ?: "-",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                type.cargoCategoryAlias + " (Maks. " + type.maxPassenger.toString() + " penumpang)",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdentityTypeBottomSheet(
    state: HomeState,
    events: (HomeEvent) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
//        onDismissRequest = { events(HomeEvent.OnIdentityState(UIComponentState.Hide)) },
        containerColor = MaterialTheme.colorScheme.background,
        sheetState = sheetState,
        dragHandle = null,
        onDismissRequest = {}
    ) {

        Spacer_32dp()

        Column(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(onClick = {  }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    "Pilih Tipe Identitas",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                )
            }

            Spacer_8dp()

            // List of ticket types
            state.home.identityType.forEach { type ->
                IdentityTypeRow(
                    type = type,
                    isSelected = type == state.selectedIdentityType,
                    onClick = {
                    }
                )
                Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            }

            Spacer_32dp()
        }
    }
}

@Composable
fun IdentityTypeRow(
    type: IdentityType,
    isSelected: Boolean,
    onClick: (IdentityType) -> Unit
) {
    val backgroundColor = if (isSelected) {
        /*MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)*/
        Color.Transparent
    } else {
        Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(type) }
            .background(backgroundColor, shape = RoundedCornerShape(8.dp))
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                type.identityTypeName.toString(),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PassengerCountBottomSheet(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var buttonEnabled by remember { mutableStateOf(true) }

    // Temporary mutable passenger list state with quantity initialized/restored
    val tempListPass = remember { mutableStateListOf<PassengerCategory>() }

    // Restore or reset passenger counts when selectedTicketType or passengerCategory changes
    LaunchedEffect(state.selectedTicketType, state.home.passengerCategory) {
        val savedCounts = state.home.passengerCategory

        tempListPass.clear()
        tempListPass.addAll(
            state.home.passengerCategory.map { category ->
                val saved =
                    savedCounts.find { it.passengerCategoryId == category.passengerCategoryId }
                category.copy(passengerCategoryQty = saved?.passengerCategoryQty ?: 0)
            }
        )
        buttonEnabled = true
    }

    // Calculate total passengers helper function
    fun totalPassengers(): Int = tempListPass.sumOf { it.passengerCategoryQty ?: 0 }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background,
        sheetState = sheetState,
        dragHandle = null,
    ) {

        Spacer_16dp()

        IconButton(onClick = onDismiss) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {

            Text(
                "Atur Jumlah Penumpang",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer_4dp()

            Text(
                "Pastikan jumlah penumpangnya sesuai biar dapat asuransi perjalanan.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer_16dp()

            LazyColumn {
                itemsIndexed(tempListPass) { index, pass ->
                    PassengerRow(
                        label = pass.passengerCategoryName ?: "",
                        count = pass.passengerCategoryQty ?: 0,
                        maxCount = state.selectedTicketType.maxPassenger ?: 0,
                        totalPassengers = totalPassengers(),
                        onIncrease = {
                            val maxTotal = state.selectedTicketType.maxPassenger ?: 0
                            val currentTotal = totalPassengers()
                            if (currentTotal < maxTotal) {
                                tempListPass[index] = pass.copy(
                                    passengerCategoryQty = (pass.passengerCategoryQty ?: 0) + 1
                                )
                                buttonEnabled = true
                            } else {
                                // Max total reached, disable button
                                buttonEnabled = false
                            }
                        },
                        onDecrease = {
                            val currentCount = pass.passengerCategoryQty ?: 0
                            if (currentCount > 0) {
                                tempListPass[index] = pass.copy(
                                    passengerCategoryQty = currentCount - 1
                                )
                                buttonEnabled = true
                            }
                        }
                    )
                }
            }

            Spacer_24dp()

            DefaultButton(
                text = "Simpan (Maks. ${state.selectedTicketType.maxPassenger ?: "-"} Penumpang)",
                enabled = buttonEnabled && totalPassengers() > 0,
                onClick = {
                    val finalResult: List<Passenger> =
                        tempListPass.filter { (it.passengerCategoryQty ?: 0) > 0 }
                            .map { pc ->
                                Passenger(
                                    categoryId = pc.passengerCategoryId ?: 0,
                                    qty = pc.passengerCategoryQty ?: 0
                                )
                            }
                    val res = totalPassengers()

                    onSave()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp)
            )
        }

        Spacer_16dp()
    }
}

@Composable
fun PassengerRow(
    label: String,
    count: Int,
    maxCount: Int,
    totalPassengers: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = onDecrease,
            enabled = count > 0
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = "Kurangi",
                tint = if (count > 0) ButtonPrimaryColor else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.width(24.dp),
            textAlign = TextAlign.Center
        )
        IconButton(
            onClick = onIncrease,
            enabled = (totalPassengers < maxCount) && (count < maxCount)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Tambah",
                tint = if ((totalPassengers < maxCount) && (count < maxCount)) ButtonPrimaryColor else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPortBottomDialog(
    state: HomeState,
    events: (HomeEvent) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background,
        sheetState = sheetState,
        dragHandle = null
    ) {

        Spacer_16dp()

        IconButton(onClick = onDismiss) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer_8dp()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {

            Text(
                "Pilih Pelabuhan",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer_16dp()

        RouteBottomSheet(
            state = state,
            events = events,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer_8dp()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDetailBottomSheet(
    state: HomeState,
    events: (HomeEvent) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var isNameError by remember { mutableStateOf(false) }
    var isAgeError by remember { mutableStateOf(false) }
    var isIdentityError by remember { mutableStateOf(false) }
    var isBirthdayError by remember { mutableStateOf(false) }
    var isPhoneError by remember { mutableStateOf(false) }
    var isEmailError by remember { mutableStateOf(false) }
    var isGenderError by remember { mutableStateOf(false) }
    var isCityError by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = {  },
        containerColor = MaterialTheme.colorScheme.background,
        sheetState = sheetState,
        dragHandle = null,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {

            Spacer(Modifier.height(16.dp))

            Text(
                "Detail Pemesan",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "Detail kontak ini akan digunakan untuk pengiriman e-ticket dan keperluan reschedule.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(16.dp))

            // Title Selection
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                listOf("Tuan", "Nyonya", "Nona").forEach { title ->
                    val isSelected = state.selectedTitle == title
                    OutlinedButton(
                        onClick = {  },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(
                                alpha = 0.1f
                            ) else Color.Transparent,
                            contentColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                        ),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(title)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Full Name
            OutlinedTextField(
                value = state.selectedName,
                onValueChange = { },
                label = { Text("Nama Lengkap Sesuai KTP/Paspor/SIM") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = isNameError
            )

            if (isNameError) {
                Text(
                    "Isi nama kamu dulu, ya.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {

                // Country Picker
                /*Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            onCountryPickerClicked()
                        }
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outline,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(country.flagRes),
                            contentDescription = null,
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = country.code,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(Modifier.width(8.dp))*/

                // Phone
                OutlinedTextField(
                    value = state.selectedPhone,
                    onValueChange = {  },
                    label = { Text("Nomor Ponsel") },
                    modifier = Modifier.weight(2f)
                        .fillMaxHeight(),
                    singleLine = true,
                    leadingIcon = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            /*Image(
                                painter = painterResource(Res.drawable.ic_bike), // Your flag asset
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                            )*/
                            Spacer(Modifier.width(4.dp))
                            Text("+62")
                            Spacer(Modifier.width(4.dp))
                        }
                    }
                )
            }

            /*Spacer(Modifier.height(12.dp))

            // Email (disabled)
            OutlinedTextField(
                value = state.selectedEmail,
                onValueChange = { events(HomeEvent.OnUpdateSelectedEmail(it)) },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                singleLine = true
            )*/

            Spacer(Modifier.height(24.dp))

            // Save Button
            DefaultButton(
                text = "Simpan",
                onClick = { },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryPickerBottomSheet(
    countries: List<Country>,
    onDismiss: () -> Unit,
    onCountrySelected: (Country) -> Unit
) {
    var query by remember { mutableStateOf("") }

    val filteredCountries = remember(query) {
        countries.filter { it.name.contains(query, ignoreCase = true) }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            // Drag handle
            Box(
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(36.dp)
                    .height(4.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        shape = RoundedCornerShape(2.dp)
                    )
            )

            Spacer(Modifier.height(16.dp))

            Text(
                "Kode Negara",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(12.dp))

            // Search bar
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                placeholder = { Text("Cari nama negara") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(8.dp))

            // Country list
            LazyColumn {
                items(filteredCountries) { country ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onCountrySelected(country)
                                onDismiss()
                            }
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Image(
                            painter = painterResource(country.flagRes),
                            contentDescription = null,
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            country.name,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            country.code,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityPickerBottomSheet(
    state: HomeState,
    events: (HomeEvent) -> Unit,
) {
    var query by remember { mutableStateOf("") }

    val filteredCities = remember(query) {
        state.home.city.filter { it.cityName.toString().contains(query, ignoreCase = true) }
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = { },
        containerColor = MaterialTheme.colorScheme.background,
        sheetState = sheetState,
        dragHandle = null,
        modifier = Modifier.fillMaxSize()
    ) {

        Spacer_32dp()

        Column(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            // Drag handle
            Box(
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(36.dp)
                    .height(4.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        shape = RoundedCornerShape(2.dp)
                    )
            )

            Spacer(Modifier.height(16.dp))

            Text(
                "Pilih Kota Domisili",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(12.dp))

            // Search bar
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                placeholder = { Text("Cari") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(12.dp))

            Column(
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                filteredCities.forEach { city ->
                    CityRow(
                        city = city,
                        isSelected = city == state.selectedCity,
                        onClick = {

                        }
                    )
                    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                }
            }
        }
    }
}

@Composable
fun CityRow(
    city: City,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
    } else {
        Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(backgroundColor, shape = RoundedCornerShape(8.dp))
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                city.cityName.toString(),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderBottomSheet(
    state: HomeState,
    events: (HomeEvent) -> Unit,
) {

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = { },
        containerColor = MaterialTheme.colorScheme.background,
        sheetState = sheetState,
        dragHandle = null,
    ) {

        Spacer_32dp()

        Column(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    "Pilih Jenis Kelamin",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(end = 48.dp)
                )
            }

            Spacer_8dp()

            // List of ticket types
            state.home.gender.forEach { data ->
                GenderRow(
                    item = data,
                    isSelected = data == state.selectedGender,
                    onClick = {

                    }
                )
                Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            }

            Spacer_32dp()
        }
    }
}

@Composable
fun GenderRow(
    item: Gender,
    isSelected: Boolean,
    onClick: (Gender) -> Unit
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
    } else {
        Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(item) }
            .background(backgroundColor, shape = RoundedCornerShape(8.dp))
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                item.genderName.toString(),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceDetailBottomSheet(
    state: HomeState,
    events: (HomeEvent) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = {  },
        containerColor = MaterialTheme.colorScheme.background,
        sheetState = sheetState,
        dragHandle = null,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            // Drag Handle
            Box(
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(36.dp)
                    .height(4.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        shape = RoundedCornerShape(2.dp)
                    )
            )

            Spacer(Modifier.height(16.dp))

            // Title
            Text(
                text = "Detail Harga",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
            )

            Spacer(Modifier.height(4.dp))

            // Route
            Text(
                text = "${state.selectedOrigin} → ${state.selectedDestination}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(16.dp))

            // Card for Express
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Regular",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                        )
                        Spacer(Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Default.ExpandMore,
                            contentDescription = null
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = "Harga",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                    )

                    Spacer(Modifier.height(8.dp))

                    // Vehicle Item
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "• ${state.selectedTicketType.cargoCategoryName.toString()}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            text = "Rp. ${state.ferryItemDetail.totalAmount.toString()}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    // Passenger Item
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        state.selectedPassengerList.forEach { item ->
                            when (item.categoryId) {
                                2 -> {
                                    Text(
                                        text = "Bayi (${item.qty}×)",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }

                                3 -> {
                                    Text(
                                        text = "Anak (${item.qty}×)",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }

                                4 -> {
                                    Text(
                                        text = "Dewasa (${item.qty}×)",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                            Spacer(Modifier.weight(1f))
                            Text(
                                text = "Termasuk",
                                color = MaterialTheme.colorScheme.tertiary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Divider()

            Spacer(Modifier.height(16.dp))

            // Other Costs
            Text(
                text = "Biaya Lainnya",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "• Pajak",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "Termasuk",
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(Modifier.height(16.dp))

            Divider()

            Spacer(Modifier.height(16.dp))

            // Total
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "Rp. ${state.ferryItemDetail.totalAmount.toString()}",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromoSelectionBottomSheet(
    promos: List<String>,
    onPromoSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background,
        sheetState = sheetState,
        dragHandle = null,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                "Pilih Promo/Voucher",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(Modifier.height(16.dp))
            promos.forEach { promo ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            onPromoSelected(promo)
                            onDismiss()
                        }
                ) {
                    Column(
                        Modifier.padding(16.dp)
                    ) {
                        Text(
                            promo,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                        )
                        Text(
                            "Deskripsi promo singkat...",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

// buat contoh
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUIComponentBottomSheet(
    title: String,
    description: String,
    onDismissRequest: () -> Unit,
    negativeAction: String = "",
    positiveAction: String = "",
    onPositiveClick: () -> Unit = {},
    onNegativeClick: () -> Unit = {},
    highRisk: Boolean = false,
    isShowIcon: Boolean = false,
    iconImage: DrawableResource = Res.drawable.ic_checked_figma
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = { onDismissRequest() },
        containerColor = MaterialTheme.colorScheme.background,
        sheetState = sheetState,
        modifier = Modifier.fillMaxWidth()
    ) {
        BottomSheetContent(
            title = title,
            description = description,
            positiveAction = positiveAction,
            onPositiveClick = {
                onDismissRequest()
                onPositiveClick()
            },
            negativeAction = negativeAction,
            onNegativeClick = {
                onDismissRequest()
                onNegativeClick.invoke()
            },
            highRisk = highRisk,
            isShowIcon = isShowIcon,
            iconImage = iconImage
        )
    }
}

@Composable
private fun BottomSheetContent(
    title: String,
    description: String,
    positiveAction: String,
    onPositiveClick: () -> Unit,
    negativeAction: String = "",
    onNegativeClick: (() -> Unit)? = null,
    highRisk: Boolean = false,
    isShowIcon: Boolean = false,
    iconImage: DrawableResource = Res.drawable.ic_checked_figma,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp, horizontal = 16.dp)
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (isShowIcon) {
            Image(
                painterResource(iconImage),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = description,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (negativeAction.isNotBlank() && onNegativeClick != null) {
                OutlinedButton(
                    onClick = onNegativeClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = negativeAction,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }

                Button(
                    onClick = onPositiveClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = positiveAction,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                Button(
                    onClick = onPositiveClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = positiveAction,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}