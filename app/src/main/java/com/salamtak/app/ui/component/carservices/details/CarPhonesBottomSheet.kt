package com.salamtak.app.ui.component.carservices.details

import android.os.Bundle
import android.util.Log
import com.salamtak.app.R
import com.salamtak.app.ui.common.BaseBottomSheetDialog
import com.salamtak.app.ui.common.listeners.RecyclerItemListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.bottom_sheet_car_branches.*
import kotlinx.android.synthetic.main.bottom_sheet_car_branches.rv_car_branches
import kotlinx.android.synthetic.main.bottom_sheet_car_location.*
import kotlinx.android.synthetic.main.bottom_sheet_car_location.iv_close
import kotlinx.android.synthetic.main.bottom_sheet_car_phones.*

@AndroidEntryPoint
class CarPhonesBottomSheet(phonesList: ArrayList<String>) : BaseBottomSheetDialog() ,
    RecyclerItemListener<String> {

    override var getLayoutId: Int
        get() = R.layout.bottom_sheet_car_phones
        set(value) {}

    var phonesList = phonesList
    lateinit  var phoneAdapter: CarPhoneAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showPhoneList()
        closeBottomSheet()

    }
    private fun closeBottomSheet() {
        iv_close.setOnClickListener { dismiss() }
    }


    private fun showPhoneList() {
        Log.d("branchesList", phonesList.toString())
        phoneAdapter = CarPhoneAdapter(this)
        if (!(phonesList.isNullOrEmpty())) {
            phoneAdapter.setList(phonesList.toMutableList())
        }
        rv_car_phone.adapter = phoneAdapter

    }

    override fun onItemSelected(item: String) {
        callPhoneNumber(item)
        closeBottomSheet()
    }

    private fun callPhoneNumber(item: String) {
        callNumber(item)
    }
}