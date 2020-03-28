package covid19.coronavirus.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import kotlinx.android.parcel.Parcelize
import java.lang.reflect.Type

@Parcelize
@Entity
class CountryInfo constructor(
    @PrimaryKey
    @SerializedName("_id")
    @ColumnInfo(name = "_id")
    var id: Int,

    @SerializedName("lat")
    @ColumnInfo(name = "lat")
    var lat: Double,

    @SerializedName("long")
    @ColumnInfo(name = "long")
    var long: Double,

    @SerializedName("flag")
    @ColumnInfo(name = "flag")
    var flag: String,

    @SerializedName("iso3")
    @ColumnInfo(name = "iso3")
    var iso3: String,

    @SerializedName("iso2")
    @ColumnInfo(name = "iso2")
    var iso2: String
) : Parcelable {
    constructor() : this(0, 0.0, 0.0, "", "", "")
}

class CountryInfoConverter {
    private val gson = Gson()

    @TypeConverter
    fun toCountryInfo(data: String): CountryInfo {
        val listType: Type =
            object : TypeToken<CountryInfo>() {}.type
        return gson.fromJson<CountryInfo>(data, listType)
    }

    @TypeConverter
    fun toString(countryInfo: CountryInfo): String {
        return gson.toJson(countryInfo)
    }
}