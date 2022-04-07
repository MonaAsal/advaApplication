package com.salamtak.app.ui.component.medicalnetwork

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.google.android.material.tabs.TabLayout
import com.salamtak.app.R
import com.salamtak.app.data.entities.MedicalNetwork
import com.salamtak.app.data.entities.responses.SalamtakListResponse

import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.ui.common.listeners.RecyclerItemListener
import com.salamtak.app.ui.component.health.doctor.DoctorActivity
import com.salamtak.app.ui.component.health.customoperations.OtherOperationActivity
import com.salamtak.app.ui.component.medicalnetwork.adapter.MedicalNetworkAdapter
import com.salamtak.app.ui.component.health.medicalprovider.MedicalProviderActivity
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.observe
import com.salamtak.app.utils.toGone
import com.salamtak.app.utils.toVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_medical_networks.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity

@AndroidEntryPoint
class MedicalNetworkActivity : BaseActivity(), RecyclerItemListener<MedicalNetwork> {
    override val layoutId: Int
        get() = R.layout.activity_medical_networks

    val medicalNetworkViewModel: MedicalNetworkViewModel by viewModels()

//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory

    lateinit var adapter: MedicalNetworkAdapter

    override fun initializeViewModel() {
//        medicalNetworkViewModel =
//            ViewModelProviders.of(this, viewModelFactory)
//                .get(MedicalNetworkViewModel::class.java)
    }

    override fun observeViewModel() {
        observe(medicalNetworkViewModel.showLoading, ::showLoadingView)
        observe(medicalNetworkViewModel.showServerError, ::showServerErrorMessage)

        observe(medicalNetworkViewModel.doctorsResponse, ::handleMedicalNetworkResponse)
        observe(medicalNetworkViewModel.providerResponse, ::handleMedicalNetworkResponse)

//        observe(
//            medicalNetworkViewModel.openDoctorOperationsScreen,
//            ::navigateDoctorOperationsScreen
//        )
//        observe(
//            medicalNetworkViewModel.openMedicalProviderScreen,
//            ::navigateMedicalProviderOperationsScreen
//        )
    }

    fun showLoadingView(show: Boolean) {
        if (show)
            progress_bar.toVisible()
        else
            progress_bar.toGone()
    }


    private fun navigateDoctorOperationsScreen(item: MedicalNetwork) {
        startActivity(
            intentFor<MedicalNetworkOperationsActivity>(
                Constants.DOCTOR_ITEM_KEY to item.id,
                Constants.KEY_PROVIDER_NAME to item.name
            )
        )
    }

    private fun navigateMedicalProviderOperationsScreen(item: MedicalNetwork) {
        startActivity(
            intentFor<MedicalNetworkOperationsActivity>(
                Constants.MEDICAL_PROVIDER_ITEM_KEY to item.id,
                Constants.KEY_PROVIDER_NAME to item.name
            )
        )
    }

    private fun handleMedicalNetworkResponse(response: SalamtakListResponse<MedicalNetwork>) {

        bindMedicalNetworkData(response.data)
    }

    private fun bindMedicalNetworkData(data: List<MedicalNetwork>) {

        if (!(data.isNullOrEmpty())) {
            adapter.setList(data.toMutableList())
            rv_doctors.adapter = adapter
//            showDataView(true)
        } else {
//            showDataView(false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        iv_toolbar_back.setOnClickListener { onBackPressed() }
        tv_toolbar_title.text = getString(R.string.medical_network)
        //getString(R.string.medical_network)

        medicalNetworkViewModel.getDoctors()

        adapter =
            MedicalNetworkAdapter(
                this
            )
        btn_custom.setOnClickListener { startActivity<OtherOperationActivity>() }

//        et_search.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(p0: Editable?) {
//            }
//
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                try {
//                    rv_doctors.adapter = null
//                    rv_doctors.adapter = adapter!!
//                    adapter.getFilter().filter(s.toString())
//                } catch (e: Exception) {
//                }
//            }
//        })


        initTabs()

    }

    private fun initTabs() {
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        Log.e("doctor", "true")
                        medicalNetworkViewModel.isDoctor = true
                        if (medicalNetworkViewModel.doctorsResponse.value == null)
                            medicalNetworkViewModel.getDoctors()
                        else
                            bindMedicalNetworkData(medicalNetworkViewModel.doctorsResponse.value!!.data!!)
                    }
                    1 -> {
                        Log.e("hospital", "true")
                        medicalNetworkViewModel.isDoctor = false
                        if (medicalNetworkViewModel.providerResponse.value == null)
                            medicalNetworkViewModel.getMedicalProviders()
                        else
                            bindMedicalNetworkData(medicalNetworkViewModel.providerResponse.value!!.data!!)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })

        tabs.addTab(tabs.newTab().setText(getString(R.string.doctors)))
        tabs.addTab(tabs.newTab().setText(getString(R.string.hospitals_clinics)))

//        adapter = OperationsTrackingAdapter(operationsTrackingViewModel)

    }

    override fun onItemSelected(item: MedicalNetwork) {
        if (medicalNetworkViewModel.isDoctor) {
            startActivity(
                intentFor<DoctorActivity>(
                    Constants.KEY_ID to item.id
                )
            )
        } else {
            startActivity(
                intentFor<MedicalProviderActivity>(
                    Constants.KEY_ID to item.id
                )
            )
        }
    }

}
