package covid19.coronavirus.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import covid19.coronavirus.model.TotalResponse

@Dao
interface TotalCasesDao {

    @Query("SELECT * FROM TotalResponse LIMIT 1")
    fun getTotalCases(): TotalResponse?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(totalResponse: TotalResponse)

    @Query("DELETE FROM TotalResponse")
    fun deleteAll()
}