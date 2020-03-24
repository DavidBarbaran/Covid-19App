package covid19.coronavirus.util.location

import android.content.IntentSender.SendIntentException
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task

class LocationApiClient(private val activity: AppCompatActivity) :
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    private var mGoogleApiClient: GoogleApiClient? = null

    var onGetLocationSuccessful: OnGetLocationSuccessful? = null
    var onGetLocationFailed: OnGetLocationFailed? = null

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null

    var isGetLocation = false
    var isLocationRequest = false

    init {
        mGoogleApiClient = GoogleApiClient.Builder(activity)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
    }

    fun connect() {
        if (!isGetLocation && !isLocationRequest) {
            mGoogleApiClient?.connect()
        }
    }

    fun disconnect() {
        if (mGoogleApiClient?.isConnected == true) {
            mGoogleApiClient?.disconnect()
            fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
        }
    }

    inline fun addOnGetLocationSuccessful(crossinline getLocationSuccessful: (location: Location) -> Unit) =
        apply {
            this.onGetLocationSuccessful = object :
                OnGetLocationSuccessful {
                override fun onGetLocationSuccessful(location: Location) {
                    getLocationSuccessful(location)
                }
            }
        }

    inline fun addOnGetLocationFailed(crossinline getLocationFailed: (exception: Exception) -> Unit) =
        apply {
            onGetLocationFailed = object :
                OnGetLocationFailed {
                override fun onGetLocationFailed(exception: Exception) {
                    getLocationFailed(exception)
                }
            }
        }

    override fun onConnected(p0: Bundle?) {
        Log.i(SmartLocation.TAG, "Location services connected.")
        try {
            isLocationRequest = true
            val locationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = 10 * 1000
            locationRequest.fastestInterval = 2 * 1000

            getSettingsClient(locationRequest)

        } catch (ex: Exception) {
            onGetLocationFailed?.onGetLocationFailed(ex)
        }
    }

    private fun getSettingsClient(locationRequest: LocationRequest) {

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .setAlwaysShow(true)

        val settingsClient = LocationServices.getSettingsClient(activity)

        settingsClient.checkLocationSettings(builder.build())
            .addOnSuccessListener {
                getLastLocation()
            }.addOnFailureListener {
                onGetLocationFailed?.onGetLocationFailed(it)
            }.addOnCanceledListener {
                onGetLocationFailed?.onGetLocationFailed(
                    LocationException(
                        "onGetLocationCanceled"
                    )
                )
            }.addOnCompleteListener {
                evaluateOnComplete(it)
            }
    }

    fun getLastLocation() {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10 * 1000
        locationRequest.fastestInterval = 2 * 1000

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                p0?.run {
                    isGetLocation = true
                    onGetLocationSuccessful?.onGetLocationSuccessful(lastLocation)
                    fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
                }
            }
        }

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(activity).apply {
                requestLocationUpdates(locationRequest, locationCallback, null)
            }
    }

    private fun evaluateOnComplete(task: Task<LocationSettingsResponse>) {
        try {
            task.getResult(ApiException::class.java)
        } catch (exception: ApiException) {
            when (exception.statusCode) {
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                    val resolvable =
                        exception as ResolvableApiException
                    resolvable.startResolutionForResult(
                        activity,
                        SmartLocation.RQ_RESOLUTION_REQUIRED
                    )
                } catch (e: SendIntentException) {
                    onGetLocationFailed?.onGetLocationFailed(e)
                } catch (e: ClassCastException) {
                    onGetLocationFailed?.onGetLocationFailed(e)
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    onGetLocationFailed?.onGetLocationFailed(
                        LocationException(
                            "SettingsChangeUnavailable: ${exception.statusCode}"
                        )
                    )
                }
            }
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        onGetLocationFailed?.onGetLocationFailed(
            LocationException(
                "onConnectionSuspended: $p0"
            )
        )
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        onGetLocationFailed?.onGetLocationFailed(
            LocationException(
                "${p0.errorCode} ${p0.errorMessage}"
            )
        )
    }
}