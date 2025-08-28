package konkuk.link.bokbookbok.component.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import konkuk.link.bokbookbok.R
import konkuk.link.bokbookbok.ui.theme.bokBookBokColors

@Composable
fun WriteFAB(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onClick,
        shape = CircleShape,
        containerColor = bokBookBokColors.second,
        contentColor = bokBookBokColors.white,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_pencil),
            contentDescription = "글쓰기",
            tint = Color.Unspecified,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun WriteFABPreview() {
    Scaffold(
        floatingActionButton = {
            WriteFAB(onClick = { })
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
        )
    }
}
