package com.salamtak.app.data.remote

import com.salamtak.app.data.Resource
import com.salamtak.app.data.entities.*
import com.salamtak.app.data.entities.cart.InsuranceRequestBody
import com.salamtak.app.data.entities.CarProviderDetails.CarProviderDetails
import com.salamtak.app.data.entities.cart.*
import com.salamtak.app.data.entities.responses.*
import okhttp3.RequestBody
import okhttp3.ResponseBody

/**
 * Created by Radwa Elsahn on 3/21/2020
 */

internal interface RemoteSource {
    suspend fun requestCategories(): Resource<SalamtakListResponse<Category>>

    suspend fun requestLogin(
        userName: String,
        password: String,
        deviceId: String,
        fcmToken: String
    ): Resource<SalamtakResponse<User>>

    suspend fun requestCities(
    ): Resource<SalamtakListResponse<City>>

    suspend fun postBookOperation(
        downPayment: Int, operationId: String,
        installmentTypeId: String
    ): Resource<BaseResponse>

    suspend fun postAddToFavourite(operationId: String): Resource<ActionResponse>

    suspend fun register(
        firstName: String,
        lastName: String,
        userName: String,
        phone: String,
        email: String,
        image: String, password: String, confirmPassword: String,
        deviceId: String,
        fcmToken: String
    ): Resource<SalamtakResponse<User>>


    suspend fun postMedicalProfile(
        PatientBloodTypeId: Int,
        Shareable: Int,
        DateOfBirth: String,
        Weight: String,
        Height: String
    ): Resource<MedicalProfileResponse>


    suspend fun postEmploymentProfile(
        Profession: String,
        CompanyName: String,
        HiringDate: String,
        NetMonthlyIncome: String,
        NetMonthlyExpense: String,
        EmploymentTypeId: Int,
        EmploymentFieldId: Int,
        StreetAddress: String,
        BuildingNum: Int,
        CityId: Int,
        AreaId: String
    ): Resource<EmploymentProfileResponse>

    suspend fun postFinancialProfile(
        NumberOfChildren: Int,
        CarManufactureYear: String,
        CarModelId: Int,
        ClubId: Int,
        BankInfoId: Int,
        MaritalStatusId: Int
    ): Resource<FinancialProfileResponse>

    suspend fun postBasicProfile(
        firstName: String,
        secondName: String,
        thirdName: String,
        lastName: String,
        gender: Int,
        nationalId: String,
        nationalIdExpireDate: String,
        isEgyptian: Int,
        residentalStatusId: Int,
        OtherResidentalStatus: String,
        passportNumber: String,
        passportExpireDate: String,
        streetAddress: String,
        buildingNum: String,
        cityId: String,
        areaId: String, image: String?
    ): Resource<BasicProfileInfoResponse>

    suspend fun postDiseases(ChronicDiseases: List<ChronicDiseaseInput>): Resource<ChronicDiseasesResponse>

    suspend fun getMedicalProfile(): Resource<MedicalProfileResponse>
    suspend fun getEmploymentProfile(): Resource<EmploymentProfileResponse>
    suspend fun getFinancialProfile(): Resource<FinancialProfileResponse>
    suspend fun getFinancialLookups(): Resource<FinancialLookupsResponse>
    suspend fun fetchMedicalLookups(): Resource<MedicalProfileLookupsResponse>
    suspend fun getBasicProfile(): Resource<BasicProfileInfoResponse>
    suspend fun fetchProfileLookups(): Resource<ProfileLookupsResponse>
    suspend fun getHomeVisit(requestId: String): Resource<HomeVisitResponse>
    suspend fun getMyHomeVisits(page: Int, pageSize: Int): Resource<HomeVisitsResponse>
    suspend fun deleteChronicDisease(id: String): Resource<SalamtakResponse<BaseResponse>>
    suspend fun cancelHomeVisit(id: String): Resource<ActionResponse>
    suspend fun cancelOperation(id: String): Resource<ActionResponse>
    suspend fun verifyNumber(verifyType: Int, verifyCode: String): Resource<SalamtakResponse<User>>
    suspend fun requestVerifyNumber(VerifyType: Int): Resource<ActionResponse>
    suspend fun addHomeVisit(
        DoctorSpecializationId: Int,
        PreferredTimeId: Int,
        IsForYou: Int, Name: String, Age: String,
        Notes: String, ContactNumber: String,
        StreetAddress: String, BuildingNum: String,
        AppartmentNumber: String, CityId: Int,
        AreaId: String, Lat: Double,
        Lng: Double, PaymentMethodId: Int, CardId: String
    ): Resource<AddHomeVisitResponse>

