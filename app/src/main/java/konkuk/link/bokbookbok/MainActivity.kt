package konkuk.link.bokbookbok

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import konkuk.link.bokbookbok.navigation.RootNavHost
import konkuk.link.bokbookbok.ui.theme.BOKBOOKBOKTheme
import konkuk.link.bokbookbok.util.TokenManager
import konkuk.link.bokbookbok.util.UserManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenManager.init(this)
        UserManager.init(this)
        enableEdgeToEdge()
        setContent {
            BOKBOOKBOKTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val navController = rememberNavController()
                    RootNavHost(navController = navController)
                }
            }
        }
    }
}
