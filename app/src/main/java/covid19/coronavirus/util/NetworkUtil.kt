package covid19.coronavirus.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object NetworkUtil {

    @Suppress("DEPRECATION")
    fun isOnline(context: Context): Boolean {
        val cm: ConnectivityManager? =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (cm != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                val ni = cm.activeNetworkInfo
                return ni?.isConnectedOrConnecting == true
            } else {
                val n = cm.activeNetwork
                if (n != null) {
                    val nc: NetworkCapabilities? = cm.getNetworkCapabilities(n)
                    return nc?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false ||
                            nc?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false
                }
            }
        }
        return false
    }
}