package covid19.coronavirus.util.location

import android.location.Location

interface OnGetLocationSuccessful {
    fun onGetLocationSuccessful(location: Location)
}