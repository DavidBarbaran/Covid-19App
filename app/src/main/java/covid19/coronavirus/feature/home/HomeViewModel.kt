package covid19.coronavirus.feature.home

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crashlytics.android.Crashlytics
import com.google.android.gms.maps.model.Marker
import covid19.coronavirus.feature.base.BaseViewModel
import covid19.coronavirus.model.Country
import covid19.coronavirus.model.TotalCases
import covid19.coronavirus.repository.CovidRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    application: Application,
    private val covidRepository: CovidRepository
) : BaseViewModel(application) {

    var showTotalCasesLiveData = MutableLiveData<TotalCases>()

    var getCountriesLiveData = MutableLiveData<MutableList<Country>>()

    var data = HashMap<Marker, Country>()

    fun getTotalCases() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val totalCases = covidRepository.getTotalCases()
                launch(Dispatchers.Main) {
                    showTotalCasesLiveData.postValue(totalCases)
                }
            } catch (ex: Exception) {
                Crashlytics.logException(ex)
            }
        }
    }

    fun getCountryCases() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val countryCases = covidRepository.getCountries()
                launch(Dispatchers.Main) {
                    getCountriesLiveData.postValue(countryCases)
                }
            } catch (ex: Exception) {
                Crashlytics.logException(ex)
            }
        }
    }
}