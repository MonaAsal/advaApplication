package com.salamtak.app.ui.component.financialinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.salamtak.app.R
import com.salamtak.app.ui.common.BaseViewModel
import com.salamtak.app.utils.convertDateFormat
import kotlinx.android.synthetic.main.dialog_date_time_picker.*
import java.util.*

/**
 * Created by Radwa Elsahn on 10/28/2018.
 */
class DateTimePickerDialog : DialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.dialog_date_time_picker, container, false)
//        dialog?.window!!.setGravity(Gravity.BOTTOM)
//        dialog?.window!!.setWindowAnimations(R.style.SlideInOutDialogAnimation)
        //        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

//        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        date_time_set.setOnClickListener {
//            val strDate =
//                date_picker.dayOfMonth.toString() + "/" + (date_picker.month + 1).toString() + "/" + date_picker.year.toString()
            //+ " " + time_picker.currentHour.toString() + ":" + time_picker.currentMinute.toString()

            val strDate = String.format(Locale.ENGLISH, getString(R.string.date_time_value),
                date_picker.dayOfMonth, date_picker.month + 1, date_picker.year)
            baseViewModel.setHireDate(convertDateFormat(strDate))
            dismiss()
        }
    }

    companion object {
        lateinit var baseViewModel: BaseViewModel
        fun newInstance(baseViewModel: BaseViewModel): DateTimePickerDialog {
            this.baseViewModel = baseViewModel
            val frag = DateTimePickerDialog()
            return frag
        }
    }

}