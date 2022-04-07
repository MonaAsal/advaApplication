package com.salamtak.app.data.remote.service

import com.salamtak.app.data.entities.*
import com.salamtak.app.data.entities.responses.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


/**
 * Created by Radwa Elsahn on 3/21/2020
 */

interface ProfileService {

    @FormUrlEncoded
    @PUT("PatientProfiles/employment")
    suspend fun postEmploymentProfile(
        @Field("Profession") Profession: String,
        @Field("CompanyName") CompanyName: String,
        @Field("HiringDate") HiringDate: String,
        @Field("NetMonthlyIncome") NetMonthlyIncome: String,
        @Field("NetMonthlyExpense") NetMonthlyExpense: String,
        @Field("EmploymentTypeId") EmploymentTypeId: Int,
        @Field("EmploymentFieldId") EmploymentFieldId: Int,
        @Field("StreetAddress") StreetAddress: String,
        @Field("BuildingNum") BuildingNum: Int,
        @Field("CityId") CityId: Int,
        @Field("AreaId") AreaId: String
    ): Response<EmploymentProfileResponse>


    @FormUrlEncoded
    @PUT("PatientProfiles/financial")
    suspend fun postFinancialProfile(
        @Field("NumberOfChildren") NumberOfChildren: Int,
        @Field("CarManufactureYear") CarManufactureYear: String,
        @Field("CarModelId") CarModelId: Int,
        @Field("ClubId") ClubId: Int,
        @Field("BankInfoId") BankInfoId: Int,
        @Field("MaritalStatusId") MaritalStatusId: Int,
        @Field("OtherCarModel") OtherCarModel: String,
        @Field("OtherClub") OtherClub: String
    ): Response<FinancialProfileResponse>


    @Multipart
    @PUT("PatientProfiles")
    suspend fun postBasicProfile(
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part Image: MultipartBody.Part?,
        @Part PassportImage: MultipartBody.Part?
    ): Response<BasicProfileInfoResponse>

    @GET("PatientProfiles/employment")
    suspend fun getEmploymentProfile(): Response<EmploymentProfileResponse>

    @GET("PatientProfiles/financial")
    suspend fun getFinancialProfile(): Response<FinancialProfileResponse>

    @GET("PatientProfiles/financial/lookups")
    suspend fun getFinancialLookups(): Response<FinancialLookupsResponse>

    @GET("PatientProfiles")
    suspend fun getBasicProfile(): Response<BasicProfileInfoResponse>

    @POST(" PatientProfiles/financial/guarantor")
    suspend fun addGuarantor(
        @Field("FirstName") FirstName: String,
        @Field("SecondName") SecondName: String,
        @Field("ThirdName") ThirdName: String,
        @Field("LastName") LastName: String,
        @Field("NationalIdNumber") NationalIdNumber: String,
        @Field("NationalIdExpiryDate") NationalIdExpiryDate: String,
        @Field("NationalIdIssuingDate") NationalIdIssuingDate: String,
        @Field("CityId") CityId: String,
        @Field("AreaId") AreaId: String,
        @Field("StreetAddress") StreetAddress: String,
        @Field("Mobile") Mobile: String,
        @Field("MaritalStatusId") MaritalStatusId: String,
        @Field("JobTitle") JobTitle: String,
        @Field("CompanyName") CompanyName: String,
        @Field("CompanyAddress") CompanyAddress: String,
        @Field("RelativeDegree") RelativeDegree: String
    )

    @GET("Patients/Newfavourites")
    suspend fun getFavourites(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int, @Query("orderByAlphabetical") alphabetical: String
    ): Response<SalamtakListResponse<Operation>>

//    @GET("userAccount")
//    suspend fun getProfileInfo(): Response<ProfileResponse>

    @Multipart
    @PUT("patients/account")
    suspend fun updateProfileInfo(
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part Image: MultipartBody.Part?
    ): Response<SalamtakResponse<UpdateProfileResponse>>

