package covid19.coronavirus.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class TotalCases(

    @PrimaryKey
    var id: String,

    @SerializedName("confirmed")
    @ColumnInfo(name = "confirmed")
    val confirmed: Int,

    @SerializedName("deaths")
    @ColumnInfo(name = "deaths")
    val deaths: Int,

    @SerializedName("recovered")
    @ColumnInfo(name = "recovered")
    val recovered: Int
)