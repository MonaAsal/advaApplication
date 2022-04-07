package com.salamtak.app.ui.component.carservices.providers

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.salamtak.app.R
import com.salamtak.app.data.entities.*
import com.salamtak.app.ui.common.BaseFragment
import com.salamtak.app.ui.common.listeners.PaginationScrollListener
import com.salamtak.app.ui.component.carservices.adapter.CarCategoriesAdapter
import com.salamtak.app.ui.component.carservices.custom.CustomCarActivity
import com.salamtak.app.ui.component.carservices.interfaces.CarCategoryProviderListener
import com.salamtak.app.ui.component.carservices.interfaces.CarViewAllListener
import com.salamtak.app.ui.component.carservices.providers.carbrands.CarBrandsBottomSheet
import com.salamtak.app.ui.component.carservices.providers.carlocation.CarLocationBottomSheet
import com.salamtak.app.ui.component.carservices.providers.carservices.CarServicesBottomSheet
import com.salamtak.app.utils.*
import kotlinx.android.synthetic.main.activity_car_services_providers.*
import kotlinx.android.synthetic.main.activity_car_services_providers.group_no_result
import kotlinx.android.synthetic.main.activity_car_services_providers.progress_bar
import kotlinx.android.synthetic.main.fragment_finishing_categories.*
import kotlinx.android.synthetic.main.layout_car_filter_keys.*
import kotlinx.android.synthetic.main.toolbar.iv_toolbar_back
import kotlinx.android.synthetic.main.toolbar.tv_toolbar_title
import org.jetbrains.anko.startActivity


