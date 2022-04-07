package com.salamtak.app.ui.component.carservices.providers.carservices

import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import com.salamtak.app.R
import com.salamtak.app.data.entities.Area
import com.salamtak.app.data.entities.Category
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.data.enums.MainCategories
import com.salamtak.app.ui.common.BaseBottomSheetDialog
import com.salamtak.app.ui.component.carservices.providers.CarFiltersListener
import com.salamtak.app.ui.component.carservices.providers.CarServicesViewModel
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.bottom_sheet_car_services.*

@AndroidEntryPoint
class CarServicesBottomSheet(
    val listener: CarFiltersListener
) : BaseBottomSheetDialog(),
    CarServiceFilterListener {
    override var getLayoutId: Int
        get() = R.layout.bottom_sheet_car_services
        set(value) {}


    private val carServiceViewModel: CarServicesViewModel by activityViewModels()

    lateinit var adapter: CarServiceAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeViewModel()
        viewsListeners()
        adapter = CarServiceAdapter(this)
        rv_car_services.adapter = adapter
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
                        rv_car_services.adapter = null
                        rv_car_services.adapter = adapter!!
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
        carServiceViewModel.selectedCarServices.value = carServiceViewModel.selectedCarServicesTmp.value
        listener.onCarServicesSelected(carServiceViewModel.selectedCarServices.value)
        dismiss()
    }

    private fun clearSelection() {
        et_search.setQuery("", false)//setText("")
        adapter.clearSelection()
        carServiceViewModel.selectedCarServicesTmp.value = null
        carServiceViewModel.selectedCarServices.value = null
        listener.onCarServicesSelected(null)
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
            getServices(MainCategories.CARS.typeId)
            observe(viewLifecycleOwner, categoriesLiveData, ::handleCategoriesList)
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


    private fun handleCategoriesList(categoriesModel: SalamtakListResponse<Category>) {
        bindListData(categoriesModel)
    }

    private fun bindListData(categories: SalamtakListResponse<Category>) {
        adapter.setList(categories.data.toMutableList(),
            carServiceViewModel.selectedCarServices.value
        )
        rv_car_services.adapter = adapter
        addLinearHorizontalItemDecoration(rv_car_services)
    }

    override fun onCarServicesSelected(categories: List<Category>?) {
        carServiceViewModel.selectedCarServicesTmp.value = categories
    }




}