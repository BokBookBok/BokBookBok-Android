package konkuk.link.bokbookbok.navigation.bottom

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import konkuk.link.bokbookbok.screen.reading.ReadingScreen
import konkuk.link.bokbookbok.screen.record.RecordScreen
import konkuk.link.bokbookbok.screen.review.ReviewScreen

@Composable
fun BottomNavHost(
    navController: NavHostController,
    innerPadding: PaddingValues,
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Reading.route,
        modifier = Modifier.padding(innerPadding),
    ) {
        composable(route = BottomNavItem.Review.route) {
            ReviewScreen()
        }
        composable(route = BottomNavItem.Reading.route) {
            ReadingScreen()
        }
        composable(route = BottomNavItem.Record.route) {
            RecordScreen()
        }
    }
}
