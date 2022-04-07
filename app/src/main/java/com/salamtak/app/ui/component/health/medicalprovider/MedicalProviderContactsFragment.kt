package com.salamtak.app.ui.component.health.medicalprovider

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.salamtak.app.R
import com.salamtak.app.data.entities.Branch
import com.salamtak.app.data.entities.MedicalProviderDetails


import com.salamtak.app.ui.component.health.medicalprovider.adapter.BranchContactsAdapter
import com.salamtak.app.ui.component.more.adapter.ContactsAdapter
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.addLinearHorizontalItemDecoration
import com.salamtak.app.utils.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_medical_provider_contacts.*

@AndroidEntryPoint
class MedicalProviderContactsFragment : Fragment()  ,BranchListener{

    lateinit var medicalProvider: MedicalProviderDetails

    val medicalProviderViewModel: MedicalProviderViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        medicalProviderViewModel =
//            ViewModelProviders.of(activity!!, viewModelFactory)
//                .get(MedicalProviderViewModel::class.java)

        observe(medicalProviderViewModel.callNumber, ::callNumber)
        observe(medicalProviderViewModel.openLink, ::openLink)
        observe(medicalProviderViewModel.sendEmail, ::sendEmail)
//        observe(medicalProviderViewModel.openMap, ::openMap)

    }

    override fun onResume() {
        super.onResume()
        arguments?.let {
            medicalProvider = it.getParcelable(Constants.BRANCH_ITEM_KEY)!!
        }
        initViews()
    }

    private fun sendEmail(to: String) {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf<String>(to))
        emailIntent.data = Uri.parse("mailto:")
        emailIntent.type = "text/plain"

        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf<String>(to))
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject")
//        emailIntent.putExtra(Intent.EXTRA_TEXT   , "Message Body")
        context?.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    private fun openLink(link: String) {
        val browserIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse(link))
        context?.startActivity(browserIntent)
    }

    private fun callNumber(number: String) {
        val intent = Intent(Intent.ACTION_DIAL)

        intent.data = Uri.parse("tel:" + number)
        context?.startActivity(intent)
    }


    private fun initViews() {

        tv_branches.text = medicalProvider.name

        medicalProvider.branches?.let {
            var adapter = BranchContactsAdapter(this)
            adapter.setList(medicalProvider.branches!!.toMutableList())
            addLinearHorizontalItemDecoration(rv_branches)
            rv_branches.adapter = adapter
        }

        medicalProvider.branches?.let {
            if (medicalProvider.branches!!.isNotEmpty()) {
                var adaptercontacts =
                    ContactsAdapter(
                        medicalProviderViewModel,
                        null
                    )
                adaptercontacts.setList(medicalProvider.branches!![0].contacts!!.toMutableList())
                rv_contacts.adapter = adaptercontacts
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_medical_provider_contacts, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: MedicalProviderDetails) =
            MedicalProviderContactsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(Constants.BRANCH_ITEM_KEY, param1)
                }
            }
    }

    override fun onMapClicked(branch: Branch) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("geo:<lat>,<long>?q=<${branch.lat}>,<${branch.lng}>(${medicalProvider.name})")
//                    geo:<lat>,<long>?q=<${branch.lat}>,<${branch.lng}>(${branch.medicalProvider.name})
        )

//        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        context!!.startActivity(intent)
    }
}
