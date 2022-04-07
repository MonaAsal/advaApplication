//package com.salamtak.app.ui.component.health.subcategories
//
//import android.os.Bundle
//import android.view.View
//import androidx.activity.viewModels
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.salamtak.app.R
//import com.salamtak.app.data.entities.MedicalProvider
//import com.salamtak.app.ui.common.listeners.PaginationScrollListener
//import com.salamtak.app.ui.common.listeners.RecyclerItemListener
//import com.salamtak.app.ui.component.health.doctor.DoctorActivity
//import com.salamtak.app.ui.component.health.medicalprovider.MedicalProviderActivity
//import com.salamtak.app.ui.component.health.BaseOperationsActivity
//import com.salamtak.app.ui.component.health.categoryproviders.MedicalProvidersAdapter
//import com.salamtak.app.ui.component.health.categoryproviders.ProvidersViewModel
//import com.salamtak.app.utils.Constants
//import com.salamtak.app.utils.observe
//import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.android.synthetic.main.activity_more_providers.*
//import kotlinx.android.synthetic.main.toolbar_back_filter.*
//import org.jetbrains.anko.intentFor
//
//@AndroidEntryPoint
//class MoreProvidersActivity : BaseOperationsActivity(), RecyclerItemListener<MedicalProvider> {
//    override val layoutId: Int
//        get() = R.layout.activity_more_providers
//
//    val providersViewModel: ProvidersViewModel by viewModels()
//
//    lateinit var adapter: MedicalProvidersAdapter
//    override fun initializeViewModel() {
////        providersViewModel = viewModelFactory.create(ProvidersViewModel::class.java)
//    }
//
//    override fun observeViewModel() {
//        with(providersViewModel)
//        {
//            observe(showLoading, ::showLoading)
//            observe(showServerError, ::showServerErrorMessage)
//            observe(providers, ::showProviders)
//            observe(openMedicalProviderProfile, ::openMedicalProviderProfile)
//            observe(openDoctorProfile, ::openDoctorProfile)
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        intent?.let {
//            providersViewModel.subcategoryId = it.getStringExtra(Constants.KEY_SUBCATEGORY_ID)!!
//            tv_toolbar_title.text = it.getStringExtra(Constants.KEY_NAME)!!
//        }
//
//        iv_toolbar_back.setOnClickListener { onBackPressed() }
//
//        providersViewModel.getMoreDoctors()
//        adapter = MedicalProvidersAdapter(this)
//
//    }
//
//    fun openMedicalProviderProfile(medicalProvider: MedicalProvider) {
//        startActivity(
//            intentFor<MedicalProviderActivity>(
//                Constants.KEY_ID to medicalProvider.id
//            )
//        )
//    }
//
//    fun openDoctorProfile(medicalProvider: MedicalProvider) {
//        startActivity(
//            intentFor<DoctorActivity>(
//                Constants.KEY_ID to medicalProvider.id
//            )
//        )
//    }
//
//    fun showLoading(show: Boolean) {
//        progress_bar.visibility = if (show) View.VISIBLE else View.GONE
//    }
//
//    private fun showProviders(providers: List<MedicalProvider>) {
//        tv_remaining_records.text = getRemaining(
//            providersViewModel.moreProvidersResponse.value!!.totalRecords,
//            providersViewModel.page
//        )
//        if (providersViewModel.page > 1) {
//            adapter.addData(providers!!)
//        } else if (!(providers.isNullOrEmpty())) {
//            tv_remaining_records.text = getRemaining(
//                providersViewModel.moreProvidersResponse.value!!.totalRecords,
//                providersViewModel.page
//            )
//
//            adapter.setList(providers.toMutableList())
//            rv_providers_list.adapter = adapter
//
//            rv_providers_list.addOnScrollListener(object :
//                PaginationScrollListener(rv_providers_list.layoutManager as LinearLayoutManager) {
//                override fun isLastPage(): Boolean {
//                    return providersViewModel.isLastPage
//                }
//
//                override fun isLoading(): Boolean {
//                    return providersViewModel.isLoading
//                }
//
//                override fun loadMoreItems() {
//                    providersViewModel.page++
//                    providersViewModel.isLoading = true
//                    providersViewModel.getMoreDoctors()
//                }
//            })
//            showDataView(true)
//        } else {
//            showDataView(false)
//        }
//
//        providersViewModel.isLoading = false
//
//    }
//
//    private fun showDataView(show: Boolean) {
//        group_no_result.visibility = if (show) View.GONE else View.VISIBLE
//        rv_providers_list.visibility = if (show) View.VISIBLE else View.GONE
////        progress_bar.toGone()
//    }
//
//    override fun onItemSelected(item: MedicalProvider) {
//        providersViewModel.itemSelected(item)
//
//
////        startActivity(
////            requireActivity().intentFor<OperationsActivityN>(
////                Constants.KEY_ID to item.id,
////                Constants.KEY_NAME to item.name,
////                Constants.KEY_CATEGORY_ID to categoryId,
////                Constants.KEY_FROM to if (item.type == ProviderType.MedicalProvider.typeId) OperationsFrom.MedicalProvider.value else OperationsFrom.Doctor.value
////            )
////        )
//    }
//
//}