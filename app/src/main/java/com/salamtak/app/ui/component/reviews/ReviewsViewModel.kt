//package com.salamtak.app.ui.component.reviews
//
//import com.salamtak.app.data.error.mapper.ErrorMapper
//import com.salamtak.app.ui.common.BaseViewModel
//import com.salamtak.app.usecase.OperationsUseCase
//import com.salamtak.app.usecase.errors.ErrorManager
//import com.salamtak.app.utils.Constants
//import javax.inject.Inject
//
///**
// * Created by Radwa Elsahn on 5/18/2020
// */
//
//class ReviewsViewModel @Inject
//constructor(private val operationsUseCase: OperationsUseCase) : BaseViewModel() {
//
//    override val errorManager: ErrorManager
//        get() = ErrorManager(ErrorMapper())
//
//    val reviewsResponse = operationsUseCase.reviewsResponse
//
//    fun getDoctorReviews(doctorId: String) {
//        operationsUseCase.getDoctorReviews(doctorId, 0, Constants.PAGE_SIZE)
//    }
//
//    fun getProviderReviews(providerId: String) {
//        operationsUseCase.getProviderReviews(providerId, 0, Constants.PAGE_SIZE)
//    }
//
//    fun getOperationsReviews(operationId: String) {
//        operationsUseCase.getOperationReviews(operationId, 0, Constants.PAGE_SIZE)
//    }
//}
