package konkuk.link.bokbookbok.navigation.bottom

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import konkuk.link.bokbookbok.navigation.Screen
import konkuk.link.bokbookbok.ui.theme.bokBookBokColors
import konkuk.link.bokbookbok.ui.theme.defaultBokBookBokTypography
import konkuk.link.bokbookbok.util.topSemiCircleShadow

@Composable
fun CustomBottomNavBar(navController: NavController) {
    val items =
        listOf(
            Screen.ReviewHome,
            Screen.ReadingHome,
            Screen.RecordHome,
        )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(88.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        Card(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(5.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
            colors = CardDefaults.cardColors(containerColor = bokBookBokColors.white),
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                items.forEach { item ->
                    if (item.route == Screen.ReadingHome.route) {
                        Column(
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clickable { navigateTo(navController, item.route) },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = item.title ?: "",
                                style = defaultBokBookBokTypography.subBody,
                                color = if (currentRoute == item.route) bokBookBokColors.fontDarkBrown else Color.Gray,
                            )
                        }
                    } else {
                        StandardNavItem(
                            item = item,
                            isSelected = currentRoute == item.route,
                        ) {
                            navigateTo(navController, item.route)
                        }
                    }
                }
            }
        }

        Box(
            modifier =
                Modifier
                    .offset(y = (-8).dp)
                    .topSemiCircleShadow(elevation = 8.dp)
                    .background(color = bokBookBokColors.white, shape = CircleShape)
                    .clip(CircleShape)
                    .clickable { navigateTo(navController, Screen.ReadingHome.route) },
            contentAlignment = Alignment.Center,
        ) {
            Screen.ReadingHome.icon?.let { iconId ->
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = Screen.ReadingHome.title,
                    modifier =
                        Modifier
                            .padding(10.dp)
                            .size(48.dp),
                    tint = Color.Unspecified,
                )
            }
        }
    }
}

@Composable
fun RowScope.StandardNavItem(
    item: Screen,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val contentColor = if (isSelected) bokBookBokColors.fontDarkBrown else Color.Gray

    Column(
        modifier =
            Modifier
                .weight(1f)
                .fillMaxHeight()
                .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        item.icon?.let {
            Icon(
                painter = painterResource(id = item.icon),
                contentDescription = item.title,
                modifier =
                    Modifier
                        .padding(top = 10.dp, start = 20.dp, end = 20.dp)
                        .size(24.dp),
                tint = contentColor,
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        item.title?.let {
            Text(
                text = it,
                style = defaultBokBookBokTypography.subBody,
                color = contentColor,
            )
        }
    }
}

private fun navigateTo(
    navController: NavController,
    route: String,
) {
    navController.navigate(route) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F0F0)
@Composable
fun CustomBottomNavBarPreview() {
    val navController = rememberNavController()
    Box(modifier = Modifier.padding(16.dp)) {
        CustomBottomNavBar(navController = navController)
    }
}
