package covid19.coronavirus.firebase.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import covid19.coronavirus.BuildConfig
import covid19.coronavirus.firebase.analytics.AnalyticsUtil.Param.ACTION

object AnalyticsUtil {

    object Param {
        const val ACTION = "action"
    }

    object Value {
        const val MENU_OPEN = "menu_open"
        const val MENU_GLOBAL_CASES = "menu_global_cases"
        const val MENU_SYMPTOMS = "menu_symptoms"
        const val MENU_EMERGENCY = "menu_emergency"
        const val MENU_SHARE = "menu_share"
        const val MENU_FEEDBACK = "menu_feedback"
        const val MENU_CREDITS = "menu_credits"
        const val MENU_CONTACT = "menu_contact"

        const val SEARCH = "search"
        const val YOUR_LOCATION = "your_location"

        const val HOME_OPEN_SEARCH = "home_open_search"
        const val HOME_GLOBAL_CASES = "home_global_cases"
        const val HOME_SYMPTOMS = "home_symptoms"
        const val HOME_SYMPTOMS_BUTTON = "home_symptoms_button"
        const val HOME_EMERGENCY = "home_emergency"
        const val HOME_EMERGENCY_BUTTON = "home_emergency_button"

        const val SYMPTOMS_EMERGENCY = "symptoms_emergency"
    }

    fun logEvent(context: Context, event: String, itemId: String = event) {
        if (!BuildConfig.DEBUG) {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, itemId)
            FirebaseAnalytics.getInstance(context).logEvent(event, bundle)
        }
    }

    fun logEventAction(context: Context, event: String, action: String) {
        if (!BuildConfig.DEBUG) {
            val bundle = Bundle()
            bundle.putString(ACTION, action)
            FirebaseAnalytics.getInstance(context).logEvent(event, bundle)
        }
    }
}