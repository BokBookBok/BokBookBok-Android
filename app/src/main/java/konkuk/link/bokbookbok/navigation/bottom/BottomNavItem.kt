package konkuk.link.bokbookbok.navigation.bottom

import konkuk.link.bokbookbok.R

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: Int,
) {
    data object Review : BottomNavItem(
        route = "review",
        title = "감상홈",
        icon = R.drawable.ic_chat_bubble_outline_rounded_black,
    )

    data object Reading : BottomNavItem(
        route = "reading",
        title = "독서홈",
        icon = R.drawable.btn_reading_book,
    )

    data object Record : BottomNavItem(
        route = "record",
        title = "기록홈",
        icon = R.drawable.ic_folder_outline_black,
    )
}
