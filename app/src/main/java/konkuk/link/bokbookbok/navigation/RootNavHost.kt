package konkuk.link.bokbookbok.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import konkuk.link.bokbookbok.navigation.auth.authNavGraph
import konkuk.link.bokbookbok.screen.MainScreen

@Composable
fun RootNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = NavigationGraph.ROOT,
        startDestination = NavigationGraph.AUTH,
    ) {
        authNavGraph(navController = navController)
        composable(route = NavigationGraph.MAIN) {
            MainScreen()
        }
    }
}
