package presentation.ui.main.ubahlokasi

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import business.constants.DataStoreKeys
import business.core.AppDataStore
import common.picklocation.PlatformPickLocationMap
import common.picklocation.rememberReverseGeocoder
import common.picklocation.rememberUserLocation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import navbuss.shared.generated.resources.Res
import navbuss.shared.generated.resources.ic_picker
import org.jetbrains.compose.resources.painterResource
import presentation.ui.main.inforute.view_model.GeoPoint
import kotlin.math.round

@Composable
fun PilihPetaScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onConfirm: (GeoPoint) -> Unit = {},
    appDataStore: AppDataStore,

    ) {
    val cs = MaterialTheme.colorScheme
    val accent = Color(0xFFD8A73B)

    val userLocation by rememberUserLocation()

    var selectedLocation by remember { mutableStateOf<GeoPoint?>(null) }
    var focusOnceLocation by remember { mutableStateOf<GeoPoint?>(null) }
    var placeName by remember { mutableStateOf("Memuat lokasi...") }
    var placeAddress by remember { mutableStateOf("Geser peta untuk memilih lokasi") }
    var isResolving by remember { mutableStateOf(false) }

    val geocoder = rememberReverseGeocoder()

    LaunchedEffect(userLocation) {
        userLocation?.let {
            if (selectedLocation == null) {
                selectedLocation = it
                focusOnceLocation = it
            }
        }
    }

    LaunchedEffect(focusOnceLocation) {
        if (focusOnceLocation != null) {
            delay(800)
            focusOnceLocation = null
        }
    }

    LaunchedEffect(geocoder) {
        snapshotFlow { selectedLocation }
            .filterNotNull()
            .map {
                GeoPoint(
                    round(it.lat * 100000) / 100000,
                    round(it.lng * 100000) / 100000
                )
            }
            .distinctUntilChanged()
            .debounce(400)
            .collectLatest { point ->
                isResolving = true
                try {
                    val result = geocoder.reverse(point.lat, point.lng)
                    if (result != null) {
                        placeName = result.title
                        placeAddress = result.subtitle ?: "Lokasi dipilih pengguna"
                    } else {
                        placeName = "Lokasi tidak diketahui"
                        placeAddress = "Geser peta untuk memilih lokasi"
                    }
                } catch (e: Exception) {
                    placeName = "Gagal memuat nama lokasi"
                    placeAddress = "Periksa koneksi internet"
                } finally {
                    isResolving = false
                }
            }
    }

    if (selectedLocation == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = accent)
        }
        return
    }

    Box(modifier = modifier.fillMaxSize()) {

        PlatformPickLocationMap(
            initial = selectedLocation!!,
            onCameraMoved = { point -> selectedLocation = point },
            modifier = Modifier.fillMaxSize(),
            focusLocation = focusOnceLocation,
            initialZoom = 16.5,
            focusZoom = 18.5
        )


        Icon(
            painter = painterResource(Res.drawable.ic_picker),
            contentDescription = null,
            tint = Color(0xFFE14B3B),
            modifier = Modifier
                .align(Alignment.Center)
                .size(54.dp)
                .padding(bottom = 24.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 12.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MapCircleButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Kembali", tint = Color.Black)
            }

            MapCircleButton(
                onClick = {
                    userLocation?.let {
                        selectedLocation = it
                        focusOnceLocation = it
                    }
                }
            ) {
                Icon(Icons.Filled.GpsFixed, contentDescription = "Lokasi Saya", tint = accent)
            }
        }

        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp),
            color = Color.White,
            shadowElevation = 16.dp
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Titik Lokasi",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = cs.onSurface
                )

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, cs.outlineVariant.copy(alpha = 0.65f))
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = accent.copy(alpha = 0.15f),
                            modifier = Modifier.size(34.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Filled.Place, contentDescription = null, tint = accent)
                            }
                        }

                        Spacer(Modifier.size(12.dp))

                        Column(Modifier.weight(1f)) {
                            Text(
                                if (isResolving) "Mencari alamat..." else placeName,
                                fontWeight = FontWeight.SemiBold,
                                color = cs.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                placeAddress,
                                color = cs.onSurfaceVariant,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                val scope = rememberCoroutineScope()


                Button(
                    onClick = {
                        selectedLocation?.let { point ->
                            scope.launch {
                                appDataStore.setValue(DataStoreKeys.USER_LAT, point.lat.toString())
                                appDataStore.setValue(DataStoreKeys.USER_LNG, point.lng.toString())

                                appDataStore.setValue(DataStoreKeys.USER_PLACE_NAME, placeName)
                                appDataStore.setValue(DataStoreKeys.USER_PLACE_ADDRESS, placeAddress)

                                onConfirm(point)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(999.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accent,
                        contentColor = Color.White
                    )
                ) {
                    Text("Pilih Lokasi Ini", fontWeight = FontWeight.SemiBold)
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MapCircleButton(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = Color.White,
        shadowElevation = 8.dp,
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
        modifier = Modifier.size(44.dp)
    ) {
        Box(contentAlignment = Alignment.Center) { content() }
    }
}
