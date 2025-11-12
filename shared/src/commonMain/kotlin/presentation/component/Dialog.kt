package presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import network.chaintech.kmp_date_time_picker.utils.now
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import presentation.theme.LightPurpleColor
import presentation.theme.PrimaryColor
import rampcheck.shared.generated.resources.Res
import rampcheck.shared.generated.resources.ic_info_alert
import rampcheck.shared.generated.resources.ic_plus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialog(
    iconRes: DrawableResource = Res.drawable.ic_info_alert,
    title: String,
    subtitle: String,
    isButtonVertical : Boolean = false,
    isButtonVisible : Boolean = false,
    positiveLabel : String,
    negativeLabel : String,
    onClickPositive : () -> Unit,
    onClickNegative : () -> Unit,
    onDismissRequest: () -> Unit,

) {

    CustomAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .fillMaxWidth().background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp).verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Image(
                painter = painterResource(iconRes),
                contentDescription = null
            )
            Spacer_32dp()
            Text(
                title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer_16dp()
            Text(
                subtitle,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            )
            Spacer_32dp()

            if(isButtonVisible){
                if(isButtonVertical){
                    Column {
                        DefaultButton(
                            modifier = Modifier.fillMaxWidth().height(DEFAULT__BUTTON_SIZE),
                            onClick = onClickPositive,
                            colors = ButtonDefaults.buttonColors(containerColor = LightPurpleColor),
                            text = positiveLabel,
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = PrimaryColor,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Spacer_8dp()

                        DefaultButton(
                            onClick = onClickNegative,
                            modifier = Modifier.fillMaxWidth().height(DEFAULT__BUTTON_SIZE),
                            text = negativeLabel,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Black)
                        )
                    }
                } else {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

                        DefaultButton(
                            modifier = Modifier.height(DEFAULT__BUTTON_SIZE).width(128.dp),
                            onClick = onClickPositive,
                            colors = ButtonDefaults.buttonColors(containerColor = LightPurpleColor),
                            text = positiveLabel,
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = PrimaryColor,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Spacer_8dp()

                        DefaultButton(
                            onClick = onClickNegative,
                            modifier = Modifier.height(DEFAULT__BUTTON_SIZE).width(128.dp),
                            text = negativeLabel,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor, contentColor = Color.White)
                        )
                    }
                }
            }




        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendEmailDialog(
    iconRes: DrawableResource = Res.drawable.ic_info_alert,
    title: String,
    subtitle: String,
    isButtonVertical : Boolean = false,
    isButtonVisible : Boolean = false,
    onDismissRequest: () -> Unit,

    ) {

    CustomAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .fillMaxWidth().background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp).verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Image(
                painter = painterResource(iconRes),
                contentDescription = null
            )
            Spacer_8dp()
            Text(
                title,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer_16dp()

            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                Text(
                    "Masukkan Alamat Email",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
                Spacer_8dp()
                DefaultTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = "",
                    onValueChange = {},
                    placeholder = "Masukkan email"
                )
                Spacer_8dp()
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(Res.drawable.ic_plus),
                        contentDescription = null
                    )
                    Spacer_8dp()
                    Text(
                        "Tambah Email",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = PrimaryColor
                        )
                    )
                }

                Spacer_64dp()

                var isChecked by remember { mutableStateOf(false) }
                Row(
                    modifier = Modifier.noRippleClickable { isChecked = !isChecked }, // Membuat seluruh Row bisa diklik
                    verticalAlignment = Alignment.Top
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { isChecked = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = PrimaryColor,
                            uncheckedColor = Color.Gray
                        ),
                        modifier = Modifier.size(16.dp) // Mengatur ukuran Checkbox (opsional)
                    )
                    Spacer_8dp()

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Kirim ke email saya",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.Black
                        )
                        Spacer_8dp()
                        Text(
                            "petugasrampcheck@gmail.com",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = Color.Gray
                            )
                        )
                    }

                }


            }


            Spacer_8dp()

            if(isButtonVisible){
                if(isButtonVertical){
                    Column {
                        DefaultButton(
                            modifier = Modifier.fillMaxWidth().height(DEFAULT__BUTTON_SIZE),
                            onClick = onDismissRequest,
                            colors = ButtonDefaults.buttonColors(containerColor = LightPurpleColor),
                            text = "VERIFIKASI SEKARANG",
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = PrimaryColor,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Spacer_8dp()

                        DefaultButton(
                            onClick = onDismissRequest,
                            modifier = Modifier.fillMaxWidth().height(DEFAULT__BUTTON_SIZE),
                            text = "BATALKAN",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Black)
                        )
                    }
                } else {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

                        DefaultButton(
                            modifier = Modifier.height(DEFAULT__BUTTON_SIZE).width(128.dp).border(1.dp,
                                PrimaryColor, shape = MaterialTheme.shapes.small),
                            onClick = onDismissRequest,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            text = "BATAL",
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = PrimaryColor,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Spacer_8dp()

                        DefaultButton(
                            onClick = onDismissRequest,
                            modifier = Modifier.height(DEFAULT__BUTTON_SIZE).width(128.dp),
                            text = "KIRIM",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor, contentColor = Color.White)
                        )
                    }
                }
            }




        }
    }
}

