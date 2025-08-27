package konkuk.link.bokbookbok.screen

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import konkuk.link.bokbookbok.navigation.bottom.BottomNavHost
import konkuk.link.bokbookbok.navigation.bottom.CustomBottomNavBar

@Composable
fun MainScreen() {
    val bottomNavController = rememberNavController()
    Scaffold(
        modifier = Modifier.navigationBarsPadding(),
        containerColor = Color.Transparent,
        bottomBar = { CustomBottomNavBar(navController = bottomNavController) },
    ) { innerPadding ->
        BottomNavHost(navController = bottomNavController, innerPadding = innerPadding)
    }
}
