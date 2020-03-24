package covid19.coronavirus.repository

import covid19.coronavirus.model.Country
import covid19.coronavirus.model.CountryLocation
import covid19.coronavirus.model.TotalCases

interface CovidRepository {

    suspend fun saveCovidData(countriesLocation: MutableList<CountryLocation>)

    suspend fun getCountries(): MutableList<Country>

    suspend fun getCountriesByConfirmedCases(): MutableList<Country>

    suspend fun getCountriesFromName(name: String): MutableList<Country>

    suspend fun getTotalCases(): TotalCases

    suspend fun getLastUpdate(): String
}