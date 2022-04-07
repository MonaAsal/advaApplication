package com.salamtak.app.ui.component.more.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salamtak.app.R
import com.salamtak.app.data.entities.Contact
import com.salamtak.app.ui.common.listeners.RecyclerItemListener
import com.salamtak.app.ui.component.health.doctor.DoctorViewModel
import com.salamtak.app.ui.component.health.medicalprovider.MedicalProviderViewModel

/**
 * Created by RadwaElsahn on 4/28/2020.
 */

class ContactsAdapter(
    val medicalProviderViewModel: MedicalProviderViewModel?,
    val doctorViewModel: DoctorViewModel?
) : RecyclerView.Adapter<ContactsViewHolder>() {


    lateinit var contacts: MutableList<Contact>

    fun setList(visits: MutableList<Contact>) {
        this.contacts = visits
    }

    private val onItemClickListener: RecyclerItemListener<Contact> =
        object : RecyclerItemListener<Contact> {

            override fun onItemSelected(contact: Contact) {
                if (doctorViewModel != null)
                    doctorViewModel?.onContactClicked(contact)
                else if (medicalProviderViewModel != null)
                    medicalProviderViewModel?.onContactClicked(contact)
            }

        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_contact, parent, false)
        return ContactsViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {

        holder.bind(contacts!![position],onItemClickListener)
    }

    override fun getItemCount(): Int {
        return if (contacts != null) contacts!!.size else 0
    }


}

