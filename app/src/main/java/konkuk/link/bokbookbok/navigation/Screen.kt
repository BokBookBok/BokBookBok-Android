package konkuk.link.bokbookbok.navigation

import androidx.annotation.DrawableRes
import konkuk.link.bokbookbok.R

sealed class Screen(
    val route: String,
    val isBottomBarVisible: Boolean = false,
    val title: String? = null,
    @DrawableRes val icon: Int? = null,
) {
    data object Splash : Screen("splash")

    data object Login : Screen("login")

    data object Register : Screen("register")

    data object Admin : Screen("admin")

    data object ReviewHome : Screen(
        route = "review_home",
        isBottomBarVisible = true,
        title = "감상홈",
        icon = R.drawable.ic_chat_bubble_outline_rounded_black,
    )

    data object ReadingHome : Screen(
        route = "reading_home",
        isBottomBarVisible = true,
        title = "독서홈",
        icon = R.drawable.btn_reading_book,
    )

    data object RecordHome : Screen(
        route = "record_home",
        isBottomBarVisible = true,
        title = "기록홈",
        icon = R.drawable.ic_folder_outline_black,
    )

    data object WriteReview : Screen("write_review/{bookId}") {
        fun createRoute(bookId: Int) = "write_review/$bookId"
    }

    data object BookRecordReview : Screen("book_record_review/{recordId}", true) {
        fun createRoute(recordId: Int) = "book_record_review/$recordId"
    }
}
