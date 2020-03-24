package covid19.coronavirus.feature.search

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crashlytics.android.Crashlytics
import covid19.coronavirus.R
import covid19.coronavirus.feature.base.BaseViewModel
import covid19.coronavirus.model.Country
import covid19.coronavirus.repository.CovidRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(application: Application, private val covidRepository: CovidRepository) :
    BaseViewModel(application) {

    var searchCountriesLiveData = MutableLiveData<MutableList<Country>>()

    fun search(q: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val countries = covidRepository.getCountriesFromName(q)
                launch(Dispatchers.Main) {
                    searchCountriesLiveData.postValue(countries)
                }
            } catch (ex: Exception) {
                Crashlytics.logException(ex)
            }
        }
    }

    fun getContries() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val countries = covidRepository.getCountries()
                countries.reverse()
                val locationNow =
                    Country(context.getString(R.string.your_location), 0, 0, 0, 0.0, 0.0)
                countries.add(locationNow)
                countries.reverse()
                launch(Dispatchers.Main) {
                    searchCountriesLiveData.postValue(countries)
                }
            } catch (ex: Exception) {
                Crashlytics.logException(ex)
            }
        }
    }
}