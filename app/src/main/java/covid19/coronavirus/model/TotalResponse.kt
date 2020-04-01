package covid19.coronavirus.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
class TotalResponse(

    @PrimaryKey
    var id: String,

    @SerializedName("cases")
    @ColumnInfo(name = "cases")
    val cases: Int,

    @SerializedName("deaths")
    @ColumnInfo(name = "deaths")
    val deaths: Int,

    @SerializedName("recovered")
    @ColumnInfo(name = "recovered")
    val recovered: Int,

    @SerializedName("updated")
    @ColumnInfo(name = "updated")
    val updated: Long,

    @SerializedName("active")
    @ColumnInfo(name = "active")
    val active: Int
)