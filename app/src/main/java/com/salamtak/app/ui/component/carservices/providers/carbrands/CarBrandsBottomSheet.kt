package com.salamtak.app.ui.component.carservices.providers.carbrands

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import com.salamtak.app.R
import com.salamtak.app.data.entities.CarModel
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.ui.common.BaseBottomSheetDialog
import com.salamtak.app.ui.component.carservices.providers.CarFiltersListener
import com.salamtak.app.ui.component.carservices.providers.CarServicesViewModel
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.bottom_sheet_car_models.*

@AndroidEntryPoint
class CarBrandsBottomSheet(
    val listener: CarFiltersListener
) : BaseBottomSheetDialog(),
    CarBrandsFilterListener {
    override var getLayoutId: Int
        get() = R.layout.bottom_sheet_car_models
        set(value) {}

    val carServiceViewModel: CarServicesViewModel by activityViewModels()

    lateinit var adapter: CarBrandsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val offsetFromTop = 100
//        (dialog as? BottomSheetDialog)?.behavior?.apply {
//            isFitToContents = false
//            setExpandedOffset(offsetFromTop)
//            state = BottomSheetBehavior.STATE_DRAGGING
//        }
//
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeViewModel()
        viewsListeners()

        adapter = CarBrandsAdapter(this)
        initSearch()

    }

    private fun viewsListeners() {
        iv_close.setOnClickListener { dismiss() }
        btn_clear.setOnClickListener {
            clearSelection()
        }

        btn_apply.setOnClickListener {
            applySelection()
        }

        et_search.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    try {
                        rv_car_brands.adapter = null
                        rv_car_brands.adapter = adapter!!
                        adapter.getFilter().filter(query.toString())
                    } catch (e: Exception) {
                    }
                    adapter.getFilter().filter(query)
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    adapter.getFilter().filter(newText)
                    return false
                }
            })
    }

    private fun applySelection() {
        carServiceViewModel.selectedCarBrands.value =
            carServiceViewModel.selectedCarBrandsTmp.value
        listener.onCarBrandsSelected(carServiceViewModel.selectedCarBrands.value)
        dismiss()
    }

    private fun clearSelection() {
        et_search.setQuery("", false)//setText("")
        adapter.clearSelection()
        carServiceViewModel.selectedCarBrandsTmp.value = null
        carServiceViewModel.selectedCarBrands.value = null
        listener.onCarBrandsSelected(null)
    }

    private fun initSearch() {
        et_search.onActionViewExpanded()
        et_search.clearFocus()

        et_search.setOnClickListener { view ->
            et_search.onActionViewExpanded()
        }

        et_search.applyFontFamily()
    }

    private fun observeViewModel() {
        with(carServiceViewModel)
        {
            getCarBrands()
            observe(viewLifecycleOwner, carBrandsLiveData, ::showCarModels)
            observe(viewLifecycleOwner, showLoading, ::showLoadingView)
        }

    }

    fun showLoadingView(show: Boolean) {
        progress_bar?.let {
            if (show) {
                progress_bar.toVisible()
            } else {
                progress_bar.toGone()
            }
        }
    }

    private fun showCarModels(carModels: SalamtakListResponse<CarModel>) {
        adapter.setList(
            carModels.data!!.toMutableList(),
            carServiceViewModel.selectedCarBrands.value
        )
        rv_car_brands.adapter = adapter
        addLinearHorizontalItemDecoration(rv_car_brands)
    }

    override fun onCarBrandsSelected(categories: List<CarModel>?) {
        categories?.let {
            it.map {
                Log.e("brands", it.id.toString())
            }
        }

        carServiceViewModel.selectedCarBrandsTmp.value = categories
    }

}