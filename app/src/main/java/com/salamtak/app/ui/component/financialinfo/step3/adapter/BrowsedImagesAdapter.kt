package com.salamtak.app.ui.component.financialinfo.step3.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.Attachment
import com.salamtak.app.ui.component.financialinfo.ImagesListener
import com.salamtak.app.utils.loadRoundedImage
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_browsed_image.*
import java.io.File


/**
 * Created by RadwaElsahn on 5/13/2020.
 */

class BrowsedImagesAdapter(
    val listener: ImagesListener,
    val typePosition: Int,
    val categoryId: Int = 0
) :
    RecyclerView.Adapter<BrowsedImagesViewHolder>() {

    lateinit var paths: MutableList<Attachment>

    fun setList(imagesPaths: MutableList<Attachment>) {
        this.paths = imagesPaths
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrowsedImagesViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_browsed_image, parent, false)
        return BrowsedImagesViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: BrowsedImagesViewHolder, position: Int) {

        holder.bind(
            paths[position], position,
            typePosition, categoryId,
            listener
        )
    }

    override fun getItemCount(): Int {
        return if (paths != null) paths!!.size else 0
    }


}

class BrowsedImagesViewHolder(
    override val containerView: View
) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(
//        bitmap: Bitmap,
        attachment: Attachment,
        position: Int,
        typePosition: Int, categoryId: Int,
        listener: ImagesListener
    ) {

//        if (attachment.canBeDeleted)
//            iv_delete.toVisible()
//        else
//            iv_delete.toGone()

        iv_delete.setOnClickListener {
            listener.deleteImage(position, typePosition, categoryId)
        }

        iv_image.setOnClickListener {
            listener.selectImage(position, attachment)
        }

        if (attachment.type == 2)
            iv_image.setImageResource(R.drawable.ic_pdf)
        else {
            try {
                if (attachment.thumbnailUrl.startsWith("uploads/images"))
                    iv_image.loadRoundedImage(attachment.thumbnailUrl)
                else {
                    val imgFile = File(attachment.thumbnailUrl)

                    if (imgFile.exists()) {
                        val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                        iv_image.loadRoundedImage(myBitmap)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }


}

