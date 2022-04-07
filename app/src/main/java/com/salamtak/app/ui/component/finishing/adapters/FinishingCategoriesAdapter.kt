package com.salamtak.app.ui.component.finishing.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.FinishingCategoryModel
import com.salamtak.app.data.entities.FinishingProvidersModel
import com.salamtak.app.ui.component.finishing.interfaces.FinishingCategoryProviderListener
import com.salamtak.app.ui.component.finishing.interfaces.FinishingViewAllListener
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_finishing_category.*

class FinishingCategoriesAdapter(
    val listener: FinishingViewAllListener<FinishingCategoryModel>,
    val providerListener: FinishingCategoryProviderListener<FinishingProvidersModel>,

    ):
    RecyclerView.Adapter<FinishingCategoriesAdapter.ViewHolder>()  {
    lateinit var finishingCategoriesList: MutableList<FinishingCategoryModel>


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_finishing_category, parent, false)
        return ViewHolder(view)
    }

    fun setList(list: MutableList<FinishingCategoryModel>) {
        this.finishingCategoriesList = list
    }

    fun addData(listItems: List<FinishingCategoryModel>) {
        finishingCategoriesList?.let {
            var size = finishingCategoriesList?.size
            finishingCategoriesList?.addAll(listItems)
            notifyItemRangeInserted(size, finishingCategoriesList?.size)
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(finishingCategoriesList[position],listener,providerListener)
    }

    override fun getItemCount(): Int {
        return if (finishingCategoriesList != null) finishingCategoriesList.size else 0
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        lateinit  var  mLayoutManager : LinearLayoutManager
        var mLastPosition= 0

        fun bind(
            finishingCategoryModel: FinishingCategoryModel,
            listener: FinishingViewAllListener<FinishingCategoryModel>,
            providerListener : FinishingCategoryProviderListener<FinishingProvidersModel>
        ) {

            tv_viewAll2.setOnClickListener { listener.onClick(finishingCategoryModel) }
            tv_title.text = finishingCategoryModel.name

            finishingCategoryModel.finshingProviders?.let {
                setupProvidersList(it,providerListener)
            }

        }

        private fun setupProvidersList(
            providersList: ArrayList<FinishingProvidersModel>,
            providerListener: FinishingCategoryProviderListener<FinishingProvidersModel>
        ) {
            var providerAdapter = FinishingNewProvidersAdapter(providerListener)
            providerAdapter.setList(providersList.toMutableList()) //add operations list
            mLayoutManager = LinearLayoutManager(containerView.context, LinearLayoutManager.HORIZONTAL, false)
            rv_finishing_sub_category.adapter = providerAdapter
            rv_finishing_sub_category.layoutManager = mLayoutManager
            rv_finishing_sub_category.post {
                providerAdapter.notifyDataSetChanged()             //solving crash issue when fast scroll

            }
        }



    }

}