    @FormUrlEncoded
    @POST("reviews/contactUs")
    suspend fun contactUs(
        @Field("Phone") Phone: String,
        @Field("Message") Message: String
    ): Response<ContactUsResponse>

    @FormUrlEncoded
    @POST("userAccount/language")
    suspend fun changeLanguage(
        @Field("Language") language: String
    ): Response<ActionResponse>

    @GET("FinanacialProfiles/precreate-customer-financial-profile")
    suspend fun getPreCreateFinancialProfileInfo(): Response<SalamtakResponse<PreFinancialProfile>>

    @FormUrlEncoded
    @POST("FinanacialProfiles/create-financial-profile")
    suspend fun saveBasicProfile(
        @Field("id") profileId: String?,
        @Field("Mobile") mobile: String,
        @Field("LoanMonthlyAmount") limit1: String,
        @Field("CreditCardLimit") limit: String,
        @Field("FirstName") firstName: String,
        @Field("MiddleName") secondName: String,
        @Field("LastName") thirdName: String,
        @Field("FamilyName") lastName: String,
        @Field("IDNumber") id: String,
        @Field("NationalIDDate") expiryDate: String,
        @Field("StreetAddress") street: String,
        @Field("building") building: String,
        @Field("cityId") cityId: String,
        @Field("areaId") areaId: String,
        @Field("PaymentCard") selectedLoanType: Int,
        @Field("FinancialProviderId") providerId: String,
        @Field("MaritalStatusId") maritalStatusId: String,
        @Field("IDFaceTempName") idFaceTempName: String?,
        @Field("IDBackTempName") idBackTempName: String?
    ): Response<SalamtakResponse<IdObject>>

    @Multipart
    @POST("Files/uploadFiles")
    suspend fun uploadFile(@Part image: MultipartBody.Part?): Response<SalamtakResponse<UploadImagesResponse>>

    @Multipart
    @POST("Files/uploadFiles")
    suspend fun uploadMultipleFiles(
        @Part images: Array<Part>
    ): Response<SalamtakResponse<UploadImagesResponse>>

    @Multipart
    @POST("FinancialProfilesV2/add-category-attachment")
    suspend fun addCategoryAttachment(
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part Image: MultipartBody.Part?
    ): Response<SalamtakResponse<AttachmentData>>


    @Multipart
    @POST("FinanacialProfiles/Upload-Financial-Profile-single-Attachment")
    suspend fun addAttachment(
        @PartMap partMap: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part Image: MultipartBody.Part?
    ): Response<SalamtakResponse<AttachmentData>>


    @FormUrlEncoded
    @POST("FinanacialProfiles/add-financial-profile-employees-category")
    suspend fun addEmployeeData(
        @Field("Job") job: String,
        @Field("Salary") salary: String,
        @Field("CompanyName") companyName: String,
        @Field("CompanyAddress") companyAddress: String,
        @Field("CustomerFinancialProfileId") customerFinancialProfileId: String,
//        @Field("CategoryId") categoryId: String,
//        @Field("BankStatementId") bankStatementId: String,
//        @Field("UtilityBillId") utilityBillId: String,
//        @Field("GuranatorIDFrontAttachmentId") guranatorIDFrontAttachmentId: String?,
//        @Field("GuranatorIDBackAttachmentId") guranatorIDBackAttachmentId: String?
    ): Response<ActionResponse>

    @GET("FinanacialProfiles/get-category")
    suspend fun getFinancialCategoryAttachments(
        @Query("FinancialProfileId") FinancialProfileId: String,
        @Query("CategoryId") categoryId: String
    ): Response<SalamtakResponse<FinancialCategoriesData>>

//    @GET("FinanacialProfiles/get-category-attachment")
//    suspend fun getAttachment(@Query("FileId") fileId: String): Response<File>

    @GET
    suspend fun downloadFileWithDynamicUrlSync(@Url fileUrl: String?): Response<ResponseBody>

