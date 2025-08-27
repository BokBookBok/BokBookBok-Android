package konkuk.link.bokbookbok.navigation.auth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import konkuk.link.bokbookbok.data.remote.RetrofitClient
import konkuk.link.bokbookbok.data.repository.AuthRepository
import konkuk.link.bokbookbok.navigation.NavigationGraph
import konkuk.link.bokbookbok.screen.auth.LoginScreen
import konkuk.link.bokbookbok.screen.auth.SignUpScreen
import konkuk.link.bokbookbok.screen.auth.SignUpViewModelFactory
import konkuk.link.bokbookbok.screen.splash.SplashScreen

fun NavGraphBuilder.authNavGraph(navController: NavController) {
    navigation(
        route = NavigationGraph.AUTH,
        startDestination = AuthScreen.Splash.route,
    ) {
        val authRepository = AuthRepository(RetrofitClient.publicApiService)

        composable(route = AuthScreen.Splash.route) {
            SplashScreen(navController = navController)
        }

        composable(route = AuthScreen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(route = AuthScreen.SignUp.route) {
            SignUpScreen(
                navController = navController,
                factory = SignUpViewModelFactory(authRepository),
            )
        }
    }
}
