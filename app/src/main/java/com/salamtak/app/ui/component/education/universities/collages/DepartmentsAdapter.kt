package com.salamtak.app.ui.component.education.universities.collages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.Collage
import com.salamtak.app.ui.common.listeners.RecyclerItemListener
import com.salamtak.app.utils.loadCircleImage
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_category.*

/**
 * Created by RadwaElsahn on 3/21/2020.
 */

class DepartmentsAdapter(
    private val listener: RecyclerItemListener<Collage>
) : RecyclerView.Adapter<DepartmentsAdapter.CollageViewHolder>() {


    lateinit var collages: MutableList<Collage>

    private val onItemClickListener: RecyclerItemListener<Collage> =
        object : RecyclerItemListener<Collage> {

            override fun onItemSelected(collage: Collage) {
                listener.onItemSelected(collage)
            }

        }

    fun setList(categories: MutableList<Collage>) {
        this.collages = categories
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CollageViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: CollageViewHolder, position: Int) {

        holder.bind(collages!![position], onItemClickListener)
    }

    override fun getItemCount(): Int {
//        return categories.size
        return if (collages != null) collages!!.size else 0
    }


    class CollageViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(collage: Collage, recyclerItemListener: RecyclerItemListener<Collage>) {
            tv_category_name.text = collage.name

            iv_category_image.loadCircleImage(collage.logoUrl, R.drawable.ic_circle)
            main_layout.setOnClickListener { recyclerItemListener.onItemSelected(collage) }
        }
    }


}

