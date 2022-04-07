package com.salamtak.app.data

import com.salamtak.app.data.entities.*
import com.salamtak.app.data.entities.CarProviderDetails.CarProviderDetails
import com.salamtak.app.data.entities.cart.*
import com.salamtak.app.data.entities.responses.*
import com.salamtak.app.data.local.LocalRepository
import com.salamtak.app.data.remote.RemoteRepository
import okhttp3.ResponseBody

import javax.inject.Inject


/**
 * Created by Radwa Elsahn on 3/21/2020
 */

class DataRepository @Inject
constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository
) : DataSource {

    /**Local**/
    override fun isLogin(): Boolean {
        return localRepository.isLogin()
    }

    override fun saveUser(user: User) {
        return localRepository.saveUserInfo(user)
    }

    override fun getUser(): User? {
        return localRepository.getUserInfo()
    }

    override fun saveDefaultCard(cardId: String) {
        return localRepository.saveDefaultCard(cardId)
    }

    override fun getDefaultCardId(): String {
        return localRepository.getDefaultCardId()
    }

    override fun isFirstTime(): Boolean {
        return localRepository.isFirstTime()
    }

    override fun notFirstTime() {
        localRepository.notFirstTime()
    }

    override fun saveUserFinancialData(key: String, value: HashMap<String, String>) {
        localRepository.saveUserFinancialData(key, value)
    }

    override fun getFinancialData(key: String): HashMap<String, String>? {
        return localRepository.getFinancialData(key)
    }

    override fun saveUserFinancialImages(key: String, value: List<FinancialTypeAttachments>) {
        localRepository.saveUserFinancialImages(key, value)
    }

    override fun getUserFinancialImages(key: String): List<FinancialTypeAttachments>? {
        return localRepository.getUserFinancialImages(key)
    }

//    override fun getAccessToken(): String {
//        return localRepository.getAccessToken()
//    }

    override fun getCategories(): List<Category>? {
        return localRepository.getCategories()
    }

    override fun saveCategories(data: List<Category>) {
        localRepository.saveCategories(data)
    }

    override fun saveNeedFinancialInfo(hasInfo: Boolean) {
        localRepository.saveNeedFinancialInfo(hasInfo)
    }

    override fun needFinancialInfo(): Boolean {
        return localRepository.needFinancialInfo()
    }

    override fun bookedOoeration() {
        return localRepository.bookedOoeration()
    }

    override fun logoutLocal() {
        localRepository.logout()
    }

    override fun getFCMToken(): String {
        return localRepository.getFCMToken()
    }

    override fun saveFCMToken(value: String) {
        return localRepository.saveFCMToken(value)
    }

    override fun toggleLocale(): String {
        return localRepository.toggleLocale()
    }

    override fun setLocale(locale: String) {
        return localRepository.setLocale(locale)
    }

    override fun getLocale(): String {
        return localRepository.getLocale()
    }

    override suspend fun setVerified(mailVerified: Boolean, phoneVerified: Boolean) {
        localRepository.setVerified(mailVerified, phoneVerified)
    }

//    override suspend fun saveUserProfile(basicProfile: BasicProfile) {
//        localRepository.saveUserProfile(basicProfile)
//    }

    override fun saveNotification(notification: Notification) {
        localRepository.saveNotification(notification)
    }

    override fun dropTableNotifications() {
        localRepository.dropTableNotifications()
    }

    override fun deleteNotifications(notification: Notification) {
        localRepository.deleteNotification(notification)
    }

    override fun deleteAll() {
        localRepository.deleteAllNotifications()
    }

    override fun getAllNotifications(): List<Notification> {
        return localRepository.getAllNotifications()
    }
    override fun getNotificationsCount(): Int {
        return localRepository.getNotificationsCount()
    }

    override fun saveFinancialProfileId(id: String) {
        return localRepository.saveFinancialProfileId(id)
    }

    override fun getFinancialProfileId(): String {
        return localRepository.getFinancialProfileId()
    }

    override fun saveCredentials4Biometric(username: String, password: String) {
        localRepository.saveCredentials4Biometric(username, password)
    }

    override fun getBioUserName(): String {
        return localRepository.getBioUserName()
    }

    override fun getBioPassword(): String {
        return localRepository.getBioPassword()
    }

    override fun saveCustomFinishingInput(value: FinishingCustomRequestBookingInput?) {
        localRepository.saveCustomFinishingInput(value)
    }

    override fun getCustomFinishingInput(): FinishingCustomRequestBookingInput? {
        return localRepository.getCustomFinishingInput()
    }
    override fun getCustomCarInput(): CarCustomRequestBookingInput? {
        return localRepository.getCustomCarInput()
    }

    override fun saveCustomCarInput(value: CarCustomRequestBookingInput?)
    {
        localRepository.saveCustomCarInput(value)
    }

    override fun saveSelectedCity(value: City) {
        localRepository.saveSelectedCity(value)
    }


    override fun getSelectedCity(): City? {
        return  localRepository.getSelectedCity()
    }

    /** Remote **/

    override suspend fun requestCategories(): Resource<SalamtakListResponse<Category>> {
        return remoteRepository.requestCategories()
    }

    override suspend fun login(
        userName: String,
        password: String,
        deviceId: String,
        fcmToken:String

    ): Resource<SalamtakResponse<User>> {
        return remoteRepository.requestLogin(userName, password, deviceId,fcmToken)
    }

//    override suspend fun requestCategoryDetails(
//        categoryId: Int,
//        page: Int,
//        pageSize: Int
//    ): Resource<SalamtakListResponse<OperationCardData>N> {
//        return remoteRepository.requestCategoryDetails(categoryId, page, pageSize)
//    }

    override suspend fun getRefreshToken(
    ): Resource<RefreshTokenResponse> {
        return remoteRepository.refreshToken()
    }

//    override suspend fun homeVisitsPayment(
//        homeVisitRequestId: String,
//        cardId: String,
//        paymentMethodId: Int
//    ): Resource<SalamtakResponse<BaseResponse>> {
//        return remoteRepository.homeVisitsPayment(homeVisitRequestId, cardId, paymentMethodId)
//    }

    override suspend fun requestCities(): Resource<SalamtakListResponse<City>> {
        return remoteRepository.requestCities()
    }

//    override suspend fun fetchBookingDetails(
//        page: Int, pageSize: Int
//    ): Resource<BookingResponse> {
//        return remoteRepository.fetchBookingDetails(page, pageSize)
//    }

    override suspend fun postBookOperation(
        downPayment: Int, operationId: String,
        installmentTypeId: String
    ): Resource<BaseResponse> {
        return remoteRepository.postBookOperation(downPayment, operationId, installmentTypeId)
    }

    override suspend fun postAddToFavourite(operationId: String): Resource<ActionResponse> {
        return remoteRepository.postAddToFavourite(operationId)
    }

    override suspend fun register(
        firstName: String,
        lastName: String,
        userName: String,
        phone: String,
        email: String,
        image: String, password: String, confirmPassword: String,
        deviceId: String,
        fcmToken:String

    ): Resource<SalamtakResponse<User>> {
        return remoteRepository.register(
            firstName,
            lastName,
            userName,
            phone,
            email,
            image,
            password,
            confirmPassword,
            deviceId,
            fcmToken

        )
    }

    override suspend fun postMedicalProfile(
        PatientBloodTypeId: Int,
        Shareable: Int,
        DateOfBirth: String,
        Weight: String,
        Height: String
    ): Resource<MedicalProfileResponse> {
        return remoteRepository.postMedicalProfile(
            PatientBloodTypeId,
            Shareable,
            DateOfBirth,
            Weight,
            Height
        )
    }

    override suspend fun postEmploymentProfile(
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
    ): Resource<EmploymentProfileResponse> {
        return remoteRepository.postEmploymentProfile(
            Profession, CompanyName, HiringDate, NetMonthlyIncome, NetMonthlyExpense,
            EmploymentTypeId, EmploymentFieldId, StreetAddress, BuildingNum, CityId, AreaId
        )
    }

    override suspend fun postFinancialProfile(
        NumberOfChildren: Int,
        CarManufactureYear: String,
        CarModelId: Int,
        ClubId: Int,
        BankInfoId: Int,
        MaritalStatusId: Int
    ): Resource<FinancialProfileResponse> {
        return remoteRepository.postFinancialProfile(
            NumberOfChildren,
            CarManufactureYear,
            CarModelId,
            ClubId,
            BankInfoId, MaritalStatusId
        )
    }

    override suspend fun getMedicalProfile(): Resource<MedicalProfileResponse> {
        return remoteRepository.getMedicalProfile()
    }

    override suspend fun getEmploymentProfile(): Resource<EmploymentProfileResponse> {
        return remoteRepository.getEmploymentProfile()
    }

    override suspend fun getFinancialProfile(): Resource<FinancialProfileResponse> {
        return remoteRepository.getFinancialProfile()
    }

    override suspend fun getFinancialLookups(): Resource<FinancialLookupsResponse> {
        return remoteRepository.getFinancialLookups()
    }

    override suspend fun fetchMedicalLookups(): Resource<MedicalProfileLookupsResponse> {
        return remoteRepository.fetchMedicalLookups()
    }

    override suspend fun postBasicProfile(
        firstName: String,
        secondName: String,
        thirdName: String,
        lastName: String,
        gender: Int,
        nationalId: String,
        nationalIdExpireDate: String,
        isEgyptian: Int,
        residentalStatusId: Int,
        otherResidentalStatus: String,
        passportNumber: String,
        passportExpireDate: String,
        streetAddress: String,
        buildingNum: String,
        cityId: String,
        areaId: String, image: String?
    ): Resource<BasicProfileInfoResponse> {
        return remoteRepository.postBasicProfile(
            firstName,
            secondName,
            thirdName,
            lastName,
            gender,
            nationalId,
            nationalIdExpireDate,
            isEgyptian,
            residentalStatusId,
            otherResidentalStatus,
            passportNumber,
            passportExpireDate,
            streetAddress,
            buildingNum,
            cityId,
            areaId, image
        )
    }

    override suspend fun getBasicProfile(): Resource<BasicProfileInfoResponse> {
        return remoteRepository.getBasicProfile()
    }

    override suspend fun fetchProfileLookups(): Resource<ProfileLookupsResponse> {
        return remoteRepository.fetchProfileLookups()
    }

    override suspend fun postDiseases(chronicDiseases: List<ChronicDiseaseInput>): Resource<ChronicDiseasesResponse> {
        return remoteRepository.postDiseases(chronicDiseases)
    }

    override suspend fun getHomeVisit(requestId: String): Resource<HomeVisitResponse> {
        return remoteRepository.getHomeVisit(requestId)
    }

    override suspend fun getMyHomeVisits(page: Int, pageSize: Int): Resource<HomeVisitsResponse> {
        return remoteRepository.getMyHomeVisits(page, pageSize)
    }

    override suspend fun deleteChronicDisease(id: String): Resource<SalamtakResponse<BaseResponse>> {
        return remoteRepository.deleteChronicDisease(id)
    }

    override suspend fun cancelHomeVisit(id: String): Resource<ActionResponse> {
        return remoteRepository.cancelHomeVisit(id)
    }

    override suspend fun cancelOperation(id: String): Resource<ActionResponse> {
        return remoteRepository.cancelOperation(id)
    }

    override suspend fun verifyNumber(
        verifyType: Int,
        verifyCode: String
    ): Resource<SalamtakResponse<User>> {
        return remoteRepository.verifyNumber(1, verifyCode)
    }

    override suspend fun requestVerifyNumber(VerifyType: Int): Resource<ActionResponse> {
        return remoteRepository.requestVerifyNumber(VerifyType)
    }

    override suspend fun addHomeVisit(
        DoctorSpecializationId: Int,
        PreferredTimeId: Int,
        IsForYou: Int, Name: String, Age: String,
        Notes: String, ContactNumber: String,
        StreetAddress: String, BuildingNum: String,
        AppartmentNumber: String, CityId: Int,
        AreaId: String, Lat: Double,
        Lng: Double, PaymentMethodId: Int, CardId: String
    ): Resource<AddHomeVisitResponse> {
        return remoteRepository.addHomeVisit(
            DoctorSpecializationId,
            PreferredTimeId,
            IsForYou,
            Name,
            Age,
            Notes,
            ContactNumber,
            StreetAddress,
            BuildingNum,
            AppartmentNumber,
            CityId,
            AreaId,
            Lat,
            Lng,
            PaymentMethodId,
            CardId
        )
    }

    override suspend fun getPreferredTimes(): Resource<PreferredTimesResponse> {
        return remoteRepository.getPreferredTimes()
    }

    override suspend fun getDoctorSpecializations(): Resource<DoctorSpecializationsResponse> {
        return remoteRepository.getDoctorSpecializations()
    }

    override suspend fun logout(): Resource<ActionResponse> {
        return remoteRepository.logout()
    }

    override suspend fun getBookedOperation(
        page: Int, pageSize: Int
    ): Resource<SalamtakListResponse<BookedOperation>> {
        return remoteRepository.getBookedOperation(page, pageSize)
    }

    override suspend fun searchOperations(
        query: String, categoryId: String, areaId: String, page: Int, pageSize: Int
    ): Resource<SalamtakListResponse<Operation>> {
        return remoteRepository.searchOperations(query, categoryId, areaId, page, pageSize)
    }

    override suspend fun getDoctorInfo(
        doctorId: String,
        page: Int,
        pageSize: Int
    ): Resource<DoctorDetails> {
        return remoteRepository.getDoctorInfo(doctorId, page, pageSize)
    }

    override suspend fun getMedicalProviderInfo(
        providerId: String,
        page: Int,
        pageSize: Int
    ): Resource<MedicalProviderDetails> {
        return remoteRepository.getMedicalProviderInfo(providerId, page, pageSize)
    }

    override suspend fun getFavourites(
        page: Int,
        pageSize: Int
    ): Resource<SalamtakListResponse<Operation>> {
        return remoteRepository.getFavourites(page, pageSize)
    }

    override suspend fun changePassword(
        OldPassword: String,
        Password: String,
        ConfirmPassword: String
    ): Resource<ActionResponse> {
        return remoteRepository.changePassword(OldPassword, Password, ConfirmPassword)
    }

    override suspend fun forgotPassword(
        email: String
    ): Resource<ActionResponse> {
        return remoteRepository.forgotPassword(email)
    }

    override suspend fun forgotPasswordReset(
        id: String,
        ResetCode: String,
        Password: String,
        ConfirmPassword: String,
        fcmToken: String
    ): Resource<SalamtakResponse<User>> {
        return remoteRepository.forgotPasswordReset(id, ResetCode, Password, ConfirmPassword,fcmToken)
    }

    override suspend fun getMedicalProviderDoctors(
        providerId: String,
        page: Int,
        pageSize: Int
    ): Resource<DoctorsResponse> {
        return remoteRepository.getMedicalProviderDoctors(providerId, page, pageSize)
    }

    override suspend fun getMedicalProviderOperations(
        providerId: String,
        page: Int,
        pageSize: Int, categoryId: String?
    ): Resource<SalamtakListResponse<Operation>> {
        return remoteRepository.getMedicalProviderOperations(providerId, page, pageSize, categoryId)
    }

    override suspend fun getDoctorOperations(
        doctorId: String, page: Int, pageSize: Int, alphabetical: String,
        categoryId: String?
    ): Resource<SalamtakListResponse<Operation>> {
        return remoteRepository.getDoctorOperations(
            doctorId,
            page,
            pageSize,
            alphabetical,
            categoryId
        )
    }

//    override suspend fun getProfileInfo(): Resource<ProfileResponse> {
//        return remoteRepository.getProfileInfo()
//    }

    override suspend fun updateProfileInfo(
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        Image: String?
    ): Resource<UpdateProfileResponse> {
        return remoteRepository.updateProfileInfo(firstName, lastName, email, phone, Image)
    }

    override suspend fun addCard(): Resource<ActionResponse> {
        return remoteRepository.addCard()
    }

    override suspend fun getPaymentMethods(): Resource<PaymentMethodsResponse> {
        return remoteRepository.getPaymentMethods()
    }

    override suspend fun deleteCard(cardId: String): Resource<ActionResponse> {
        return remoteRepository.deleteCard(cardId)
    }

    override suspend fun setDefaultCard(cardId: String): Resource<ActionResponse> {
        return remoteRepository.setDefaultCard(cardId)
    }

    override suspend fun addBookingReview(
        requestId: String,
        experienceRate: Int,
        doctorRate: Int,
        medicalProviderRate: Int,
        review: String
    ): Resource<ActionResponse> {
        return remoteRepository.addBookingReview(
            requestId,
            experienceRate,
            doctorRate,
            medicalProviderRate,
            review
        )
    }

    override suspend fun addHomeVisitReview(
        requestId: String,
        experienceRate: Int,
        doctorRate: Int,
        review: String
    ): Resource<ActionResponse> {
        return remoteRepository.addHomeVisitReview(requestId, experienceRate, doctorRate, review)
    }

    override suspend fun getOperationReviews(
        operationId: String,
        page: Int,
        pageSize: Int
    ): Resource<ReviewsResponse> {
        return remoteRepository.getOperationReviews(operationId, page, pageSize)
    }

    override suspend fun getDoctorReviews(
        doctorId: String,
        page: Int,
        pageSize: Int
    ): Resource<ReviewsResponse> {
        return remoteRepository.getDoctorReviews(doctorId, page, pageSize)
    }

    override suspend fun getProviderReviews(
        providerId: String,
        page: Int,
        pageSize: Int
    ): Resource<ReviewsResponse> {
        return remoteRepository.getProviderReviews(providerId, page, pageSize)
    }

    override suspend fun updateFcmToken(
        token: String
    ): Resource<ActionResponse> {
        return remoteRepository.updateFcmToken(token)
    }


    override suspend fun contactUs(
        Phone: String,
        Message: String
    ): Resource<ContactUsResponse> {
        return remoteRepository.contactUs(Phone, Message)
    }

    override suspend fun changeLanguage(
        language: String
    ): Resource<ActionResponse> {
        return remoteRepository.changeLanguage(language)
    }

    override suspend fun getOffersByProvider(
        providerId: String,
        page: Int,
        pageSize: Int
    ): Resource<OffersResponse> {
        return remoteRepository.getOffersByProvider(providerId, page, pageSize)
    }

    override suspend fun getOffersProviders(
        page: Int,
        pageSize: Int
    ): Resource<OffersProvidersResponse> {
        return remoteRepository.getOffersProviders(page, pageSize)
    }

    override suspend fun getOffersUsage(
        page: Int,
        pageSize: Int
    ): Resource<SalamtakListResponse<OfferHistory>> {
        return remoteRepository.getOffersUsage(page, pageSize)
    }

    override suspend fun getQrCode(id: String): Resource<SalamtakResponse<QrData>> {
        return remoteRepository.getQrCode(id)
    }

    override suspend fun fetchCategoryOperations(
        categoryId: Int,
        page: Int,
        pageSize: Int,
        alphabetical: String
    ): Resource<SalamtakListResponse<Operation>> {
        return remoteRepository.fetchCategoryOperations(categoryId, page, pageSize, alphabetical)
    }

    override suspend fun bookCustomOperation(
        FullName: String,
        Phone: String,
        DoctorName: String,
        OperationName: String,
        CategoryId: Int,
        InstallmentTypeId: String,
        MonthlyInstallment: Double,
        DownPayment: Double,
        TotalCost: Double
    ): Resource<BaseResponse> {
        return remoteRepository.bookCustomOperation(
            FullName,
            Phone,
            DoctorName,
            OperationName,
            CategoryId,
            InstallmentTypeId,
            MonthlyInstallment,
            DownPayment,
            TotalCost
        )
    }

    override suspend fun getCustomOperationLookups(): Resource<CustomOperationLookupsResponse> {
        return remoteRepository.getCustomOperationLookups()
    }

    override suspend fun getDoctors(
        page: Int, pageSize: Int
    ): Resource<SalamtakListResponse<MedicalNetwork>> {
        return remoteRepository.getDoctors(page, pageSize)
    }

    override suspend fun getMedicalProviders(
        page: Int, pageSize: Int
    ): Resource<SalamtakListResponse<MedicalNetwork>> {
        return remoteRepository.getMedicalProviders(page, pageSize)
    }

    override suspend fun getSubCategories(
        categoryId: String,
        page: Int,
        pageSize: Int
    ): Resource<SalamtakListResponse<SubCategory>> {
        return remoteRepository.getSubCategories(categoryId, page, pageSize)
    }

    override suspend fun getNewSubCategories(
        categoryId: Int,
        page: Int,
        pageSize: Int
    ): Resource<SalamtakListResponse<SubCategoryModel>> {
        return remoteRepository.getNewSubCategories(categoryId, page, pageSize)
    }


//    override suspend fun getOperationDetails(
//        operationId: Int
//    ): Resource<SalamtakResponse<OperationN>> {
//        return remoteRepository.getOperationDetails(operationId)
//    }

    override suspend fun getSubCategoryOperations(
        subCategoryId: Int,
        page: Int,
        pageSize: Int
    ): Resource<SalamtakListResponse<Operation>> {
        return remoteRepository.getSubCategoryOperations(subCategoryId, page, pageSize)
    }

    override suspend fun getPreCreateFinancialProfileInfo(): Resource<SalamtakResponse<PreFinancialProfile>> {
        return remoteRepository.getPreCreateFinancialProfileInfo()
    }

    override suspend fun saveBasicProfile(
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
    ): Resource<SalamtakResponse<IdObject>> {
        return remoteRepository.saveBasicProfile(
            profileId,
            mobile,
            limit,
            firstName,
            secondName,
            thirdName,
            lastName,
            id,
            expiryDate,
            street,
            building,
            cityId,
            areaId,
            selectedLoanType,
            providerId, maritalStatusId,
            idFaceTempName,
            idBackTempName
        )
    }

    override suspend fun uploadFile(image: String): Resource<SalamtakResponse<UploadImagesResponse>> {
        return remoteRepository.uploadFile(image)
    }

    override suspend fun uploadMultipleFiles(
        images: Array<String>
    ): Resource<SalamtakResponse<UploadImagesResponse>> {
        return remoteRepository.uploadMultipleFiles(images)
    }

    override suspend fun addAttachments(
        FinancnailProfileId: String, CategoryId: String, AttachmentId: String,
        image: String, position: Int
    ): Resource<SalamtakResponse<AttachmentData>> {
        return remoteRepository.addAttachments(
            FinancnailProfileId,
            CategoryId,
            AttachmentId,
            image,
            position
        )
    }

    override suspend fun addCategoryAttachment(
        FinancnailProfileId: String, CategoryId: String, AttachmentId: String,
        image: String, position: Int
    ): Resource<SalamtakResponse<AttachmentData>> {
        return remoteRepository.addCategoryAttachment(
            FinancnailProfileId,
            CategoryId,
            AttachmentId,
            image,
            position
        )
    }


    override suspend fun addEmployeeData(
        job: String,
        salary: String,
        companyName: String,
        companyAddress: String,
        customerFinancialProfileId: String,
//        bankStatement: String,
//        utilityBillId: String,
//        guranatorIDFrontAttachmentId: String?,
//        guranatorIDBackAttachmentId: String?
    ): Resource<ActionResponse> {
        return remoteRepository.addEmployeeData(
            job,
            salary,
            companyName,
            companyAddress,
            customerFinancialProfileId,
//            bankStatement,
//            utilityBillId,
//            guranatorIDFrontAttachmentId,
//            guranatorIDBackAttachmentId
        )
    }

    override suspend fun getFinancialCategoryAttachments(
        FinancialProfileId: String,
        categoryId: String
    ): Resource<SalamtakResponse<FinancialCategoriesData>> {
        return remoteRepository.getFinancialCategoryAttachments(FinancialProfileId, categoryId)
    }

//    override suspend fun getAttachment(fileId: String): Resource<File> {
//        return remoteRepository.getAttachment(fileId)
//    }

    override suspend fun downloadFileWithDynamicUrlSync(url: String): ResponseBody? {
        return remoteRepository.downloadFileWithDynamicUrlSync(url)
    }


    override suspend fun addPensionData(
        customerFinancialProfileId: String,
        amount: String,
//        pensionId: String,
//        pension2Id: String,
//        utilityBillId: String,
//        guranatorIDFrontAttachmentId: String?,
//        guranatorIDBackAttachmentId: String?
    ): Resource<ActionResponse> {
        return remoteRepository.addPensionData(
            customerFinancialProfileId,
            amount,
//            pensionId,
//            pension2Id,
//            utilityBillId,
//            guranatorIDFrontAttachmentId,
//            guranatorIDBackAttachmentId
        )
    }


    override suspend fun addAssetOwnerData(
        customerFinancialProfileId: String,
        NetIncome: String,
//        AssetContractAttachmentId: String,
//        LeasingContractAttachmentId: String,
//        UtilityBillAttachmentId: String,
//        guranatorIDFrontAttachmentId: String?,
//        guranatorIDBackAttachmentId: String?
    ): Resource<ActionResponse> {
        return remoteRepository.addAssetOwnerData(
            customerFinancialProfileId,
            NetIncome,
//            AssetContractAttachmentId,
//            LeasingContractAttachmentId,
//            UtilityBillAttachmentId,
//            guranatorIDFrontAttachmentId,
//            guranatorIDBackAttachmentId
        )
    }

    override suspend fun addBusinessOwnerData(
        customerFinancialProfileId: String,
        Job: String,
        CompanyNetIncome: String,
        CompanyName: String,
        CompanyAddress: String
    ): Resource<ActionResponse> {
        return remoteRepository.addBusinessOwnerData(
            customerFinancialProfileId,
            Job,
            CompanyNetIncome,
            CompanyName,
            CompanyAddress,
//            CommercialRegisterAttachmentId,
//            TaxIDAttachmentId,
//            TaxReturnAttachmentId,
//            BankStatementAttachmentId,
//            guranatorIDFrontAttachmentId,
//            guranatorIDBackAttachmentId
        )
    }

    override suspend fun addClubData(
        customerFinancialProfileId: String,

        ): Resource<ActionResponse> {
        return remoteRepository.addClubData(
            customerFinancialProfileId
        )
    }

    override suspend fun addCarOwnerData(
        customerFinancialProfileId: String,

        ): Resource<ActionResponse> {
        return remoteRepository.addCarOwnerData(
            customerFinancialProfileId,
        )
    }

    override suspend fun addBankCertificateData(
        customerFinancialProfileId: String,
        NetIncome: String
    ): Resource<ActionResponse> {
        return remoteRepository.addBankCertificateData(
            customerFinancialProfileId,
            NetIncome
        )
    }

    override suspend fun isFinancialProfileCompleted(): Resource<FinancialProfileCompleted> {
        return remoteRepository.isFinancialProfileCompleted()
    }

    override suspend fun getNationalIdAttachments(
        FinancialProfileId: String,
        IsFaceImage: Boolean
    ): ResponseBody? {
        return remoteRepository.getNationalIdAttachments(FinancialProfileId, IsFaceImage)
    }

    override suspend fun deleteAttachment(
        fileId: String,
//        financialProfileId: String,
//        categoryId: String
    ): Resource<BaseResponse> {
        return remoteRepository.deleteAttachment(fileId)
    }

    override suspend fun getCategoryProviders(
        categoryId: Int,
        page: Int,
        pageSize: Int, filter: String?
    ): Resource<SalamtakListResponse<MedicalProvider>> {
        return remoteRepository.getCategoryProviders(categoryId, page, pageSize, filter)
    }

    override suspend fun getOperationDetails(
        operationId: String
    ): Resource<OperationDetails> {
        return remoteRepository.getOperationDetails(operationId)
    }

    override suspend fun searchHealth(
        query: String, page: Int, pageSize: Int
    ): Resource<SalamtakListResponse<Operation>> {
        return remoteRepository.searchHealth(query, page, pageSize)
    }

    override suspend fun CreateCustomInsuranceBooking(
        fullName: String,
        phoneNumber: String,
        insuranceCompanyName: String,
        insuranceType: Int,
        monthlyInstallment: String, downPayment: String, installmentTypeId: String,
        price: String
    ): Resource<ActionResponse> {
        return remoteRepository.CreateCustomInsuranceBooking(
            fullName,
            phoneNumber,
            insuranceCompanyName,
            insuranceType,
            monthlyInstallment,
            downPayment, installmentTypeId, price
        )
    }

    override suspend fun CreateCustomEducationBooking(input: EducationBookingInput): Resource<ActionResponse> {
        return remoteRepository.CreateCustomEducationBooking(input)
    }

    override suspend fun getInstallmentsLookup(type: String): Resource<InstallmentTypesData> {
        return remoteRepository.getInstallmentsLookup(type)
    }

    override suspend fun getLatestAdded(
        page: Int,
        pageSize: Int
    ): Resource<SalamtakListResponse<MedicalNetwork>> {
        return remoteRepository.getLatestAdded(page, pageSize)
    }


    override suspend fun getProvidersNames(categoryId: String): Resource<List<MedicalProvider>> {
        return remoteRepository.getProvidersNames(categoryId)
    }

    override suspend fun getPreHealthCategoryFilter(categoryId: String): Resource<FilterData> {
        return remoteRepository.getPreHealthCategoryFilter(categoryId)
    }

    override suspend fun getPreHealthAdvancedFilter(): Resource<FilterData> {
        return remoteRepository.getPreHealthAdvancedFilter()
    }

    override suspend fun categoryHealthAdvancedSearch(
        categoryId: String,
        subCategoryId: String,
        medicalProvider: String,
        cityId: String,
        startPrice: String,
        endPrice: String,
        page: Int,
        pageSize: Int
    ): Resource<SalamtakListResponse<Operation>> {
        return remoteRepository.categoryHealthAdvancedSearch(
            categoryId,
            subCategoryId,
            medicalProvider,
            cityId,
            startPrice,
            endPrice,
            page,
            pageSize
        )
    }

    override suspend fun healthAdvancedSearch(
        categoryId: String,
        subCategoryId: String,
        medicalProvider: String,
        cityId: String,
        startPrice: String,
        endPrice: String,
        Operation: String,
        page: Int,
        pageSize: Int
    ): Resource<SalamtakListResponse<Operation>> {
        return remoteRepository.healthAdvancedSearch(
            categoryId, subCategoryId,
            medicalProvider,
            cityId,
            startPrice,
            endPrice,
            Operation,
            page,
            pageSize
        )
    }

    //health
    override suspend fun AddCategoryHealthBookingToCart(
        requestBody: HealthCategoryRequestBody,
    ): Resource<ActionResponse> {
        return remoteRepository.AddCategoryHealthBookingToCart(requestBody)
    }
    //health custom...
    override suspend fun AddCustomHealthBookingToCart(
        requestBody: HealthCustomRequestBody,
    ): Resource<ActionResponse> {
        return remoteRepository.AddCustomHealthBookingToCart(requestBody)
    }

    //finishing
    override suspend fun AddCategoryFinishingBookingToCart(
        requestBody: FinishingCategoryRequestBody,
    ): Resource<ActionResponse> {
        return remoteRepository.AddCategoryFinishingBookingToCart(requestBody)
    }

    //finishing custom...
    override suspend fun AddCustomFinishingBookingToCart(
        requestBody: FinishingCustomRequestBody,
    ): Resource<ActionResponse> {
        return remoteRepository.AddCustomFinishingBookingToCart(requestBody)
    }

    //car
    override suspend fun AddCategoryCarBookingToCart(
        requestBody: CarCategoryRequestBody,
    ): Resource<ActionResponse> {
        return remoteRepository.AddCategoryCarBookingToCart(requestBody)
    }

    //car custom...
    override suspend fun AddCustomCarBookingToCart(
        requestBody: CarCustomRequestBody,
    ): Resource<ActionResponse> {
        return remoteRepository.AddCustomCarBookingToCart(requestBody)
    }

    //Education...
    override suspend fun AddCustomEducationBookingToCart(
        requestBody: EducationCustomRequestBody,
    ): Resource<ActionResponse> {
        return remoteRepository.AddCustomEducationBookingToCart(requestBody)
    }

    override suspend fun AddEducationBookingToCart(
        requestBody: EducationRequestBody,
    ): Resource<ActionResponse> {
        return remoteRepository.AddEducationBookingToCart(requestBody)
    }


    //wedding...
    override suspend fun AddCustomWeddingBookingToCart(
        requestBody: WeddingRequestBody,
    ): Resource<ActionResponse> {
        return remoteRepository.AddCustomWeddingBookingToCart(requestBody)
    }


    override suspend fun CreateCustomWeddingBooking(
        fullName: String,
        phoneNumber: String,
        venueName: String,
        inviteesNumber: Int,
        monthlyInstallment: String,
        downPayment: String,
        installmentTypeId: String,
        price: String
    ): Resource<ActionResponse> {
        return remoteRepository.CreateCustomWeddingBooking(
            fullName,
            phoneNumber,
            venueName,
            inviteesNumber,
            monthlyInstallment,
            downPayment,
            installmentTypeId,
            price
        )
    }


    override suspend fun getMoreDoctors(
        subCategoryId: String,
        page: Int,
        pageSize: Int
    ): Resource<SalamtakListResponse<MedicalProvider>> {
        return remoteRepository.getMoreDoctors(subCategoryId, page, pageSize)
    }

    override suspend fun getCategories(maincategoryId: Int): Resource<SalamtakListResponse<Category>> {
        return remoteRepository.getCategories(maincategoryId)
    }

    override suspend fun getAllEducationByCategoryId(
        categoryId: Int, page: Int, pageSize: Int
    ): Resource<SalamtakObjectListResponse<EducationSubcategoriesData>> {
        return remoteRepository.getAllEducationByCategoryId(categoryId, page, pageSize)
    }


    override suspend fun getAllEducationBySubCategoryId(
        SubCategoryId: Int, page: Int, pageSize: Int
    ): Resource<SalamtakListResponse<School>> {
        return remoteRepository.getAllEducationBySubCategoryId(SubCategoryId, page, pageSize)
    }


    override suspend fun getSchoolDetailsById(schoolId: String): Resource<SalamtakResponse<SchoolDetails>> {
        return remoteRepository.getSchoolDetailsById(schoolId)
    }

    override suspend fun createSchoolBooking(input: SchoolRequestBookingInput): Resource<ActionResponse> {
        return remoteRepository.createSchoolBooking(input)
    }

    override suspend fun createUniversityBooking(input: UniversityRequestBookingInput): Resource<ActionResponse> {
        return remoteRepository.createUniversityBooking(input)
    }

    override suspend fun createInstituteBooking(input: UniversityRequestBookingInput): Resource<ActionResponse> {
        return remoteRepository.createInstituteBooking(input)
    }

    override suspend fun getUniversitiesAndInstitutes(
        categoryId: Int,
        page: Int,
        pageSize: Int
    ): Resource<SalamtakListResponse<University>> {
        return remoteRepository.getUniversitiesAndInstitutes(categoryId, page, pageSize)
    }

    override suspend fun getCollages(
        id: String,
        type: Int,
        page: Int,
        pageSize: Int
    ): Resource<SalamtakResponse<University>> {
        return remoteRepository.getCollages(id, type, page, pageSize)
    }

    override suspend fun getCollageDetails(id: String): Resource<SalamtakResponse<Collage>> {
        return remoteRepository.getCollageDetails(id)
    }

    override suspend fun VOTP(
        otp: String,
        bookingId: String, referenceNumber: String
    ): Resource<BaseResponse> {
        return remoteRepository.VOTP(otp, bookingId, referenceNumber)
    }

    override suspend fun GOTP(
        cardNumber: String,
        expiry: String,
        BookingId: String
    ): Resource<SalamtakResponse<PremiumData>> {
        return remoteRepository.GOTP(cardNumber, expiry, BookingId)
    }

    override suspend fun GTRD(referenceNumber: String): Resource<SalamtakResponse<TransactionDetails>> {
        return remoteRepository.GTRD(referenceNumber)
    }

    override suspend fun getFinancialProgress(): Resource<BaseResponse> {
        return remoteRepository.getFinancialProgress()
    }

    override suspend fun financialPreStepOne(): Resource<PreStep1Data> {
        return remoteRepository.financialPreStepOne()
    }

    override suspend fun financialPreStepThree(id: String): Resource<Step3Data> {
        return remoteRepository.financialPreStepThree(id)
    }

    override suspend fun financialPreStepTwo(id: String): Resource<PreStep2Data> {
        return remoteRepository.financialPreStepTwo(id)
    }

    override suspend fun postStepOne(input: FinancialProfile): Resource<SalamtakResponse<IdObject>> {
        return remoteRepository.postStepOne(input)
    }

    override suspend fun postStepTwo(
        input: Step2
    ): Resource<ActionResponse> {
        return remoteRepository.postStepTwo(
            input
        )
    }

    override suspend fun postStepThree(input: Step3Data): Resource<ActionResponse> {
        return remoteRepository.postStepThree(input)
    }

    override suspend fun getFinishingProviders(
        categoryId: Int?,
        page: Int,
        pageSize: Int,
        query: String,
        sort: Int?
    ): Resource<SalamtakObjectListResponse<FinishingProvidersData>> {
        return remoteRepository.getFinishingProviders(categoryId, page, pageSize, query, sort)
    }

    override suspend fun getFinishingCategories(
        page: Int,
        pageSize: Int,
    ): Resource<SalamtakListResponse<FinishingCategoryModel>>{
        return remoteRepository.getFinishingCategories(page, pageSize)
    }

    override suspend fun getFinishingProviderDetails(id: String): Resource<SalamtakResponse<FinishingProvider>> {
        return remoteRepository.getFinishingProviderDetails(id)
    }

    override suspend fun createCustomFinishingBooking(input: FinishingCustomRequestBookingInput): Resource<ActionResponse> {
        return remoteRepository.createCustomFinishingBooking(input)
    }

    override suspend fun createFinishingBooking(input: FinishingRequestBookingInput): Resource<ActionResponse> {
        return remoteRepository.createFinishingBooking(input)
    }

    ///cars
    override suspend fun createCarServiceBooking(input: CarServiceRequestBookingInput): Resource<ActionResponse> {
        return remoteRepository.createCarServiceBooking(input)
    }

    override suspend fun createCustomCarServiceBooking(input: CarCustomRequestBookingInput): Resource<ActionResponse> {
        return remoteRepository.createCustomCarServiceBooking(input)
    }

    override suspend fun getCarServiceCenterDetails(id: String): Resource<SalamtakResponse<CarServiceCenter>> {
        return remoteRepository.getCarServiceCenterDetails(id)
    }

    override suspend fun getCarProviderDetails(id: String): Resource<SalamtakResponse<CarProviderDetails>> {
        return remoteRepository.getCarProviderDetails(id)
    }
    override suspend fun getCarProvidersViewAll(page: Int,pageSize: Int,categoryId: Int
    ): Resource<SalamtakObjectListResponse<CarProvidersData>> {
        return remoteRepository.getCarProvidersViewAll(page,pageSize,categoryId)
    }

    override suspend fun getCarServiceCenters(input: GetCarServiceInput
    ): Resource<SalamtakListResponse<CarCategoryModel>> {
        return remoteRepository.getCarServiceCenters(input)
    }

    override suspend fun getPackageDetails(id: String): Resource<FinishingPackage> {
        return remoteRepository.getPackageDetails(id)
    }

    override suspend fun getCarBrands(): Resource<SalamtakListResponse<CarModel>>
    {
        return remoteRepository.getCarBrands()
    }

    override suspend fun getAllCities(): Resource<SalamtakListResponse<City>>
    {
        return  remoteRepository.getCities()
    }

    override suspend fun getCitiesForCarFilter(): Resource<SalamtakListResponse<City>>
    {
        return remoteRepository.getCitiesForCarFilter()
    }

//    override suspend fun createCustomCarBooking(input: CarCustomRequestBookingInput): Resource<ActionResponse> {
//        return remoteRepository.createCustomCarBooking(input)
//    }
     override suspend fun getFeaturedCategories(): Resource<FeaturedCategoriesResponse> {
    return remoteRepository.getFeaturedCategories()
    }
    override suspend fun getAdvertisements(): Resource<AdvertisementsResponse> {
        return remoteRepository.getAdvertisements()
    }

    override suspend fun getPromotions(): Resource<PromotionsResponse> {
        return remoteRepository.getPromotions()
    }

    override suspend fun getTopProviders(): Resource<TopProvidersResponse> {
        return remoteRepository.getTopProviders()
    }

    override suspend fun getServicesCategories(): Resource<ServicesCategoriesResponse> {
        return remoteRepository.getServicesCategories()
    }

    override suspend fun addCustomInsuranceToCart(requestBody: InsuranceRequestBody, ): Resource<ActionResponse> {
        return remoteRepository.addCustomInsuranceToCart(requestBody)
    }

    override suspend fun GetCartData(cartUID :String): Resource<GetCartResponse> {
        return remoteRepository.GetCartData(cartUID)
    }
    override suspend fun deleteCartItem(ItemId :String):Resource<BaseResponse>{
        return remoteRepository.deleteCartItem(ItemId)
    }

    override suspend fun checkoutCart(cartUID :String):Resource<BaseResponse>{
        return remoteRepository.checkoutCart(cartUID)
    }

    override suspend fun getCartCount(cartUID :String):Resource<cartCountResponse>{
        return remoteRepository.getCartCount(cartUID)
    }


}