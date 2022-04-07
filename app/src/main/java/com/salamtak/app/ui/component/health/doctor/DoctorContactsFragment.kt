package com.salamtak.app.ui.component.health.doctor

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.salamtak.app.R
import com.salamtak.app.data.entities.Contact


import com.salamtak.app.ui.component.more.adapter.ContactsAdapter
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_doctor_contacts.*

@AndroidEntryPoint
class DoctorContactsFragment : Fragment()  {

//    lateinit var doctor: Doctor
    lateinit var contacts: List<Contact>
   val doctorViewModel: DoctorViewModel by viewModels()


    override fun onResume() {
        super.onResume()
//        arguments?.let {
//            doctor = it.getParcelable(Constants.DOCTOR_ITEM_KEY)!!
//        }

        arguments?.let {
            contacts = it.getParcelableArrayList<Contact>(Constants.KEY_CONTACTS)!!
        }
        initViews()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        doctorViewModel =
//            ViewModelProviders.of(activity!!, viewModelFactory)
//                .get(DoctorViewModel::class.java)

        observe(doctorViewModel.callNumber, ::callNumber)
        observe(doctorViewModel.openLink, ::openLink)

        observe(doctorViewModel.sendEmail, ::sendEmail)

    }

    private fun sendEmail(to: String) {
        val email = Intent(Intent.ACTION_SEND)
        email.putExtra(Intent.EXTRA_EMAIL, arrayOf<String>(to))
//        email.putExtra(Intent.EXTRA_SUBJECT, subject)
//        email.putExtra(Intent.EXTRA_TEXT, id.message)

        email.type = "message/rfc822"

        startActivity(Intent.createChooser(email, "Choose an Email client :"))
    }

    private fun openLink(link: String) {
        try {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(link))

            context?.startActivity(browserIntent)
        }catch (e:Exception)
        {}
    }

    private fun callNumber(number: String) {
        val intent = Intent(Intent.ACTION_DIAL)

        intent.data = Uri.parse("tel:" + number)
        context?.startActivity(intent)
    }

    private fun initViews() {

        var adapter =
            ContactsAdapter(
                null,
                doctorViewModel
            )
        adapter.setList(contacts?.toMutableList()!!)
        rv_contacts.adapter = adapter
//        tv_phone.text = doctor.contacts[0].contact
//        tv_website.text = doctor.contacts[0].contact
//        tv_rate.text =
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_doctor_contacts, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance(contacts: List<Contact>) =
            DoctorContactsFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(Constants.KEY_CONTACTS, contacts as ArrayList<out Parcelable> )
                }
            }

//        fun newInstance(param1: Doctor) =
//            DoctorContactsFragment().apply {
//                arguments = Bundle().apply {
//                    putParcelable(Constants.DOCTOR_ITEM_KEY, param1)
//                }
//            }
    }
}
