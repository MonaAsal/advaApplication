package com.salamtak.app.ui.component.education.universities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.University
import com.salamtak.app.utils.changeIconFavouriteToIsFavourite
import com.salamtak.app.utils.loadCircleImage
import com.salamtak.app.utils.toGone
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_school.*

/**
 * Created by RadwaElsahn on 4/26/2021.
 */

class UniversitiesAdapter(val listener: UniversityListener) :
    RecyclerView.Adapter<UniversityViewHolder>() {

    lateinit var universities: MutableList<University>

    fun setList(list: MutableList<University>) {
        this.universities = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UniversityViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_school, parent, false)
        return UniversityViewHolder(
            view
        )
    }

    fun addData(listItems: List<University>) {
        var size = universities?.size!!
        universities?.addAll(listItems)
        notifyItemRangeInserted(size, universities?.size!!)
    }

    override fun onBindViewHolder(holder: UniversityViewHolder, position: Int) {

        holder.bind(universities!![position], listener)
    }

    override fun getItemCount(): Int {
        return if (universities != null) universities!!.size else 0
    }
}

class UniversityViewHolder(
    override val containerView: View
) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(
        university: University, listener: UniversityListener
    ) {
        iv_school_image.loadCircleImage(
            university.logoUrl,
            R.drawable.ic_circle
        )

        changeIconFavouriteToIsFavourite(
            iv_school_favorite,
            university.isLiked ?: false
        )



//        iv_operation_favorite.setOnClickListener {
//            changeIconFavouriteToIsFavourite(
//                iv_operation_favorite,
//                (school.isLiked).not()
//            )
//            listener.onFavouriteClicked(school, adapterPosition)
//            school.isLiked = school.isLiked.not()
//        }
        tv_school_name.text = university.name
        rb_school.text = university.rating.toString()
        university.locations?.let {
            if (it.isNotEmpty())
                tv_location.text = it[0]
            else
                tv_location.toGone()
        }

        itemView.setOnClickListener { listener.onUniversityClicked(university) }
        btn_details.setOnClickListener { listener.onUniversityClicked(university) }


    }


}


