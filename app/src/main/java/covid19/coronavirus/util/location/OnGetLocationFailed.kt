package covid19.coronavirus.util.location

import java.lang.Exception

interface OnGetLocationFailed {
    fun onGetLocationFailed(exception: Exception)
}