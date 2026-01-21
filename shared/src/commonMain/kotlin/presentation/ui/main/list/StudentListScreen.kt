package presentation.ui.main.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import business.domain.model.Student
import presentation.ui.main.register.state.StudentState
import presentation.ui.main.register.view_model.StudentViewModel
import androidx.compose.material.icons.filled.ArrowBack


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentListScreen(
    vm: StudentViewModel,
    onBack: () -> Unit,
    onDetail: (Student) -> Unit
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        vm.loadStudents()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Daftar Siswa",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }

            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            when (state) {
                is StudentState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is StudentState.Data -> {
                    val students = (state as StudentState.Data).students

                    if (students.isEmpty()) {
                        EmptyState()
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(vertical = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(students) { student ->
                                StudentItem(
                                    student = student,
                                    onClick = { onDetail(student) }
                                )
                            }
                        }
                    }
                }

                is StudentState.Error -> {
                    Text(
                        text = (state as StudentState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    EmptyState()
                }
            }
        }
    }
}

@Composable
private fun StudentItem(
    student: Student,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        ListItem(
            leadingContent = {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            headlineContent = {
                Text(
                    text = student.fullName,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            supportingContent = {
                Text(
                    text = "NISN: ${student.nisn} • ${student.major.name}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        )
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Belum ada siswa terdaftar",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
