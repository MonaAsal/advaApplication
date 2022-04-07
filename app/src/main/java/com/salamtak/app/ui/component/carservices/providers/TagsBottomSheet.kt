package com.salamtak.app.ui.component.carservices.providers

import android.os.Bundle
import com.salamtak.app.R
import com.salamtak.app.data.entities.CarServiceCenter
import com.salamtak.app.ui.common.BaseBottomSheetDialog
import com.salamtak.app.utils.loadCircleImage
import com.salamtak.app.utils.tagsadapter.TagAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.bottom_sheet_tags.*

@AndroidEntryPoint
class TagsBottomSheet(
    val provider: CarServiceCenter
) : BaseBottomSheetDialog() {
    override var getLayoutId: Int
        get() = R.layout.bottom_sheet_tags
        set(value) {}

    lateinit var adapter: TagAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initializeViewModel()
        iv_close.setOnClickListener { dismiss() }

        iv_logo.loadCircleImage(
            provider.logoUrl,
            R.drawable.ic_circle
        )

        tv_name.text = provider.name
        rb_provider.rating = provider.rating?.let { provider.rating.toFloat() } ?: 5f
        tv_branches.text = String.format(
            getString(R.string.num_branch),
            provider.branches
        )
        provider.services?.let {
            rv_tags.apply {
                adapter = TagAdapter(it.toMutableList())
            }
        }
    }

    private fun initializeViewModel() {

    }

}