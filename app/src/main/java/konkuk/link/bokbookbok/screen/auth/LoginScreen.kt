package konkuk.link.bokbookbok.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
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
import konkuk.link.bokbookbok.component.common.AuthTextFieldComponent
import konkuk.link.bokbookbok.component.common.ButtonComponent
import konkuk.link.bokbookbok.component.common.ButtonTypeEnum
import konkuk.link.bokbookbok.navigation.NavigationGraph
import konkuk.link.bokbookbok.navigation.auth.AuthScreen
import konkuk.link.bokbookbok.ui.theme.bokBookBokColors
import konkuk.link.bokbookbok.ui.theme.defaultBokBookBokTypography

@Composable
fun LoginScreen(navController: NavController) {
    var idValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bokBookBokColors.white),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "로그인",
            style = defaultBokBookBokTypography.header
        )

        Spacer(modifier = Modifier.height(70.dp))

        AuthTextFieldComponent(
            value = idValue,
            onValueChange = { newValue -> idValue = newValue },
            placeholder = "아이디",
            isPassword = false
        )

        Spacer(modifier = Modifier.height(24.dp))

        AuthTextFieldComponent(
            value = passwordValue,
            onValueChange = { newValue -> passwordValue = newValue },
            placeholder = "비밀번호",
            isPassword = true
        )

        Spacer(modifier = Modifier.height(46.dp))
        ButtonComponent(
            buttonText = "로그인",
            buttonType = ButtonTypeEnum.FILL,
            onClick = {
                navController.navigate(NavigationGraph.MAIN) {
                    popUpTo(NavigationGraph.AUTH) {
                        inclusive = true
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(55.dp))

        HorizontalDivider(
            color = bokBookBokColors.borderLightGray,
            thickness = 1.dp,
            modifier = Modifier.width(326.dp)
        )

        Spacer(modifier = Modifier.height(35.dp))

        Text(
            text = "bookbokbook이\n" +
                    "처음이신가요?",
            style = defaultBokBookBokTypography.subHeader,
            color = bokBookBokColors.fontDarkGray,
            modifier = Modifier.width(325.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        ButtonComponent(
            buttonText = "회원가입",
            buttonType = ButtonTypeEnum.LINE,
            onClick = {
                navController.navigate(AuthScreen.SignUp.route)
            }
        )
    }
}