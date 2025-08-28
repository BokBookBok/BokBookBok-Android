package konkuk.link.bokbookbok.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import konkuk.link.bokbookbok.data.remote.RetrofitClient
import konkuk.link.bokbookbok.data.repository.AuthRepository
import konkuk.link.bokbookbok.data.repository.ReviewRepository
import konkuk.link.bokbookbok.screen.auth.LoginScreen
import konkuk.link.bokbookbok.screen.auth.LoginViewModelFactory
import konkuk.link.bokbookbok.screen.auth.RegisterScreen
import konkuk.link.bokbookbok.screen.auth.RegisterViewModelFactory
import konkuk.link.bokbookbok.screen.reading.ReadingScreen
import konkuk.link.bokbookbok.screen.record.RecordScreen
import konkuk.link.bokbookbok.screen.review.ReviewHomeViewModelFactory
import konkuk.link.bokbookbok.screen.review.ReviewScreen
import konkuk.link.bokbookbok.screen.review.ReviewWriteScreen
import konkuk.link.bokbookbok.screen.review.ReviewWriteViewModelFactory
import konkuk.link.bokbookbok.screen.splash.SplashScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    innerPadding: PaddingValues,
) {
    // repository
    val authRepository = remember { AuthRepository(RetrofitClient.publicApiService) }
    val reviewRepository = remember { ReviewRepository(RetrofitClient.authApiService) }
    // val readingRepository = remember { ReadingRepository(RetrofitClient.authApiService) }

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = Modifier.padding(innerPadding),
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(route = Screen.Login.route) {
            val factory = remember { LoginViewModelFactory(authRepository) }
            LoginScreen(navController = navController, factory = factory)
        }
        composable(route = Screen.Register.route) {
            val factory = remember { RegisterViewModelFactory(authRepository) }
            RegisterScreen(
                navController = navController,
                factory = factory,
            )
        }

        composable(route = Screen.ReviewHome.route) {
            val factory = remember { ReviewHomeViewModelFactory(reviewRepository) }
            ReviewScreen(
                navController = navController,
                viewModel = viewModel(factory = factory),
            )
        }
        composable(route = Screen.ReadingHome.route) {
            // val factory = remember { ReadingViewModelFactory(readingRepository) }
            ReadingScreen(
                // navController = navController,
                // viewModel = viewModel(factory = factory)
            )
        }
        composable(route = Screen.RecordHome.route) {
            RecordScreen()
        }

        composable(
            route = Screen.WriteReview.route,
            arguments = listOf(navArgument("bookId") { type = NavType.IntType }),
        ) {
            val factory = remember { ReviewWriteViewModelFactory(reviewRepository) }
            ReviewWriteScreen(
                navController = navController,
                factory = factory,
            )
        }
        composable(route = Screen.BookRecordReview.route) {
            // BookRecordReviewScreen(navController = navController)
        }
    }
}