    suspend fun getPreferredTimes(): Resource<PreferredTimesResponse>
    suspend fun getDoctorSpecializations(): Resource<DoctorSpecializationsResponse>
    suspend fun logout(): Resource<ActionResponse>

    suspend fun getBookedOperation(
        page: Int, pageSize: Int
    ): Resource<SalamtakListResponse<BookedOperation>>

    suspend fun searchOperations(
        query: String, categoryId: String, areaId: String, page: Int, pageSize: Int
    ): Resource<SalamtakListResponse<Operation>>

    suspend fun getDoctorInfo(doctorId: String, page: Int, pageSize: Int): Resource<DoctorDetails>

    suspend fun getMedicalProviderInfo(
        providerId: String,
        page: Int,
        pageSize: Int
    ): Resource<MedicalProviderDetails>

    suspend fun getFavourites(
        page: Int,
        pageSize: Int
    ): Resource<SalamtakListResponse<Operation>>

    suspend fun changePassword(
        OldPassword: String,
        Password: String,
        ConfirmPassword: String
    ): Resource<ActionResponse>

    suspend fun forgotPassword(
        email: String
    ): Resource<ActionResponse>

    suspend fun forgotPasswordReset(
        id: String,
        ResetCode: String,
        Password: String,
        ConfirmPassword: String,
        fcmToken: String
    ): Resource<SalamtakResponse<User>>

    suspend fun getMedicalProviderDoctors(
        providerId: String,
        page: Int,
        pageSize: Int
    ): Resource<DoctorsResponse>


    suspend fun getMedicalProviderOperations(
        providerId: String,
        page: Int,
        pageSize: Int, categoryId: String?
    ): Resource<SalamtakListResponse<Operation>>

    suspend fun getDoctorOperations(
        doctorId: String, page: Int, pageSize: Int, alphabetical: String,
        categoryId: String?
    ): Resource<SalamtakListResponse<Operation>>

//    suspend fun getProfileInfo(): Resource<ProfileResponse>

    suspend fun updateProfileInfo(
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        Image: String?
    ): Resource<UpdateProfileResponse>

    suspend fun addCard(): Resource<ActionResponse>

    suspend fun getPaymentMethods(): Resource<PaymentMethodsResponse>
    suspend fun deleteCard(cardId: String): Resource<ActionResponse>
    suspend fun setDefaultCard(cardId: String): Resource<ActionResponse>

    suspend fun addBookingReview(
        requestId: String,
        experienceRate: Int,
        doctorRate: Int,
        medicalProviderRate: Int,
        review: String
    ): Resource<ActionResponse>

    suspend fun addHomeVisitReview(
        requestId: String,
        experienceRate: Int,
        doctorRate: Int,
        review: String
    ): Resource<ActionResponse>

    suspend fun getOperationReviews(
        operationId: String,
        page: Int,
        pageSize: Int
    ): Resource<ReviewsResponse>

    suspend fun getDoctorReviews(
        doctorId: String,
        page: Int,
        pageSize: Int
    ): Resource<ReviewsResponse>

    suspend fun getProviderReviews(
        providerId: String,
        page: Int,
        pageSize: Int
    ): Resource<ReviewsResponse>

    suspend fun updateFcmToken(
        token: String
    ): Resource<ActionResponse>

    suspend fun contactUs(
        Phone: String,
        Message: String
    ): Resource<ContactUsResponse>

    suspend fun changeLanguage(
        language: String
    ): Resource<ActionResponse>

    suspend fun getOffersByProvider(
        providerId: String, page: Int, pageSize: Int
    ): Resource<OffersResponse>

    suspend fun getOffersProviders(
        page: Int,
        pageSize: Int
    ): Resource<OffersProvidersResponse>

    suspend fun getOffersUsage(
        page: Int,
        pageSize: Int
    ): Resource<SalamtakListResponse<OfferHistory>>

    suspend fun getQrCode(id: String): Resource<SalamtakResponse<QrData>>

    suspend fun fetchCategoryOperations(
        categoryId: Int,
        page: Int,
        pageSize: Int,
        alphabetical: String
    ): Resource<SalamtakListResponse<Operation>>

    suspend fun bookCustomOperation(
        FullName: String,
        Phone: String,
        DoctorName: String,
        OperationName: String,
        CategoryId: Int,
        InstallmentTypeId: String,
        MonthlyInstallment: Double,
        DownPayment: Double,
        TotalCost: Double
    ): Resource<BaseResponse>

    suspend fun getCustomOperationLookups(): Resource<CustomOperationLookupsResponse>

