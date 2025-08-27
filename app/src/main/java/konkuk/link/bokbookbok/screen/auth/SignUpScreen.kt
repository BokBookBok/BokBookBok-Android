package konkuk.link.bokbookbok.screen.auth

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import konkuk.link.bokbookbok.data.model.request.RegisterRequest

@Composable
fun SignUpScreen(
    navController: NavController,
    factory: SignUpViewModelFactory,
) {
    val viewModel: SignUpViewModel =
        viewModel(
            factory = factory,
        )

    var email by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Button(
        onClick = {
            val request = RegisterRequest(email, nickname, password)
            viewModel.register(request)
        },
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text("가입하기", style = MaterialTheme.typography.bodyLarge)
    }
}
