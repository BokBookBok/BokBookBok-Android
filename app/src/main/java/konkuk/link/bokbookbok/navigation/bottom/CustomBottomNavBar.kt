package konkuk.link.bokbookbok.navigation.bottom

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun CustomBottomNavBar(navController: NavController) {
    val items =
        listOf(
            BottomNavItem.Review,
            BottomNavItem.Reading,
            BottomNavItem.Record,
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
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White), // Todo: 북북북 색으로 변경
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                items.forEach { item ->
                    if (item.route == BottomNavItem.Reading.route) {
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
                                text = item.title,
                                fontSize = 12.sp,
                                color = if (currentRoute == item.route) Color(0xFFD18A8A) else Color.Gray,
                                fontWeight = if (currentRoute == item.route) FontWeight.Bold else FontWeight.Normal,
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
                    .size(68.dp)
                    .offset(y = (-8).dp) // Y축으로 -8dp 만큼 위로 이동
                    .shadow(elevation = 4.dp, shape = CircleShape)
                    .clip(CircleShape)
                    .background(
                        brush =
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFFFFF3E0), Color.White),
                            ),
                    ).border(width = 1.dp, color = Color.White.copy(alpha = 0.8f), shape = CircleShape)
                    .clickable { navigateTo(navController, BottomNavItem.Reading.route) },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(id = BottomNavItem.Reading.icon),
                contentDescription = BottomNavItem.Reading.title,
                modifier = Modifier.size(32.dp),
                tint = Color(0xFFD18A8A), // 아이콘 색상 고정
            )
        }
    }
}

@Composable
fun RowScope.StandardNavItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val contentColor = if (isSelected) Color(0xFF4C4C4C) else Color.Gray
    val icon = if (isSelected) item.icon else item.icon

    Column(
        modifier =
            Modifier
                .weight(1f)
                .fillMaxHeight()
                .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = item.title,
            modifier = Modifier.size(24.dp),
            tint = contentColor,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = item.title,
            fontSize = 12.sp,
            color = contentColor,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
        )
    }
}

fun navigateTo(
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
