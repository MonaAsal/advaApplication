package com.salamtak.app.ui.component.health.subcategory


import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.MedicalProvider
import com.salamtak.app.data.entities.Operation
import com.salamtak.app.data.entities.SubCategory
import com.salamtak.app.data.entities.SubCategoryModel
import com.salamtak.app.data.enums.OperationsFrom
import com.salamtak.app.data.enums.ProviderType
import com.salamtak.app.ui.common.BaseFragment
import com.salamtak.app.ui.common.listeners.PaginationScrollListener
import com.salamtak.app.ui.component.health.adapter.OperationListenerN
import com.salamtak.app.ui.component.health.bookoperation.BookHealthRequestActivity
import com.salamtak.app.ui.component.health.subcategory.adapters.NewSubCategoriesAdapter
import com.salamtak.app.ui.component.health.subcategory.listeners.OperationListener
import com.salamtak.app.ui.component.health.subcategory.listeners.SubcategoryListener
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.LogUtil
import com.salamtak.app.utils.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_subcategories_list.*
import kotlinx.android.synthetic.main.fragment_subcategories_list.group_no_result
import kotlinx.android.synthetic.main.fragment_subcategories_list.progress_bar
import org.jetbrains.anko.intentFor

@AndroidEntryPoint
class SubcategoriesOperationsFragment : BaseFragment() ,
    SubcategoryListener<SubCategoryModel>,
    OperationListener<Operation>,
    OperationListenerN
{
    companion object {

        @JvmStatic
        fun newInstance(categoryId: Int) =
            SubcategoriesOperationsFragment().apply {
                arguments = Bundle().apply {
                    putInt(
                        Constants.CATEGORY_ID_KEY,
                        categoryId
                    )
                }
            }
    }



    override val layoutId: Int
        get() = R.layout.fragment_subcategories_list

    val openMedicalProviderProfile = MutableLiveData<MedicalProvider>()
    val openDoctorProfile = MutableLiveData<MedicalProvider>()
    var categoryId: Int = 0
    val subcategoriesViewModel: SubCategoryViewModel by viewModels()
    lateinit var newSubCategoryAdapter: NewSubCategoriesAdapter
    lateinit  var  mLayoutManager :LinearLayoutManager
    var state: Parcelable? = null //to save recycler focus


    //lateinit var adapter: SubCategoriesAdapter
    //  val arrayListSub = ArrayList<SubCategoryModel>()//Creating an empty arraylist
    // val arrayListSubSUb = ArrayList<SubSubCategoryModel>()//Creating an empty arraylist

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("subcategories state","onCreate")

        arguments?.let {
            categoryId = it.getInt(Constants.CATEGORY_ID_KEY)
        }
        subcategoriesViewModel.categoryId = categoryId.toString()
        subcategoriesViewModel.page = 1
        subcategoriesViewModel.getNewSubCategories(categoryId)
        newSubCategoryAdapter = NewSubCategoriesAdapter(this,this,this)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mLayoutManager = LinearLayoutManager(context)
        restoreRecyclerPosition() //restore recycler focus
    }

    fun restoreRecyclerPosition(){
        if(state != null) {
            mLayoutManager.onRestoreInstanceState(state);
        }
    }

    override fun onPause() {
        super.onPause()
        state = mLayoutManager.onSaveInstanceState() // save recycler focus
    }
    override fun onResume() {
        super.onResume()
        Log.d("subcategories state","onResume")
        dataObservation()
        restoreRecyclerPosition()
    }

    private fun dataObservation() {
        // subcategoriesViewModel.page = 1
        with(subcategoriesViewModel)
        {
            observe(newSubCategories, ::showNewSubCategory)
            observe(showLoading, ::showLoading)
            observe(showServerError, ::showServerErrorMessage)
        }
    }

    override fun observeViewModel() {

        with(subcategoriesViewModel)
        {
            observe(newSubCategories, ::showNewSubCategory)
            observe(showLoading, ::showLoading)
            observe(showServerError, ::showServerErrorMessage)
        }

    }

    private fun showprovidersNumbers(totalRecords: Int) {
        providerServicesNum.text = totalRecords.toString()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d("subcategories state","onViewStateRestored")

        with(subcategoriesViewModel)
        {
            observe(newSubCategories, ::showNewSubCategory)
        }

    }

    override fun onFavouriteClicked(operation: Operation, position: Int) {
        subcategoriesViewModel.addToFavourite(operation.id)
    }

    fun showLoading(show: Boolean) {
        progress_bar.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun showNewSubCategory(subcategories: List<SubCategoryModel>){
        if (subcategoriesViewModel.page > 1 ) {
            remaining_records_tv.text = getRemaining(
                subcategoriesViewModel.newSubCategoriesResponse.value!!.totalRecords,
                subcategoriesViewModel.page
            )
            newSubCategoryAdapter.addData(subcategories!!)

        } else if (!(subcategories.isNullOrEmpty())) {
            remaining_records_tv.text = getRemaining(
                subcategoriesViewModel.newSubCategoriesResponse.value!!.totalRecords,
                subcategoriesViewModel.page
            )
            showprovidersNumbers(subcategoriesViewModel.newSubCategoriesResponse.value!!.totalRecords)
            newSubCategoryAdapter.setList(subcategories.toMutableList())
            rv_new_subcategories.adapter = newSubCategoryAdapter
            rv_new_subcategories.layoutManager = mLayoutManager
            rv_new_subcategories.addOnScrollListener(object :
                PaginationScrollListener(mLayoutManager) {
                override fun isLastPage(): Boolean {
                    return subcategoriesViewModel.isLastPage
                }

                override fun isLoading(): Boolean {
                    return subcategoriesViewModel.isLoading
                }

                override fun loadMoreItems() {
                    subcategoriesViewModel.page++
                    subcategoriesViewModel.isLoading = true
                    subcategoriesViewModel.getNewSubCategories(categoryId)

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

        subcategoriesViewModel.isLoading = false

    }

    fun showSubCategory(subcategories: List<SubCategory>) {
//        if (subcategoriesViewModel.page > 1) {
//            adapter.addData(subcategories!!)
//        } else if (!(subcategories.isNullOrEmpty())) {
//            adapter.setList(subcategories.toMutableList())
//            rv_subcategories.adapter = adapter
//
//            rv_subcategories.addOnScrollListener(object :
//                PaginationScrollListener(rv_subcategories.layoutManager as LinearLayoutManager) {
//                override fun isLastPage(): Boolean {
//                    return subcategoriesViewModel.isLastPage
//                }
//
//                override fun isLoading(): Boolean {
//                    return subcategoriesViewModel.isLoading
//                }
//
//                override fun loadMoreItems() {
//                    subcategoriesViewModel.page++
//                    subcategoriesViewModel.isLoading = true
//                    subcategoriesViewModel.getSubCategories()
//                }
//            })
//            showDataView(true)
//        } else {
//            showDataView(false)
//        }
//
//        subcategoriesViewModel.isLoading = false
    }

    private fun showDataView(show: Boolean) {
        group_no_result.visibility = if (show) View.GONE else View.VISIBLE
        rv_new_subcategories.visibility = if (show) View.VISIBLE else View.GONE
        //    rv_subcategories.visibility = if (show) View.VISIBLE else View.GONE
//        progress_bar.toGone()
    }


    //sub category click to view all
    override fun onClick(item: SubCategoryModel) {

        val bundle = bundleOf(
            Constants.KEY_ID to item.id ,
            Constants.KEY_NAME to item.name,
            Constants.KEY_FROM to OperationsFrom.Subcategories.value
        )
        navigateToViewAll(bundle)


//        val bundle = bundleOf(
//            Constants.KEY_ID to item.id ,
//            Constants.KEY_NAME to item.name,
//            Constants.KEY_FROM to OperationsFrom.Subcategories.value
//        )
//        navigateToOperation(bundle)
//        startActivity(
//            requireActivity().intentFor<OperationsActivity>(
//                Constants.KEY_ID to item.id,
//                Constants.KEY_NAME to item.name,
//                Constants.KEY_FROM to OperationsFrom.Subcategories.value
//            )
//        )
    }

    override fun onMoreDoctorsClick(item: SubCategoryModel) {
//        val bundle = bundleOf(
//            Constants.KEY_ID to item.id ,
//            Constants.KEY_NAME to item.name,
//            Constants.KEY_FROM to OperationsFrom.Subcategories.value
//        )
//        navigateToMoreProvider(bundle)

//        startActivity(
//            requireActivity().intentFor<MoreProvidersActivity>(
//                Constants.KEY_SUBCATEGORY_ID to item.id.toString(),
//                Constants.KEY_NAME to item.name
        //               Constants.KEY_FROM to OperationsFrom.Subcategories.value
//            )
//        )

    }

    override fun onOperationClicked(item: Operation) {
        navigateToOperationBookingScreen(item)

    }

    override fun onMoreDoctorsClicked(item: Operation) {
        val bundle = bundleOf(
            Constants.KEY_ID to item.id ,
            Constants.KEY_NAME to item.name,
            Constants.KEY_FROM to OperationsFrom.Subcategories.value
        )
        navigateToMoreProvider(bundle)
    }

    fun navigateToOperationBookingScreen(operation: Operation) {
        LogUtil.LogFirebaseEvent(
            "btn_book_now",
            "screen_" + this::class.java.simpleName,
            "operation",
            operation.id
        )

        startActivity(
            activity?.intentFor<BookHealthRequestActivity>(
                Constants.KEY_OPERATION_ITEM to operation.id
            )
        )
    }



    private fun setupDummeySubCategory() {
//        arrayListSubSUb.add(SubSubCategoryModel(2,"yasmen"))
//        arrayListSubSUb.add(SubSubCategoryModel(2,"ahmed"))
//        arrayListSubSUb.add(SubSubCategoryModel(2,"hassan"))
//        arrayListSubSUb.add(SubSubCategoryModel(2,"mahmoud"))
//        arrayListSub.add(SubCategoryModel("Ajay" ,arrayListSubSUb))//Adding object in arraylist
//        arrayListSub.add(SubCategoryModel("Ajay" ,arrayListSubSUb))//Adding object in arraylist
//        arrayListSub.add(SubCategoryModel("Ajay" ,arrayListSubSUb))//Adding object in arraylist

        //      newSubCategoryAdapter = NewSubCategoriesAdapter()

//        newSubCategoryAdapter.setList(arrayListSub.toMutableList())
//        rv_new_subcategories.adapter = newSubCategoryAdapter

    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
////        subcategoriesViewModel =
////            ViewModelProviders.of(requireActivity()!!, viewModelFactory)
////                .get(SubCategoryViewModel::class.java)
//
////        with(subcategoriesViewModel)
////        {
////           observe(subCategories, ::showSubCategory)
////            observe(showLoading, ::showLoading)
////            observe(showServerError, ::showServerErrorMessage)
////        }
//
//        //       adapter = SubCategoriesAdapter(this)
//    }
override fun onOwnerClicked(operation: Operation) {
//    Log.d(" operationowner", operation.owner?.type.toString())
//    if (operation.owner?.type == ProviderType.MedicalProvider.typeId)
//        openMedicalProviderProfile.value = operation.owner
//    else
//        openDoctorProfile.value = operation.owner

}
}
