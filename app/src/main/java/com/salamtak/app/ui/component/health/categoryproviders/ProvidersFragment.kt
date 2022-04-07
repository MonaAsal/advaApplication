package com.salamtak.app.ui.component.health.categoryproviders

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.MedicalProvider
import com.salamtak.app.ui.common.BaseFragment
import com.salamtak.app.ui.common.listeners.PaginationScrollListener
import com.salamtak.app.ui.common.listeners.RecyclerItemListener
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_providers_list.*
import kotlinx.android.synthetic.main.fragment_providers_list.group_no_result
import kotlinx.android.synthetic.main.fragment_providers_list.progress_bar
import kotlinx.android.synthetic.main.fragment_subcategories_list.*


@AndroidEntryPoint
class ProvidersFragment : BaseFragment() ,
    RecyclerItemListener<MedicalProvider> {
    override val layoutId: Int
        get() = R.layout.fragment_providers_list

    companion object {

        @JvmStatic
        fun newInstance(categoryId: Int) =
            ProvidersFragment().apply {
                arguments = Bundle().apply {
                    putInt(
                        Constants.CATEGORY_ID_KEY,
                        categoryId
                    )
                }
            }
    }

    var categoryId: Int = 0
    val providersViewModel: ProvidersViewModel by viewModels()
    lateinit var providerAdapter: MedicalProvidersAdapter
    lateinit  var  mLayoutManager :LinearLayoutManager
    var state: Parcelable? = null //to save recycler focus


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("providers state","onCreate")

        arguments?.let {
            categoryId = it.getInt(Constants.CATEGORY_ID_KEY)!!
        }
        providersViewModel.categoryId = categoryId.toString()
        providersViewModel.page = 1
        providersViewModel.getProvidersNames(categoryId.toString())
        providersViewModel.getCategoryProviders(categoryId)
        providerAdapter = MedicalProvidersAdapter(this)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mLayoutManager = LinearLayoutManager(context)
        restoreRecyclerPosition()
        search()
    }

    override fun onPause() {
        super.onPause()
        state = mLayoutManager.onSaveInstanceState()
    }

    fun restoreRecyclerPosition(){
        if(state != null) {
            mLayoutManager.onRestoreInstanceState(state);
        }
    }

    private fun search() {
        et_search.setOnEditorActionListener(TextView.OnEditorActionListener {
                v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                if (et_search.text.toString().isNotEmpty()) {
                    providersViewModel.searchProviders(categoryId, et_search.text.toString())
                    openSearchScreen(et_search.text.toString())

                }

                return@OnEditorActionListener true
            }
            false
        })
    }


    override fun onResume() {
        super.onResume()
        //Log.d("providers state",providersViewModel.categoryProvidersResponse.value?.data?.get(0)?.name.toString())
        et_search.setText("")
        dataObservation()
        restoreRecyclerPosition()

    }
    fun dataObservation(){
        //  providersViewModel.page = 1
        Log.d("pagenumber providers",providersViewModel.page.toString())

        with(providersViewModel)
        {
            observe(showLoading, ::showLoading)
            observe(showServerError, ::showServerErrorMessage)
            observe(providers, ::showProviders)
        }
    }

    override fun observeViewModel() {

        with(providersViewModel)
        {
            observe(showLoading, ::showLoading)
            observe(showServerError, ::showServerErrorMessage)
            observe(providers, ::showProviders)
            observe(openMedicalProviderProfile, ::openMedicalProviderProfile)
            observe(openDoctorProfile, ::openDoctorProfile)
            observe(filterProvidersList, ::bindProvidersList)

        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d("providers state","onViewStateRestored")
        //to save design when swip bwtween fragment
        with(providersViewModel){
            observe(providers, ::showProviders)
        }
    }

    private fun showprovidersNumbers(totalRecords: Int) {
        providersnum.text = totalRecords.toString()
    }

    private fun bindProvidersList(list: List<MedicalProvider>) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.select_dialog_item, list.map { it.name })

        et_search.threshold = 1
        et_search.setAdapter(adapter)
        et_search.setOnItemClickListener { parent, view, position, id ->
            if (et_search!!.text.toString().isNotEmpty()) {
                providersViewModel.getSelectedItem(et_search.text.toString())
            }
        }

    }

    fun openMedicalProviderProfile(medicalProvider: MedicalProvider) {
        val bundle = bundleOf(
            Constants.KEY_ID to medicalProvider.id,
        )
        navigateToMedicalProvider(bundle)

//        startActivity(
//            requireActivity().intentFor<MedicalProviderActivity>(
//                Constants.KEY_ID to medicalProvider.id
//            )
//        )
    }

    fun openDoctorProfile(medicalProvider: MedicalProvider) {
        val bundle = bundleOf(
            Constants.DOCTOR_ITEM_KEY to medicalProvider.id
        )
        navigateToDoctor(bundle)

//        startActivity(
//            requireActivity().intentFor<DoctorActivity>(
//                Constants.KEY_ID to medicalProvider.id
//            )
//        )
    }

    fun showLoading(show: Boolean) {
        progress_bar.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun onItemSelected(item: MedicalProvider) {
        providersViewModel.itemSelected(item)


//        startActivity(
//            requireActivity().intentFor<OperationsActivityN>(
//                Constants.KEY_ID to item.id,
//                Constants.KEY_NAME to item.name,
//                Constants.KEY_CATEGORY_ID to categoryId,
//                Constants.KEY_FROM to if (item.type == ProviderType.MedicalProvider.typeId) OperationsFrom.MedicalProvider.value else OperationsFrom.Doctor.value
//            )
//        )
    }


    private fun showProviders(providers: List<MedicalProvider>) {
        if (providersViewModel.page > 1 && providersViewModel.isSearch.not()) {
            tv_remaining_records.text = getRemaining(
                providersViewModel.categoryProvidersResponse.value!!.totalRecords,
                providersViewModel.page
            )
            providerAdapter.addData(providers!!)

        }
        else if (!(providers.isNullOrEmpty())) {
            tv_remaining_records.text = getRemaining(
                providersViewModel.categoryProvidersResponse.value!!.totalRecords,
                providersViewModel.page

            )
            showprovidersNumbers(providersViewModel.categoryProvidersResponse.value!!.totalRecords)

            providerAdapter.setList(providers.toMutableList())
            rv_providers_list.adapter = providerAdapter
            rv_providers_list.layoutManager = mLayoutManager
            rv_providers_list.addOnScrollListener(object :
                PaginationScrollListener(mLayoutManager) {
                override fun isLastPage(): Boolean {
                    return providersViewModel.isLastPage
                }

                override fun isLoading(): Boolean {
                    return providersViewModel.isLoading
                }

                override fun loadMoreItems() {
                    providersViewModel.page++
                    providersViewModel.isLoading = true
                    providersViewModel.loadMorePages(categoryId, et_search.text.toString())
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

        providersViewModel.isLoading = false

    }

    fun getRemaining(totalRecords: Int, page: Int): String {
        return if (page * Constants.PAGE_SIZE <= totalRecords)
            String.format(
                getString(R.string.num_more_records),
                (totalRecords - (page * Constants.PAGE_SIZE)).toString()
            )
        else
            getString(R.string.no_more_records)
    }

    private fun showDataView(show: Boolean) {
        group_no_result.visibility = if (show) View.GONE else View.VISIBLE
        rv_providers_list.visibility = if (show) View.VISIBLE else View.GONE
//        progress_bar.toGone()
    }

}


