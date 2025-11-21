package common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun Base64PdfViewer(
    url: String,
    modifier: Modifier = Modifier
)
