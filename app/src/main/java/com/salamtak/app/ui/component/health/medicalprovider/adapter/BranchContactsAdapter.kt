package com.salamtak.app.ui.component.health.medicalprovider.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.Branch
import com.salamtak.app.ui.component.health.medicalprovider.BranchListener
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_branch.*

/**
 * Created by RadwaElsahn on 4/28/2020.
 */

class BranchContactsAdapter(val listener: BranchListener) : RecyclerView.Adapter<BranchContactsViewHolder>() {

    lateinit var branches: MutableList<Branch>

    fun setList(list: MutableList<Branch>) {
        this.branches = list
    }

//    private val onLocationClicked: RecyclerItemListener<Branch> =
//        object : RecyclerItemListener<Branch> {
//            override fun onItemSelected(item: Branch) {
//
//                medicalProviderViewModel.onLocationClicked(item)
//            }
//
//        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BranchContactsViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_branch, parent, false)
        return BranchContactsViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: BranchContactsViewHolder, position: Int) {

        holder.bind(branches!![position],listener)
    }

    override fun getItemCount(): Int {
        return if (branches != null) branches!!.size else 0
    }


}
class BranchContactsViewHolder(
    override val containerView: View
) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(
        branch: Branch,listener: BranchListener
    ) {
        tv_branch_name.text = branch.name
        tv_branch_address.text = branch.streetAddress

        btn_location.setOnClickListener {
            listener.onMapClicked(branch)
        }
    }


}


