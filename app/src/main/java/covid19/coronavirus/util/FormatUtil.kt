package covid19.coronavirus.util

import java.lang.Exception
import java.text.DecimalFormat

fun formatNumber(n: Int): String {
    return try {
        val numberFormat = DecimalFormat("#,##0")
        numberFormat.format(n)
    } catch (ex: Exception) {
        n.toString()
    }
}