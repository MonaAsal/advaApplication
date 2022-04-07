package com.salamtak.app.ui.component.finishing.packagedetails


import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.salamtak.app.R
import com.salamtak.app.data.entities.FinishingPackage
import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.ui.component.finishing.providers.FinishingProviderAdapter
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_finishing_packages.*
import kotlinx.android.synthetic.main.toolbar_back.*

@AndroidEntryPoint
class FinishingPackageDetailsActivity : BaseActivity() {
    override val layoutId: Int
        get() = R.layout.activity_finishing_packages

    val viewModel: FinishingPackageViewModel by viewModels()


    lateinit var adapter: FinishingProviderAdapter

    override fun initializeViewModel() {
//        viewModel = viewModelFactory.create(FinishingPackageViewModel::class.java)
    }

    override fun observeViewModel() {

        with(viewModel)
        {
            observe(packageDetailsResponse, ::showPackageData)
            observe(showLoading, ::showLoading)
            observe(showServerError, ::showServerErrorMessage)
        }
    }


    private fun showPackageData(finishingPackage: FinishingPackage) {
        tv_package_name.text = finishingPackage.name.trim()

        finishingPackage.details
        finishingPackage.details?.let {
            if (finishingPackage.details.isNotEmpty()) tv_desc.text = finishingPackage.details
            else
                tv_desc.toGone()
        } ?: tv_desc.toGone()

        tv_price.text =
            String.format(
                getString(R.string.num_egp_m2),
                toDecimalNumberFormat(
                    finishingPackage.pricePerMeter.toDouble()
                )
            )

        finishingPackage.packageCategories?.let {
            if (it.isNotEmpty()) {
                var adapter = FinishingPackagesCategoriesAdapter(it.toMutableList())
                rv_package_categories.adapter = adapter
                addVerticalItemDecoration(rv_package_categories)
            }
        }

    }

    fun showLoading(show: Boolean) {
        progress_bar.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tv_toolbar_title.text = getString(R.string.package_detaisl)
        iv_toolbar_back.setOnClickListener { onBackPressed() }
        LogUtil.LogFirebaseEvent(
            "btn_finishing_provider_package_selected",
            "screen_" + this::class.java.simpleName
        )

        var id = intent.getStringExtra(Constants.KEY_ID) ?: "aa01dd92-e191-4cff-8965-721857ac9045"
        //"9c21734a-6a25-4609-ab2d-8711cccc7d84"
        viewModel.getPackageDetails(id)


//        adapter = ProviderAdapter(this)
//        categoriesAdapter =
//            GenericCategoriesAdapter(
//                this
//            )
//

    }


}