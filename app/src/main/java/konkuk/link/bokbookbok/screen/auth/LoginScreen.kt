package konkuk.link.bokbookbok.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import konkuk.link.bokbookbok.component.auth.AuthTextFieldComponent
import konkuk.link.bokbookbok.component.common.AlertComponent
import konkuk.link.bokbookbok.component.common.ButtonComponent
import konkuk.link.bokbookbok.component.common.ButtonTypeEnum
import konkuk.link.bokbookbok.data.model.request.login.LoginRequest
import konkuk.link.bokbookbok.navigation.Screen
import konkuk.link.bokbookbok.ui.theme.bokBookBokColors
import konkuk.link.bokbookbok.ui.theme.defaultBokBookBokTypography

@Composable
fun LoginScreen(
    navController: NavController,
    factory: LoginViewModelFactory,
) {
    val viewModel: LoginViewModel = viewModel(factory = factory)

    var showEmailFormatAlert by remember { mutableStateOf(false) }

    val emailCheckState by viewModel.emailCheckState.collectAsStateWithLifecycle()
    val loginErrorMessage by viewModel.loginErrorMessage.collectAsStateWithLifecycle()

    var emailValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loginEvent.collect { event ->
            // ✅ 2. 이벤트 종류에 따라 분기 처리합니다.
            when (event) {
                is LoginEvent.NavigateToMain -> {
                    navController.navigate(Screen.ReadingHome.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
                is LoginEvent.NavigateToAdmin -> {
                    navController.navigate(Screen.Admin.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            }
        }
    }

    if (showEmailFormatAlert) {
        AlertComponent(
            { showEmailFormatAlert = false },
            title = "알림",
            text = "올바른 이메일 형식이 아닙니다.",
            confirmButtonText = "확인",
        )
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(bokBookBokColors.white)
                .padding(horizontal = 28.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "로그인",
            style = defaultBokBookBokTypography.header,
        )

        Spacer(modifier = Modifier.height(70.dp))

        AuthTextFieldComponent(
            modifier = Modifier.fillMaxWidth(),
            value = emailValue,
            onValueChange = {
                emailValue = it
                if (emailCheckState != DuplicateCheckState.IDLE) viewModel.resetEmailCheckState()
                viewModel.clearLoginError()
            },
            placeholder = "이메일",
            isPassword = false,
        )

        Spacer(modifier = Modifier.height(24.dp))

        AuthTextFieldComponent(
            modifier = Modifier.fillMaxWidth(),
            value = passwordValue,
            onValueChange = {
                passwordValue = it
                viewModel.clearLoginError()
            },
            placeholder = "비밀번호",
            isPassword = true,
        )

        loginErrorMessage?.let { message ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                color = bokBookBokColors.second, // 에러를 나타내는 색상
                style = defaultBokBookBokTypography.subBody,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.height(if (loginErrorMessage == null) 46.dp else 24.dp))

        ButtonComponent(
            buttonText = "로그인",
            buttonType = ButtonTypeEnum.FILL,
            onClick = {
                val request = LoginRequest(emailValue, passwordValue)
                viewModel.login(request)
            },
        )

        Spacer(modifier = Modifier.height(55.dp))

        HorizontalDivider(
            color = bokBookBokColors.borderLightGray,
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(35.dp))

        Text(
            text =
                "bookbokbook이\n" +
                    "처음이신가요?",
            style = defaultBokBookBokTypography.subHeader,
            color = bokBookBokColors.fontDarkGray,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(20.dp))

        ButtonComponent(
            buttonText = "회원가입",
            buttonType = ButtonTypeEnum.LINE,
            onClick = {
                navController.navigate(Screen.Register.route)
            },
        )
    }
}
