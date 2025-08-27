package konkuk.link.bokbookbok.screen

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import konkuk.link.bokbookbok.navigation.bottom.BottomNavHost
import konkuk.link.bokbookbok.navigation.bottom.CustomBottomNavBar

@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        modifier = Modifier.navigationBarsPadding(),
        containerColor = Color.Transparent,
        bottomBar = { CustomBottomNavBar(navController = navController) },
    ) { innerPadding ->
        BottomNavHost(navController = navController, innerPadding = innerPadding)
    }
}
