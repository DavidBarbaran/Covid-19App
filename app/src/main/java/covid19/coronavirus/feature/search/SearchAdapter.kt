package covid19.coronavirus.feature.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import covid19.coronavirus.R
import covid19.coronavirus.model.Country
import kotlinx.android.synthetic.main.item_search.view.*

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    var list: MutableList<Country>? = null
    var onClickSearch: OnClickSearch? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SearchViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
    )

    override fun getItemCount() = list?.size ?: 0

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        list?.get(position)?.run { holder.bind(this) }
    }

    inline fun setOnClickSearch(crossinline onClickSearch: (country: Country) -> Unit) {
        this.onClickSearch = object : OnClickSearch {
            override fun onClickSearch(country: Country) {
                onClickSearch(country)
            }
        }
    }

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(country: Country) {
            itemView.apply {
                nameText.text = country.location
                setOnClickListener {
                    onClickSearch?.onClickSearch(country)
                }
            }
        }
    }

    interface OnClickSearch {
        fun onClickSearch(country: Country)
    }
}