    suspend fun getDoctors(
        page: Int, pageSize: Int
    ): Resource<SalamtakListResponse<MedicalNetwork>>

    suspend fun getMedicalProviders(
        page: Int, pageSize: Int

    ): Resource<SalamtakListResponse<MedicalNetwork>>

    suspend fun getSubCategories(
        categoryId: String,
        page: Int,
        pageSize: Int
    ): Resource<SalamtakListResponse<SubCategory>>

    suspend fun getNewSubCategories(
        categoryId: Int,
        page: Int,
        pageSize: Int
    ): Resource<SalamtakListResponse<SubCategoryModel>>

//    suspend fun getOperationDetails(
//        operationId: Int
//    ): Resource<SalamtakResponse<OperationN>>

    suspend fun getSubCategoryOperations(
        subCategoryId: Int,
        page: Int,
        pageSize: Int
    ): Resource<SalamtakListResponse<Operation>>

    suspend fun getPreCreateFinancialProfileInfo(): Resource<SalamtakResponse<PreFinancialProfile>>

    suspend fun saveBasicProfile(
        profileId: String?,
        mobile: String,
        limit: String,
        firstName: String,
        secondName: String,
        thirdName: String,
        lastName: String,
        id: String,
        expiryDate: String,
        street: String,
        building: String,
        cityId: String,
        areaId: String,
        selectedLoanType: Int,
        providerId: String, maritalStatusId: String,
        idFaceTempName: String?,
        idBackTempName: String?
    ): Resource<SalamtakResponse<IdObject>>

    suspend fun uploadFile(image: String): Resource<SalamtakResponse<UploadImagesResponse>>

    suspend fun uploadMultipleFiles(
        images: Array<String>
    ): Resource<SalamtakResponse<UploadImagesResponse>>

    suspend fun addCategoryAttachment(
        FinancnailProfileId: String, CategoryId: String, AttachmentId: String,
        image: String, position: Int
    ): Resource<SalamtakResponse<AttachmentData>>

    suspend fun addAttachments(
        FinancnailProfileId: String, CategoryId: String, AttachmentId: String,
        image: String, position: Int
    ): Resource<SalamtakResponse<AttachmentData>>

    suspend fun addEmployeeData(
        job: String,
        salary: String,
        companyName: String,
        companyAddress: String,
        customerFinancialProfileId: String,
//        bankStatement: String,
//        utilityBillId: String,
//        guranatorIDFrontAttachmentId: String?,
//        guranatorIDBackAttachmentId: String?
    ): Resource<ActionResponse>

    suspend fun getFinancialCategoryAttachments(
        FinancialProfileId: String,
        categoryId: String
    ): Resource<SalamtakResponse<FinancialCategoriesData>>

    //    suspend fun getAttachment(fileId: String): Resource<File>
    suspend fun downloadFileWithDynamicUrlSync(url: String): ResponseBody?


    suspend fun addPensionData(
        customerFinancialProfileId: String,
        amount: String,
//        pensionId: String,
//        pension2Id: String,
//        utilityBillId: String,
//        guranatorIDFrontAttachmentId: String?,
//        guranatorIDBackAttachmentId: String?
    ): Resource<ActionResponse>


    suspend fun addAssetOwnerData(
        customerFinancialProfileId: String,
        NetIncome: String,
//        AssetContractAttachmentId: String,
//        LeasingContractAttachmentId: String,
//        UtilityBillAttachmentId: String,
//        guranatorIDFrontAttachmentId: String?,
//        guranatorIDBackAttachmentId: String?
    ): Resource<ActionResponse>


    suspend fun addBusinessOwnerData(
        customerFinancialProfileId: String,
        Job: String,
        CompanyNetIncome: String,
        CompanyName: String,
        CompanyAddress: String,
//        CommercialRegisterAttachmentId: String,
//        TaxIDAttachmentId: String,
//        TaxReturnAttachmentId: String,
//        BankStatementAttachmentId: String,
//        guranatorIDFrontAttachmentId: String?,
//        guranatorIDBackAttachmentId: String?
    ): Resource<ActionResponse>

    suspend fun addClubData(
        customerFinancialProfileId: String,
    ): Resource<ActionResponse>

    suspend fun addCarOwnerData(
        customerFinancialProfileId: String
    ): Resource<ActionResponse>

    suspend fun addBankCertificateData(
        customerFinancialProfileId: String,
        NetIncome: String
    ): Resource<ActionResponse>

    suspend fun isFinancialProfileCompleted(): Resource<FinancialProfileCompleted>

