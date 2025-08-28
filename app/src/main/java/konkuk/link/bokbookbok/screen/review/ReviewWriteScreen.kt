package konkuk.link.bokbookbok.screen.review

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import konkuk.link.bokbookbok.component.common.BookInfoCard
import konkuk.link.bokbookbok.ui.theme.bokBookBokColors
import konkuk.link.bokbookbok.ui.theme.defaultBokBookBokTypography

@Composable
fun ReviewWriteScreen(
    navController: NavController,
    factory: ReviewWriteViewModelFactory,
) {

    val viewModel: ReviewWriteViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var reviewText by remember { mutableStateOf("") }

    LaunchedEffect(uiState.postState) {
        when (val state = uiState.postState) {
            is ReviewWritePostState.Success -> {
                Toast.makeText(context, "감상평이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
            is ReviewWritePostState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(bokBookBokColors.white)
                .padding(horizontal = 28.dp, vertical = 12.dp),
    ) {
        ReviewWriteTopBar(
            onCloseClick = { navController.popBackStack() },
            onPostClick = {
                viewModel.postReview(reviewText)
            },
            isPostEnabled = reviewText.isNotBlank() && uiState.postState !is ReviewWritePostState.Loading
        )
        Spacer(modifier = Modifier.height(21.dp))
        // TODO: 서버에 책 정보 받아오기 구현
        BookInfoCard(
            imageUrl = "https://placehold.co/120x160/F5C54D/FFFFFF?text=Book",
            title = "데일 카네기 인간관계론",
            author = "데일 카네기",
        )
        Spacer(modifier = Modifier.height(21.dp))
        ReviewWriteTextField(
            value = reviewText,
            onValueChange = { reviewText = it },
        )
    }
}

@Composable
fun ReviewWriteTopBar(
    onCloseClick: () -> Unit,
    onPostClick: () -> Unit,
    isPostEnabled: Boolean,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        IconButton(onClick = onCloseClick) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "닫기", tint = bokBookBokColors.fontLightGray)
        }
        Text(text = "감상평 작성하기", style = defaultBokBookBokTypography.subHeader, color = bokBookBokColors.fontDarkBrown)
        TextButton(onClick = onPostClick, enabled = isPostEnabled) {
            Text(text = "게시", style = defaultBokBookBokTypography.body, color = bokBookBokColors.fontLightGray)
        }
    }
}

@Composable
fun ReviewWriteTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isTextEmpty = value.isEmpty()

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier =
            modifier
                .fillMaxSize(),
        placeholder = {
            Text("당신의 감상평이 토론 주제가 될 수 있어요.", color = bokBookBokColors.fontLightGray, style = defaultBokBookBokTypography.body)
        },
        shape = RoundedCornerShape(10.dp),
        colors =
            OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (!isTextEmpty) bokBookBokColors.borderYellow else bokBookBokColors.borderDarkGray,
                unfocusedBorderColor = if (!isTextEmpty) bokBookBokColors.borderYellow else bokBookBokColors.borderDarkGray,
                cursorColor = bokBookBokColors.borderDarkGray,
            ),
    )
}

@Preview(showBackground = true)
@Composable
fun ReviewWriteTopBarPreview() {
    ReviewWriteTopBar(
        onCloseClick = {},
        onPostClick = {},
        isPostEnabled = true
    )
}

@Preview(showBackground = true)
@Composable
fun ReviewWriteTextFieldPreview() {
    ReviewWriteTextField(
        value = "",
        onValueChange = {},
    )
}

@Preview(showBackground = true)
@Composable
fun ReviewWriteTextFieldWithTextPreview() {
    ReviewWriteTextField(
        value = "좋은 책이네요. 다음에 또 읽어보고 싶어요.",
        onValueChange = {},
    )
}

@Preview(showBackground = true)
@Composable
fun ReviewWriteScreenPreview() {
    // ReviewWriteScreen(navController = NavController(LocalContext.current))
}
