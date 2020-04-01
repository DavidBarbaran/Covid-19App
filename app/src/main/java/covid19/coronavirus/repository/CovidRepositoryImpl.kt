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

class CovidRepositoryImpl(
    private val context: Context,
    private val restApi: RestApi,
    private val database: AppDatabase,
    private val sessionManager: SessionManager
) : CovidRepository {

    override suspend fun saveCovidData() {
        if (NetworkUtil.isOnline(context)) {

            val currentResponse = restApi.getCurrent()

            database.countryDao().deleteAll()
            database.countryDao().insertAll(currentResponse)

            val totalResponse = restApi.getTotal()
            totalResponse.id = totalResponse.javaClass.simpleName
            sessionManager.lastDateUpdate = formatDateFromMillis(totalResponse.updated)
            database.totalCasesDao().insert(totalResponse)
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
        return database.totalCasesDao().getTotalCases()
    }

    override suspend fun getLastUpdate(): String {
        return sessionManager.lastDateUpdate
    }
}