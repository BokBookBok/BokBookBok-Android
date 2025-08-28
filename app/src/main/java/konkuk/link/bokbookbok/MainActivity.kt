package konkuk.link.bokbookbok

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import konkuk.link.bokbookbok.navigation.AppNavHost
import konkuk.link.bokbookbok.navigation.Screen
import konkuk.link.bokbookbok.navigation.bottom.CustomBottomNavBar
import konkuk.link.bokbookbok.ui.theme.BOKBOOKBOKTheme
import konkuk.link.bokbookbok.util.TokenManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenManager.init(this)
        enableEdgeToEdge()
        setContent {
            BOKBOOKBOKTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                val screens = listOf(
                    Screen.Splash,
                    Screen.Login,
                    Screen.Register,
                    Screen.Admin,
                    Screen.ReadingHome,
                    Screen.ReviewHome,
                    Screen.RecordHome,
                    Screen.WriteReview,
                    Screen.BookRecordReview
                )

                val currentScreen = screens.find {
                    val routePattern = it.route.split('/').first()
                    currentDestination?.route?.startsWith(routePattern) == true
                }

                Scaffold(
                    bottomBar = {
                        AnimatedVisibility(
                            visible = currentScreen?.isBottomBarVisible == true,
                        ) {
                            CustomBottomNavBar(navController = navController)
                        }
                    }
                ) { innerPadding ->
                    AppNavHost(navController = navController, innerPadding = innerPadding)
                }
            }
        }
    }
}
