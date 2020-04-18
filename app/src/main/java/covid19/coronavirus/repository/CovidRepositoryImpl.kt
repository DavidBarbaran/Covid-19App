package covid19.coronavirus.repository

import android.content.Context
import covid19.coronavirus.R
import covid19.coronavirus.data.database.AppDatabase
import covid19.coronavirus.data.net.RestApi
import covid19.coronavirus.data.session.SessionManager
import covid19.coronavirus.exception.NetworkException
import covid19.coronavirus.model.CountryResponse
import covid19.coronavirus.model.TotalResponse
import covid19.coronavirus.util.NetworkUtil
import covid19.coronavirus.util.formatDateFromMillis
import retrofit2.HttpException
import java.lang.Exception

class CovidRepositoryImpl(
    private val context: Context,
    private val restApi: RestApi,
    private val database: AppDatabase,
    private val sessionManager: SessionManager
) : CovidRepository {

    override suspend fun saveCovidData() {
        if (NetworkUtil.isOnline(context)) {

            val currentResponse = restApi.getCurrent()
            if (currentResponse.isSuccessful) {
                currentResponse.body()?.let {
                    database.countryDao().deleteAll()
                    database.countryDao().insertAll(it)
                }
            } else {
                if (database.countryDao().getCountry().isEmpty()) {
                    throw HttpException(currentResponse)
                }
            }

            val totalResponse = restApi.getTotal()
            if (totalResponse.isSuccessful) {
                totalResponse.body()?.let {
                    it.id = totalResponse.javaClass.simpleName
                    sessionManager.lastDateUpdate = formatDateFromMillis(it.updated)
                    database.totalCasesDao().insert(it)
                }
            } else {
                if (database.totalCasesDao().getTotalCases() == null){
                    throw HttpException(totalResponse)
                }
            }
        } else {
            if (database.countryDao().getCountry().isEmpty()) {
                throw NetworkException(context.getString(R.string.network_error_message))
            }
        }
    }

    override suspend fun getCountries(): MutableList<CountryResponse> {
        return database.countryDao().getCountry()
    }

    override suspend fun getCountriesByConfirmedCases(): MutableList<CountryResponse> {
        return database.countryDao().getCountryByConfirmedCases()
    }

    override suspend fun getCountriesFromName(name: String): MutableList<CountryResponse> {
        return database.countryDao().getCountry(name)
    }

    override suspend fun getTotalCases(): TotalResponse {
        return database.totalCasesDao().getTotalCases()!!
    }

    override suspend fun getLastUpdate(): String {
        return sessionManager.lastDateUpdate
    }

    override suspend fun updateCountEnterData() {
        val countNow = sessionManager.enterAppCount
        sessionManager.enterAppCount = countNow.plus(1)
    }

    override suspend fun getCountEnter(): Int {
        return sessionManager.enterAppCount
    }

    override suspend fun resetCountData() {
        sessionManager.enterAppCount = 0
    }
}