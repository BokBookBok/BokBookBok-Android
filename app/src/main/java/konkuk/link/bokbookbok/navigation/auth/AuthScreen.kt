package konkuk.link.bokbookbok.navigation.auth

sealed class AuthScreen(
    val route: String,
) {
    data object Splash : AuthScreen("splash")

    data object Login : AuthScreen("login")

    data object Register : AuthScreen("register")

    data object Admin : AuthScreen("admin")
}
