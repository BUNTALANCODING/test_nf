package presentation.ui.main.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import business.domain.model.Student

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDetailScreen(
    student: Student,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detail Siswa") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = student.fullName,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "Informasi siswa",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    DetailRow(
                        icon = { Icon(Icons.Default.Person, contentDescription = null) },
                        label = "Nama Lengkap",
                        value = student.fullName
                    )
                    DetailRow(
                        icon = { Icon(Icons.Default.Badge, contentDescription = null) },
                        label = "NISN",
                        value = student.nisn
                    )
                    DetailRow(
                        icon = { Icon(Icons.Default.Cake, contentDescription = null) },
                        label = "Tanggal Lahir",
                        value = student.birthDate.toString()
                    )
                    DetailRow(
                        icon = { Icon(Icons.Default.School, contentDescription = null) },
                        label = "Jurusan",
                        value = student.major.name
                    )
                }
            }

            Spacer(Modifier.height(4.dp))
        }
    }
}

@Composable
private fun DetailRow(
    icon: @Composable () -> Unit,
    label: String,
    value: String
) {
    ListItem(
        leadingContent = {
            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.primary) {
                icon()
            }
        },
        headlineContent = {
            Text(text = label, style = MaterialTheme.typography.labelLarge)
        },
        supportingContent = {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    )
    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
}
