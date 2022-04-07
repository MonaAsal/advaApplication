package com.salamtak.app.ui.component.carservices.providers.carlocation

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import com.salamtak.app.R
import com.salamtak.app.data.entities.Area
import com.salamtak.app.data.entities.City
import com.salamtak.app.data.entities.responses.SalamtakListResponse

import com.salamtak.app.ui.common.BaseBottomSheetDialog
import com.salamtak.app.ui.common.listeners.RecyclerItemListener
import com.salamtak.app.ui.common.listeners.RecyclerItemMultiSelectionListener
import com.salamtak.app.ui.component.carservices.providers.CarFiltersListener
import com.salamtak.app.ui.component.carservices.providers.CarServicesViewModel
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.bottom_sheet_car_location.*
import kotlinx.android.synthetic.main.bottom_sheet_car_location.btn_apply
import kotlinx.android.synthetic.main.bottom_sheet_car_location.iv_close
import kotlinx.android.synthetic.main.bottom_sheet_car_models.progress_bar
import kotlinx.android.synthetic.main.bottom_sheet_car_services.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class CarLocationBottomSheet (
    val listener: CarFiltersListener)
    : BaseBottomSheetDialog() ,
    RecyclerItemListener<City> ,
    RecyclerItemMultiSelectionListener<Area>,
    CarAreasFilterListener {

    override var getLayoutId: Int
        get() = R.layout.bottom_sheet_car_location
        set(value) {}

    val carServiceViewModel: CarServicesViewModel by activityViewModels()


    lateinit var cityAdapter: CarCitiesAdapter
    lateinit var areaAdapter: CarAreasAdapter
    var areasList :ArrayList<Area> = ArrayList()
    var selectedAreasList :ArrayList<Area> = ArrayList()
    var selectedCity : ArrayList<City> = ArrayList()
    lateinit  var selectedCityModel : City
    var scrollPoistion=0
    var applyEnabled=false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        cityAdapter = CarCitiesAdapter(this)
        areaAdapter = CarAreasAdapter(this)

        initalizeViewModel()
        initSearch()
        closeBottomSheet()
        clearSelection()
        Search()
        applyFilterBtn()
       // ApplyWithCities()

    }

    private fun applyFilterBtn() {

        btn_apply.setOnClickListener {
            applySelection()
        }
    }

    private fun applySelection() {
        carServiceViewModel.selectedArea.value = carServiceViewModel.selectedCarAreasTmp.value
        listener.onCarLocationSelected(carServiceViewModel.selectedArea.value)
        dismiss()
    }

    private fun ApplyWithCities() {
        btn_apply.setOnClickListener {
//            carServiceViewModel.selectedCity.value?.let {
//                selectedCityModel = City(selectedAreasList, it.id, it.name, it.name2, it.isSelected)
//                selectedCity.add(selectedCityModel)
//                Log.e("selected city model", selectedCity.toString())
//              //  listener.onCarLocationSelected(selectedCityModel)
//                carServiceViewModel.saveSelectedCity(selectedCityModel)
//            }

            dismiss()
        }
    }

    private fun Search() {
        edt_search.applyFontFamily()
        edt_search.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {

                    try {
                        rv_car_area.adapter = null
                        rv_car_area.adapter = areaAdapter
                        areaAdapter.getFilter().filter(query.toString())
                    } catch (e: Exception) {
                    }
                    areaAdapter.getFilter().filter(query)
                    return false                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    areaAdapter.getFilter().filter(newText)

                    return false
                }
            })

    }

    private fun initSearch() {
        edt_search.onActionViewExpanded()
        edt_search.clearFocus()
        edt_search.setOnClickListener { view ->
            edt_search.onActionViewExpanded()
        }
    }

    fun clearSelection() {
        btn_Clear.setOnClickListener {
            areasList.clear()
            areaAdapter.clearSelection()
            edt_search.setQuery("", false)//setText("")
            carServiceViewModel.selectedCarAreasTmp.value = null
            carServiceViewModel.selectedArea.value = null
            listener.onCarLocationSelected(null)
            applyEnabled = false
        }
    }

    private fun closeBottomSheet() {
        iv_close.setOnClickListener {
            dismiss()
        }
    }

    private fun initalizeViewModel() {
//        carServiceViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(CarServicesViewModel::class.java)
        with(carServiceViewModel)
        {
            getCitiesForCarFilter()
            observe(viewLifecycleOwner, citiesLiveData, ::showAreaModels)
            observe(viewLifecycleOwner, showLoading, ::showLoadingView)
        }

    }

    private fun showAreaModels(cityModels: SalamtakListResponse<City>) {
        areasList.clear()
        cityModels?.let {
            for (i in 0 .. cityModels.data.size-1){
                cityModels.data.get(i).areas?.let {
                        it1 -> areasList.addAll(it1)
                }
            }
            showAllAreas(areasList, carServiceViewModel.selectedArea.value)
        }
    }
    private fun showAllAreas(areas: ArrayList<Area>, selectedList: List<Area>?) {
        areas?.let {
            areaAdapter.setList(areas?.toMutableList(), selectedList)
            rv_car_area.adapter = areaAdapter
         //   addLinearHorizontalItemDecoration(rv_car_area)

        }
    }


    fun showLoadingView(show: Boolean) {
        progress_bar?.let {
            if (show) {
                progress_bar.toVisible()
            } else {
                progress_bar.toGone()
            }
        }
    }

    override fun onCarLocationSelected(area: List<Area>?) {
      //  carServiceViewModel.selectedArea.value = area
        carServiceViewModel.selectedCarAreasTmp.value = area
    }

