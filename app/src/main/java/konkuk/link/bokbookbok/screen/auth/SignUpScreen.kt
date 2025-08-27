package konkuk.link.bokbookbok.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import konkuk.link.bokbookbok.component.auth.AuthButtonComponent
import konkuk.link.bokbookbok.component.auth.AuthButtonTypeEnum
import konkuk.link.bokbookbok.component.auth.AuthTextFieldComponent
import konkuk.link.bokbookbok.component.common.ButtonComponent
import konkuk.link.bokbookbok.component.common.ButtonTypeEnum
import konkuk.link.bokbookbok.navigation.auth.AuthScreen
import konkuk.link.bokbookbok.ui.theme.bokBookBokColors
import konkuk.link.bokbookbok.ui.theme.defaultBokBookBokTypography

@Composable
fun SignUpScreen(navController: NavController) {
    var idValue by remember { mutableStateOf("") }
    var nicknameValue by remember { mutableStateOf("")}
    var passwordValue by remember { mutableStateOf("") }
    var passwordConfirmValue by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bokBookBokColors.white)
            .padding(horizontal = 28.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "회원가입",
            style = defaultBokBookBokTypography.header
        )

        Spacer(modifier = Modifier.height(70.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AuthTextFieldComponent(
                modifier = Modifier.weight(1f),
                value = idValue,
                onValueChange = { newValue -> idValue = newValue },
                placeholder = "아이디",
                isPassword = false
            )
            Spacer(modifier = Modifier.width(14.dp))
            AuthButtonComponent(
                buttonText = "중복확인",
                buttonType = AuthButtonTypeEnum.BEFORE,
                onClick = {

                },
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AuthTextFieldComponent(
                modifier = Modifier.weight(1f),
                value = nicknameValue,
                onValueChange = { newValue -> nicknameValue = newValue },
                placeholder = "닉네임",
                isPassword = false
            )
            Spacer(modifier = Modifier.width(14.dp))
            AuthButtonComponent(
                buttonText = "중복확인",
                buttonType = AuthButtonTypeEnum.BEFORE,
                onClick = {

                },
            )

        }
        Spacer(modifier = Modifier.height(24.dp))

        AuthTextFieldComponent(
            modifier = Modifier.fillMaxWidth(),
            value = passwordValue,
            onValueChange = { newValue -> passwordValue = newValue },
            placeholder = "비밀번호",
            isPassword = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        AuthTextFieldComponent(
            modifier = Modifier.fillMaxWidth(),
            value = passwordConfirmValue,
            onValueChange = { newValue -> passwordConfirmValue = newValue },
            placeholder = "비밀번호확인",
            isPassword = true
        )

        Spacer(modifier = Modifier.height(116.dp))

        ButtonComponent(
            buttonText = "회원가입",
            buttonType = ButtonTypeEnum.LINE,
            onClick = {
                navController.navigate(AuthScreen.Login.route) {
                    popUpTo(AuthScreen.SignUp.route) {
                        inclusive = true
                    }
                }
            }
        )
    }
}