package com.salamtak.app.ui.component.health

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustEvent
import com.salamtak.app.R
import com.salamtak.app.data.entities.Area
import com.salamtak.app.data.entities.Category
import com.salamtak.app.data.entities.Operation
import com.salamtak.app.data.entities.SalamtakOperation
import com.salamtak.app.data.entities.responses.ErrorResponse
import com.salamtak.app.data.enums.ProviderType
import com.salamtak.app.ui.common.listeners.ActionBarView
import com.salamtak.app.ui.common.listeners.BaseView
import com.salamtak.app.ui.component.financialinfo.step1.FinancialInfoStep1Activity
import com.salamtak.app.ui.component.health.adapter.OperationListenerN
import com.salamtak.app.ui.component.health.bookoperation.BookHealthRequestActivity
import com.salamtak.app.ui.component.health.doctor.DoctorActivity
import com.salamtak.app.ui.component.health.medicalprovider.MedicalProviderActivity
import com.salamtak.app.ui.component.health.search.OperationsSearchActivity
import com.salamtak.app.ui.component.main.MainActivity
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import org.jetbrains.anko.startActivity

@AndroidEntryPoint
abstract class BaseOperationsActivity :
    AppCompatActivity(),
    BaseView,
    ActionBarView,
    OperationListenerN {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(base!!))
    }

//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory

    val operationsViewModel: OperationsViewModelN by viewModels()

    abstract val layoutId: Int

    protected abstract fun initializeViewModel()
    abstract fun observeViewModel()
