//package presentation.component
//
//import androidx.compose.material3.DropdownMenuItem
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.ExposedDropdownMenuBox
//import androidx.compose.material3.ExposedDropdownMenuDefaults
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DropdownWithOutlinedTextField(
//    options: List<String>,
//    label: String,
//    onOptionSelected: (String) -> Unit
//) {
//    var expanded by remember { mutableStateOf(false) }
//    var selectedText by remember { mutableStateOf(options.firstOrNull() ?: "") }
//
//    ExposedDropdownMenuBox(
//        expanded = expanded,
//        onExpandedChange = { expanded = !expanded }
//    ) {
//        OutlinedTextField(
//            value = selectedText,
//            onValueChange = {}, // Read-only
//            readOnly = true,
//            label = { Text(label) },
//            trailingIcon = {
//                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
//            },
//            modifier = Modifier.menuAnchor(type, enabled) // Important for proper positioning
//        )
//
//        ExposedDropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false }
//        ) {
//            options.forEach { selectionOption ->
//                DropdownMenuItem(
//                    text = { Text(selectionOption) },
//                    onClick = {
//                        selectedText = selectionOption
//                        expanded = false
//                        onOptionSelected(selectionOption)
//                    }
//                )
//            }
//        }
//    }
//}