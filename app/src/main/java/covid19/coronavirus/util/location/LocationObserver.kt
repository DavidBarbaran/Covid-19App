package covid19.coronavirus.util.location

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class LocationObserver : LifecycleObserver {

    var onLocationConnectObserver: OnLocationConnectObserver? = null

    var onLocationDisconnectObserver: OnLocationDisconnectObserver? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun connectListener() {
        onLocationConnectObserver?.onConnect()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun disconnectListener() {
        onLocationDisconnectObserver?.onDisconnect()
    }

    inline fun addOnConnectListener(crossinline onConnect: () -> Unit) = apply {
        onLocationConnectObserver = object :
            OnLocationConnectObserver {
            override fun onConnect() {
                onConnect()
            }
        }
    }

    inline fun addOnDisconnectListener(crossinline onDisconnect: () -> Unit) = apply {
        onLocationDisconnectObserver = object :
            OnLocationDisconnectObserver {
            override fun onDisconnect() {
                onDisconnect()
            }
        }
    }

    interface OnLocationConnectObserver {
        fun onConnect()
    }

    interface OnLocationDisconnectObserver {
        fun onDisconnect()
    }

    companion object {
        private var locationObserver: LocationObserver? = null

        @Synchronized
        fun getInstance(): LocationObserver {
            if (locationObserver == null) {
                locationObserver =
                    LocationObserver()
            }
            return locationObserver!!
        }
    }
}