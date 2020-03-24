package covid19.coronavirus.feature.global

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crashlytics.android.Crashlytics
import covid19.coronavirus.model.Country
import covid19.coronavirus.model.TotalCases
import covid19.coronavirus.repository.CovidRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GlobalCasesViewModel(private val covidRepository: CovidRepository) : ViewModel() {

    var showTotalCasesLiveData = MutableLiveData<TotalCases>()

    var getCountriesLiveData = MutableLiveData<MutableList<Country>>()

    var getLastUpdateLiveData = MutableLiveData<String>()

    fun getTotalCases() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val totalCases = covidRepository.getTotalCases()
                val countryCases = covidRepository.getCountriesByConfirmedCases()
                val lastUpdate = covidRepository.getLastUpdate()
                launch(Dispatchers.Main) {
                    showTotalCasesLiveData.postValue(totalCases)
                    getLastUpdateLiveData.postValue(lastUpdate)
                    getCountriesLiveData.postValue(countryCases)
                }
            } catch (ex: Exception) {
                Crashlytics.logException(ex)
            }
        }
    }
}