package com.salamtak.app.ui.component.health.filter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import com.salamtak.app.R
import com.salamtak.app.data.entities.*


import com.salamtak.app.ui.common.BaseDialogFragment
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.observe
import com.salamtak.app.utils.toGone
import com.salamtak.app.utils.toVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_filter.*
import kotlinx.android.synthetic.main.dialog_filter.progress_bar
import org.jetbrains.anko.intentFor
import javax.inject.Inject

@AndroidEntryPoint
class FilterDialog : BaseDialogFragment(),
    AdapterView.OnItemSelectedListener  {

    val filterViewModel: FilterViewModel by viewModels()

//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory

    override fun onStart() {
        super.onStart()
        val lp = dialog?.window?.attributes
        lp?.width = WindowManager.LayoutParams.MATCH_PARENT
        val displayMetrics = DisplayMetrics()
        requireActivity()!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        lp?.height = height
        val window = dialog?.window
        window?.attributes = lp
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_filter, container, false)
        dialog?.window?.setGravity(Gravity.CENTER)
//        dialog?.window?.setWindowAnimations(R.style.SlideInOutDialogAnimation)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        filterViewModel =
//            ViewModelProviders.of(requireActivity()!!, viewModelFactory)
//                .get(FilterViewModel::class.java)

        filterViewModel.categoryId = arguments?.getInt(Constants.CATEGORY_ID_KEY, 0).toString()!!

        if (filterViewModel.categoryId != "0" && filterViewModel.categoryId != "") {
            group_category.toGone()
        }

        filterViewModel.loadFilterData()

        observeViewModel()
        tv_title.setOnClickListener { dismiss() }
        btn_search.setOnClickListener {
            //filterViewModel.searchOperations("")
//            (activity as BaseOperationsActivity).openSearchScreen(
//                "",
//                if (filterViewModel.categoryItem.value != null) filterViewModel.categoryItem.value!! else null,
//                if (filterViewModel.selectedArea.value != null) filterViewModel.selectedArea.value!! else null
//            )

            dismiss()
        }


        rangeBar.setIndicatorTextDecimalFormat("0")

        rangeBar?.setOnRangeChangedListener(object :
            OnRangeChangedListener {
            override fun onRangeChanged(
                rangeSeekBar: RangeSeekBar, leftValue: Float,
                rightValue: Float, isFromUser: Boolean
            ) {
//                setIndicatorData()

//                if(filterViewModel.getLocaleString().equals(Constants.ARABIC_LOCALE)) {
//                    rangeBar.rightSeekBar.setIndicatorText(
//                        rangeBar.leftSeekBar.progress.toInt().toString()
//                    )
//                    rangeBar.leftSeekBar.setIndicatorText(
//                        rangeBar.rightSeekBar.progress.toInt().toString()
//                    )
//                }
                //   Log.e("min", "from${leftValue} to: ${rightValue}")
            }

            override fun onStartTrackingTouch(
                view: RangeSeekBar?,
                isLeft: Boolean
            ) {
                setIndicatorData()
            }

            override fun onStopTrackingTouch(
                view: RangeSeekBar?,
                isLeft: Boolean
            ) {

                setIndicatorData()
                Log.e(
                    "progress",
                    "from: ${rangeBar.leftSeekBar.progress.toInt()}, to: ${rangeBar.rightSeekBar.progress.toInt()}"
                )
            }
        })


        btn_search.setOnClickListener {
            requireActivity().startActivity(
                requireActivity().intentFor<OperationsFilterActivity>(
                    Constants.ADVANCED to filterViewModel.advanced,
                    Constants.CATEGORY_ID_KEY to filterViewModel.categoryId,
                    Constants.KEY_SUBCATEGORY_ID to filterViewModel.selectedSubCategoryId,
                    Constants.KEY_CITY_ID to if (filterViewModel.selectedCity.value == null) "" else filterViewModel.selectedCity.value!!.id.toString(),
                    Constants.KEY_PROVIDER_NAME to et_provider.text.toString(),
                    Constants.KEY_OPERATION_ITEM to et_operation.text.toString(),
                    Constants.KEY_FROM to rangeBar.leftSeekBar.progress.toInt().toString(),
                    Constants.KEY_TO to rangeBar.rightSeekBar.progress.toInt().toString()
                )
            )
        }
    }

    private fun setIndicatorData() {
        if (filterViewModel.getLocaleString() == Constants.ARABIC_LOCALE) {
            tv_start_price.text = rangeBar.rightSeekBar.progress.toInt().toString()
            tv_end_price.text = rangeBar.leftSeekBar.progress.toInt().toString()
        } else {
            tv_start_price.text = rangeBar.leftSeekBar.progress.toInt().toString()
            tv_end_price.text = rangeBar.rightSeekBar.progress.toInt().toString()
        }
    }


    fun observeViewModel() {
        observe(
            viewLifecycleOwner,
            filterViewModel.showLoading,
            ::showloading
        )
        observe(viewLifecycleOwner, filterViewModel.filterData, ::showData)
    }

    private fun showloading(show: Boolean) {
        if (show)
            progress_bar.toVisible()
        else
            progress_bar.toGone()

    }

    private fun showData(filterData: FilterData) {

        rangeBar.setRange(filterData.startPrice.toFloat(), filterData.endPrice.toFloat())
        rangeBar.setProgress(filterData.startPrice.toFloat(), filterData.endPrice.toFloat())

        if (filterViewModel.getLocaleString() == Constants.ARABIC_LOCALE) {
            tv_start_price.text = filterData.endPrice.toFloat().toInt().toString()
            tv_end_price.text = filterData.startPrice.toFloat().toInt().toString()
        }
        else
        {
            tv_start_price.text = filterData.startPrice.toFloat().toInt().toString()
            tv_end_price.text = filterData.endPrice.toFloat().toInt().toString()
        }

        bindCitiesData(filterData.cities)
        bindProvidersList(filterData.providers)
        filterData.categoriesAndSubCategories?.let {
            bindCategoriesData(it)
            it[0]?.let { category ->
                category.subCategories?.let {
                    bindSubCategoriesData(category.subCategories)
                }
            }

        } ?: group_category.toGone()

        filterData.subCategories?.let {
            bindSubCategoriesData(it)
        }


    }

    private fun bindProvidersList(list: List<MedicalProvider>) {
        val adapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.select_dialog_item,
                list.map { it.name }
            )

        et_provider.threshold = 1
        et_provider.setAdapter(adapter)
    }


    private fun bindCategoriesData(categories: List<Category>) {
        var names = categories.map { it.name }.toMutableList()
        names.add(0, getString(R.string.all))
        val adapter = ArrayAdapter(
            requireActivity()!!,
            R.layout.item_spinner, names
        )

        spinner_categories.adapter = adapter
        spinner_categories.onItemSelectedListener = this

        categories[0]?.let { category ->
            category.subCategories?.let { subcategories ->
                subcategories?.let { bindSubCategoriesData(it) }
            }
        }
    }

    private fun bindSubCategoriesData(subcategories: List<SubCategory>) {
        var names = subcategories!!.map { it.name }.toMutableList()
        names.add(0, getString(R.string.all))
        subcategories?.let {
            val adapter = ArrayAdapter(
                requireActivity()!!,
                R.layout.item_spinner, names
            )

            spinner_subcategories.adapter = adapter
            spinner_subcategories.onItemSelectedListener = this
        }
        filterViewModel.selectSubCategoryAt(0)
    }

    private fun bindCitiesData(data: List<City>) {
        progress_bar.toGone()
//        filterViewModel.citiesNamesList = (data.map { it.name }).toMutableList()
//        bindAreasData(data[0].areas!!)

        var names = data!!.map { it.name }.toMutableList()
        names.add(0, getString(R.string.all))

        val adapter = ArrayAdapter(
            requireActivity()!!,
            R.layout.item_spinner, names
        )

        spinner_cities.adapter = adapter
        spinner_cities.onItemSelectedListener = this
    }

//    private fun bindAreasData(data: List<Area>) {
//        filterViewModel.areasNamesList = (data.map { it.name }).toMutableList()
//
//        val adapter = ArrayAdapter(
//            requireActivity()!!,
//            R.layout.item_spinner, filterViewModel.areasNamesList
//        )
//
//        spinner_area.adapter = adapter
//        spinner_area.onItemSelectedListener = this
//    }

//    companion object {
//        fun newInstance(category: Category): FilterDialog {
//            val frag = FilterDialog()
//            val args = Bundle()
//            args.putParcelable(Constants.CATEGORY_ITEM_KEY, category)
//            frag.arguments = args
//            return frag
//        }
//    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.spinner_cities -> {
                filterViewModel.selectCityAt(position - 1)
//                bindAreasData(filterViewModel.selectedCity.value!!.areas!!)
            }
            R.id.spinner_categories -> {
                filterViewModel.selectCategoryAt(position - 1)
                filterViewModel.selectedCategory?.let {
                    it.subCategories?.let { subcategories -> bindSubCategoriesData(subcategories) }
                }
            }
            R.id.spinner_subcategories -> {
                filterViewModel.selectSubCategoryAt(position - 1)
            }
        }
    }
}