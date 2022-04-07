package com.salamtak.app.ui.component.health.subcategory.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.App.Companion.context
import com.salamtak.app.R
import com.salamtak.app.data.entities.Operation
import com.salamtak.app.data.entities.SubCategoryModel
import com.salamtak.app.ui.common.listeners.PaginationScrollListener
import com.salamtak.app.ui.component.health.adapter.OperationListenerN
import com.salamtak.app.ui.component.health.subcategory.listeners.OperationListener
import com.salamtak.app.ui.component.health.subcategory.listeners.SubcategoryListener
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_new_subcategory.*
import kotlinx.android.synthetic.main.item_subcategory.tv_subcategory_name

class NewSubCategoriesAdapter(val listener: SubcategoryListener<SubCategoryModel>,
                              val operationlistener: OperationListener<Operation>,
                              val Favlistener: OperationListenerN
) :
    RecyclerView.Adapter<NewSubCategoriesAdapter.ViewHolder>()  {

   lateinit var subCategoriesList: MutableList<SubCategoryModel>


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_new_subcategory, parent, false)
        return ViewHolder(view)
    }

    fun setList(list: MutableList<SubCategoryModel>) {
        this.subCategoriesList = list
    }

    fun addData(listItems: List<SubCategoryModel>) {
        subCategoriesList?.let {
            var size = subCategoriesList?.size
            subCategoriesList?.addAll(listItems)
            notifyItemRangeInserted(size, subCategoriesList?.size)
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(subCategoriesList[position],listener,operationlistener,Favlistener)
    }

    override fun getItemCount(): Int {
        return if (subCategoriesList != null) subCategoriesList.size else 0
    }


    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        lateinit  var  mLayoutManager :LinearLayoutManager
        var mLastPosition= 0

        fun bind(
            subCategory: SubCategoryModel,
            listener: SubcategoryListener<SubCategoryModel>,
            operationlistener: OperationListener<Operation>,
            Favlistener: OperationListenerN
        ) {

            tv_viewAll.setOnClickListener { listener.onClick(subCategory) }
            tv_subcategory_name.text = subCategory.name
            subCategory.operations?.let { setupOperationsList(it,operationlistener,Favlistener ) }

        }


        private fun setupOperationsList(
            operations: ArrayList<Operation>,
            operationlistener: OperationListener<Operation>,
            Favlistener: OperationListenerN
        ) {
            var operationsAdapter = OperationsGridAdapter(operationlistener,Favlistener)
            operationsAdapter.setList(operations.toMutableList()) //add operations list
            mLayoutManager = object : LinearLayoutManager(
                context,
                HORIZONTAL, false
            ) {
                override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                    lp.width = width / 2 -40
                    return true
                }
            }

          //  mLayoutManager = LinearLayoutManager(containerView.context, LinearLayoutManager.HORIZONTAL, false)
            rv_sub_sub_category.adapter = operationsAdapter
            rv_sub_sub_category.layoutManager = mLayoutManager
            operationsAdapter.stateRestorationPolicy = StateRestorationPolicy.ALLOW
            setRecyclerViewScrollListener()
            rv_sub_sub_category.post {
                operationsAdapter.notifyDataSetChanged()             //solving crash issue when fast scroll

            }
        }

        private fun setRecyclerViewScrollListener() {
            rv_sub_sub_category.addOnScrollListener(object : PaginationScrollListener(mLayoutManager) {
                override fun isLastPage(): Boolean {
                   return false
                }

                override fun isLoading(): Boolean {
                    return false
                }

                override fun loadMoreItems() {

                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val firstPosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    var lastPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val firstCompletePosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                    mLastPosition = firstPosition
                }
            })
        }




    }
}