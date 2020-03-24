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

    companion object {
        const val PREFERENCE_NAME = BuildConfig.APPLICATION_ID
        private const val LAST_DATE_UPDATE = "last_date_update"
    }
}