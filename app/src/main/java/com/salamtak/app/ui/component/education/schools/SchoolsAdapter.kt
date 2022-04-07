package com.salamtak.app.ui.component.education.schools

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.School
import com.salamtak.app.utils.changeIconFavouriteToIsFavourite
import com.salamtak.app.utils.loadCircleImage
import com.salamtak.app.utils.loadRoundedImage
import com.salamtak.app.utils.toGone
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_school.*

/**
 * Created by RadwaElsahn on 4/26/2021.
 */

class SchoolsAdapter(val listener: SchoolListener) :
    RecyclerView.Adapter<SchoolViewHolder>() {

    lateinit var schools: MutableList<School>

    fun setList(list: MutableList<School>) {
        this.schools = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_school, parent, false)
        return SchoolViewHolder(
            view
        )
    }

    fun addData(listItems: List<School>) {
        var size = schools?.size!!
        schools?.addAll(listItems)
        notifyItemRangeInserted(size, schools?.size!!)
    }

    override fun onBindViewHolder(holder: SchoolViewHolder, position: Int) {

        holder.bind(schools!![position], listener)
    }

    override fun getItemCount(): Int {
        return if (schools != null) schools!!.size else 0
    }
}

class SchoolViewHolder(
    override val containerView: View
) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(
        school: School, listener: SchoolListener
    ) {
        iv_school_image.loadRoundedImage(
            school.logoUrl,
            R.drawable.ic_default
        )

        changeIconFavouriteToIsFavourite(
            iv_school_favorite,
            school.isLiked ?: false
        )

//        iv_operation_favorite.setOnClickListener {
//            changeIconFavouriteToIsFavourite(
//                iv_operation_favorite,
//                (school.isLiked).not()
//            )
//            listener.onFavouriteClicked(school, adapterPosition)
//            school.isLiked = school.isLiked.not()
//        }
        tv_school_name.text = school.name
        rb_school.text = school.rating.toString()
        school.locations?.let {
            if (it.isNotEmpty())
                tv_location.text = it[0]
            else
                tv_location.toGone()
        }
        itemView.setOnClickListener { listener.onSchoolClicked(school) }
        main_cell.setOnClickListener { listener.onSchoolClicked(school) }


    }


}


