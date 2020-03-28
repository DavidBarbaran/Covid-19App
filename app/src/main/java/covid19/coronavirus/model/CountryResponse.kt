package covid19.coronavirus.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity
class CountryResponse(
    @SerializedName("country")
    @ColumnInfo(name = "country")
    val country: String,

    @SerializedName("countryInfo")
    @ColumnInfo(name = "countryInfo")
    val countryInfo: CountryInfo,

    @SerializedName("cases")
    @ColumnInfo(name = "cases")
    val cases: Int,

    @SerializedName("todayCases")
    @ColumnInfo(name = "todayCases")
    val todayCases: Int,

    @SerializedName("deaths")
    @ColumnInfo(name = "deaths")
    val deaths: Int,

    @SerializedName("todayDeaths")
    @ColumnInfo(name = "todayDeaths")
    val todayDeaths: Int,

    @SerializedName("recovered")
    @ColumnInfo(name = "recovered")
    val recovered: Int,

    @SerializedName("active")
    @ColumnInfo(name = "active")
    val active: Int,

    @SerializedName("critical")
    @ColumnInfo(name = "critical")
    val critical: Int,

    @SerializedName("casesPerOneMillion")
    @ColumnInfo(name = "casesPerOneMillion")
    val casesPerOneMillion: Int,

    @SerializedName("deathsPerOneMillion")
    @ColumnInfo(name = "deathsPerOneMillion")
    val deathsPerOneMillion: Int
)