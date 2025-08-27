package konkuk.link.bokbookbok.component.auth

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import konkuk.link.bokbookbok.screen.auth.DuplicateCheckState

private fun getButtonState(state: DuplicateCheckState): Pair<String, AuthButtonTypeEnum> {
    return when (state) {
        DuplicateCheckState.SUCCESS -> "사용가능" to AuthButtonTypeEnum.SUCCESS
        DuplicateCheckState.FAILURE -> "사용불가" to AuthButtonTypeEnum.FAIL
        DuplicateCheckState.IDLE -> "중복확인" to AuthButtonTypeEnum.BEFORE
    }
}

@Composable
fun DuplicateCheckInputComponent(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    checkState: DuplicateCheckState,
    onCheckClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row (
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AuthTextFieldComponent(
            modifier = Modifier.weight(1f),
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            isPassword = false,
        )
        Spacer(modifier = Modifier.width(14.dp))
        val (buttonText, buttonType) = getButtonState(checkState)
        AuthButtonComponent(
            buttonText = buttonText,
            buttonType = buttonType,
            onClick = onCheckClick,
        )
    }
}