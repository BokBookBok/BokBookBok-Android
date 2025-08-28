package konkuk.link.bokbookbok.screen.auth

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import konkuk.link.bokbookbok.component.auth.DuplicateCheckInputComponent
import konkuk.link.bokbookbok.component.auth.PasswordInputSectionComponent
import konkuk.link.bokbookbok.component.common.AlertComponent
import konkuk.link.bokbookbok.component.common.ButtonComponent
import konkuk.link.bokbookbok.component.common.ButtonTypeEnum
import konkuk.link.bokbookbok.data.model.request.register.RegisterEmailRequest
import konkuk.link.bokbookbok.data.model.request.register.RegisterNicknameRequest
import konkuk.link.bokbookbok.data.model.request.register.RegisterRequest
import konkuk.link.bokbookbok.navigation.Screen
import konkuk.link.bokbookbok.ui.theme.bokBookBokColors
import konkuk.link.bokbookbok.ui.theme.defaultBokBookBokTypography

private fun String.isValidEmail(): Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()

@Composable
fun RegisterScreen(
    navController: NavController,
    factory: RegisterViewModelFactory,
) {
    val viewModel: RegisterViewModel = viewModel(factory = factory)

    var emailValue by remember { mutableStateOf("") }
    var nicknameValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }
    var passwordConfirmValue by remember { mutableStateOf("") }
    var showEmailFormatAlert by remember { mutableStateOf(false) }

    val emailCheckState by viewModel.emailCheckState.collectAsStateWithLifecycle()
    val nicknameCheckState by viewModel.nicknameCheckState.collectAsStateWithLifecycle()

    val passwordsMatch = passwordValue.isNotEmpty() && passwordValue == passwordConfirmValue
    val isRegisterEnabled =
        emailCheckState == DuplicateCheckState.SUCCESS &&
            nicknameCheckState == DuplicateCheckState.SUCCESS &&
            passwordsMatch

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
            text = "회원가입",
            style = defaultBokBookBokTypography.header,
            color = bokBookBokColors.fontDarkBrown,
        )

        Spacer(modifier = Modifier.height(70.dp))

        DuplicateCheckInputComponent(
            value = emailValue,
            onValueChange = {
                emailValue = it
                if (emailCheckState != DuplicateCheckState.IDLE) viewModel.resetEmailCheckState()
            },
            placeholder = "이메일",
            checkState = emailCheckState,
            onCheckClick = {
                if (emailValue.isValidEmail()) {
                    viewModel.registerEmail(RegisterEmailRequest(emailValue))
                } else {
                    showEmailFormatAlert = true
                }
            },
        )

        Spacer(modifier = Modifier.height(24.dp))

        DuplicateCheckInputComponent(
            value = nicknameValue,
            onValueChange = {
                nicknameValue = it
                if (nicknameCheckState != DuplicateCheckState.IDLE) viewModel.resetNicknameCheckState()
            },
            placeholder = "닉네임",
            checkState = nicknameCheckState,
            onCheckClick = { viewModel.registerNickname(RegisterNicknameRequest(nicknameValue)) },
        )

        Spacer(modifier = Modifier.height(24.dp))

        PasswordInputSectionComponent(
            passwordValue = passwordValue,
            onPasswordValueChange = { passwordValue = it },
            passwordConfirmValue = passwordConfirmValue,
            onPasswordConfirmValueChange = { passwordConfirmValue = it },
            passwordsMatch = passwordsMatch,
        )

        Spacer(modifier = Modifier.height(if (passwordConfirmValue.isEmpty()) 116.dp else 88.dp))

        ButtonComponent(
            buttonText = "회원가입",
            buttonType = if (isRegisterEnabled) ButtonTypeEnum.FILL else ButtonTypeEnum.LINE,
            enabled = isRegisterEnabled,
            onClick = {
                if (isRegisterEnabled) {
                    val request = RegisterRequest(emailValue, nicknameValue, passwordValue)
                    viewModel.register(request)
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            },
        )
    }
}
