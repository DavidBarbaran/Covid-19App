package covid19.coronavirus.feature.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import covid19.coronavirus.R
import covid19.coronavirus.model.CountryResponse
import kotlinx.android.synthetic.main.item_search.view.*

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    var list: MutableList<CountryResponse>? = null
    var onClickSearch: OnClickSearch? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SearchViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
    )

    override fun getItemCount() = list?.size ?: 0

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        list?.get(position)?.run { holder.bind(this) }
    }

    inline fun setOnClickSearch(crossinline onClickSearch: (countryResponse: CountryResponse) -> Unit) {
        this.onClickSearch = object : OnClickSearch {
            override fun onClickSearch(countryResponse: CountryResponse) {
                onClickSearch(countryResponse)
            }
        }
    }

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(countryResponse: CountryResponse) {
            itemView.apply {
                nameText.text = countryResponse.country
                setOnClickListener {
                    onClickSearch?.onClickSearch(countryResponse)
                }
            }
        }
    }

    interface OnClickSearch {
        fun onClickSearch(countryResponse: CountryResponse)
    }
}