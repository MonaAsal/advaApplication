package com.salamtak.app.ui.component.medicalhistory

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProviders
import com.salamtak.app.R
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.ChronicDisease
import com.salamtak.app.data.entities.MedicalProfileLookupsResponse
import com.salamtak.app.data.entities.responses.ChronicDiseasesResponse
import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.ui.common.LookupsViewModel
import com.salamtak.app.ui.component.financialinfo.bank.MedicalProfileInfoViewModel
import com.salamtak.app.ui.component.medicalhistory.adapter.DiseasesAdapter
import com.salamtak.app.utils.addLinearHorizontalItemDecoration
import com.salamtak.app.utils.observe
import com.salamtak.app.utils.toGone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_chronic_diseases.*
import kotlinx.android.synthetic.main.toolbar.*

@AndroidEntryPoint
class MedicalProfileQuestionActivity : BaseActivity() {

    override val layoutId: Int
        get() = R.layout.activity_chronic_diseases

    val medicalProfileInfoViewModel: MedicalProfileInfoViewModel by viewModels()

    val lookupsViewModel: LookupsViewModel by viewModels()


    lateinit var diseasesAdapter: DiseasesAdapter

    override fun initializeViewModel() {
//        medicalProfileInfoViewModel =
//            ViewModelProviders.of(this, viewModelFactory)
//                .get(MedicalProfileInfoViewModel::class.java)
//
//        lookupsViewModel = ViewModelProviders.of(this, viewModelFactory)
//            .get(LookupsViewModel::class.java)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lookupsViewModel.getMedicalProfileLookups()
        tv_toolbar_title.text = getString(R.string.medical_history)
        iv_toolbar_back.setOnClickListener { onBackPressed() }
        tv_select_all.setOnClickListener {
            medicalProfileInfoViewModel.selectAll()
            diseasesAdapter.notifyDataSetChanged()
//            diseasesAdapter.notifyItemRangeChanged(0, medicalProfileInfoViewModel.diseases.size)

        }

        btn_next.setOnClickListener {
            medicalProfileInfoViewModel.addDiseases()
            //            medicalProfileInfoViewModel.update(
//
//            )
        }
    }

    override fun observeViewModel() {
        observeToast(medicalProfileInfoViewModel.showToast)
        observeError(medicalProfileInfoViewModel.showError)
        observe(lookupsViewModel.medicalLookupsLiveData, ::handleLookUpResponse)
        observe(
            medicalProfileInfoViewModel.postDiseasesResponseMutableLiveData,
            ::handleDiseasesChangeResponse
        )
    }

    private fun handleDiseasesChangeResponse(resource: Resource<ChronicDiseasesResponse>?) {
        when (resource) {
            is Resource.Loading -> showLoadingView(progress_bar)
            is Resource.Success -> {
                progress_bar.toGone()
                navigateToProfileScreen()
                finish()
            }
            is Resource.NetworkError -> {
                progress_bar.toGone()
                resource.errorCode?.let {
                    var error = lookupsViewModel.getToastMessage(it)
                    showErrorMessage(error)
                }
            }
            is Resource.DataError -> {
                progress_bar.toGone()
                resource.errorResponse?.let { showServerErrorMessage(it) }
            }
        }
    }


    private fun handleLookUpResponse(resource: Resource<MedicalProfileLookupsResponse>?) {
        when (resource) {
            is Resource.Loading -> showLoadingView(progress_bar)
            is Resource.Success -> {
                progress_bar.toGone()
                resource.data?.data.let {
                    bindDiseasesData(resource.data?.data!!.chronicDiseases)
                }
            }
            is Resource.NetworkError -> {
                progress_bar.toGone()
                resource.errorCode?.let {
                    var error = lookupsViewModel.getToastMessage(it)
                    showErrorMessage(error)
                }
            }
            is Resource.DataError -> {
                progress_bar.toGone()
                resource.errorResponse?.let { showServerErrorMessage(it) }
            }
        }
    }

    private fun bindDiseasesData(chronicDiseases: List<ChronicDisease>) {
        diseasesAdapter =
            DiseasesAdapter(
                medicalProfileInfoViewModel
            )

        medicalProfileInfoViewModel.diseases = chronicDiseases.toMutableList()
//        diseasesAdapter.setList(chronicDiseases.toMutableList())

        addLinearHorizontalItemDecoration(rv_diseases)

        rv_diseases.adapter = diseasesAdapter

    }


}