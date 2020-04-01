package covid19.coronavirus.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import covid19.coronavirus.R
import kotlinx.android.synthetic.main.dialog_update.*

class MessageDialog(context: Context) : Dialog(context) {

    var onClickAccept: OnClickAccept? = null
    var onClickCancel: OnClickCancel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDialog()
        setOnClick()
    }

    private fun initDialog() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_update)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        setCancelable(false)
    }

    private fun setOnClick() {
        cancelButton.setOnClickListener {
            onClickCancel?.onClickCancel()
            cancel()
        }
        acceptButton.setOnClickListener {
            onClickAccept?.onClickAccept(this)
        }
    }

    fun setTitle(title: String) {
        titleText.text = title
    }

    fun setMessage(message: String) {
        messageText.text = message
    }

    fun setPositiveButtonText(text: String) {
        acceptButton.text = text
    }

    fun setNegativeButtonText(text: String) {
        cancelButton.text = text
    }

    interface OnClickAccept {
        fun onClickAccept(dialog: MessageDialog)
    }

    interface OnClickCancel {
        fun onClickCancel()
    }


    class Builder(val context: Context) {

        private var title = ""
        private var message = ""
        private var positiveButtonText = context.getString(R.string.dialog_default_accept)
        private var negativeButtonText = context.getString(R.string.dialog_default_cancel)
        var onClickAccept: OnClickAccept? = null
        var onClickCancel: OnClickCancel? = null

        fun setTitle(title: String) = apply {
            this.title = title
        }

        fun setMessage(message: String) = apply {
            this.message = message
        }

        fun setPositiveButtonText(text: String)  = apply {
            positiveButtonText = text
        }

        fun setNegativeButtonText(text: String)  = apply {
            negativeButtonText = text
        }


        inline fun setOnClickDownload(crossinline onClickDownload: (dialog: MessageDialog) -> Unit) =
            apply {
                this.onClickAccept = object : OnClickAccept {
                    override fun onClickAccept(dialog: MessageDialog) {
                        onClickDownload(dialog)
                    }
                }
            }

        inline fun setOnClickCancel(crossinline onClickCancel: () -> Unit) = apply {
            this.onClickCancel = object : OnClickCancel {
                override fun onClickCancel() {
                    onClickCancel()
                }
            }
        }

        fun show() {
            val dialog = MessageDialog(context)
            dialog.show()
            dialog.setTitle(title)
            dialog.setMessage(message)
            dialog.setPositiveButtonText(positiveButtonText)
            dialog.setNegativeButtonText(negativeButtonText)
            dialog.onClickAccept = onClickAccept
            dialog.onClickCancel = onClickCancel
        }
    }
}