    suspend fun getNationalIdAttachments(
        FinancialProfileId: String,
        IsFaceImage: Boolean
    ): ResponseBody?

    suspend fun deleteAttachment(
        fileId: String,
//        financialProfileId: String,
//        categoryId: String
    ): Resource<BaseResponse>

    suspend fun getCategoryProviders(
        categoryId: Int, page: Int, pageSize: Int, filter: String?
    ): Resource<SalamtakListResponse<MedicalProvider>>

    suspend fun getOperationDetails(
        operationId: String
    ): Resource<OperationDetails>

    suspend fun searchHealth(
        query: String, page: Int, pageSize: Int
    ): Resource<SalamtakListResponse<Operation>>

    suspend fun CreateCustomInsuranceBooking(
        fullName: String,
        phoneNumber: String,
        insuranceCompanyName: String,
        insuranceType: Int,
        monthlyInstallment: String, downPayment: String, installmentTypeId: String, price: String
    ): Resource<ActionResponse>

    suspend fun CreateCustomEducationBooking(input: EducationBookingInput): Resource<ActionResponse>
    suspend fun getInstallmentsLookup(type: String): Resource<InstallmentTypesData>

    suspend fun getLatestAdded(
        page: Int,
        pageSize: Int
    ): Resource<SalamtakListResponse<MedicalNetwork>>

    suspend fun getProvidersNames(categoryId: String): Resource<List<MedicalProvider>>
    suspend fun getPreHealthCategoryFilter(categoryId: String): Resource<FilterData>
    suspend fun getPreHealthAdvancedFilter(): Resource<FilterData>

    suspend fun healthAdvancedSearch(
        categoryId: String,
        subCategoryId: String,
        medicalProvider: String,
        cityId: String,
        startPrice: String,
        endPrice: String,
        Operation: String,
        page: Int,
        pageSize: Int
    ): Resource<SalamtakListResponse<Operation>>

    suspend fun categoryHealthAdvancedSearch(
        categoryId: String,
        subCategoryId: String,
        medicalProvider: String,
        cityId: String,
        startPrice: String,
        endPrice: String,
        page: Int,
        pageSize: Int
    ): Resource<SalamtakListResponse<Operation>>

    //health..
    suspend fun AddCategoryHealthBookingToCart(
        requestBody: HealthCategoryRequestBody,
    ): Resource<ActionResponse>

    //health custom...
    suspend fun AddCustomHealthBookingToCart(
        requestBody: HealthCustomRequestBody,
    ): Resource<ActionResponse>

    //finishing..
    suspend fun AddCategoryFinishingBookingToCart(
        requestBody: FinishingCategoryRequestBody,
    ): Resource<ActionResponse>

    //finishing custom...
    suspend fun AddCustomFinishingBookingToCart(
        requestBody: FinishingCustomRequestBody,
    ): Resource<ActionResponse>

    //car..
    suspend fun AddCategoryCarBookingToCart(
        requestBody: CarCategoryRequestBody,
    ): Resource<ActionResponse>

    //car custom...
    suspend fun AddCustomCarBookingToCart(
        requestBody: CarCustomRequestBody,
    ): Resource<ActionResponse>


    //education custom...
    suspend fun AddCustomEducationBookingToCart(
        requestBody: EducationCustomRequestBody,
    ): Resource<ActionResponse>


    //education ...
    suspend fun AddEducationBookingToCart(
        requestBody: EducationRequestBody,
    ): Resource<ActionResponse>

    //wedding custom...
    suspend fun AddCustomWeddingBookingToCart(
        requestBody: WeddingRequestBody,
    ): Resource<ActionResponse>

    suspend fun CreateCustomWeddingBooking(
        fullName: String,
        phoneNumber: String,
        venueName: String,
        inviteesNumber: Int,
        monthlyInstallment: String,
        downPayment: String,
        installmentTypeId: String,
        price: String
    ): Resource<ActionResponse>

    suspend fun getMoreDoctors(
        subCategoryId: String,
        page: Int,
        pageSize: Int
    ): Resource<SalamtakListResponse<MedicalProvider>>

    suspend fun getCategories(maincategoryId: Int): Resource<SalamtakListResponse<Category>>

    suspend fun getAllEducationByCategoryId(
        categoryId: Int, page: Int, pageSize: Int
    ): Resource<SalamtakObjectListResponse<EducationSubcategoriesData>>


    suspend fun getAllEducationBySubCategoryId(
        SubCategoryId: Int, page: Int, pageSize: Int
    ): Resource<SalamtakListResponse<School>>


    suspend fun getSchoolDetailsById(schoolId: String): Resource<SalamtakResponse<SchoolDetails>>

