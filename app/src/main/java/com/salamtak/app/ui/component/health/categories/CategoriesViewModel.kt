package com.salamtak.app.ui.component.health.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.Category
import com.salamtak.app.data.entities.responses.SalamtakListResponse
import com.salamtak.app.data.error.mapper.ErrorMapper
import com.salamtak.app.ui.common.BaseViewModel
import com.salamtak.app.usecase.CategoriesUseCase
import com.salamtak.app.usecase.errors.ErrorManager
import com.salamtak.app.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Radwa Elsahn on 3/21/2020
 */

@HiltViewModel
class CategoriesViewModel @Inject constructor(private val categoriesDataUseCase: CategoriesUseCase) : BaseViewModel() {

    override val errorManager: ErrorManager
        get() = ErrorManager(ErrorMapper())

    var categoriesLiveData: MutableLiveData<Resource<SalamtakListResponse<Category>>> =
        categoriesDataUseCase.categoriesLiveData

    private val noSearchFoundPrivate: MutableLiveData<Unit> = MutableLiveData()
    val noSearchFound: LiveData<Unit> get() = noSearchFoundPrivate

    /**
     * UI actions as event, user action is single one time event, Shouldn't be multiple time consumption
     */
    private val openCategoryDetailsPrivate = MutableLiveData<Event<Category>>()
    val openCategoryDetails: LiveData<Event<Category>> get() = openCategoryDetailsPrivate

    fun getCategories(categoryId: Int) {
        categoriesDataUseCase.getCategories(categoryId)
    }


    fun openCategoryDetails(item: Category) {
        openCategoryDetailsPrivate.value = Event(item)
    }

    fun onSearchClick(title: String) {
        if (title.isNotEmpty()) {
            val item = categoriesDataUseCase.searchByTitle(title)
            if (item != null) {
//                newsSearchFoundPrivate.value = newsItem
            } else {
                noSearchFoundPrivate.postValue(Unit)
            }
        } else {
            noSearchFoundPrivate.postValue(Unit)
        }
    }

    fun shouldAddFinancialInfo(): Boolean {
        return categoriesDataUseCase.needFinancialInfo()
    }

}