@Composable
fun KartuTidakAdaDialog(
    keterangan: String,
    onKeteranganChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSimpan: () -> Unit,
    enableSimpan: Boolean
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(20.dp)
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Title
                Text(
                    text = "Kartu Tidak Ada",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                // Label
                Text(
                    text = "Keterangan",
                    fontWeight = FontWeight.Medium
                )

                // TextField
                OutlinedTextField(
                    value = keterangan,
                    onValueChange = onKeteranganChange,
                    placeholder = { Text("Masukkan Keterangan") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(Modifier.height(6.dp))

                // Buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {


                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF0E4FF),
                            contentColor = PrimaryColor
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("KEMBALI")
                    }

                    // Simpan
                    Button(
                        onClick = onSimpan,
                        modifier = Modifier.weight(1f),
                        enabled = enableSimpan,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (enableSimpan) PrimaryColor else Color(0xFFDDDDDD),
                            contentColor = if (enableSimpan) Color.White else Color.Gray
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("SIMPAN")
                    }
                }
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialDatePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    // Indonesia timezone (WIB = UTC+7)
    val indonesiaTimeZone = TimeZone.of("Asia/Jakarta")

    // Get today's date in Indonesia timezone
    val todayIndonesia = Clock.System.now().toLocalDateTime(indonesiaTimeZone).date

    // Convert Indonesia date to UTC milliseconds for DatePicker
    val todayMillisUtc = todayIndonesia.atTime(0, 0, 0, 0)
        .toInstant(TimeZone.UTC)
        .toEpochMilliseconds()

    val datePickerState = rememberDatePickerState(
        initialDisplayedMonthMillis = todayMillisUtc,
        initialSelectedDateMillis = todayMillisUtc, // Optional: if you want today to be pre-selected
        selectableDates = PastOrPresentSelectableDates
    )

    val selectedDateMillis = datePickerState.selectedDateMillis
    val selectedDate = selectedDateMillis?.let {
        Instant.fromEpochMilliseconds(it)
            .toLocalDateTime(TimeZone.UTC)
            .date
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        dragHandle = null,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Spacer_16dp()

        androidx.compose.material3.IconButton(onClick = onDismiss) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        /*Spacer_8dp()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {

            Text(
                "Pilih Waktu Keberangkatan",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer_4dp()
            Text(
                "Silahkan pilih waktu keberangkatan Anda di bawah ini.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }*/

        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(containerColor = Color.White)
        )

        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            DefaultButton(
                text = "Simpan",
                onClick = { onConfirm(selectedDate.toString()) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp)
            )
        }
    }
}

@Composable
fun SignaturePad(
    modifier: Modifier = Modifier,
    onSave: (ImageBitmap) -> Unit,
    onClose: () -> Unit
) {
    var paths by remember { mutableStateOf(mutableListOf<List<Offset>>()) }
    var currentPath by remember { mutableStateOf<List<Offset>>(emptyList()) }

    val pathColor = Color.Black
    var strokeWidth = 4f

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            "Tanda tangan disini",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(8.dp))

        // Canvas tempat coret-coret
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray.copy(alpha = 0.3f))
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            currentPath = listOf(offset)
                        },
                        onDrag = { change, _ ->
                            currentPath = currentPath + change.position
                        },
                        onDragEnd = {
                            paths.add(currentPath)
                            currentPath = emptyList()
                        }
                    )
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                paths.forEach { path ->
                    for (i in 0 until path.size - 1) {
                        drawLine(
                            color = pathColor,
                            start = path[i],
                            end = path[i + 1],
                            strokeWidth = strokeWidth
                        )
                    }
                }
                for (i in 0 until currentPath.size - 1) {
                    drawLine(
                        color = pathColor,
                        start = currentPath[i],
                        end = currentPath[i + 1],
                        strokeWidth = strokeWidth
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onClose,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray
                )
            ) {
                Text("BATAL")
            }

            Button(
                onClick = {
                    // Convert paths menjadi ImageBitmap beneran
                    val bitmapWidth = 1000
                    val bitmapHeight = 600
                    val imageBitmap = ImageBitmap(bitmapWidth, bitmapHeight)
                    val canvas = androidx.compose.ui.graphics.Canvas(imageBitmap)

                    val paint = Paint().apply {
                        color = pathColor
                        strokeWidth = strokeWidth
                        style = PaintingStyle.Stroke
                        isAntiAlias = true
                    }

                    paths.forEach { path ->
                        for (i in 0 until path.size - 1) {
                            canvas.drawLine(path[i], path[i + 1], paint)
                        }
                    }

                    // include coretan yang belum dilepas
                    for (i in 0 until currentPath.size - 1) {
                        canvas.drawLine(currentPath[i], currentPath[i + 1], paint)
                    }

                    onSave(imageBitmap)
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E006D)
                )
            ) {
                Text("SIMPAN")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
object PastOrPresentSelectableDates: SelectableDates {
    // Use Indonesia timezone for calculating "today"
    private val indonesiaTimeZone = TimeZone.of("Asia/Jakarta")
    private val nowIndonesia = Clock.System.now().toLocalDateTime(indonesiaTimeZone).date
    private val dayStartUtc = nowIndonesia.atTime(0, 0, 0, 0).toInstant(TimeZone.UTC).toEpochMilliseconds()

    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis >= dayStartUtc
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year >= LocalDate.now().year && year <= LocalDate.now().year
    }
}

