package konkuk.link.bokbookbok.util

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import konkuk.link.bokbookbok.R
import konkuk.link.bokbookbok.ui.theme.BOKBOOKBOKTheme
import konkuk.link.bokbookbok.ui.theme.BokBookBokTheme.bokBookBokColors
import konkuk.link.bokbookbok.ui.theme.defaultBokBookBokTypography

class BokToast(
    private val context: Context,
) : Toast(context) {
    fun makeText(
        message: String,
        icon: Int? = R.drawable.ic_book_main,
        lifecycleOwner: LifecycleOwner,
        duration: Int = LENGTH_SHORT,
    ) {
        val views = ComposeView(context)

        views.setContent {
            BOKBOOKBOKTheme {
                BokToastContent(
                    messageTxt = message,
                    resourceIcon = icon,
                )
            }
        }

        views.setViewTreeLifecycleOwner(lifecycleOwner)
        views.setViewTreeSavedStateRegistryOwner(lifecycleOwner as? SavedStateRegistryOwner)
        views.setViewTreeViewModelStoreOwner(lifecycleOwner as? ViewModelStoreOwner)

        this.duration = duration
        this.view = views

        this.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 100)
        this.show()
    }
}

@Composable
fun BokToastContent(
    messageTxt: String,
    resourceIcon: Int? = R.drawable.ic_book_main,
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier =
                Modifier
                    .background(
                        color = bokBookBokColors.white,
                        shape = RoundedCornerShape(size = 30.dp),
                    ).border(width = 2.dp, color = bokBookBokColors.main, shape = RoundedCornerShape(30.dp))
                    .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            resourceIcon?.let { iconRes ->
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = "toastIcon",
                    modifier = Modifier.padding(end = 7.dp),
                )
            }
            Text(
                text = messageTxt,
                style =
                    defaultBokBookBokTypography.body.copy(
                        color = bokBookBokColors.fontDarkBrown,
                    ),
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
fun MementoToastContentPreview() {
    BOKBOOKBOKTheme {
        BokToastContent(
            messageTxt = "책을 다 읽은 후에만 투표할 수 있습니다.",
        )
    }
}
