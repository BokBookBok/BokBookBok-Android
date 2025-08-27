package konkuk.link.bokbookbok.navigation.auth

sealed class AuthScreen(
    val route: String,
) {
    data object Login : AuthScreen("login")

    data object SignUp : AuthScreen("signup")

    data object Admin : AuthScreen("admin")
}
