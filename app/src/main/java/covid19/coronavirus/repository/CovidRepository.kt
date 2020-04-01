package covid19.coronavirus.repository

import covid19.coronavirus.model.CountryResponse
import covid19.coronavirus.model.TotalResponse

interface CovidRepository {

    suspend fun saveCovidData()

    suspend fun getCountries(): MutableList<CountryResponse>

    suspend fun getCountriesByConfirmedCases(): MutableList<CountryResponse>

    suspend fun getCountriesFromName(name: String): MutableList<CountryResponse>

    suspend fun getTotalCases(): TotalResponse

    suspend fun getLastUpdate(): String
}