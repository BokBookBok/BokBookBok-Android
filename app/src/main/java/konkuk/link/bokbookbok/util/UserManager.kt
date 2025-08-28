package konkuk.link.bokbookbok.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit

object UserManager {
    private const val PREFS_NICKNAME = "nickname_prefs"
    private const val USER_NICKNAME = "nickname"

    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(UserManager.PREFS_NICKNAME, Context.MODE_PRIVATE)
    }

    fun saveNickname(nickname: String) {
        sharedPreferences.edit {
            putString(USER_NICKNAME, nickname)
        }
        Log.d("UserManager", "Nickname saved: $nickname")
    }

    fun getNickname(): String? = sharedPreferences.getString(USER_NICKNAME, null)

    fun clearNickname() {
        sharedPreferences.edit {
            remove(USER_NICKNAME)
        }
    }
}
