package com.salamtak.app.ui.common

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustEvent
import com.salamtak.app.R
import com.salamtak.app.data.entities.Operation
import com.salamtak.app.data.entities.responses.ErrorResponse
import com.salamtak.app.ui.common.listeners.BaseView
import com.salamtak.app.ui.component.financialinfo.step1.FinancialInfoStep1Activity
import com.salamtak.app.ui.component.health.adapter.OperationListenerN
import com.salamtak.app.ui.component.health.search.OperationsSearchActivity
import com.salamtak.app.ui.component.verifynumber.VerifyNumberActivity
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import java.util.*

/**
 * Created by Radwa Elsahn on 3/21/2020
 */

@AndroidEntryPoint
abstract class BaseFragment : Fragment(), BaseView, OperationListenerN {

    protected var baseViewModel: BaseViewModel? = null

    abstract val layoutId: Int
    abstract fun observeViewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModel()

//        initializeDagger()
//        initializePresenter()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    fun setTitle(title: String) {
        val actionBar = (activity as BaseActivity).supportActionBar
        if (actionBar != null) {
            val titleTextView = activity?.findViewById<TextView>(R.id.txt_toolbar_title)
            if (title.isNotEmpty()) {
                titleTextView?.text = title
            }
        }
    }

    fun showServerErrorMessage(errorResponse: ErrorResponse) {
        if (errorResponse?.errors != null && errorResponse?.errors?.isNotEmpty()!!)
            childFragmentManager?.let {
                openSalamtakDialog(
                    childFragmentManager,
                    getString(R.string.error),
                    errorResponse?.errors?.get(0)?.error!!,
                    R.drawable.ic_server_error
                )
            }

    }

    fun LogAdjustEvent(token: String) {
        val adjustEvent = AdjustEvent(token)
        Adjust.trackEvent(adjustEvent)
    }

    fun showErrorMessage(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        activity?.let {
            openSalamtakDialog(
                it?.supportFragmentManager,
                getString(R.string.error),
                message,
                R.drawable.ic_server_error
            )
        }
    }

    fun showMessage(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
//        getString(R.string.alert)
        activity?.supportFragmentManager?.let {
            openSalamtakDialog(
                it,
                "",
                message,
                R.drawable.ic_warning
            )
        }
    }

    fun observeToast(event: LiveData<Event<Any>>) {
        if (event.value != null)
            showMessage(event.value.toString())
    }

    fun showLoadingView(progress_bar: ProgressBar, show: Boolean) {
        if (show)
            progress_bar.toVisible()
        else
            progress_bar.toGone()
    }

    fun hideNotificationBar(activity: Activity) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE)
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
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

    fun callNumber(number: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:" + number)
        startActivity(intent)
    }

    override fun onFavouriteClicked(operation: Operation, position: Int) {

    }

    override fun onOwnerClicked(operation: Operation) {
    }

    override fun onDetailsClicked(operation: Operation) {
    }

    fun preventUiPopingAboveKeyBoard() {
        Objects.requireNonNull(requireActivity()).getWindow()
            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }

    fun showLoadingView(progress_bar: ProgressBar) {
        progress_bar.toVisible()
    }

    fun onBackPressed() {
        try {
            findNavController().popBackStack()
        } catch (e: Exception) {
        }
    }

    fun observeError(event: LiveData<Event<String>>) {
        if (event.value != null)
            showErrorMessage(event.value.toString())
    }

    fun navigateToFinancialInfoScreen() {
        activity?.startActivity<FinancialInfoStep1Activity>()
        activity?.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
    }

    fun openSearchScreen(query: String) {
        startActivity(
            activity?.intentFor<OperationsSearchActivity>(
                Constants.QUERY_KEY to query
            )
        )
    }

    fun navigateToVerifyNumberScreen(phone: String) {
        activity?.startActivity(
            activity?.intentFor<VerifyNumberActivity>(
                Constants.KEY_PHONE to phone,
            )
        )
        activity?.finish()
        activity?.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
    }

    fun navigateToHome() {
        findNavController().navigate(R.id.homeFragment)
    }

    fun navigateToServices() {
        findNavController().navigate(R.id.servicesFragment)
    }

    fun clearBacStack() {
        findNavController().popBackStack()
    }

    fun navigateToRequests() {
        findNavController().navigate(R.id.requestsFragment)
    }

    fun navigateToBooking() {
        findNavController().navigate(R.id.bookingFragment)
    }

    fun navigateToProfile() {
        findNavController().navigate(R.id.profileFragment)
    }

    fun navigateToEditProfile() {
        findNavController().navigate(R.id.editProfileFragment)
    }

    fun navigateToWishList() {
        findNavController().navigate(R.id.wishListFragment)
    }

    fun navigateToHealth() {
        findNavController().navigate(R.id.healthFragment)
    }

    fun navigateToSubcategoryHealthFragment(bundle: Bundle) {
        try {
            findNavController().navigate(R.id.subcategoryHealthFragment, bundle)
        } catch (e: Exception) {
        }
    }

    fun navigateToMedicalNetworkFragment() {
        findNavController().navigate(R.id.medicalNetworkFragment)
    }

    fun navigateToOtherOperations() {
        findNavController().navigate(R.id.otherOperationFragment)
    }

    fun navigateToOperation(bundle: Bundle) {
        findNavController().navigate(R.id.operationFragment, bundle)
    }

    fun navigateToDoctor(bundle: Bundle) {
        findNavController().navigate(R.id.doctorFragment, bundle)
    }

    fun navigateToMedicalProvider(bundle: Bundle) {
        findNavController().navigate(R.id.medicalProviderFragment, bundle)
    }

    fun navigateToMoreProvider(bundle: Bundle) {
        findNavController().navigate(R.id.moreProvidersFragment, bundle)
    }

    fun navigateToCarServicesProviders() {
        findNavController().navigate(R.id.carServicesProvidersFragment)
    }


    fun navigateToCarServicesViewllProviders(bundle: Bundle) {
        findNavController().navigate(R.id.carServicesProvidersViewAllfragment, bundle)
    }

    fun navigateToCarProviderDetailsRequest(bundle: Bundle) {
        findNavController().navigate(R.id.CarProviderDetailsRequestFragment, bundle)
    }

    fun navigateToEducationForm() {
        findNavController().navigate(R.id.educationFormFragment)
    }
    fun navigateToEducationCategory() {
        findNavController().navigate(R.id.EducationCategoriesFragment)
    }

    fun navigateToWeddingForm() {
        findNavController().navigate(R.id.weddingRequestFragment)
    }

    fun navigateToInsuranceForm(bundle: Bundle) {
        findNavController().navigate(R.id.insuranceFragment, bundle)

    }

    fun navigateToFinishing(bundle: Bundle) {
        findNavController().navigate(R.id.finishingProvidersFragment, bundle)
    }

    fun navigateToFinishingProviderRequestFragment(bundle: Bundle) {
        findNavController().navigate(R.id.finishingProviderRequestFragment, bundle)
    }

    fun navigateToComingSoon(bundle: Bundle) {
        findNavController().navigate(R.id.comingSoonFragment, bundle)
    }

    fun navigateToHelp() {
        findNavController().navigate(R.id.HelpFragment)
    }

    fun navigateToAboutUs() {
        findNavController().navigate(R.id.AboutUsFragment)
    }

    fun navigateToViewAll(bundle: Bundle) {
        findNavController().navigate(R.id.SubCategoryViewAllFragment, bundle)
    }

    fun navigateToAccountSettings() {
        findNavController().navigate(R.id.AccountSettings)
    }

    fun navigateToFinishingCategoriesFragment() {
        findNavController().navigate(R.id.finishingCategoriesFragment)
    }
}
