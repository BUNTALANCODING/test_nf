package common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun Base64PdfViewer(
    base64Pdf: String,
    modifier: Modifier = Modifier
)