    suspend fun createSchoolBooking(input: SchoolRequestBookingInput): Resource<ActionResponse>
    suspend fun createUniversityBooking(input: UniversityRequestBookingInput): Resource<ActionResponse>
    suspend fun createInstituteBooking(input: UniversityRequestBookingInput): Resource<ActionResponse>
    suspend fun getUniversitiesAndInstitutes(
        categoryId: Int,
        page: Int,
        pageSize: Int
    ): Resource<SalamtakListResponse<University>>

    suspend fun getCollages(
        id: String,
        type: Int,
        page: Int,
        pageSize: Int
    ): Resource<SalamtakResponse<University>>

    suspend fun getCollageDetails(id: String): Resource<SalamtakResponse<Collage>>

    suspend fun VOTP(
        otp: String,
        bookingId: String, referenceNumber: String
    ): Resource<BaseResponse>

    suspend fun GOTP(
        cardNumber: String,
        expiry: String,
        BookingId: String
    ): Resource<SalamtakResponse<PremiumData>>


    suspend fun GTRD(
        referenceNumber: String
    ): Resource<SalamtakResponse<TransactionDetails>>

    suspend fun getFinancialProgress(): Resource<BaseResponse>
    suspend fun financialPreStepOne(): Resource<PreStep1Data>
    suspend fun financialPreStepTwo(id: String): Resource<PreStep2Data>
    suspend fun financialPreStepThree(id: String): Resource<Step3Data>

    suspend fun postStepOne(input: FinancialProfile): Resource<SalamtakResponse<IdObject>>
    suspend fun postStepTwo(
        input: Step2
    ): Resource<ActionResponse>

    suspend fun postStepThree(input: Step3Data): Resource<ActionResponse>
    suspend fun getFinishingProviders(
        categoryId: Int?,
        page: Int,
        pageSize: Int,
        query:String,
        sort:Int?
    ): Resource<SalamtakObjectListResponse<FinishingProvidersData>>

    suspend fun getFinishingCategories(
        page: Int,
        pageSize: Int,
    ): Resource<SalamtakListResponse<FinishingCategoryModel>>


    suspend fun getFinishingProviderDetails(id: String): Resource<SalamtakResponse<FinishingProvider>>
    suspend fun createCustomFinishingBooking(input: FinishingCustomRequestBookingInput): Resource<ActionResponse>
    suspend fun createFinishingBooking(input: FinishingRequestBookingInput): Resource<ActionResponse>

    ///cars
    suspend fun createCarServiceBooking(input: CarServiceRequestBookingInput): Resource<ActionResponse>
    suspend fun createCustomCarServiceBooking(input: CarCustomRequestBookingInput): Resource<ActionResponse>
    suspend fun getCarServiceCenterDetails(id: String): Resource<SalamtakResponse<CarServiceCenter>>

    suspend fun getCarProviderDetails(id: String): Resource<SalamtakResponse<CarProviderDetails>>
    suspend fun getCarProvidersViewAll(page: Int,pageSize: Int,categoryId: Int
    ): Resource<SalamtakObjectListResponse<CarProvidersData>>

    suspend fun getCarServiceCenters(input: GetCarServiceInput
    ): Resource<SalamtakListResponse<CarCategoryModel>>

    suspend fun getPackageDetails(id: String): Resource<FinishingPackage>

    suspend fun getCarBrands(): Resource<SalamtakListResponse<CarModel>>

    suspend fun getCities(): Resource<SalamtakListResponse<City>>

    suspend fun getCitiesForCarFilter(): Resource<SalamtakListResponse<City>>

    //    suspend fun createCustomCarBooking(input: CarCustomRequestBookingInput): Resource<ActionResponse>

    suspend fun getFeaturedCategories(): Resource<FeaturedCategoriesResponse>

    suspend fun getAdvertisements(): Resource<AdvertisementsResponse>

    suspend fun getPromotions(): Resource<PromotionsResponse>

    suspend fun getTopProviders(): Resource<TopProvidersResponse>

    suspend fun getServicesCategories(): Resource<ServicesCategoriesResponse>

    suspend fun addCustomInsuranceToCart(requestBody: InsuranceRequestBody, ): Resource<ActionResponse>

    suspend fun GetCartData(cartUID: String): Resource<GetCartResponse>

    suspend fun  deleteCartItem(ItemID: String): Resource<BaseResponse>

    suspend fun  checkoutCart(cartUID: String): Resource<BaseResponse>

    suspend fun  getCartCount(cartUID: String): Resource<cartCountResponse>

}
