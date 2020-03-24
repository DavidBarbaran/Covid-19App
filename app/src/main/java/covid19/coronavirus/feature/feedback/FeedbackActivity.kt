package covid19.coronavirus.feature.feedback

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import covid19.coronavirus.R
import covid19.coronavirus.util.hideKeyboard
import kotlinx.android.synthetic.main.activity_feedback.*
import org.koin.android.ext.android.inject

class FeedbackActivity : AppCompatActivity() {

    private val viewModel: FeedbackViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        setViewModel()
        setOnClick()
    }

    private fun setOnClick() {
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        sendButton.setOnClickListener {
            sendFeedback()
        }
    }

    private fun setViewModel() {
        viewModel.showLoadingLiveData.observe(this, observerShowLoading())
        viewModel.sendFeedbackSuccessfulLiveData.observe(this, observerSendFeedbackSuccessful())
        viewModel.sendFeedbackFailedLiveData.observe(this, observerSendFeedbackFailed())
    }

    private fun sendFeedback() {
        clearError()
        hideKeyboard(this)
        val email = emailEdit.text.toString()
        val message = feedbackEdit.text.toString()

        if (!isValidEmail(email)) {
            emailInputLayout.error = getString(R.string.feedback_email_error)
            return
        }

        if (message.isBlank()) {
            feedbackInputLayout.error = getString(R.string.feedback_feedback_error)
            return
        }
        viewModel.sendFeedback(email, message)
    }

    private fun clearError() {
        emailInputLayout.error = ""
        feedbackInputLayout.error = ""
    }

    private fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    /** Observers **/

    private fun observerShowLoading() = Observer<Boolean> {
        loadView.visibility = if (it) View.VISIBLE else View.GONE
    }

    private fun observerSendFeedbackSuccessful() = Observer<String> {
        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun observerSendFeedbackFailed() = Observer<String> {
        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
    }
}
