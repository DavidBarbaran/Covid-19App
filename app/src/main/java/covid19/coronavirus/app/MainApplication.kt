package covid19.coronavirus.app

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.google.firebase.messaging.FirebaseMessaging
import covid19.coronavirus.BuildConfig
import covid19.coronavirus.di.main.listModule
import covid19.coronavirus.util.getCountryCode
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.lang.Exception

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
        initCloudMessaging()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@MainApplication)
            modules(listModule)
        }
    }

    private fun initCloudMessaging() {
        FirebaseMessaging.getInstance().subscribeToTopic(BuildConfig.BUILD_TYPE)
        subscribeTopicCountryIso()
    }

    private fun subscribeTopicCountryIso() {
        try {
            FirebaseMessaging.getInstance()
                .subscribeToTopic(getCountryCode(this.applicationContext))
        } catch (ex: Exception) {
            Crashlytics.logException(ex)
        }
    }
}