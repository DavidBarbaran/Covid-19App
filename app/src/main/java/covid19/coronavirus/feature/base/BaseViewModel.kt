package covid19.coronavirus.feature.base

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    val context: Context = application.applicationContext
}