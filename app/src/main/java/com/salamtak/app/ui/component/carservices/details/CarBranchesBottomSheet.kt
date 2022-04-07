package com.salamtak.app.ui.component.carservices.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.URLUtil
import com.salamtak.app.R
import com.salamtak.app.data.entities.CarProviderDetails.Location
import com.salamtak.app.ui.common.BaseBottomSheetDialog
import com.salamtak.app.ui.common.listeners.RecyclerItemListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.bottom_sheet_car_branches.*
import kotlinx.android.synthetic.main.bottom_sheet_car_location.iv_close


@AndroidEntryPoint
class CarBranchesBottomSheet(locationsList: ArrayList<Location>) : BaseBottomSheetDialog(),
    RecyclerItemListener<Location> {

    override var getLayoutId: Int
        get() = R.layout.bottom_sheet_car_branches
        set(value) {}

    var branchesList = locationsList
    lateinit var branchAdapter: CarBranchesAdapter


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showLocationList()
        closeBottomSheet()

    }

    private fun closeBottomSheet() {
        iv_close.setOnClickListener { dismiss() }
    }

    private fun showLocationList() {
        Log.d("branchesList", branchesList.toString())
        branchAdapter = CarBranchesAdapter(this)
        if (!(branchesList.isNullOrEmpty())) {
            branchAdapter.setList(branchesList.toMutableList())
        }
        rv_car_branches.adapter = branchAdapter
    }

    override fun onItemSelected(item: Location) {
        openMap(item)
    }

    private fun openMap(item: Location) {
        item.let {
            //  val url = "http://maps.google.com/maps?daddr=" +item.latitude+ "," + item.longitude
            val apiUrl = item.url
            if (apiUrl.isNullOrEmpty().not() && URLUtil.isValidUrl(apiUrl)) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(apiUrl))
                startActivity(intent)
                dismiss()
            } else {
                dismiss()
            }
        }
    }

}