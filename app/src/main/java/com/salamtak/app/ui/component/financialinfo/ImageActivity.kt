package com.salamtak.app.ui.component.financialinfo

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.viewModels
import com.salamtak.app.BuildConfig
import com.salamtak.app.R
import com.salamtak.app.data.entities.Attachment
import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_image.*
import okhttp3.ResponseBody
import java.io.File

@AndroidEntryPoint
class ImageActivity : BaseActivity() {

    //    @Inject
//    lateinit
    val financialViewModel: FinancialViewModel by viewModels()
//
//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory

    override fun initializeViewModel() {
//        financialViewModel = viewModelFactory.create(FinancialViewModel::class.java)
    }

    override fun observeViewModel() {
        observe(financialViewModel.fileResponseBody, ::showPdfFile)
    }

    private fun showPdfFile(responseBody: ResponseBody) {
        val file = writeResponseBodyToDisk(responseBody)
        pdfView.fromFile(file).load()
    }

    override val layoutId: Int
        get() = R.layout.activity_image

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        iv_close.setOnClickListener { onBackPressed() }
        if (intent.extras!!.containsKey(Constants.KEY_ID)) {
            var isIdFront = intent.getBooleanExtra(Constants.KEY_IMAGE, true)
            var profileId = intent.getStringExtra(Constants.KEY_ID)
            var guarantor = intent.getBooleanExtra(Constants.KEY_GUARANTOR, false)
            var url =
                BuildConfig.BASE_URL + BuildConfig.API_URL + "FinanacialProfiles/get-nationalID-attachment?IsFaceImage=${isIdFront}&FinancialProfileId=${profileId}"
            if (guarantor)
                url =
                    BuildConfig.BASE_URL + BuildConfig.API_URL + "FinancialProfilesV2/guarantor-image?IsFaceImage=${isIdFront}&FinancialProfileId=${profileId}"
            iv_image.loadImageWithHeaderRefresh(url)
        } else {
            var attachment = intent.getParcelableExtra<Attachment>(Constants.KEY_IMAGE)!!
            if (attachment.type == 1) {
                pdfView.toGone()
                iv_image.toVisible()
                if (attachment!!.thumbnailUrl!!.startsWith("uploads/images")) {
                    var url =
                        BuildConfig.BASE_URL + BuildConfig.API_URL + "FinanacialProfiles/get-category-attachment?FileId=" + attachment.id
                    iv_image.loadImageWithHeader(url)

                } else {
                    val imgFile = File(attachment!!.thumbnailUrl)

                    if (imgFile.exists()) {
                        try {
                            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                            iv_image.setImageBitmap(myBitmap)
                        } catch (e: OutOfMemoryError) {

                        }
                    }
                }
            } else if (attachment!!.type == 2) {
                pdfView.toVisible()
                iv_image.toGone()
                var url =
                    BuildConfig.BASE_URL + BuildConfig.API_URL + "FinanacialProfiles/get-category-attachment?FileId=" + attachment.id
                financialViewModel.downloadFileWithDynamicUrlSync(url)
            }
        }
    }
}