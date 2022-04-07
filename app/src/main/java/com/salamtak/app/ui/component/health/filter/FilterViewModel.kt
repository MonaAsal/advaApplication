package com.salamtak.app.ui.component.health.filter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.salamtak.app.data.DataRepository
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.*
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.component.health.BaseOperationViewModelN
import com.salamtak.app.usecase.errors.ErrorManager
import com.salamtak.app.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(private val dataRepository: DataRepository) :
    BaseOperationViewModelN(dataRepository) {
    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    var searchResult = MutableLiveData<SalamtakListResponse<Operation>>()

    var categoryId = ""
    var advanced = false
    var page = 1
    var isLastPage = false
    var isLoading = false
    val operations: MutableLiveData<List<Operation>> = MutableLiveData()

    //    var subcategoryId = ""
    var cityId = ""
//    var providerName = ""
    var operationName = ""
    var from = ""
    var to = ""

//    var categoriesList: MutableLiveData<List<Category>> = MutableLiveData()
//    var citiesLiveData = MutableLiveData<Resource<SalamtakListResponse<City>>>()

    lateinit var citiesNamesList: MutableList<String>
    lateinit var areasNamesList: MutableList<String>

    var selectedCategory: Category? = null
    var selectedSubCategoryId = ""
    var selectedProvider: MutableLiveData<String> = MutableLiveData()
    var selectedCity: MutableLiveData<City> = MutableLiveData()

    //    var selectedArea: MutableLiveData<Area> = MutableLiveData()
    var medicalProvider = ""

    //    val governoratesLiveData = MutableLiveData<SalamtakListResponse<City>>()
    val filterData = MutableLiveData<FilterData>()

    fun loadAdvancedFilterData() {

        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getPreHealthAdvancedFilter()) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        filterData.postValue(it)
                    }
                }
                is Resource.NetworkError -> {
                    showLoading.postValue(false)
                }

                is Resource.DataError -> {
                    showLoading.postValue(false)
                    resource.errorResponse?.let { showServerError.postValue(it) }
                }
            }

        }
    }

    fun loadPreHealthCategoryFilter(categoryId: String) {

        viewModelScope.launch {
            showLoading.postValue(true)
            when (val resource =
                dataRepository.getPreHealthCategoryFilter(categoryId)) {
                is Resource.Success -> {
                    showLoading.postValue(false)
                    resource.data?.let {
                        filterData.postValue(it)
                    }
                }
                is Resource.NetworkError -> {
                    showLoading.postValue(false)
                }

                is Resource.DataError -> {
                    showLoading.postValue(false)
                    resource.errorResponse?.let { showServerError.postValue(it) }
                }
            }

        }
    }

    fun selectCategoryAt(position: Int) {
        if (position != -1) {
            selectedCategory = filterData.value!!.categoriesAndSubCategories!![position]
            categoryId = selectedCategory!!.id.toString()
        } else {
            selectedCategory = null
            categoryId = ""
        }

    }

    fun selectSubCategoryAt(position: Int) {
        if (position != -1) {
            selectedCategory?.let {
                it.subCategories?.let { subcategories ->
                    selectedSubCategoryId = subcategories[position].id.toString()
                }
            } ?: filterData.value?.subCategories?.let {
                selectedSubCategoryId = it[position].id.toString()
            }
        } else
            selectedSubCategoryId = ""
    }

    fun selectCityAt(position: Int) {
        if (position != -1) {
            selectedCity.value =
                filterData.value!!.cities!!!![position]
        } else
            selectedCity.value = null
    }

//    fun selectAreaAt(position: Int) {
//        selectedArea.value =
//            selectedCity.value!!.areas?.get(position)
//    }

//    fun searchOperations(query: String) {
//        searchOperations(
//            query,
//            if (categoryItem.value != null) categoryItem.value!!.id.toString() else "",
//            if (selectedArea.value != null) selectedArea.value!!.id.toString() else ""
//
//        )
//    }

    fun findResults(

    ) {
        if (advanced)
            healthAdvancedSearch(
                medicalProvider,
                cityId,
                from,
                to,
                operationName
            )
        else
            categoryHealthAdvancedSearch(
                categoryId,
                selectedSubCategoryId,
                medicalProvider,
                cityId,
                from,
                to
            )
    }

    fun healthAdvancedSearch(
        medicalProvider: String,
        cityId: String,
        startPrice: String,
        endPrice: String,
        operation: String
    ) {

        if (page == 1 || page <= operationsResponse.value!!?.totalPages!!) {
            viewModelScope.launch {
                showLoading.postValue(true)
                when (val resource =
                    dataRepository.healthAdvancedSearch(
                        categoryId, selectedSubCategoryId,
                        medicalProvider,
                        cityId,
                        startPrice,
                        endPrice,
                        operation,
                        page,
                        Constants.PAGE_SIZE
                    )) {
                    is Resource.Success -> {
                        isLoading = false
                        showLoading.postValue(false)
                        resource.data?.let {
                            operationsResponse.value = it
                            operations.value = it.data
                        }
                    }
                    is Resource.NetworkError -> {
                        isLoading = false
                        showLoading.postValue(false)
//                        showError.postValue(showError)
                    }

                    is Resource.DataError -> {
                        isLoading = false
                        showLoading.postValue(false)
                        resource.errorResponse?.let { showServerError.postValue(it) }
                        //showError.postValue(Event(resource.errorResponse!!.message!!))
                        // error ="cannot create the live liveSession"
                    }
                }
            }
        }
    }

    fun categoryHealthAdvancedSearch(
        categoryId: String,
        subCategoryId: String,
        medicalProvider: String,
        cityId: String,
        startPrice: String,
        endPrice: String
    ) {
        if (page == 1 || page <= operationsResponse.value!!?.totalPages!!) {
            viewModelScope.launch {
                showLoading.postValue(true)
                when (val resource =
                    dataRepository.categoryHealthAdvancedSearch(
                        categoryId,
                        subCategoryId,
                        medicalProvider,
                        cityId,
                        startPrice,
                        endPrice,
                        page,
                        Constants.PAGE_SIZE

                    )) {
                    is Resource.Success -> {
                        isLoading = false
                        showLoading.postValue(false)
                        resource.data?.let {
                            operationsResponse.value = it
                            operations.value = it.data
                        }
                    }
                    is Resource.NetworkError -> {
                        isLoading = false
                        showLoading.postValue(false)
//                        showError.postValue(showError)
                    }

                    is Resource.DataError -> {
                        isLoading = false
                        showLoading.postValue(false)
                        resource.errorResponse?.let { showServerError.postValue(it) }
                        //showError.postValue(Event(resource.errorResponse!!.message!!))
                        // error ="cannot create the live liveSession"
                    }
                }
            }
        }
    }

    fun loadFilterData() {
        if (categoryId != "" && categoryId != "0") {
            loadPreHealthCategoryFilter(categoryId)
        } else {
            loadAdvancedFilterData()
            advanced = true
        }
    }


}