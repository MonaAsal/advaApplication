package com.salamtak.app.ui.component.carservices.providers

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.salamtak.app.R
import com.salamtak.app.data.entities.*
import com.salamtak.app.data.entities.responses.SalamtakObjectListResponse
import com.salamtak.app.ui.common.BaseFragment
import com.salamtak.app.ui.common.listeners.PaginationScrollListener
import com.salamtak.app.ui.component.carservices.custom.CustomCarActivity
import com.salamtak.app.ui.component.carservices.providers.carbrands.CarBrandsBottomSheet
import com.salamtak.app.ui.component.carservices.providers.carlocation.CarLocationBottomSheet
import com.salamtak.app.ui.component.carservices.providers.carservices.CarServicesBottomSheet
import com.salamtak.app.ui.component.carservices.providers.carprovidersviewall.CarViewAllViewModel
import com.salamtak.app.utils.*
import kotlinx.android.synthetic.main.activity_car_services_providers.*
import kotlinx.android.synthetic.main.layout_car_filter_keys.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.startActivity

class CarServicesProvidersViewAllfragment  : BaseFragment() ,
    CarServiceProviderViewAllListener,
    CarFiltersListener {
    override val layoutId: Int
        get() = R.layout.fragment_car_services_provider_viewall

    val viewModel: CarViewAllViewModel by viewModels()

    lateinit var adapter: CarServiceProviderAdapterViewAll

    var firebaseScreenName = "CarServicesWithFilter_Android"

    val servicesBottomSheet = CarServicesBottomSheet(this)
    val brandsBottomSheet = CarBrandsBottomSheet(this)
    val carBottomSheet = CarLocationBottomSheet(this)
    var categoryId= 0

    override fun observeViewModel() {
        with(viewModel)
        {
            observe(providersViewllResponse,::showProvidersViewAll)
            observe(showLoading, ::showLoading)
            observe(showServerError, ::showServerErrorMessage)
        }

    }


    fun showProvidersViewAll(salamtakListResponse: SalamtakObjectListResponse<CarProvidersData>) {
        activity?.let { hideKeyboard(it) }
        val providers = salamtakListResponse.data?.carServiceProviders?.toMutableList()
        Log.d("yasmendata",providers?.get(0)?.name.toString())
        val totalRecords = salamtakListResponse?.totalRecords
        tv_remaining_records.text = getRemaining(
            salamtakListResponse!!.totalRecords,
            viewModel.page
        )
        if (viewModel.page > 1) {
            providers?.toMutableList()?.let { adapter.addData(it) }
        } else {
            if (!providers?.isNullOrEmpty()!!) {
                adapter.setList(providers)
                rv_providers_list.adapter = adapter
                rv_providers_list.addOnScrollListener(object :
                    PaginationScrollListener(rv_providers_list.layoutManager as LinearLayoutManager) {
                    override fun isLastPage(): Boolean {
                        return viewModel.isLastPage
                    }

                    override fun isLoading(): Boolean {
                        return viewModel.isLoading
                    }

                    override fun loadMoreItems() {
                        viewModel.page++
                        viewModel.isLoading = true
                        viewModel.getCarProvidersViewAll(categoryId = categoryId)
                    }
                })
                showDataView(true)
            } else
                showDataView(false)
        }
        viewModel.isLoading = false
    }


    fun openCarBrands() {
        LogUtil.LogFirebaseEvent(
            "CarBrandsFilterClicked_Android",
            firebaseScreenName
        )
//        val modalBottomSheet = CarBrandsBottomSheet(this, viewModel.selectedCarBrands.value)
        activity?.let { brandsBottomSheet.show(it?.supportFragmentManager, "CarBrandsBottomSheet") }
    }

    fun openCarServices() {
        LogUtil.LogFirebaseEvent(
            "CarServicesFilterClicked_Android",
            firebaseScreenName
        )
        activity?.supportFragmentManager?.let { servicesBottomSheet.show(it, "CarServicesBottomSheet") }
    }

    fun openCarLocations() {
        LogUtil.LogFirebaseEvent(
            "CarLocationFilterClicked_Android",
            firebaseScreenName
        )
        //  val modalBottomSheet = CarLocationBottomSheet(this)

        activity?.let { carBottomSheet.show(it?.supportFragmentManager, "CarLocationBottomSheet") }
    }

    fun showLoading(show: Boolean) {
        progress_bar.visibility = if (show) View.VISIBLE else View.GONE
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViews()
        arguments?.let {
            categoryId = it.getInt(Constants.CATEGORY_ID_KEY)
        }
        viewModel.page = 1
        viewModel.getCarProvidersViewAll(categoryId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = CarServiceProviderAdapterViewAll(this)

    }

    private fun initViews() {

        iv_toolbar_back.setOnClickListener { onBackPressed() }
        tv_toolbar_title.text = getString(R.string.car_service)

        btn_filter.setOnClickListener {
            if (layout_filter.visibility == View.GONE) {
                showFilter()
            } else {
                hideFilter()
            }
        }

        iv_car_services.setOnClickListener { openCarServices() }
        tv_car_services.setOnClickListener { iv_car_services.performClick() }

        iv_car_locations.setOnClickListener { openCarLocations() }
        tv_car_locations.setOnClickListener { iv_car_locations.performClick() }

        iv_car_brands.setOnClickListener { openCarBrands() }
        tv_car_brands.setOnClickListener { iv_car_brands.performClick() }

        tv_clear_all.setOnClickListener {

            et_search.setQuery("", false)
            viewModel.selectedArea.value = null
            viewModel.selectedCarBrands.value = null
            viewModel.selectedCarServices.value = null
            viewModel.selectedCarBrandsTmp.value = null
            viewModel.selectedCarServicesTmp.value = null
            viewModel.query = null
            viewModel.page = 1
            //  viewModel.getCarServicesProviders()
            viewModel.getCarProvidersViewAll(categoryId)

            viewModel.categoriesLiveData.value = null
            viewModel.carBrandsLiveData.value = null
            viewModel.citiesLiveData.value = null
            iv_car_locations.setBackgroundResource(R.drawable.ic_circle_shadowed)
            iv_car_services.setBackgroundResource(R.drawable.ic_circle_shadowed)
            iv_car_brands.setBackgroundResource(R.drawable.ic_circle_shadowed)

//            hideFilter()
        }

        btn_add_custom.setOnClickListener { openCustomCarServiceRequestScreen() }

//        iv_add.setOnClickListener {
//            openCustomCarServiceRequestScreen()
//        }
//
//        tv_add_provider.setOnClickListener {
//            openCustomCarServiceRequestScreen()
//        }

        initSearchView()
        et_search.findViewById<ImageView>(R.id.search_close_btn).setOnClickListener {
            Log.e("query", "clear")
            et_search.setQuery("", false)
            et_search.clearFocus()
            viewModel.query = null
            viewModel.page = 1
            //   viewModel.getCarServicesProviders()
            viewModel.getCarProvidersViewAll(categoryId)

            activity?.let { it1 -> hideKeyboard(it1) }

        }

        et_search.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    try {
                        Log.e("query", query.toString())
                        LogUtil.LogFirebaseEvent(
                            "btn_car_search",
                            firebaseScreenName,
                            "search_key",
                            et_search!!.query.toString()
                        )
                        viewModel.query = et_search.query.toString()
                        viewModel.page = 1
                        viewModel.getCarProvidersViewAll(categoryId)

                        //    viewModel.getCarServicesProviders()

                    } catch (e: Exception) {
                    }

                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    Log.e("query1", newText.toString())
                    return false
                }
            })
    }

    private fun initSearchView() {
        et_search.onActionViewExpanded()
        et_search.clearFocus()
        et_search.setOnClickListener { view ->
            et_search.onActionViewExpanded()
        }

        et_search.applyFontFamily()

    }

    private fun openCustomCarServiceRequestScreen() {
        activity?.startActivity<CustomCarActivity>()
        activity?.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
    }

    private fun hideFilter() {
        layout_filter.toGone()
    }

    private fun showFilter() {
        layout_filter.toVisible()
    }


    private fun showDataView(show: Boolean) {
        group_no_result.visibility = if (show) View.GONE else View.VISIBLE
      //  tv_remaining_records.visibility = if (show) View.VISIBLE else View.GONE
        rv_providers_list.visibility = if (show) View.VISIBLE else View.GONE
    }


    override fun onProviderClicked(provider: CarServiceProvidersModel) {
        provider.name?.let {
            LogUtil.LogFirebaseEvent(
                "CarServiceProviderSelected_Android",
                firebaseScreenName,
                "CarProviderName", it
            )
        }
        val bundle = bundleOf(
            Constants.KEY_ID to provider.id,
            Constants.KEY_TYPE to viewModel.categoryId)
        navigateToCarProviderDetailsRequest(bundle)

    }

    override fun onMoreTagsClicked(provider: CarServiceProvidersModel) {

        LogUtil.LogFirebaseEvent(
            "CarBrandsFilterClicked_Android",
            firebaseScreenName
        )
    }

    override fun onCarBrandsSelected(carmodels: List<CarModel>?) {
        carmodels?.let {
            it.map {
                Log.e("carBrandSelected", it?.id.toString())
                LogUtil.LogFirebaseEvent(
                    "CarServiceBrandSelected_Android",
                    firebaseScreenName,
                    "SelectedCarBrand", it.name
                )
            }
        }
        if (!(viewModel.selectedCarBrands.value.isNullOrEmpty())) {
            iv_car_brands.setBackgroundResource(R.drawable.ic_circle_orange_border)
        } else {
            iv_car_brands.setBackgroundResource(R.drawable.ic_circle_shadowed)
        }
        viewModel.page = 1
        viewModel.getCarProvidersViewAll(categoryId)
    }

    override fun onCarServicesSelected(categories: List<Category>?) {
        categories?.let {
            it.map {
                Log.e("onCarServicesSelected", it?.id.toString())
                LogUtil.LogFirebaseEvent(
                    "CarServiceServiceSelected_Android",
                    firebaseScreenName,
                    "SelectedCarService", it.name
                )
            }
        }
        if (!(viewModel.selectedCarServices.value.isNullOrEmpty())) {
            iv_car_services.setBackgroundResource(R.drawable.ic_circle_orange_border)
        } else {
            iv_car_services.setBackgroundResource(R.drawable.ic_circle_shadowed)
        }

        viewModel.page = 1
        viewModel.getCarProvidersViewAll(categoryId)

        // viewModel.getCarServicesProviders()
    }

    override fun onCarLocationSelected(area: List<Area>?) {
        area?.let {
            it.map { Log.e("onCarLocationSelected", it?.id.toString()) }
        }
        if (!(viewModel.selectedArea.value.isNullOrEmpty())) {
            iv_car_locations.setBackgroundResource(R.drawable.ic_circle_orange_border)
            Log.e("selectedArea", viewModel.selectedArea.value.toString())
        } else {
            iv_car_locations.setBackgroundResource(R.drawable.ic_circle_shadowed)
            Log.e("unselectedArea", viewModel.selectedArea.value.toString())
        }
        viewModel.page = 1
        viewModel.getCarProvidersViewAll(categoryId)
        //     viewModel.getCarServicesProviders()

    }



}