//    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
//        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
//        App.firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        initializeToolbar()
//        initializeViewModel()
        observeViewModel()
    }

    private fun initializeToolbar() {
//        if (toolbar != null) {
//            setSupportActionBar(toolbar)
//            supportActionBar?.title = ""
//        }
    }

    override fun setUpIconVisibility(visible: Boolean) {
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(visible)
    }

    override fun setTitle(titleKey: String) {
        val actionBar = supportActionBar
        if (actionBar != null) {
            val title = findViewById<TextView>(R.id.txt_toolbar_title)
            title?.text = titleKey
        }
    }

    override fun setSettingsIconVisibility(visibility: Boolean) {
        val actionBar = supportActionBar
        if (actionBar != null) {
            val icon =
                findViewById<androidx.appcompat.widget.AppCompatImageView>(R.id.ic_toolbar_notifications)
            icon?.visibility = if (visibility) View.VISIBLE else View.GONE
        }
    }

    override fun setRefreshVisibility(visibility: Boolean) {
//        val actionBar = supportActionBar
//        if (actionBar != null) {
//            val icon =
//                findViewById<androidx.appcompat.widget.AppCompatImageView>(R.id.ic_toolbar_profile)
//            icon?.visibility = if (visibility) View.VISIBLE else View.GONE
//        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

//    fun setAnimation() {
//        if (Build.VERSION.SDK_INT > 21) {
//            var slide = Slide()
//            slide.setSlideEdge(Gravity.LEFT)
//            slide.setDuration(400)
//            slide.setInterpolator(DecelerateInterpolator())
//            getWindow().exitTransition = slide
//            getWindow().enterTransition = slide
//        }
//    }
//
//
//    fun showLoadingView(progress_bar: ProgressBar) {
//        progress_bar.toVisible()
////        tv_no_data.toGone()
////        rv_categories_list.toGone()
//        //EspressoIdlingResource.increment()
//    }

//    fun showLoadingView(progress_bar: ProgressBar, show: Boolean) {
//        if (show)
//            progress_bar.toVisible()
//        else
//            progress_bar.toGone()
//    }
//
//    fun showMessage(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
//        openSalamtakDialog(getString(R.string.error), message, R.drawable.ic_warning)
//    }
//
//    fun openSalamtakDialog(title: String, text: String, icon: Int) {
//
//        val fm = supportFragmentManager
//        val dialog =
//            SalamtakDialog()
//
//        val bundle = Bundle()
//        bundle.putInt(Constants.KEY_ICON, icon)
//        bundle.putString(Constants.KEY_TITLE, title)
//        bundle.putString(Constants.KEY_TEXT, text)
//
//        dialog.arguments = bundle
//
//        dialog.show(fm, getString(R.string.app_name))
//
//    }
//

//    fun navigateToOperationBookingScreen(operationItem: Operation) {
//
//        startActivity(intentFor<BookOperationActivity>(Constants.OPERATION_ITEM_KEY to operationItem))
//    }
//
//
//    fun hideNotificationBar() {
//        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )
//    }

    fun handleOperationBookingEvent(navigateEvent: Event<SalamtakOperation>) {
        navigateEvent.getContentIfNotHandled()?.let {
//            var operation = it
//            operation.branch = null

            LogUtil.LogFirebaseEvent(
                "btn_operation_select",
                "screen_" + this::getParent::class.java.simpleName,
                "operation",
                it.id
            )

//            navigateToOperationBookingScreen(it)
        }
    }

    fun navigateToOperationBookingScreen(operation: Operation) {
        LogUtil.LogFirebaseEvent(
            "btn_book_now",
            "screen_" + this::class.java.simpleName,
            "operation",
            operation.id
        )

        startActivity(
            intentFor<BookHealthRequestActivity>(
                Constants.KEY_OPERATION_ITEM to operation.id
            )
        )
    }

    fun handleOpenDoctorEvent(navigateEvent: Event<String>) {
        navigateEvent.getContentIfNotHandled()?.let {
            LogUtil.LogFirebaseEvent(
                "btn_operation_doctor",
                "screen_" + this::getParent::class.java.simpleName,
                "doctor",
                it
            )

            navigateToDoctorScreen(it)
        }
    }

    fun handleOpenReviewsEvent(event: Event<Operation>) {
        event.getContentIfNotHandled()?.let {
            LogUtil.LogFirebaseEvent(
                "btn_operation_review",
                "screen_" + this::getParent::class.java.simpleName,
                "operation",
                it.id
            )
            navigateToReviewsScreen(Constants.KEY_OPERATION_ITEM, it.id)
        }
    }

    fun handleOpenMedicalProviderEvent(navigateEvent: Event<String>) {
        navigateEvent.getContentIfNotHandled()?.let {
            LogUtil.LogFirebaseEvent(
                "btn_operation_provider",
                "screen_" + this::getParent::class.java.simpleName,
                "provider",
                it
            )
            navigateToMedicalProviderScreen(it)
        }
    }


//    fun navigateToFinancialInfoScreen() {
//        startActivity<FinancialInfoActivity>()
//        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
//    }

    fun navigateToDoctorScreen(doctorId: String) {
//        if (doctor != null) {
        startActivity(
            intentFor<DoctorActivity>(
                Constants.DOCTOR_ITEM_KEY to doctorId
//                Constants.DOCTOR_NAME to doctorName
            )
        )
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
//        }
    }

    fun navigateToMedicalProviderScreen(medicalProviderId: String) {

        startActivity(intentFor<MedicalProviderActivity>(Constants.MEDICAL_PROVIDER_ITEM_KEY to medicalProviderId))
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)

    }


