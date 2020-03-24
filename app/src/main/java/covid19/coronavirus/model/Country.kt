package covid19.coronavirus.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Country(

    @SerializedName("location")
    @PrimaryKey
    @ColumnInfo(name = "location")
    val location: String,

    @SerializedName("confirmed")
    @ColumnInfo(name = "confirmed")
    val confirmed: Int,

    @SerializedName("deaths")
    @ColumnInfo(name = "deaths")
    val deaths: Int,

    @SerializedName("recovered")
    @ColumnInfo(name = "recovered")
    val recovered: Int,

    @ColumnInfo(name = "latitude")
    var latitude: Double,

    @ColumnInfo(name = "longitude")
    var longitude: Double
) : Parcelable