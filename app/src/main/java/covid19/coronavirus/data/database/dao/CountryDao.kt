package covid19.coronavirus.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import covid19.coronavirus.model.CountryResponse

@Dao
interface CountryDao {

    @Query("SELECT * FROM CountryResponse")
    fun getCountry(): MutableList<CountryResponse>

    @Query("SELECT * FROM CountryResponse ORDER BY cases DESC")
    fun getCountryByConfirmedCases(): MutableList<CountryResponse>

    @Query("SELECT * FROM CountryResponse WHERE country LIKE '%' || :q || '%'")
    fun getCountry(q : String): MutableList<CountryResponse>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(country: MutableList<CountryResponse>)

    @Query("DELETE FROM CountryResponse")
    fun deleteAll()
}