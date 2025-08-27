package konkuk.link.bokbookbok.component.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import konkuk.link.bokbookbok.ui.theme.bokBookBokColors
import konkuk.link.bokbookbok.ui.theme.defaultBokBookBokTypography

@Composable
fun PasswordInputSectionComponent(
    passwordValue: String,
    onPasswordValueChange: (String) -> Unit,
    passwordConfirmValue: String,
    onPasswordConfirmValueChange: (String) -> Unit,
    passwordsMatch: Boolean,
) {
    AuthTextFieldComponent(
        modifier = Modifier.fillMaxWidth(),
        value = passwordValue,
        onValueChange = onPasswordValueChange,
        placeholder = "비밀번호",
        isPassword = true,
    )

    Spacer(modifier = Modifier.height(24.dp))

    AuthTextFieldComponent(
        modifier = Modifier.fillMaxWidth(),
        value = passwordConfirmValue,
        onValueChange = onPasswordConfirmValueChange,
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
}