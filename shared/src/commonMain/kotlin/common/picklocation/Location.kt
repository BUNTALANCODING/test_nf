package common.picklocation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import presentation.ui.main.inforute.view_model.GeoPoint

@Composable
expect fun rememberUserLocation(): State<GeoPoint?>
