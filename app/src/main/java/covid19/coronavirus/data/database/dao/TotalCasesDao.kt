package covid19.coronavirus.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import covid19.coronavirus.model.TotalCases

@Dao
interface TotalCasesDao {

    @Query("SELECT * FROM TotalCases LIMIT 1")
    fun getTotalCases(): TotalCases

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(totalCases: TotalCases)
}