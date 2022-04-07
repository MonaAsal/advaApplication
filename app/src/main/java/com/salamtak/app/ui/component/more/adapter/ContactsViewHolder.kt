package com.salamtak.app.ui.component.more.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.data.entities.Contact
import com.salamtak.app.ui.common.listeners.RecyclerItemListener
import com.salamtak.app.utils.Constants.INSTANCE.CONTACT_TYPES
import com.salamtak.app.utils.Constants.INSTANCE.CONTACT_TYPES_IMAGES
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_contact.*

/**
 * Created by RadwaElsahn on 4/28/2020.
 */

class ContactsViewHolder(
    override val containerView: View
) :
    RecyclerView.ViewHolder(containerView),
    LayoutContainer {

    fun bind(
        contact: Contact,onItemClickListener: RecyclerItemListener<Contact>
    ) {
        tv_contact_type_name.text = CONTACT_TYPES[contact.contactType]
        tv_contact.text = contact.contact
        iv_icon.setImageDrawable(CONTACT_TYPES_IMAGES[contact.contactType])

        tv_contact.setOnClickListener {  onItemClickListener.onItemSelected(contact)}
        containerView.setOnClickListener {  onItemClickListener.onItemSelected(contact)}
    }


}

