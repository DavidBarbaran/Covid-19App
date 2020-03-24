package covid19.coronavirus.util.location

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import java.lang.Exception

class SmartLocation(activity: AppCompatActivity) {

    private val locationObserver: LocationObserver =
        LocationObserver.getInstance()

    private var locationApiClient: LocationApiClient? = null
    var onGetLocationSuccessful: OnGetLocationSuccessful? = null
    var onGetLocationFailed: OnGetLocationFailed? = null

    init {
        locationApiClient = LocationApiClient(
            activity
        ).addOnGetLocationSuccessful {
            onGetLocationSuccessful?.onGetLocationSuccessful(it)
        }.addOnGetLocationFailed {
            onGetLocationFailed?.onGetLocationFailed(it)
        }
        locationApiClient?.isGetLocation = false
        locationApiClient?.isLocationRequest = false
        locationObserver.addOnConnectListener {
            locationApiClient?.connect()
        }.addOnDisconnectListener {
            locationApiClient?.disconnect()
        }
        activity.lifecycle.addObserver(locationObserver)
    }

    inline fun addOnGetLocationSuccessful(crossinline onGetLocationSuccessful: (location: Location) -> Unit) =
        apply {
            this.onGetLocationSuccessful = object :
                OnGetLocationSuccessful {
                override fun onGetLocationSuccessful(location: Location) {
                    onGetLocationSuccessful(location)
                }
            }
        }

    inline fun addOnGetLocationFailed(crossinline onGetLocationFailed: (exception: Exception) -> Unit) =
        apply {
            this.onGetLocationFailed = object :
                OnGetLocationFailed {
                override fun onGetLocationFailed(exception: Exception) {
                    onGetLocationFailed(exception)
                }
            }
        }

    fun activateGPS() {
        locationApiClient?.getLastLocation()
    }

    companion object {
        const val TAG = "LocationListener"
        const val RQ_RESOLUTION_REQUIRED = 200
    }
}