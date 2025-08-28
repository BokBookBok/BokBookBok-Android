package konkuk.link.bokbookbok

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import konkuk.link.bokbookbok.navigation.AppNavHost
import konkuk.link.bokbookbok.navigation.Screen
import konkuk.link.bokbookbok.navigation.bottom.CustomBottomNavBar
import konkuk.link.bokbookbok.ui.theme.BOKBOOKBOKTheme
import konkuk.link.bokbookbok.ui.theme.bokBookBokColors
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
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                val screens =
                    listOf(
                        Screen.Splash,
                        Screen.Login,
                        Screen.Register,
                        Screen.Admin,
                        Screen.ReadingHome,
                        Screen.ReviewHome,
                        Screen.RecordHome,
                        Screen.RecordDetail,
                        Screen.WriteReview,
                        Screen.BookRecordReview,
                    )

                val currentScreen =
                    screens.find {
                        val routePattern = it.route.split('/').first()
                        currentDestination?.route?.startsWith(routePattern) == true
                    }

                Scaffold(
                    containerColor = bokBookBokColors.white,
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        AnimatedVisibility(
                            visible = currentScreen?.isBottomBarVisible == true,
                        ) {
                            CustomBottomNavBar(navController = navController)
                        }
                    },
                ) { innerPadding ->
                    val contentPadding = PaddingValues(
                        top = innerPadding.calculateTopPadding(),
                        start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                        end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
                        bottom = 0.dp
                    )

                    AppNavHost(navController = navController, modifier = Modifier.padding(contentPadding))
                }
            }
        }
    }
}
