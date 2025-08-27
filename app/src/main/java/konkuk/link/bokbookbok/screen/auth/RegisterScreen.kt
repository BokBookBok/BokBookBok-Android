package konkuk.link.bokbookbok.screen.auth

import android.util.Patterns
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import konkuk.link.bokbookbok.component.auth.AuthButtonComponent
import konkuk.link.bokbookbok.component.auth.AuthButtonTypeEnum
import konkuk.link.bokbookbok.component.auth.AuthTextFieldComponent
import konkuk.link.bokbookbok.component.common.ButtonComponent
import konkuk.link.bokbookbok.component.common.ButtonTypeEnum
import konkuk.link.bokbookbok.data.model.request.register.RegisterEmailRequest
import konkuk.link.bokbookbok.data.model.request.register.RegisterNicknameRequest
import konkuk.link.bokbookbok.data.model.request.register.RegisterRequest
import konkuk.link.bokbookbok.navigation.auth.AuthScreen
import konkuk.link.bokbookbok.ui.theme.bokBookBokColors
import konkuk.link.bokbookbok.ui.theme.defaultBokBookBokTypography

private fun getButtonState(state: DuplicateCheckState): Pair<String, AuthButtonTypeEnum> {
    return when (state) {
        DuplicateCheckState.SUCCESS -> "사용가능" to AuthButtonTypeEnum.SUCCESS
        DuplicateCheckState.FAILURE -> "사용불가" to AuthButtonTypeEnum.FAIL
        DuplicateCheckState.IDLE -> "중복확인" to AuthButtonTypeEnum.BEFORE
    }
}

private fun String.isValidEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

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

    val isRegisterEnabled = emailCheckState == DuplicateCheckState.SUCCESS &&
            nicknameCheckState == DuplicateCheckState.SUCCESS &&
            passwordsMatch

    if (showEmailFormatAlert) {
        AlertDialog(
            onDismissRequest = { showEmailFormatAlert = false },
            title = {
                Text(
                    text = "알림",
                    style = defaultBokBookBokTypography.subHeader,
                    color = bokBookBokColors.second ) },
            text = {
                Text(
                    text = "올바른 이메일 형식이 아닙니다.",
                    style = defaultBokBookBokTypography.body,
                    color = bokBookBokColors.fontLightGray ) },
            confirmButton = {
                TextButton(
                    onClick = { showEmailFormatAlert = false }) {
                    Text(
                        text = "확인",
                        style = defaultBokBookBokTypography.body,
                        color = bokBookBokColors.second )
                }
            },
            containerColor = bokBookBokColors.white
        )
    }

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
            style = defaultBokBookBokTypography.header,
            color = bokBookBokColors.fontDarkBrown
        )

        Spacer(modifier = Modifier.height(70.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AuthTextFieldComponent(
                modifier = Modifier.weight(1f),
                value = emailValue,
                onValueChange = { newValue ->
                    emailValue = newValue
                    if (emailCheckState != DuplicateCheckState.IDLE) {
                        viewModel.resetEmailCheckState()
                    }
                },
                placeholder = "이메일",
                isPassword = false,
            )
            Spacer(modifier = Modifier.width(14.dp))
            val (buttonText, buttonType) = getButtonState(emailCheckState)
            AuthButtonComponent(
                buttonText = buttonText,
                buttonType = buttonType,
                onClick = {
                    if (emailValue.isValidEmail()) {
                        val request = RegisterEmailRequest(emailValue)
                        viewModel.registerEmail(request)
                    } else {
                        showEmailFormatAlert = true
                    }
                },
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AuthTextFieldComponent(
                modifier = Modifier.weight(1f),
                value = nicknameValue,
                onValueChange = { newValue ->
                    nicknameValue = newValue
                    if (nicknameCheckState != DuplicateCheckState.IDLE) {
                        viewModel.resetNicknameCheckState()
                    }
                },
                placeholder = "닉네임",
                isPassword = false,
            )
            Spacer(modifier = Modifier.width(14.dp))
            val (buttonText, buttonType) = getButtonState(nicknameCheckState)
            AuthButtonComponent(
                buttonText = buttonText,
                buttonType = buttonType,
                onClick = {
                    val request = RegisterNicknameRequest(nicknameValue)
                    viewModel.registerNickname(request)
                },
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        AuthTextFieldComponent(
            modifier = Modifier.fillMaxWidth(),
            value = passwordValue,
            onValueChange = { newValue -> passwordValue = newValue },
            placeholder = "비밀번호",
            isPassword = true,
        )

        Spacer(modifier = Modifier.height(24.dp))

        AuthTextFieldComponent(
            modifier = Modifier.fillMaxWidth(),
            value = passwordConfirmValue,
            onValueChange = { newValue -> passwordConfirmValue = newValue },
            placeholder = "비밀번호 확인",
            isPassword = true,
        )

        if (passwordConfirmValue.isNotEmpty()) {
            val message = if (passwordsMatch) "비밀번호가 일치합니다." else "비밀번호가 일치하지 않습니다."
            val color = if (passwordsMatch) bokBookBokColors.green else bokBookBokColors.second

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                color = color,
                style = defaultBokBookBokTypography.subBody,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(if (passwordConfirmValue.isEmpty()) 116.dp else 88.dp))

        ButtonComponent(
            buttonText = "회원가입",
            buttonType = if (isRegisterEnabled) ButtonTypeEnum.FILL else ButtonTypeEnum.LINE,
            enabled = isRegisterEnabled,
            onClick = {
                if (isRegisterEnabled) {
                    val request = RegisterRequest(emailValue, nicknameValue,passwordValue)
                    viewModel.register(request)
                    navController.navigate(AuthScreen.Login.route) {
                        popUpTo(AuthScreen.Register.route) { inclusive = true }
                    }
                }
            },
        )
    }
}