//    fun handleShowMessage(event: Event<Any>) {
//        showMessage(event.getContentIfNotHandled().toString())
//    }
//
//    fun openSearchScreen(query: String) {
//        startActivity(intentFor<OperationsSearchActivity>(Constants.QUERY_KEY to query))
//    }

    fun openSearchScreen(query: String, category: Category?, area: Area?) {
        startActivity(
            intentFor<OperationsSearchActivity>(
                Constants.QUERY_KEY to query,
                Constants.CATEGORY_ID_KEY to category,
                Constants.AREA_KEY to area
            )
        )
    }

    fun navigateToReviewsScreen(key: String, id: String) {
//        startActivity(intentFor<ReviewsActivity>(key to id))
    }

    fun openSearchScreen(query: String, category: Category) {
        startActivity(
            intentFor<OperationsSearchActivity>(
                Constants.QUERY_KEY to query,
                Constants.CATEGORY_ID_KEY to category
            )
        )
    }

    fun openSearchScreen(query: String) {
        startActivity(
            intentFor<OperationsSearchActivity>(
                Constants.QUERY_KEY to query
            )
        )
    }


    fun openMap(operation: SalamtakOperation) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("geo:<lat>,<long>?q=<${operation.branch?.lat}>,<${operation.branch?.lng}>(${operation.title})")
        )
        startActivity(intent)

    }

    fun showLoadingView(progress_bar: ProgressBar, show: Boolean) {
        if (show)
            progress_bar.toVisible()
        else
            progress_bar.toGone()
    }

    fun showLoadingView(progress_bar: ProgressBar) {
        if (progress_bar.visibility == View.VISIBLE)
            progress_bar.toGone()
        else
            progress_bar.toVisible()
    }

    fun observeToast(event: LiveData<Event<Any>>) {
        if (event.value != null)
            showMessage(event.value.toString())
    }

    fun observeError(event: LiveData<Event<String>>) {

        if (event.value != null)
            showErrorMessage(event.value.toString())
    }

    fun showMessage(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
//        getString(R.string.alert)
        openSalamtakDialog(supportFragmentManager, "", message, R.drawable.ic_warning)
    }

    fun showErrorMessage(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        openSalamtakDialog(
            supportFragmentManager,
            getString(R.string.error),
            message,
            R.drawable.ic_server_error
        )
    }

    fun showServerErrorMessage(errorResponse: ErrorResponse) {
        if (errorResponse?.errors != null && errorResponse?.errors?.isNotEmpty()!!)
            openSalamtakDialog(
                supportFragmentManager,
                getString(R.string.error),
                errorResponse?.errors?.get(0)?.error!!,
                R.drawable.ic_server_error
            )
    }


    fun LogAdjustEvent(token: String) {
        val adjustEvent = AdjustEvent(token)
        Adjust.trackEvent(adjustEvent)
    }

    fun LogAdjustEvent(eventId: String, key: String, value: String) {
        val adjustEvent = AdjustEvent(eventId)
        adjustEvent.addCallbackParameter(key, value)
        Adjust.trackEvent(adjustEvent)
    }

    fun navigateToFinancialInfoScreen() {
//        startActivity<FinancialInfoTypesActivity>()
//        startActivity<FinancialInfoActivity>()
        startActivity<FinancialInfoStep1Activity>()
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
    }

    override fun onFavouriteClicked(operation: Operation, position: Int) {
        operationsViewModel.addToFavourite(operation.id)
    }

    override fun onOwnerClicked(operation: Operation) {
        ///hnaaaaaaaaa
        if (operation.owner!!.type == ProviderType.Doctor.typeId)
            startActivity(
                intentFor<DoctorActivity>(
                    Constants.KEY_ID to operation.owner!!.id
                )
            )
        else
            startActivity(
                intentFor<MedicalProviderActivity>(
                    Constants.KEY_ID to operation.owner!!.id
                )
            )
    }

    override fun onDetailsClicked(operation: Operation) {
        navigateToOperationBookingScreen(operation)

    }

    fun getRemaining(totalRecords: Int, page: Int, locale: String = "en"): String {
        return if (page * Constants.PAGE_SIZE <= totalRecords) {
            String.format(
                getString(R.string.num_more_records),
                (totalRecords - (page * Constants.PAGE_SIZE)).toString()
            )
        } else
            getString(R.string.no_more_records)
    }

    fun navigateToMainScreen() {
        startActivity(intentFor<MainActivity>().newTask().clearTask())
        finish()
    }
}