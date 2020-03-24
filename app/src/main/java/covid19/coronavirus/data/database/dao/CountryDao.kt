package covid19.coronavirus.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import covid19.coronavirus.model.Country

@Dao
interface CountryDao {

    @Query("SELECT * FROM Country")
    fun getCountry(): MutableList<Country>

    @Query("SELECT * FROM Country ORDER BY confirmed DESC")
    fun getCountryByConfirmedCases(): MutableList<Country>

    @Query("SELECT * FROM Country WHERE location LIKE '%' || :q || '%'")
    fun getCountry(q : String): MutableList<Country>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(country: MutableList<Country>)

    @Query("DELETE FROM Country")
    fun deleteAll()
}