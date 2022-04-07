package com.salamtak.app.ui.component.carservices.custom

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import com.salamtak.app.R
import com.salamtak.app.data.entities.Area
import com.salamtak.app.data.entities.CarModel
import com.salamtak.app.data.entities.City
import com.salamtak.app.data.entities.responses.SalamtakListResponse


import com.salamtak.app.ui.common.BaseFragment
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_insurance.*
import kotlinx.android.synthetic.main.fragment_custom_car_step2.*
import kotlinx.android.synthetic.main.layout_custom_steps.*
import javax.inject.Inject

@AndroidEntryPoint
class CustomCarStep2Fragment : BaseFragment() ,
    AdapterView.OnItemSelectedListener {

    override val layoutId: Int
        get() = R.layout.fragment_custom_car_step2

    lateinit var carBrandsAdapter: ArrayAdapter<String>
    lateinit var citiesAdapter: ArrayAdapter<String>
    lateinit var areasAdapter: ArrayAdapter<String>

    val viewmodel: CustomCarViewModel by activityViewModels()

//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory

    var firstTime = true

    companion object {
        @JvmStatic
        fun newInstance() =
            CustomCarStep2Fragment()
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        preventUiPopingAboveKeyBoard()
//        viewmodel =
//            ViewModelProviders.of(requireActivity(), viewModelFactory)
//                .get(CustomCarViewModel::class.java)
        et_phone_step2.filters= Constants.DisableDecimalWithMaxLength(11)
        tv_lbl_provider_name.text = tv_lbl_provider_name.text.toString() + "*"
        tv_car_model_lbl.text = tv_car_model_lbl.text.toString() + "*"
        tv_city.text = tv_city.text.toString() + "*"
        observeViewModel2()

        if (viewmodel.isStep2Completed())
            (requireActivity() as CustomCarActivity).iv_circle2.toVisible()

        showCahchedData()

    }

    private fun showCahchedData() {
        viewmodel.getCustomCarInput()?.let {
            it.providerName?.let {
                et_provider_name.setText(it)
            }
            it.providerPhoneNumber?.let {
                et_phone_step2.setText(it)
                et_phone_step2.filters =  (Constants.DisableDecimalWithMaxLength(11))

            }

            it.additionalInfo?.let {
                et_more_into.setText(it)
            }

            showCahchedSpinnersSelectedValues()
        }

    }

    private fun showCahchedSpinnersSelectedValues() {
        selectSavedBrand()
        selectSavedCity()
        selectSavedArea()
    }

    private fun selectSavedCity() {
        viewmodel.liveInput.value?.let { input ->
            viewmodel.citiesLiveData!!.value?.let {
                input.cityId?.let {
                    var position = viewmodel.getCityPosition(it)
                    spinner_cities.setSelection(position + 1)
                }
            } ?: spinner_cities.setSelection(0)
        } ?: run {
            spinner_cities.setSelection(0)
        }
    }

    private fun selectSavedArea() {
        viewmodel.liveInput.value?.let { input ->
            viewmodel.selectedCityBasic?.let {
                input.areaId?.let { areaId ->
                    var position = viewmodel.getAreaPosition(areaId)
                    spinner_areas.setSelection(position + 1)
                } ?: spinner_areas.setSelection(0)
            } ?: spinner_areas.setSelection(0)
        } ?: spinner_areas.setSelection(0)


//        viewmodel.liveInput.value?.let { input ->
//            viewmodel.citiesLiveData!!.value?.let {
//                input.areaId?.let { areaId ->
//                    viewmodel.selectedAreaBasic?.let {
//                        var position = viewmodel.getAreaPosition(areaId)
//                        spinner_areas.setSelection(position + 1)
//                    } ?: spinner_areas.setSelection(0)
//                } ?: spinner_areas.setSelection(0)
//            } ?: spinner_areas.setSelection(0)
//        } ?: run {
//            spinner_areas.setSelection(0)
//        }
    }

    private fun selectSavedBrand() {
        viewmodel.liveInput.value?.let { input ->
            viewmodel.carBrandsLiveData!!.value?.let {
                input.carBrandId?.let {
                    var position = viewmodel.getCarBrandPosition(it)
                    spinner_car_model.setSelection(position + 1)
                } ?: spinner_car_model.setSelection(0)
            } ?: spinner_car_model.setSelection(0)
        } ?: spinner_car_model.setSelection(0)
    }

     fun observeViewModel2() {
        with(viewmodel)
        {
            observe(viewLifecycleOwner, showLoading, ::showLoading)
            observe(viewLifecycleOwner, customFromState, ::handleFormState)
            observe(viewLifecycleOwner, carBrandsLiveData, ::bindCarBrandsData)
            observe(viewLifecycleOwner, citiesLiveData, ::handleCitiesData)
            onStep2Start()
        }
    }

     override fun observeViewModel() {
//        with(viewmodel)
//        {
//            observe(viewLifecycleOwner, showLoading, ::showLoading)
//            observe(viewLifecycleOwner, customFromState, ::handleFormState)
//            observe(viewLifecycleOwner, carBrandsLiveData, ::bindCarBrandsData)
//            observe(viewLifecycleOwner, citiesLiveData, ::handleCitiesData)
//            onStep2Start()
//        }
    }

    private fun handleCitiesData(salamtakListResponse: SalamtakListResponse<City>) {
        bindCitiesData(salamtakListResponse.data)

//        val handler = Handler()
//        handler.postDelayed({
//            spinner_areas.setSelection(0)
//        }, 1000)
    }

    private fun bindCitiesData(data: List<City>) {
        progress_bar2.toGone()
        viewmodel.citiesNamesList = (data.map { it.name }).toMutableList()
        viewmodel.citiesNamesList.add(0, getString(R.string.choose))
        citiesAdapter = ArrayAdapter(
            requireActivity(),
            R.layout.item_spinner, viewmodel.citiesNamesList
        )

        spinner_cities.adapter = citiesAdapter
        spinner_cities.onItemSelectedListener = this

        et_cities_error.error = null

        selectSavedCity()
        bindEmptyAreasData()
    }


    private fun bindEmptyAreasData() {
        var list = listOf(getString(R.string.choose))
        areasAdapter = ArrayAdapter(
            requireActivity(),
            R.layout.item_spinner, list
        )

        spinner_areas.adapter = areasAdapter
        spinner_areas.onItemSelectedListener = this
    }

    private fun bindAreasData(data: List<Area>) {
        viewmodel.areasNamesList = (data.map { it.name }).toMutableList()
        viewmodel.areasNamesList.add(0, getString(R.string.choose))
        areasAdapter = ArrayAdapter(
            requireActivity(),
            R.layout.item_spinner, viewmodel.areasNamesList
        )

        spinner_areas.adapter = areasAdapter
        spinner_areas.onItemSelectedListener = this

        viewmodel.selectedAreaBasic = null
        et_areas_error.error = null

        if (firstTime) {
            selectSavedArea()
            firstTime = false
        }
    }

    private fun bindCarBrandsData(brandsData: SalamtakListResponse<CarModel>) {
        var list = brandsData.data.map { it.name }.toMutableList()
        list.add(0, getString(R.string.choose))
        carBrandsAdapter = ArrayAdapter(
            requireActivity(),
            R.layout.item_spinner, list
        )

        spinner_car_model.adapter = carBrandsAdapter
        spinner_car_model.onItemSelectedListener = this

        selectSavedBrand()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.spinner_cities -> {
                viewmodel.selectCityAt(position - 1)
                viewmodel.selectedCityBasic?.let {
                    bindAreasData(it.areas!!)
                }

            }

            R.id.spinner_areas -> {
                viewmodel.selectAreaAt(position - 1)
                et_areas_error.error = null
            }

            R.id.spinner_car_model -> {
                viewmodel.selectCarBrandAt(position - 1)

                et_car_model_error.error = null
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    fun onNextClicked() {
        viewmodel.saveStep2Data(
            et_provider_name.text.toString(),
            et_phone_step2.text.toString(),
            et_more_into.text.toString()
        )
    }

    fun showLoading(show: Boolean) {
        progress_bar2?.let {
            progress_bar2.visibility = if (show) View.VISIBLE else View.GONE
        }
    }

    fun handleFormState(customOperationFormState: CarFormState?) {
        if (customOperationFormState?.providerNameError != null) {
            et_provider_name.error = getString(customOperationFormState.providerNameError)
            et_provider_name.shakeView()
        }
        if (customOperationFormState?.phoneError != null) {
            et_phone_step2.error = getString(customOperationFormState.phoneError)
            et_phone_step2.shakeView()
        }

        if (customOperationFormState?.carBrandError != null) {
//            spinner_car_model.setSpinnerError(getString(customOperationFormState.carBrandError))
            et_car_model_error.error = getString(customOperationFormState.carBrandError)
            spinner_car_model.shakeView()
        } else
            et_car_model_error.error = null

        if (customOperationFormState?.cityError != null) {
//            spinner_cities.setSpinnerError(getString(customOperationFormState.cityError))
            et_cities_error.error = getString(customOperationFormState.cityError)
            spinner_cities.shakeView()
        } else
            et_cities_error.error = null

        if (customOperationFormState?.areaError != null) {
//            spinner_areas.setSpinnerError(getString(customOperationFormState.areaError))
            et_areas_error.error = getString(customOperationFormState.areaError)
            spinner_areas.shakeView()
        } else
            et_areas_error.error = null
    }

//    fun Spinner.setSpinnerError(error: String?) {
//        val errorText = this.selectedView as TextView
//        error?.let {
//            errorText.error = null
////            errorText.setTextColor(requireContext().color(R.color.textColor))
//        } ?: run {
//            errorText.error = error
////            errorText.setTextColor(Color.RED)
//        }
//
//    }


}