package com.salamtak.app.ui.component.profile

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.salamtak.app.R
import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.utils.Constants
import com.salamtak.app.utils.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_choose_language.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

@AndroidEntryPoint
class ChooseLanguageActivity : BaseActivity() {
    var locale = Constants.ENGLISH_LOCALE
    override val layoutId: Int
        get() = R.layout.activity_choose_language

//    @Inject
//    lateinit
//    val  viewModelFactory: ViewModelFactory  by viewModels()

    //    @Inject
//    lateinit
    val viewModel: LanguageViewModel by viewModels()

    override fun initializeViewModel() {
//        viewModel = ViewModelProviders.of(this, viewModelFactory)
//            .get(LanguageViewModel::class.java)
    }


    override fun observeViewModel() {
        observe(viewModel.restartActivity, ::handleRestartActivity)
    }

    private fun handleRestartActivity(b: Boolean?) {
//        val intent = intent
//        finish()
//        startActivity(intent)

        //  logLanguageToAdjust()
        navigateToMainScreen()
    }


    private fun updateSelectedUI(currentLocale: String) {
        if (currentLocale == Constants.ARABIC_LOCALE) {
            iv_arabic_selected.visibility = View.VISIBLE
            iv_english_selected.visibility = View.GONE
        } else {
            iv_arabic_selected.visibility = View.GONE
            iv_english_selected.visibility = View.VISIBLE
        }
//        tv_arabic.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_correct, 0)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        iv_toolbar_back.setOnClickListener {
            onBackPressed()
        }

        tv_toolbar_title.text = getString(R.string.choose_language)

        tv_arabic.setOnClickListener {
            changeLanguageTo(Constants.ARABIC_LOCALE)

        }
        tv_english.setOnClickListener {
            changeLanguageTo(Constants.ENGLISH_LOCALE)

        }
        var currentLocale = viewModel.getLocale()
        updateSelectedUI(currentLocale)

    }

    private fun changeLanguageTo(locale: String) {
        this.locale = locale
        viewModel.changeAppLanguage(locale, this)

//        var otherLang = getString(R.string.english)
//        if (locale == Constants.ARABIC_LOCALE)
//            otherLang = getString(R.string.arabic)
//        openSalamtakDialog(
//            supportFragmentManager,
//            getString(R.string.confirm_change_language),
//            String.format(getString(R.string.confirm_change_lang), otherLang),
//            R.drawable.ic_warning,
//            ::yesClicked,
//            ::noClicked
//        )
    }

    private fun noClicked() {
    }

    private fun yesClicked() {
//        Toast.makeText(
//            this, "you choose yes action for alertbox",
//            Toast.LENGTH_SHORT
//        ).show()
        viewModel.changeAppLanguage(locale, this)
    }


}
