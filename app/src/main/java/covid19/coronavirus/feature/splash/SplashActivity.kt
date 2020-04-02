package covid19.coronavirus.feature.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import covid19.coronavirus.R
import covid19.coronavirus.feature.home.HomeActivity
import kotlinx.android.synthetic.main.activity_splash.*
import org.koin.android.ext.android.inject

class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setOnClick()
        setViewModel()
        viewModel.getData()
    }

    private fun setOnClick() {
        retryButton.setOnClickListener {
            viewModel.getData()
        }
    }

    private fun setViewModel() {
        viewModel.apply {
            showLoadingLiveData.observe(this@SplashActivity, observerShowLoading())
            getDataSuccessfulLiveData.observe(this@SplashActivity, observerGetDataSuccessful())
            getDataFailedLiveData.observe(this@SplashActivity, observerGetDataFailed())
        }
    }

    private fun observerShowLoading() = Observer<Boolean> {
        if (it) {
            progressBar.visibility = View.VISIBLE
            retryButton.visibility = View.GONE
            messageErrorText.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            retryButton.visibility = View.VISIBLE
            messageErrorText.visibility = View.VISIBLE
        }
    }

    private fun observerGetDataSuccessful() = Observer<Boolean> {
        if (it) {
            goToHome()
        }
    }

    private fun observerGetDataFailed() = Observer<String> {
        messageErrorText.text = it
    }

    private fun goToHome() {
        finish()
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
