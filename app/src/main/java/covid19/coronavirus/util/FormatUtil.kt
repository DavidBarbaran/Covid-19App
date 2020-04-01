package covid19.coronavirus.util

import java.lang.Exception
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

fun formatNumber(n: Int): String {
    return try {
        val numberFormat = DecimalFormat("#,##0")
        numberFormat.format(n)
    } catch (ex: Exception) {
        n.toString()
    }
}

fun formatDateFromMillis(milliSeconds: Long): String {
    var formatDate: String
    try {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        formatDate = formatter.format(calendar.time)
    } catch (ex: Exception) {
        formatDate = milliSeconds.toString()
    }
    return formatDate
}