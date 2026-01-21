package presentation.ui.main.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import presentation.ui.main.register.state.StudentState
import presentation.ui.main.register.view_model.StudentViewModel

@Composable
fun LoginScreen(
    vm: StudentViewModel,
    onSuccess: () -> Unit
) {
    var u by remember { mutableStateOf("") }
    var p by remember { mutableStateOf("") }

    var attemptedLogin by remember { mutableStateOf(false) }

    val state by vm.state.collectAsState()

    LaunchedEffect(state, attemptedLogin) {
        if (attemptedLogin && state is StudentState.Idle) {
            onSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = u,
            onValueChange = { u = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = p,
            onValueChange = { p = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                attemptedLogin = true
                vm.onLogin(u, p)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = state !is StudentState.Loading
        ) {
            if (state is StudentState.Loading) {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Loading")
            } else {
                Text("Login")
            }
        }

        Spacer(Modifier.height(12.dp))

        if (state is StudentState.Error) {
            Text(
                text = (state as StudentState.Error).message,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(Modifier.height(8.dp))
        Text(
            text = "Dummy login: admin / admin",
            style = MaterialTheme.typography.bodySmall
        )
    }
}
