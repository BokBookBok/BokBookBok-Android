package konkuk.link.bokbookbok.screen.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import konkuk.link.bokbookbok.navigation.NavigationGraph
import konkuk.link.bokbookbok.navigation.auth.AuthScreen

@Composable
fun LoginScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "로그인 화면")
        Button(onClick = {
            navController.navigate(NavigationGraph.MAIN) {
                popUpTo(NavigationGraph.AUTH) {
                    inclusive = true
                }
            }
        }) {
            Text("로그인하고 메인으로 가기")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.navigate(AuthScreen.SignUp.route)
        }) {
            Text("회원가입 화면으로")
        }
    }
}
