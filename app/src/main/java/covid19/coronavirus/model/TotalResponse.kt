package covid19.coronavirus.model

import com.google.gson.annotations.SerializedName

class TotalResponse (
    @SerializedName("data")
    val data: TotalCases,

    @SerializedName("dt")
    val dt: String,

    @SerializedName("ts")
    val ts: Double
)