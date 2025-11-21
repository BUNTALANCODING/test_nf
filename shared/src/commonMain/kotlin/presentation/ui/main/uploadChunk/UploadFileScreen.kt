package presentation.ui.main.uploadChunk

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import business.core.UIComponent
import coil3.Uri
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import presentation.ui.main.riwayat.viewmodel.RiwayatEvent
import presentation.ui.main.riwayat.viewmodel.RiwayatState

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import coil3.compose.LocalPlatformContext
import common.toFileContainer
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
fun UploadFileScreen(
    viewModel: UploadViewModel
) {
    val state by viewModel.state.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    // 🟢 Needed for the .toFileContainer() extension
    val context = LocalPlatformContext.current

    val launcher = rememberFilePickerLauncher(
        type = PickerType.Video,
        title = "Select Video"
    ) { videoFile ->
        if (videoFile != null) {
            // 🟢 1. Convert FileKit file to FileContainer
            val container = videoFile.toFileContainer(context)

            // 🟢 2. Set file and Start
            viewModel.setFile(container)
            viewModel.startUploadInterior()
            showDialog = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { launcher.launch() }) {
            Text("Select & Upload File")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (state.isUploading) {
            Text(text = "Uploading: ${state.fileName}")
            // Progress is 0 to 100, Indicator needs 0.0 to 1.0
            LinearProgressIndicator(progress = state.progress / 100f)

            // 🟢 REMOVED: Text(text = "Chunk ...")
            // because we aren't tracking chunk numbers anymore.
            Text(text = "${state.progress}%")
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Uploading ${state.fileName}") },
            text = {
                Column {
                    LinearProgressIndicator(
                        progress = state.progress / 100f,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // 🟢 Updated to show Percentage instead of Chunk numbers
                    Text("${state.progress}% Completed")
                }
            },
            confirmButton = {
                Row {
                    Button(onClick = { viewModel.pause() }) { Text("Pause") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { viewModel.resume() }) { Text("Resume") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        viewModel.cancel()
                        showDialog = false
                    }) { Text("Cancel") }
                }
            }
        )
    }
}
