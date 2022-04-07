package com.salamtak.app.ui.common

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import com.salamtak.app.R

import com.salamtak.app.ui.common.listeners.DialogCallBacks
import com.salamtak.app.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_salamtak.*

@AndroidEntryPoint
class SalamtakDialog(var dialogCallBacks: DialogCallBacks?, val onlyYes: Boolean) :
    BaseDialogFragment() {

    override fun onStart() {
        super.onStart()
        val lp = dialog?.window?.attributes
        lp?.width = WindowManager.LayoutParams.MATCH_PARENT
        val displayMetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        lp?.height = height
        val window = dialog?.window
        window?.attributes = lp
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_salamtak, container, false)
        dialog?.window?.setGravity(Gravity.CENTER)
//        dialog?.window?.setWindowAnimations(R.style.SlideInOutDialogAnimation)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViews()

    }

    private fun initViews() {

        arguments?.let { arguments ->
            var title = arguments?.getString(Constants.KEY_TITLE)
            if (title?.isEmpty()!!)
                tv_title.visibility = View.GONE
            else tv_title.text = title

            btn_ok.text = arguments?.getString(Constants.KEY_YES_TEXT, getString(R.string.ok))

            btn_cancel.text =
                arguments?.getString(Constants.KEY_NO_TEXT, getString(R.string.cancel))
            tv_text.text = arguments?.getString(Constants.KEY_TEXT)
            iv_icon.setImageResource(arguments?.getInt(Constants.KEY_ICON) ?: R.drawable.ic_warning)
        }

        if (onlyYes) {
            btn_cancel.visibility = View.GONE
        } else if (dialogCallBacks != null) {
            btn_cancel.visibility = View.VISIBLE
        }

        btn_cancel.setOnClickListener {
            dialogCallBacks?.onNoClick()
            dismiss()
        }

        btn_ok.setOnClickListener {
            //if (dialogCallBacks != null)
            dialogCallBacks?.onOkClick()
            dismiss()
        }
    }


}