class CarServicesProvidersFragment : BaseFragment() , CarServiceProviderListener,
    CarFiltersListener, CarViewAllListener<CarCategoryModel>,
    CarCategoryProviderListener<CarProvidersModel> {
    override val layoutId: Int
        get() = R.layout.fragment_car_services_providers

    val viewModel: CarServicesViewModel by viewModels()

    lateinit var adapter: CarCategoriesAdapter
    lateinit var mLayoutManager: LinearLayoutManager
    var state: Parcelable? = null //to save recycler focus
    var firebaseScreenName = "CarServicesWithFilter_Android"

    private val servicesBottomSheet = CarServicesBottomSheet(this)
    private val brandsBottomSheet = CarBrandsBottomSheet(this)
    private val carBottomSheet = CarLocationBottomSheet(this)

    override fun observeViewModel() {


    }

    /*private fun showProvidersData(salamtakListResponse: List<CarServiceProvidersData>) {
        activity?.let { hideKeyboard(it) }
        val providers = salamtakListResponse.data?.carServiceProviders?.toMutableList()
        providers?.let {
            tv_remaining_records.text = getRemaining(
                salamtakListResponse!!.totalRecords,
                viewModel.page
            )

            if (viewModel.page > 1) {
                adapter.addData(providers)
            } else {
                if (providers.isNotEmpty()) {
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
                            viewModel.getCarServicesProviders()
                        }
                    })
                    showDataView(true)
//                    if (salamtakListResponse.data?.carServiceProviders.size < Constants.PAGE_SIZE)
//                        tv_remaining_records.toGone()
                } else
                    showDataView(false)
            }
        } ?: run {
            showDataView(false)
        }
        viewModel.isLoading = false
    }*/
    private fun showCarCategories(CarCategoriesList: List<CarCategoryModel>?) {
        if (viewModel.page > 1) {
            tv_remaining_records.text = getRemaining(
                viewModel.providersResponse.value!!.totalRecords,
                viewModel.page
            )
            adapter.addData(CarCategoriesList!!)

        } else if (!(CarCategoriesList.isNullOrEmpty())) {

            adapter = CarCategoriesAdapter(this, this, CarCategoriesList?.toMutableList()!!)
            rv_providers_list.layoutManager = GridLayoutManager(context, 2)
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            showprovidersNumbers(viewModel.providersResponse.value!!.totalRecords)

            CarCategoriesList?.toMutableList()?.let {
                adapter.setList(it)
            }
            rv_providers_list.adapter = adapter
            rv_providers_list.layoutManager = mLayoutManager
            rv_providers_list.addOnScrollListener(object :
                PaginationScrollListener(mLayoutManager) {
                override fun isLastPage(): Boolean {
                    return viewModel.isLastPage
                }

                override fun isLoading(): Boolean {
                    return viewModel.isLoading
                }

                override fun loadMoreItems() {
                    if(viewModel.providersResponse.value!!.totalPages>viewModel.page){
                        viewModel.page++
                        viewModel.isLoading = true
                        viewModel.getCarServicesProviders()

                    }
                    //viewModel.getFinishingCategories()

                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        state = mLayoutManager.onSaveInstanceState()
                    }
                }
            })
            showDataView(true)
        } else {
            showDataView(false)
        }

        viewModel.isLoading = false

    }

    private fun showprovidersNumbers(totalRecords: Int) {
        providerServicesNum.text = totalRecords.toString()

    }

   /* private fun showProviders(list: List<CarCategoryModel>) {
        if (!(list.isNullOrEmpty())) {
            adapter.setList(list.toMutableList())
            rv_providers_list.layoutManager = GridLayoutManager(activity, 2)
            //StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            rv_providers_list.adapter = adapter
//            addVerticalItemDecorationGrid(rv_providers_list)
            showDataView(true)
        } else {
            showDataView(false)
        }
    }
*/
   private fun openCarBrands() {
        LogUtil.LogFirebaseEvent(
            "CarBrandsFilterClicked_Android",
            firebaseScreenName
        )
//        val modalBottomSheet = CarBrandsBottomSheet(this, viewModel.selectedCarBrands.value)
        activity?.let { brandsBottomSheet.show(it?.supportFragmentManager, "CarBrandsBottomSheet") }
    }

    private fun openCarServices() {
        LogUtil.LogFirebaseEvent(
            "CarServicesFilterClicked_Android",
            firebaseScreenName
        )
        activity?.supportFragmentManager?.let { servicesBottomSheet.show(it, "CarServicesBottomSheet") }
    }

    private fun openCarLocations() {
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

        iv_toolbar_back.setOnClickListener { onBackPressed() }
//        iv_toolbar_icon.setImageResource(R.drawable.ic_car_service)
        mLayoutManager = LinearLayoutManager(context)
        tv_toolbar_title.text = getString(R.string.car_service)
        initViews()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private fun initViews() {
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
            viewModel.getCarServicesProviders()

            viewModel.categoriesLiveData.value = null
            viewModel.carBrandsLiveData.value = null
            viewModel.citiesLiveData.value = null
            iv_car_locations.setBackgroundResource(R.drawable.ic_circle_shadowed)
            iv_car_services.setBackgroundResource(R.drawable.ic_circle_shadowed)
            iv_car_brands.setBackgroundResource(R.drawable.ic_circle_shadowed)

//            hideFilter()
        }

        btn_add_custom.setOnClickListener { openCustomCarServiceRequestScreen() }

        iv_add.setOnClickListener {
            openCustomCarServiceRequestScreen()
        }

        tv_add_provider.setOnClickListener {
            openCustomCarServiceRequestScreen()
        }

        initSearchView()
        et_search.findViewById<ImageView>(R.id.search_close_btn).setOnClickListener {
            Log.e("query", "clear")
            et_search.setQuery("", false)
            et_search.clearFocus()
            viewModel.query = null
            viewModel.page = 1
            viewModel.getCarServicesProviders()
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
                        viewModel.getCarServicesProviders()

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
       // iv_add.visibility = if (show) View.VISIBLE else View.GONE
       // tv_add_provider.visibility = if (show) View.VISIBLE else View.GONE
    }


    override fun onProviderClicked(provider: CarServiceCenter) {
        LogUtil.LogFirebaseEvent(
            "CarServiceProviderSelected_Android",
            firebaseScreenName,
            "CarProviderName", provider.name
        )
        val bundle = bundleOf(
            Constants.KEY_ID to provider.id,
            Constants.KEY_TYPE to viewModel.categoryId)

        navigateToCarProviderDetailsRequest(bundle)


//        startActivity(
//            activity?.intentFor<CarProviderDetailsRequestActivity>(
//                Constants.KEY_ID to provider.id,
//                Constants.KEY_TYPE to viewModel.categoryId
//            )
//        )

    }

    override fun onMoreTagsClicked(provider: CarServiceCenter) {

        LogUtil.LogFirebaseEvent(
            "CarBrandsFilterClicked_Android",
            firebaseScreenName
        )
        val modalBottomSheet = TagsBottomSheet(provider)
        activity?.supportFragmentManager?.let { modalBottomSheet.show(it, "CarTagsBottomSheet") }
    }

    override fun onCarBrandsSelected(carModel: List<CarModel>?) {
        carModel?.let {
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
        viewModel.getCarServicesProviders()
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
        viewModel.getCarServicesProviders()
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
        viewModel.getCarServicesProviders()

    }

    override fun onClick(item: CarCategoryModel) {
        val bundle = bundleOf(Constants.CATEGORY_ID_KEY to item.id)
        navigateToCarServicesViewllProviders(bundle)

    }

    override fun onProviderClick(item: CarProvidersModel) {
        LogUtil.LogFirebaseEvent(
            "CarServiceProviderSelected_Android",
            firebaseScreenName,
            "CarProviderName", item.name!!
        )
        val bundle = bundleOf(
            Constants.KEY_ID to item.id,
            Constants.KEY_TYPE to viewModel.categoryId)

        navigateToCarProviderDetailsRequest(bundle)

    }
    private fun ShowCarServicesProviderList(){
        viewModel.getCarServicesProviders()
        with(viewModel)
        {
            observe(CarCategoriesList, ::showCarCategories)
            observe(showLoading, ::showLoading)
            observe(showServerError, ::showServerErrorMessage)
        }
    }

    override fun onResume() {

        viewModel.page = 1
        ShowCarServicesProviderList()
        super.onResume()
    }
}