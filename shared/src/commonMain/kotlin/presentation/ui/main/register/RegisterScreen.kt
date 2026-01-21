package presentation.ui.main.register

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import business.domain.model.Major
import business.domain.model.Student
import presentation.ui.main.register.state.StudentState
import presentation.ui.main.register.view_model.StudentViewModel
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    vm: StudentViewModel,
    onSuccess: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var nisn by remember { mutableStateOf("") }
    var birth by remember { mutableStateOf("") }
    var major by remember { mutableStateOf(Major.IPA) }
    var expanded by remember { mutableStateOf(false) }

    var showFormError by remember { mutableStateOf(false) }
    var formErrorText by remember { mutableStateOf("") }

    var showDateDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val state by vm.state.collectAsState()

    LaunchedEffect(state) {
        if (state is StudentState.RegisterSuccess) {
            onSuccess()
        }
    }

    if (showDateDialog) {
        DatePickerDialog(
            onDismissRequest = { showDateDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val millis = datePickerState.selectedDateMillis
                        if (millis != null) {
                            birth = millisToIsoDate(millis)
                        }
                        showDateDialog = false
                    }
                ) { Text("Pilih") }
            },
            dismissButton = {
                TextButton(onClick = { showDateDialog = false }) { Text("Batal") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Form Pendaftaran", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        if (showFormError) {
            Text(
                text = formErrorText,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(Modifier.height(8.dp))
        }

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                if (showFormError) showFormError = false
            },
            label = { Text("Nama Lengkap") },
            modifier = Modifier.fillMaxWidth(),
            isError = showFormError && name.isBlank()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = nisn,
            onValueChange = {
                nisn = it.filter(Char::isDigit)
                if (showFormError) showFormError = false
            },
            label = { Text("NISN (10 digit)") },
            modifier = Modifier.fillMaxWidth(),
            isError = showFormError && (nisn.isBlank() || nisn.length != 10),
            supportingText = {
                if (showFormError && (nisn.isBlank() || nisn.length != 10)) {
                    Text("NISN wajib 10 digit")
                }
            }
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = birth,
            onValueChange = {},
            readOnly = true,
            label = { Text("Tanggal Lahir") },
            modifier = Modifier.fillMaxWidth(),
            isError = showFormError && birth.isBlank(),
            trailingIcon = {
                TextButton(onClick = { showDateDialog = true }) {
                    Text("Pilih")
                }
            }
        )
        Spacer(Modifier.height(8.dp))

        Text("Jurusan")
        Spacer(Modifier.height(6.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = major.name,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                label = { Text("Pilih Jurusan") }
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                Major.values().forEach { m ->
                    DropdownMenuItem(
                        text = { Text(m.name) },
                        onClick = { major = m; expanded = false }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                val missing = mutableListOf<String>()
                if (name.isBlank()) missing.add("Nama Lengkap")
                if (nisn.isBlank()) missing.add("NISN")
                if (nisn.isNotBlank() && nisn.length != 10) missing.add("NISN harus 10 digit")
                if (birth.isBlank()) missing.add("Tanggal Lahir")

                if (missing.isNotEmpty()) {
                    showFormError = true
                    formErrorText = if (missing.any { it == "NISN harus 10 digit" }) {
                        "Lengkapi form: NISN harus 10 digit."
                    } else {
                        "Lengkapi form: ${missing.joinToString(", ")}."
                    }
                    return@Button
                }

                vm.onRegister(
                    Student(
                        fullName = name,
                        nisn = nisn,
                        birthDate = birth,
                        major = major
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = state !is StudentState.Loading
        ) {
            Text(if (state is StudentState.Loading) "Menyimpan..." else "Simpan")
        }

        Spacer(Modifier.height(12.dp))

        if (state is StudentState.Error) {
            Text(
                (state as StudentState.Error).message,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

private fun millisToIsoDate(millis: Long): String {
    val days = millis / 86_400_000L

    var z = days + 719468
    val era = if (z >= 0) z / 146097 else (z - 146096) / 146097
    val doe = z - era * 146097
    val yoe = (doe - doe/1460 + doe/36524 - doe/146096) / 365
    var y = yoe + era * 400
    val doy = doe - (365*yoe + yoe/4 - yoe/100)
    val mp = (5*doy + 2) / 153
    val d = doy - (153*mp + 2)/5 + 1
    val m = mp + if (mp < 10) 3 else -9
    y += if (m <= 2) 1 else 0

    val yy = y.toString().padStart(4, '0')
    val mm = m.toString().padStart(2, '0')
    val dd = d.toString().padStart(2, '0')
    return "$yy-$mm-$dd"
}
