package covid19.coronavirus.data.session

import android.content.SharedPreferences
import covid19.coronavirus.BuildConfig

class SessionManagerImpl(
    private val sharedPreferences: SharedPreferences
) : SessionManager {
    override var lastDateUpdate: String
        get() = sharedPreferences.getString(LAST_DATE_UPDATE, "") ?: ""
        set(value) {
            sharedPreferences.edit().putString(LAST_DATE_UPDATE, value).apply()
        }
    override var enterAppCount: Int
        get() = sharedPreferences.getInt(ENTER_COUNT, 0)
        set(value) {
            sharedPreferences.edit().putInt(ENTER_COUNT, value).apply()
        }

    companion object {
        const val PREFERENCE_NAME = BuildConfig.APPLICATION_ID
        private const val LAST_DATE_UPDATE = "last_date_update"
        private const val ENTER_COUNT = "enter_count"
    }
}