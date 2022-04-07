package com.salamtak.app.ui.component.paymentmethods

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProviders
import com.salamtak.app.R
import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.PaymentMethodCard
import com.salamtak.app.data.entities.responses.ActionResponse
import com.salamtak.app.data.entities.responses.PaymentMethodsResponse

import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.ui.component.paymentmethods.adapter.CardsAdapter
import com.salamtak.app.utils.observe
import com.salamtak.app.utils.toGone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_doctor.progress_bar
import kotlinx.android.synthetic.main.activity_payment_methods.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject
@AndroidEntryPoint
class PaymentMethodsActivity : BaseActivity() {
  override val layoutId: Int
    get() = R.layout.activity_payment_methods

  lateinit var adapter: CardsAdapter
//  @Inject
//  lateinit var viewModelFactory: ViewModelFactory

  val viewModel: PaymentMethodsViewModel  by viewModels()

  override fun initializeViewModel() {
//    viewModel = ViewModelProviders.of(this, viewModelFactory)
//      .get(PaymentMethodsViewModel::class.java)
  }


  override fun observeViewModel() {
    observe(viewModel.paymentMethodsResponse, ::handleGetPaymentMethodsResponse)
    observe(viewModel.addCardResponse, ::handleAddCard)
    observeToast(viewModel.showToast)
    observeError(viewModel.showError)
  }

  private fun handleAddCard(resource: Resource<ActionResponse>?) {
    when (resource) {
      is Resource.Loading -> showLoadingView(progress_bar)
      is Resource.Success -> {
        progress_bar.toGone()
        navigateToAddCardScreen(resource.data?.data!!, false)
      }

      is Resource.NetworkError -> {
        progress_bar.toGone()
        resource.errorCode?.let {
          var error = viewModel.getToastMessage(it)
          showErrorMessage(error)
        }
      }
      is Resource.DataError -> {
        progress_bar.toGone()
        resource.errorResponse?.let { showServerErrorMessage(it) }
      }
    }
  }

  private fun handleGetPaymentMethodsResponse(resource: Resource<PaymentMethodsResponse>?) {
    when (resource) {
      is Resource.Loading -> showLoadingView(progress_bar)
      is Resource.Success -> {
        progress_bar.toGone()
        bindPaymentMethodsData(resource.data!!.data!!)
      }

      is Resource.NetworkError -> {
        progress_bar.toGone()
        resource.errorCode?.let {
          var error = viewModel.getToastMessage(it)
          showErrorMessage(error)
        }
      }
      is Resource.DataError -> {
        progress_bar.toGone()
        resource.errorResponse?.let { showServerErrorMessage(it) }
      }
    }
  }

  private fun bindPaymentMethodsData(data: List<PaymentMethodCard>) {
    if (!(data.isNullOrEmpty())) {
      adapter = CardsAdapter(viewModel)
//            adapter.setList(data.toMutableList())
      rv_payment_methods.adapter = adapter
      showDataView(true)
    } else {
      showDataView(false)
    }

  }

  private fun showDataView(show: Boolean) {
    v_empty.visibility = if (show) View.GONE else View.VISIBLE
    rv_payment_methods.visibility = if (show) View.VISIBLE else View.GONE

  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    viewModel.getPaymentMethods()
    initViews()
  }

  private fun initViews() {
    tv_toolbar_title.text = getString(R.string.payment_methods)
    iv_toolbar_back.setOnClickListener { onBackPressed() }
    btn_add_payment_method.setOnClickListener {
      viewModel.addPaymentCard()
    }
  }
}
