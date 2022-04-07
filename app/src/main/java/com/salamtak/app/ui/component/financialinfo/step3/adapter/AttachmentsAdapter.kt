package com.salamtak.app.ui.component.financialinfo.saveinfo.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.FinancialTypeAttachments
import com.salamtak.app.ui.component.financialinfo.AttachCallBacks
import com.salamtak.app.ui.component.financialinfo.ImagesListener
import com.salamtak.app.ui.component.financialinfo.step3.adapter.BrowsedImagesAdapter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_attchments.*

/**
 * Created by RadwaElsahn on 5/13/2020.
 */

class AttachmentsAdapter(
    val listener: AttachCallBacks,
    val imagesListener: ImagesListener,
    val categoryId: Int = 0
) :
    RecyclerView.Adapter<AttachmentsViewHolder>() {


    lateinit var types: MutableList<FinancialTypeAttachments>

    fun setList(types: MutableList<FinancialTypeAttachments>) {
        this.types = types
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentsViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_attchments, parent, false)
        return AttachmentsViewHolder(
            view
        )
    }


    override fun getItemCount(): Int {
        return if (types != null) types!!.size else 0
    }

    override fun onBindViewHolder(holder: AttachmentsViewHolder, position: Int) {
        holder.bind(types!![position], position, listener, imagesListener, categoryId)
    }

    fun updateImages(position: Int) {
        notifyItemChanged(position)
    }
}

class AttachmentsViewHolder(
    override val containerView: View
) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(
        type: FinancialTypeAttachments,
        position: Int,
        attachCallBacks: AttachCallBacks, imagesListener: ImagesListener, categoryId: Int
    ) {
        if (type.isRequired)
            tv_title.text = type.title + "*"
        else
            tv_title.text = type.title

//        Log.e("type", type.title)
//        Log.e("AttachmentId", type.attachmentId.toString())

        btn_browse.setOnClickListener {
            type.attachments?.let {
                Log.e("AttachmentId", type.attachmentId.toString())
                Log.e("position", position.toString())
                if (type.attachments!!.size < 5)
                    attachCallBacks.browseClicked(
                        type,
                        position,
                        5 - type.attachments!!.size,
                        categoryId
                    )
            } ?: attachCallBacks.browseClicked(type, position, 5 - 0, categoryId)
        }

        if (type.attachments != null) {
            var adapter = BrowsedImagesAdapter(imagesListener, position, categoryId)
//            adapter.setPathes(type.imagesPaths!!)
            adapter.setList(type.attachments!!.toMutableList()!!)
            rv_images.adapter = adapter
        }

    }


}



