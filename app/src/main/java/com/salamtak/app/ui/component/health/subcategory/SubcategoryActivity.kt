//package com.salamtak.app.ui.component.health.subcategories
//
//import android.os.Bundle
//import com.google.android.material.tabs.TabLayout
//import com.salamtak.app.R
//import com.salamtak.app.data.entities.Category
//import com.salamtak.app.ui.common.BaseActivity
//import com.salamtak.app.ui.common.MyViewPageStateAdapter
//import com.salamtak.app.ui.component.health.categoryproviders.ProvidersFragment
//import com.salamtak.app.ui.component.health.filter.FilterDialog
//import com.salamtak.app.ui.component.health.subcategories.adapters.SubCategoriesAdapter
//import com.salamtak.app.utils.Constants
//import com.salamtak.app.utils.toVisible
//import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.android.synthetic.main.activity_subcategory.*
//
//import kotlinx.android.synthetic.main.toolbar.iv_toolbar_back
//import kotlinx.android.synthetic.main.toolbar.tv_toolbar_title
//import kotlinx.android.synthetic.main.toolbar_back_filter.*
//@AndroidEntryPoint
//class SubcategoryActivity : BaseActivity() {
//    override val layoutId: Int
//        get() = R.layout.activity_subcategory
//
//    lateinit var category: Category
//
//    override fun initializeViewModel() {
//
//    }
//
//    override fun observeViewModel() {
//
//    }
//
//    var selectedTab: Int = 0
//    lateinit var myViewPageStateAdapter: MyViewPageStateAdapter
//    lateinit var adapter: SubCategoriesAdapter
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        initViews()
//    }
//
//    fun initViews() {
//
//        category = intent.getParcelableExtra(Constants.CATEGORY_ITEM_KEY)!!
//        tv_toolbar_title.text = category.name
//        iv_toolbar_back.setOnClickListener { onBackPressed() }
//        setupTabsViewPager()
//        iv_toolbar_filter.toVisible()
//        iv_toolbar_filter.setOnClickListener {
//            openFilterDialog()
//        }
////        if (subcategoriesViewModel.shouldAddFinancialInfo())
////            tv_add_financial_profile.visibility = View.VISIBLE
////        else
////            tv_add_financial_profile.visibility = View.GONE
//
//    }
//
//    private fun openFilterDialog() {
//        val fm = supportFragmentManager
//        val dialog =
//            FilterDialog()
//
//        val bundle = Bundle()
//
////        Log.e("aaa", operation!!.id)
//        bundle.putInt(Constants.CATEGORY_ID_KEY, category.id)
//        dialog.arguments = bundle
//
//        dialog.show(fm, getString(R.string.app_name))
//    }
//
//
//    private fun setupTabsViewPager() {
//        try {
//            myViewPageStateAdapter =
//                MyViewPageStateAdapter(
//                    supportFragmentManager
//                )
//
//            myViewPageStateAdapter.addFragment(
//                SubcategoriesFragment.newInstance(category.id),
//                getString(R.string.operations)
//            )
//
//            myViewPageStateAdapter.addFragment(
//                ProvidersFragment.newInstance(category.id),
//                getString(R.string.medical_network)
//            )
//
//            viewPager.adapter = myViewPageStateAdapter
//            tabs.setupWithViewPager(viewPager, true)
//            tabs.getTabAt(selectedTab)?.select()
//
//            tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//                override fun onTabSelected(tab: TabLayout.Tab) {
//                    viewPager.currentItem = tab.position
//                    selectedTab = tab.position
//                }
//
//                override fun onTabUnselected(tab: TabLayout.Tab) {
//
//                }
//
//                override fun onTabReselected(tab: TabLayout.Tab) {
//                }
//            })
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//    }
//
//}