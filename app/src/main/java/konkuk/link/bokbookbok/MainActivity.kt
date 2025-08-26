package konkuk.link.bokbookbok

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import konkuk.link.bokbookbok.ui.theme.BOKBOOKBOKTheme
import konkuk.link.bokbookbok.ui.theme.BokBookBokTheme
import konkuk.link.bokbookbok.ui.theme.bokBookBokColors

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BOKBOOKBOKTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
) {
    Column {
        Text(
            text = "책을 읽읍시다",
            modifier = modifier,
            color = bokBookBokColors.blue,
            style = BokBookBokTheme.bokBookBokTypography.body
        )
        Text(
            text = "책을 읽읍시다",
            modifier = modifier,
            color = bokBookBokColors.main,
            style = BokBookBokTheme.bokBookBokTypography.subLogo
        )
        Text(
            text = "책을 읽읍시다",
            modifier = modifier,
            color = bokBookBokColors.fontDarkGray,
            style = BokBookBokTheme.bokBookBokTypography.logo
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BOKBOOKBOKTheme {
        Greeting("Android")
    }
}
