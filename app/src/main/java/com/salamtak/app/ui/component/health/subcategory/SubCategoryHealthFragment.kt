package com.salamtak.app.ui.component.health.subcategory

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.salamtak.app.R
import com.salamtak.app.data.entities.Category
import com.salamtak.app.ui.common.BaseFragment
import com.salamtak.app.ui.common.MyViewPageStateAdapter
import com.salamtak.app.ui.common.ViewPagerAdapter
import com.salamtak.app.ui.component.health.categoryproviders.ProvidersFragment
import com.salamtak.app.ui.component.health.filter.FilterDialog
import com.salamtak.app.ui.component.health.subcategory.adapters.SubCategoriesAdapter
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.LogUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_subcategory_health.*
import kotlinx.android.synthetic.main.toolbar_back.*
import kotlinx.android.synthetic.main.toolbar_back.iv_toolbar_back
import kotlinx.android.synthetic.main.toolbar_back.tv_toolbar_title
import kotlinx.android.synthetic.main.toolbar_back_filter.*


@AndroidEntryPoint
class SubCategoryHealthFragment : BaseFragment() {
    override val layoutId: Int
        get() = R.layout.fragment_subcategory_health

    var selectedTab: Int = 0
    lateinit var category: Category
    lateinit var myViewPageStateAdapter: MyViewPageStateAdapter
    lateinit var adapter: SubCategoriesAdapter

    override fun observeViewModel() {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
    }

    override fun onResume() {
        preventUiPopingAboveKeyBoard()
        super.onResume()
        Log.d("stateparent","onResume")

    }

    fun initViews() {
        category = arguments?.getParcelable("${Constants.CATEGORY_ITEM_KEY}")!!
      //  category = intent.getParcelableExtra(Constants.CATEGORY_ITEM_KEY)!!
        tv_toolbar_title.text = category.name
        iv_toolbar_back.setOnClickListener { onBackPressed() }
       // setupTabsViewPager()
        setupTabsWithViewPager2()
        iv_toolbar_filter.setOnClickListener {
            openFilterDialog()
        }
        //iv_toolbar_filter.toVisible()
//        iv_toolbar_filter.setOnClickListener {
//            openFilterDialog()
//        }
        et_search!!.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)

                if (et_search!!.text.toString().isNotEmpty()) {
                    LogUtil.LogFirebaseEvent(
                        "btn_search",
                        "screen_" + this::class.java.simpleName,
                        "search_key",
                        et_search!!.text.toString()
                    )

                    openSearchScreen(et_search.text.toString())
                }

                return@OnEditorActionListener true
            }
            false
        })

    }

    private fun openFilterDialog() {
        val fm = activity?.supportFragmentManager
        val dialog = FilterDialog()
        val bundle = Bundle()
        bundle.putInt(Constants.CATEGORY_ID_KEY, category.id)
        dialog.arguments = bundle

        fm?.let { dialog.show(it, getString(R.string.app_name)) }
    }

    fun setupTabsWithViewPager2(){

        category?.id?.let {

            var fragmentList = arrayListOf<Fragment>(
                SubcategoriesOperationsFragment.newInstance(category.id),
                ProvidersFragment.newInstance(category.id),
            ).toMutableList()

            var viewPagerAdapter2 = ViewPagerAdapter(requireActivity(),fragmentList)

            viewpager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            viewpager2.isUserInputEnabled = false;

            viewpager2.adapter = viewPagerAdapter2
            //setup viewPager2 with tabs
            TabLayoutMediator(tabs, viewpager2) { tab, position ->
                when(position){
                    0 ->  tab.text = getString(R.string.operations)
                    1 ->   tab.text =  getString(R.string.providers)
                }

            }.attach()

        }
    }

//    private fun setupTabsViewPager() {
//        try {
//
//
//            myViewPageStateAdapter = childFragmentManager?.let {
//                    MyViewPageStateAdapter(it)
//            }
//            //operations
//            myViewPageStateAdapter.addFragment(
//                SubcategoriesFragment.newInstance(category.id),
//                getString(R.string.operations)
//            )
//            //providers
//            myViewPageStateAdapter.addFragment(
//                ProvidersFragment.newInstance(category.id),
//                getString(R.string.providers)
//            )
//
//
//            viewPagerr.adapter = myViewPageStateAdapter
//            tabs.setupWithViewPager(viewPagerr, true)
//            tabs.getTabAt(selectedTab)?.select()
//
//            tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//                override fun onTabSelected(tab: TabLayout.Tab) {
//                    viewPagerr.currentItem = tab.position
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

            // }

   }

