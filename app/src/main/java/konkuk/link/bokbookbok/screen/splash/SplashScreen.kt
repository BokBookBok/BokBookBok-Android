package konkuk.link.bokbookbok.screen.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import konkuk.link.bokbookbok.R
import konkuk.link.bokbookbok.navigation.auth.AuthScreen
import konkuk.link.bokbookbok.ui.theme.bokBookBokColors
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1500)
        )
        delay(2000)

        navController.navigate(AuthScreen.Login.route) {
            popUpTo(AuthScreen.Splash.route) {
                inclusive = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background( // 그라데이션 배경 추가
                brush = Brush.verticalGradient(
                    colors = listOf(
                        bokBookBokColors.backGroundStart,
                        bokBookBokColors.backGroundEnd
                    )
                )
            )
            .alpha(alpha.value),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_bok_logo),
            contentDescription = "ic_bok_logo",
        )
    }
}
