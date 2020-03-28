package covid19.coronavirus.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity
class CountryInfo(
    @SerializedName("_id")
    @ColumnInfo(name = "_id")
    val id: Int,

    @SerializedName("lat")
    @ColumnInfo(name = "lat")
    val lat: Int,

    @SerializedName("long")
    @ColumnInfo(name = "long")
    val long: Int,

    @SerializedName("flag")
    @ColumnInfo(name = "flag")
    val flag: String,

    @SerializedName("iso3")
    @ColumnInfo(name = "iso3")
    val iso3: String,

    @SerializedName("iso2")
    @ColumnInfo(name = "iso2")
    val iso2: String
)