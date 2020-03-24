package covid19.coronavirus.util

import android.content.Context
import com.crashlytics.android.Crashlytics
import covid19.coronavirus.R
import covid19.coronavirus.exception.NetworkException
import java.lang.Exception

fun getErrorMessage(context: Context, ex: Exception): String {
    return if (ex is NetworkException) {
        ex.message!!
    } else {
        Crashlytics.logException(ex)
        context.getString(R.string.default_error_message)
    }
}