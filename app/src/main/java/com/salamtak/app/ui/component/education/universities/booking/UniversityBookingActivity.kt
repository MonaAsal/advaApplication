package com.salamtak.app.ui.component.education.universities.booking


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.salamtak.app.R
import com.salamtak.app.data.entities.*
import com.salamtak.app.data.entities.responses.BaseResponse

import com.salamtak.app.ui.common.BaseActivity
import com.salamtak.app.ui.common.listeners.RecyclerItemPositionListener
import com.salamtak.app.ui.component.education.custom.EducationFormState
import com.salamtak.app.ui.component.health.bookoperation.adapter.InstallmentTypesAdapter
import com.salamtak.app.ui.component.successrequest.RequestSubmittedSuccessfullyActivity
import com.salamtak.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_university_booking.btn_another_student
import kotlinx.android.synthetic.main.activity_university_booking.btn_submit
import kotlinx.android.synthetic.main.activity_university_booking.cb_bus_fees
import kotlinx.android.synthetic.main.activity_university_booking.tv_down_payment
import kotlinx.android.synthetic.main.activity_university_booking.et_student1_name
import kotlinx.android.synthetic.main.activity_university_booking.et_student2_name
import kotlinx.android.synthetic.main.activity_university_booking.et_student3_name
import kotlinx.android.synthetic.main.activity_university_booking.group_student2
import kotlinx.android.synthetic.main.activity_university_booking.group_student3
import kotlinx.android.synthetic.main.activity_university_booking.iv_back
import kotlinx.android.synthetic.main.activity_university_booking.iv_remove_student_2
import kotlinx.android.synthetic.main.activity_university_booking.iv_remove_student_3
import kotlinx.android.synthetic.main.activity_university_booking.iv_school_image
import kotlinx.android.synthetic.main.activity_university_booking.iv_school_logo
import kotlinx.android.synthetic.main.activity_university_booking.main_layout
import kotlinx.android.synthetic.main.activity_university_booking.progress_bar
import kotlinx.android.synthetic.main.activity_university_booking.rb_school
import kotlinx.android.synthetic.main.activity_university_booking.rv_installment_types_list
import kotlinx.android.synthetic.main.activity_university_booking.spinner_grade_student1
import kotlinx.android.synthetic.main.activity_university_booking.spinner_grade_student2
import kotlinx.android.synthetic.main.activity_university_booking.spinner_grade_student3
import kotlinx.android.synthetic.main.activity_university_booking.spinner_location
import kotlinx.android.synthetic.main.activity_university_booking.tv_bus_fees
import kotlinx.android.synthetic.main.activity_university_booking.tv_more
import kotlinx.android.synthetic.main.activity_university_booking.tv_reviews
import kotlinx.android.synthetic.main.activity_university_booking.tv_school_details
import kotlinx.android.synthetic.main.activity_university_booking.tv_school_name
import kotlinx.android.synthetic.main.activity_university_booking.tv_student2
import kotlinx.android.synthetic.main.activity_university_booking.tv_student3
import org.jetbrains.anko.intentFor
import javax.inject.Inject
@AndroidEntryPoint
class UniversityBookingActivity : BaseActivity(), AdapterView.OnItemSelectedListener,
    RecyclerItemPositionListener<InstallmentType> {
    override val layoutId: Int
        get() = R.layout.activity_university_booking

    lateinit var installmentTypesAdapter: InstallmentTypesAdapter

    val viewModel: UniversityBookingViewModel by viewModels()

//    @Inject
//    lateinit var viewModelFactory: ViewModelFactory

    override fun initializeViewModel() {
//        viewModel = viewModelFactory.create(UniversityBookingViewModel::class.java)
    }

    override fun observeViewModel() {

        with(viewModel)
        {
            observe(showLoading, ::showLoading)
            observe(customOperationFromState, ::handleFormState)
            observe(showServerError, ::showServerErrorMessage)
            observe(collageDetailsResponse, ::showSchoolDetails)
            observe(installmentTypes, ::bindInstallmentTypes)
            observe(downPayment, ::showDownPayment)
            observe(customOperationResponse, ::handleBookingResponse)
            observe(busFees, ::showBusFees)
        }
    }

    private fun showBusFees(busFees: Int) {
        tv_bus_fees.text = busFees.toString()
    }

    private fun showDownPayment(downpayment: Int) {
        Log.e("showDownPayment", downpayment.toString())
        tv_down_payment.setText(downpayment.toString())
        viewModel.installmentTypes.value?.let {
            if (viewModel.selectedGradeStudent1 != null || viewModel.selectedGradeStudent2 != null || viewModel.selectedGradeStudent3 != null) {
                resetInstallmentTypes()
            }
        }
    }

    private fun resetInstallmentTypes() {
        viewModel.installmentTypes.value?.let { types ->
            if (!(types.isNullOrEmpty())) {
                types.map { type ->
                    type.monthlyAmount =
                        viewModel.getInstallmentPerMonth(type)
                            .toDouble()
                }

                installmentTypesAdapter.resetList(types.toMutableList())
            }
        }
    }

    private fun bindInstallmentTypes(types: List<InstallmentType>) {
        if (!(types.isNullOrEmpty())) {
            types.map { type ->
                type.monthlyAmount =
                    viewModel.getInstallmentPerMonth(type).toDouble()
            }

            installmentTypesAdapter.setList(types.toMutableList())
            installmentTypesAdapter.setSelectedItem(0, types[0])
            rv_installment_types_list.adapter = installmentTypesAdapter

            addVerticalItemDecoration(rv_installment_types_list)
//            val linearLayoutManager =
//                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//            rv_installment_types_list.layoutManager = linearLayoutManager
//            val dividerItemDecoration = DividerItemDecoration(
//                rv_installment_types_list.context,
//                linearLayoutManager.orientation
//            )
//
//            dividerItemDecoration.setDrawable(
//                ContextCompat.getDrawable(
//                    this,
//                    R.drawable.vertical_divider
//                )!!
//            )
//
//            rv_installment_types_list.addItemDecoration(dividerItemDecoration)

        }

    }

    private fun showSchoolDetails(schoolDetails: Collage) {
        bindBranches(schoolDetails.branches!!)

        iv_school_image.changeSizeAspectRatio(0)
        iv_school_image.loadImage(schoolDetails.imageUrl)
        iv_school_logo.loadCircleImage(schoolDetails.logoUrl, R.drawable.ic_circle)
        tv_school_name.text = schoolDetails.name
        tv_reviews.text = String.format(getString(R.string.num_reviews), 10)
//        tv_reviews.text = String.format(
//            viewModel.getLocale(),
//            getString(R.string.num_reviews),
//            schoolDetails.reviewsCount
//        )

        tv_school_details.text = schoolDetails.about
        rb_school.rating = schoolDetails.rating.toFloat()

        if (tv_school_details.lineCount > 2) {
            tv_school_details.maxLines = 2
            tv_more.toVisible()
        } else {
            tv_more.toGone()
        }

        tv_more.setOnClickListener {
            if (tv_school_details.maxLines > 2) {
                tv_school_details.maxLines = 2
                tv_more.text = getString(R.string.see_more)
            } else {
                tv_school_details.maxLines = 10
                tv_more.text = getString(R.string.view_less)

            }
        }

        btn_submit.setOnClickListener {
            viewModel.createUniversityRequest(
                tv_down_payment.text.toString(),
                if (cb_bus_fees.isChecked) tv_bus_fees.text.toString() else "0",
                et_student1_name.text.toString(),
                et_student2_name.text.toString(),
                et_student3_name.text.toString()
            )
        }
    }

    private fun bindBranches(data: List<CollageBranch>) {

        var branches = (data.map { it.name }).toMutableList()

        val adapter = ArrayAdapter(
            this,
            R.layout.item_spinner, branches
        )

        spinner_location.adapter = adapter
        spinner_location.onItemSelectedListener = this
    }

    private fun bindGrades(grades: List<Semester>) {
        var list =
            (grades.map { it.name }).toMutableList()
        list.add(0, getString(R.string.choose))

        val adapter = ArrayAdapter(
            this,
            R.layout.item_spinner, list
        )

        spinner_grade_student1.adapter = adapter
        spinner_grade_student2.adapter = adapter
        spinner_grade_student3.adapter = adapter
        spinner_grade_student1.onItemSelectedListener = this
        spinner_grade_student2.onItemSelectedListener = this
        spinner_grade_student3.onItemSelectedListener = this
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.spinner_location -> {
                viewModel.selectBranch(position, cb_bus_fees.isChecked)

                viewModel.selectedBranch?.let {
                    bindGrades(it.semesters)
                }

            }
            R.id.spinner_grade_student1 -> {
                viewModel.selectGrade(
                    position,
                    1
                )
            }
            R.id.spinner_grade_student2 -> {
                viewModel.selectGrade(position, 2)
            }
            R.id.spinner_grade_student3 -> {
                viewModel.selectGrade(position, 3)
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }


    fun showLoading(show: Boolean) {
        if (show) {
            progress_bar.toVisible()
            btn_submit.isEnabled = false
        } else {
            progress_bar.toGone()
            btn_submit.isEnabled = true
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.collageId = intent.getStringExtra(Constants.KEY_ID)!!
        viewModel.universityId = intent.getStringExtra(Constants.KEY_UNIVERSITY_ID)!!
        viewModel.categoryId = intent.getIntExtra(Constants.KEY_TYPE, 0)!!


//        if (viewModel.categoryId == EducationTypes.Institute.value)
//            tv_about_school_lbl.text = getString(R.string.about_the_institute)

        viewModel.getCollageDetailsById()
        viewModel.getInstallmentsLookup()

        iv_remove_student_3.setOnClickListener {
            group_student3.toGone()
            viewModel.student3Added = false
            spinner_grade_student3.setSelection(0)
            viewModel.clearSelectedGrade(3)
            viewModel.recalculate()
            btn_another_student!!.toVisible()

        }

        iv_remove_student_2.setOnClickListener {
            group_student2.toGone()
            viewModel.student2Added = false
            viewModel.clearSelectedGrade(2)
            spinner_grade_student2.setSelection(0)
            viewModel.recalculate()
            btn_another_student!!.toVisible()
        }

        btn_another_student.setOnClickListener {
            if (group_student2.visibility == View.VISIBLE) {
                viewModel.student3Added = true
                group_student3.toVisible()
                btn_another_student.toGone()
            } else {
                viewModel.student2Added = true
                group_student2.toVisible()
            }
            viewModel.recalculate()
        }

        cb_bus_fees.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.busSubscribed = isChecked
            //            viewModel.calculateTotalPrice()
            viewModel.recalculate()
        }

        iv_back.setOnClickListener { onBackPressed() }
        installmentTypesAdapter = InstallmentTypesAdapter(this, viewModel.getLocale())

        //forDevelopers()

//        tv_bus_fees.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable) {
//                if (s.isNotEmpty())
//                    viewModel.calculateTotalPrice(cb_bus_fees.isChecked)
//            }
//
//            override fun beforeTextChanged(
//                s: CharSequence, start: Int,
//                count: Int, after: Int
//            ) {
//            }
//
//            override fun onTextChanged(
//                s: CharSequence, start: Int,
//                before: Int, count: Int
//            ) {
//            }
//        })

    }

    fun forDevelopers() {
        et_student1_name.setText("radwa")
//        et_bus_fees.setText("1000")
    }

    override fun onItemSelected(item: InstallmentType, position: Int) {
        installmentTypesAdapter.setSelectedItem(position, item)
        viewModel.selectInstallmentType(item)
    }

    private fun handleBookingResponse(response: BaseResponse) {
        btn_submit.isEnabled = true
        if (response.status!!) {
//            LogAdjustEvent("2pnh1n")
            response.let {
//                        showMessage(response.data.data, ::yesClicked)
                startActivity(intentFor<RequestSubmittedSuccessfullyActivity>(Constants.NEED_FINANCIAL_INFO to viewModel.needFinancialInfo()))
            }
        } else {
            showMessage(response.message!!)
            //viewModelBooking.checkNavigation()oncli
        }
    }

    private fun handleFormState(customOperationFormState: EducationFormState) {
        btn_submit.isEnabled = true

        if (customOperationFormState?.gradeError != null) {
            spinner_grade_student1.setSpinnerError(getString(customOperationFormState.gradeError))
            spinner_grade_student1.shakeView()
            main_layout.scrollTo(spinner_grade_student1.x.toInt(), spinner_grade_student1.y.toInt())
        }
//        else
//            spinner_grade_student1.error = null


        if (customOperationFormState?.nameError != null) {
            et_student1_name.error = getString(customOperationFormState.nameError)
            et_student1_name.shakeView()
            main_layout.scrollTo(et_student1_name.x.toInt(), et_student1_name.y.toInt())
        } else
            et_student1_name.error = null

        if (customOperationFormState?.gradeError2 != null) {
            spinner_grade_student2.setSpinnerError(getString(customOperationFormState.gradeError2))
            spinner_grade_student2.shakeView()
            main_layout.scrollTo(spinner_grade_student2.x.toInt(), spinner_grade_student2.y.toInt())
        } else
            tv_student2.error = null

        if (customOperationFormState?.nameError2 != null) {
            et_student2_name.error = getString(customOperationFormState.nameError2)
            et_student2_name.shakeView()
            main_layout.scrollTo(et_student2_name.x.toInt(), et_student2_name.y.toInt())
        } else
            et_student2_name.error = null

        if (customOperationFormState?.gradeError3 != null) {
            spinner_grade_student3.setSpinnerError(getString(customOperationFormState.gradeError3))
            spinner_grade_student3.shakeView()
            main_layout.scrollTo(spinner_grade_student3.x.toInt(), spinner_grade_student3.y.toInt())
        } else
            tv_student3.error = null

        if (customOperationFormState?.nameError3 != null) {
            et_student3_name.error = getString(customOperationFormState.nameError3)
            et_student3_name.shakeView()
            main_layout.scrollTo(et_student3_name.x.toInt(), et_student3_name.y.toInt())
        } else
            et_student3_name.error = null
    }

}