package com.salamtak.app.ui.component.financialinfo.bank

import androidx.lifecycle.MutableLiveData
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.ChronicDisease
import com.salamtak.app.data.entities.ChronicDiseaseInput
import com.salamtak.app.data.entities.responses.BaseResponse
import com.salamtak.app.data.entities.responses.ChronicDiseasesResponse
import com.salamtak.app.data.entities.responses.SalamtakResponse
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.common.BaseViewModel
import com.salamtak.app.usecase.MedicalProfileUseCase
import com.salamtak.app.usecase.errors.ErrorManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MedicalProfileInfoViewModel @Inject
constructor(private val medicalUseCase: MedicalProfileUseCase) : BaseViewModel() {


    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    var postDiseasesResponseMutableLiveData: MutableLiveData<Resource<ChronicDiseasesResponse>> =
        medicalUseCase.postDiseasesResponseMutableLiveData

    var deleteDiseasesResponseMutableLiveData: MutableLiveData<Resource<SalamtakResponse<BaseResponse>>> =
        medicalUseCase.deleteDiseasesResponseMutableLiveData

    lateinit var diseases: MutableList<ChronicDisease>


    fun toggleSelectDisease(position: Int) {
        var checked = diseases[position]?.hasThisChronic!!.not()
        diseases[position]?.hasThisChronic = checked
    }

    fun selectAll() {
        diseases.map { it.hasThisChronic = true }
    }

    fun addDiseases() {
        var input = prepareInput()
        medicalUseCase.addDiseases(input)
    }

    private fun prepareInput(): List<ChronicDiseaseInput> {

        var selected = diseases.filter { it.hasThisChronic }
        var list = ArrayList<ChronicDiseaseInput>()
        for (d in selected)
            list.add(ChronicDiseaseInput(d.id, "11/11/2019", " "))

        return list
    }
}