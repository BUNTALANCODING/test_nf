package common.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun RequireArPermissions(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
)