//    private fun showCityModels(cityModels: SalamtakListResponse<City>) {
//        if(carServiceViewModel.selectedCity.value != null){
//            carServiceViewModel.selectedCity.value  = carServiceViewModel.getSelectedCity()
//        }else{
//            carServiceViewModel.selectedCity.value  = cityModels.data.get(0)
//        }
//        cityAdapter.setList(cityModels.data?.toMutableList(), carServiceViewModel.selectedCity.value)
//        rv_car_city.adapter = cityAdapter
//        scrollToSelectedPosition(cityModels.data?.toMutableList())
//        addVerticalItemDecorationWithScrolling(rv_car_city,position = scrollPoistion)
//        carServiceViewModel.selectedCity.value?.let {
//            showAreasModels(it)
//        }
//
//    }

    private fun scrollToSelectedPosition(cityList: MutableList<City>) {
        for(index in 0..cityList.size-1){
            if(cityList[index].isSelected){
                scrollPoistion = index
            }
        }
    }

  //  private fun showAreasModels(item: City) {
   //     item?.let {
//            item.areas?.let {
//                areasList = item.areas as ArrayList<Area>
//                areaAdapter.setList(areasList?.toMutableList())
//                rv_car_area.adapter = areaAdapter
//                Log.d("Areas", areasList.toString())
//            }
      //  }
  //  }

    //on city clicked ....
    override fun onItemSelected(item: City) {
//        areasList?.let {
//            for(row in areasList) row.isSelected=false //remove areas selection
//            selectedAreasList.clear() //clear area selected list
//            //showAreasModels(item)
//            Log.e("carCitySelected", item.name)
//            carServiceViewModel.selectedCity.value = item
//        }

    }
    //on area clicked
    override fun onItemMultiSelected(item: Area) {
        Log.e("carAreaSelected", item.name)
      //  selectedAreasList.add(item)
       // rv_car_area.adapter?.notifyDataSetChanged()
    }
    override fun onItemDeSelected(item: Area) {
        Log.e("carAreaUnSelected", item.name)
      //  rv_car_area.adapter?.notifyDataSetChanged()
    }



    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

//        if (applyEnabled == false){
//            carServiceViewModel.selectedArea.value = null
//        }

    }
}