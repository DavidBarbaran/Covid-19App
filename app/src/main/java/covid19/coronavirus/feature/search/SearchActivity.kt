package covid19.coronavirus.feature.search

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import covid19.coronavirus.R
import covid19.coronavirus.model.CountryResponse
import covid19.coronavirus.util.setTransparentStatusBar
import kotlinx.android.synthetic.main.activity_search.*
import org.koin.android.ext.android.inject

class SearchActivity : AppCompatActivity() {

    private val viewModel: SearchViewModel by inject()
    private val searchAdapter: SearchAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setTransparentStatusBar()
        setRecycler()
        setViewModel()
        setSearch()
        setOnClick()
    }

    private fun setRecycler() {
        searchAdapter.setOnClickSearch {
            val returnIntent = Intent()
            returnIntent.putExtra(SEARCH_RESULT, it)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
        searchRecycler.adapter = searchAdapter
    }

    private fun setViewModel() {
        viewModel.searchCountriesLiveData.observe(this, observerSearchCountries())
        viewModel.getContries()
    }

    private fun setSearch() {
        searchEdit.doOnTextChanged { text, _, _, _ ->
            val search = text.toString()
            if (search.isBlank()) {
                viewModel.getContries()
            } else {
                viewModel.search(search)
            }
        }
    }

    private fun setOnClick() {
        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    /** Observers **/

    private fun observerSearchCountries() = Observer<MutableList<CountryResponse>> {
        searchAdapter.list = it
        searchAdapter.notifyDataSetChanged()
    }

    companion object {
        const val SEARCH_RESULT = "search_result"
    }
}
