package covid19.coronavirus.model

import com.google.gson.annotations.SerializedName

class CurrentResponse(
    @SerializedName("data")
    val data: MutableList<Country>,

    @SerializedName("dt")
    val dt: String,

    @SerializedName("ts")
    val ts: Double
)