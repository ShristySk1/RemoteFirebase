package com.avyaas.nameonline.core.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.ayata.esewaremotefirebase.R


class ProgressDialog(private var type: EnumDialogType) : DialogFragment() {

    companion object {
        private var updateFunction: (() -> Unit)? = null
        private var title: String = ""
        private var message: String = ""
        const val TAG = "SimpleDialog"
        fun newInstance(
            dialogType: EnumDialogType,
            title: String,
            message: String,
            updateFunction: (() -> Unit)? = null
        ): ProgressDialog {
            this.title = title
            this.message = message
            when (dialogType) {
                EnumDialogType.ALERT -> {
                    this.updateFunction = updateFunction
                }
                EnumDialogType.PROMOTION -> {

                }

            }
            return ProgressDialog(dialogType)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View? = null
        when (type) {
            EnumDialogType.ALERT -> view =
                inflater.inflate(R.layout.dialog_alert, container, false)
            EnumDialogType.PROMOTION -> view =
                inflater.inflate(R.layout.dialog_promotion, container, false)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.setCanceledOnTouchOutside(false)
        var tvtitle = view.findViewById<TextView>(R.id.tv_title)
        var tvmessage = view.findViewById<TextView>(R.id.tv_message)
        tvtitle.text = (title)
        tvmessage.text = (message)
        when (type) {
            EnumDialogType.ALERT -> {
                val dismiss = view.findViewById<Button>(R.id.btnCancel)
                dismiss.setOnClickListener {
                    dismiss()
                }
                val update = view.findViewById<Button>(R.id.btnUpdate)
                update.setOnClickListener {
                    updateFunction?.invoke()
                    dismiss()
                }
            }
            EnumDialogType.PROMOTION -> {
                val dismiss = view.findViewById<ImageView>(R.id.iv_close)
                dismiss.setOnClickListener {
                    dismiss()
                }
            }
        }
    }


}