    @FormUrlEncoded
    @POST("FinanacialProfiles/add-financial-profile-pension-category")
    suspend fun addPensionData(
        @Field("CustomerFinancialProfileId") customerFinancialProfileId: String,
//        @Field("CategoryId") categoryId: String,
        @Field("PensionNetAmount") amount: String,
//        @Field("Pension1Id") pensionId: String,
//        @Field("Pension2Id") pension2Id: String,
//        @Field("UtilityBillId") utilityBillId: String,
//        @Field("GuranatorIDFrontAttachmentId") guranatorIDFrontAttachmentId: String?,
//        @Field("GuranatorIDBackAttachmentId") guranatorIDBackAttachmentId: String?
    ): Response<ActionResponse>

    @FormUrlEncoded
    @POST("FinanacialProfiles/add-financial-profile-assets-owner-category")
    suspend fun addAssetOwnerData(
        @Field("CustomerFinancialProfileId") customerFinancialProfileId: String,
        @Field("NetIncome") NetIncome: String,
    ): Response<ActionResponse>


    @FormUrlEncoded
    @POST("FinanacialProfiles/add-financial-profile-business-owner-category")
    suspend fun addBusinessOwnerData(
        @Field("CustomerFinancialProfileId") customerFinancialProfileId: String,
        @Field("Job") Job: String,
        @Field("CompanyNetIncome") CompanyNetIncome: String,
        @Field("CompanyName") CompanyName: String,
        @Field("CompanyAddress") CompanyAddress: String,
    ): Response<ActionResponse>

    @FormUrlEncoded
    @POST("FinanacialProfiles/add-financial-profile-club-membership-category")
    suspend fun addClubData(
        @Field("CustomerFinancialProfileId") customerFinancialProfileId: String,
    ): Response<ActionResponse>

    @FormUrlEncoded
    @POST("FinanacialProfiles/add-financial-profile-car-owner-category")
    suspend fun addCarOwnerData(
        @Field("CustomerFinancialProfileId") customerFinancialProfileId: String
    ): Response<ActionResponse>

    @FormUrlEncoded
    @POST("FinanacialProfiles/add-financial-profile-bank-certificate-category")
    suspend fun addBankCertificateData(
        @Field("CustomerFinancialProfileId") customerFinancialProfileId: String,
        @Field("NetIncome") NetIncome: String,
    ): Response<ActionResponse>

    @GET("FinanacialProfiles/isFinancialProfileCompleted")
    suspend fun isFinancialProfileCompleted(): Response<SalamtakResponse<FinancialProfileCompleted>>

    @GET("FinanacialProfiles/get-nationalID-attachment")
    suspend fun getNationalIdAttachments(
        @Query("FinancialProfileId") FinancialProfileId: String,
        @Query("IsFaceImage") IsFaceImage: Boolean
    ): Response<ResponseBody>

    @DELETE("FinanacialProfiles/delete-category-attachment")
    suspend fun deleteAttachment(
        @Query("fileId") fileId: String
//        @Field("FinancialProfileId") financialProfileId: String,
//        @Field("CategoryId") CategoryId: String
    ): Response<BaseResponse>

    @GET("FinancialProfilesV2/Progress")
    suspend fun getFinancialProgress(): Response<SalamtakResponse<FinancialStatus>>

    @GET("FinancialProfilesV2/pre-step-one")
    suspend fun financialPreStepOne(): Response<SalamtakResponse<PreStep1Data>>

    @GET("FinancialProfilesV2/pre-step-two")
    suspend fun financialPreStepTwo(@Query("id") id: String): Response<SalamtakResponse<PreStep2Data>>

    @GET("FinancialProfilesV2/pre-step-three")
    suspend fun financialPreStepThree(@Query("id") id: String): Response<SalamtakResponse<Step3Data>>

    @POST("FinancialProfilesV2/step-one")
    suspend fun postStepOne(@Body input: FinancialProfile): Response<SalamtakResponse<IdObject>>

    @POST("FinancialProfilesV2/step-two")
    suspend fun postStepTwo(
        @Body input: Step2
    ): Response<ActionResponse>

    @POST("FinancialProfilesV2/step-three")
    suspend fun postStepThree(@Body input: Step3Data): Response<ActionResponse>
}
