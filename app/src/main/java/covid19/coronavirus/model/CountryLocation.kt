package covid19.coronavirus.model

import com.google.gson.annotations.SerializedName

class CountryLocation(

    @SerializedName("country")
    val name : String,

    @SerializedName("latitude")
    val latitude : Double,

    @SerializedName("longitude")
    val longitude : Double
)