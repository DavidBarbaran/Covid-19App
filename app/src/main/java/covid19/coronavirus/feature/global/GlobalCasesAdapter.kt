package covid19.coronavirus.feature.global

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import covid19.coronavirus.R
import covid19.coronavirus.model.CountryResponse
import covid19.coronavirus.util.formatNumber
import kotlinx.android.synthetic.main.item_global_case.view.*
import java.util.*

class GlobalCasesAdapter : RecyclerView.Adapter<GlobalCasesAdapter.GlobalCasesViewHolder>() {

    var list: MutableList<CountryResponse>? = null
    var listSelected: Array<Boolean?>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = GlobalCasesViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_global_case, parent, false)
    )

    override fun getItemCount() = list?.size ?: 0

    override fun onBindViewHolder(holder: GlobalCasesViewHolder, position: Int) {
        list?.get(position)?.run {
            listSelected?.get(position)?.let {
                holder.bind(this, it)
            }
        }
    }

    fun setData(list: MutableList<CountryResponse>) {
        listSelected = arrayOfNulls(list.size)
        Arrays.fill(listSelected, false)
        this.list = list
    }

    fun getPositionFromCountry(country: String): Int? {
        list?.run {
            for ((index, value) in this.withIndex()) {
                if (value.country == country) {
                    return index
                }
            }
            return -1
        } ?: return -1
    }

    inner class GlobalCasesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(country: CountryResponse, isSelected: Boolean) {
            itemView.apply {
                numberText.text = "#${adapterPosition + 1}"
                countryName.text = country.country
                confirmCountText.text = formatNumber(country.cases)
                deathCountText.text = formatNumber(country.deaths)
                recoveredCountText.text = formatNumber(country.recovered)
                contentItemView.setBackgroundColor(
                    if (isSelected) ContextCompat.getColor(
                        context,
                        R.color.redAccentOpacity10
                    ) else Color.WHITE
                )
            }
        }
    }
}