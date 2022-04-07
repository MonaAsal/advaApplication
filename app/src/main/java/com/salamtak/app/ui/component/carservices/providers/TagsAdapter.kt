package com.salamtak.app.ui.component.carservices.providers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_tag.*

class TagsAdapter(
    val list: MutableList<String>
) : RecyclerView.Adapter<SimpleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_tag, parent, false)
        return SimpleViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        holder.bind(list!![position])
    }

    override fun getItemCount(): Int {
        return if (list != null) list!!.size else 0
    }
}

class SimpleViewHolder(
    override val containerView: View
) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(
        item: String
    ) {
        if (item.isNotEmpty())
            rowTitle.text = item
    }
}


