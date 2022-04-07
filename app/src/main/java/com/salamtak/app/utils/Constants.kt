package com.salamtak.app.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.provider.Settings
import android.text.InputFilter
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.salamtak.app.App.Companion.context
import com.salamtak.app.R
import com.salamtak.app.data.entities.FinancialTypeAttachments

/**
 * Created by Radwa Elsahn on 3/21/2020
 */

class Constants {
    companion object INSTANCE {
        val DEVICE_ID: String = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

        val IS_FROM_FISCOUNT = "IS_FROM_FISCOUNT"
        val TYPE_CASH = 1
        val TYPE_CARD = 2
        val GALLERY = 1
        val CAMERA = 2
        val TYPE_FOR_ME = 1
        val TYPE_FOR_OTHER = 2
        val PAGE_SIZE = 10
        val PAGE_SIZE_BIG = 100
        val IMAGE_CORNER_SMALL = 6
        val IMAGE_CORNER = 20
        val IMAGE_CORNER2 = 30
        val IMAGE_CORNER_CIRCLE = 70
        const val SPLASH_DELAY = 500
        const val PULSE_ANIMATION_TIME = 310

        val TYPE_PHONE = 1
        val TYPE_MAIL = 2

        val secret_key_name = "secret_key_name"

        val SALAMTAK_PHONE_NUMBER = "15864"

        const val CATEGORY_ITEM_KEY = "CATEGORY_ITEM_KEY"
        const val KEY_OPERATION_ITEM = "OPERATION_ITEM_KEY"
        const val KEY_AMOUNT = "KEY_AMOUNT"
        const val HOME_VISIT_ITEM_KEY = "HOME_VISIT_ITEM_KEY"
        const val NEED_FINANCIAL_INFO = "NEED_FINANCIAL_INFO"
        const val DOCTOR_ITEM_KEY = "DOCTOR_ITEM_KEY"
        const val DOCTOR_NAME = "DOCTOR_NAME"
        const val BRANCH_ITEM_KEY = "BRANCH_ITEM_KEY"
        const val MEDICAL_PROVIDER_ITEM_KEY = "MEDICAL_PROVIDER_ITEM_KEY"
        const val KEY_PROVIDER_NAME = "KEY_PROVIDER_NAME"
        const val QUERY_KEY = "QUERY_KEY"
        const val AREA_KEY = "AREA_KEY"
        const val CATEGORY_ID_KEY = "CATEGORY_ID_KEY"
        const val SubCATEGORY_ID_KEY = "CATEGORY_ID_KEY"

        const val KEY_CONTACTS = "KEY_CONTACTS"
        const val FROM = "FROM"
        const val ADVANCED = "ADVANCED"

        const val IMAGES_LIMIT = 3
        const val PhoneNumRegEx = "[01]\\d{9}$"
        const val password_lenght = 6
        const val HOME_VISIT_PRICE = 500

        const val ENGLISH_LOCALE = "en"
        const val ARABIC_LOCALE = "ar"

        const val LANG_SENT = "LANG_SENT"

        //const val KEY_LOCALE = "KEY_LOCALE"
        const val KEY_FROM_RECOVERY = "KEY_FROM_RECOVERY"
        const val KEY_VERIFICATION_CODE = "KEY_VERIFICATION_CODE"
        const val KEY_ID = "KEY_ID"
        const val KEY_REFERENCE = "KEY_REFERENCE"
        const val KEY_EXPIRY = "KEY_EXPIRY"
        const val KEY_CARD_NUM = "KEY_CARD_NUM"
        const val KEY_UNIVERSITY_ID = "KEY_UNIVERSITY_ID"
        const val KEY_NAME = "KEY_NAME"
        const val KEY_CATEGORY_ID = "KEY_CATEGORY_ID"
        const val KEY_FROM = "KEY_FROM"
        const val KEY_PHONE = "KEY_PHONE"
        const val KEY_URL = "KEY_URL"
        const val TYPE_VISA = "visa"
        const val KEY_FROM_HOME_VISIT = "KEY_FROM_HOME_VISIT"
        const val KEY_TITLE = "KEY_TITLE"
        const val KEY_TEXT = "KEY_TEXT"
        const val KEY_ICON = "KEY_ICON"
        const val WHO_WE_ARE = "WHO_WE_ARE"
        const val KEY_TO = "KEY_TO"
        const val TYPE_DOCTOR_REVIEW = 1
        const val TYPE_PROVIDER_REVIEW = 2
        const val TYPE_OPERATION_REVIEW = 3
        const val TYPE_OPERATION_REVIEW2 = 4
        const val KEY_INVALID_TOKEN = "invalid token"
        const val WORK_MANAGER_NAME = "WORK_MANAGER_NAME"
        const val TAG_OUTPUT = "TAG_OUTPUT"
        const val KEY_WORKER_INPUT = "KEY_WORKER_INPUT"
        val KEY_YES_TEXT = "KEY_YES_TEXT"
        val KEY_NO_TEXT = "KEY_NO_TEXT"
        val KEY_IMAGE = "KEY_IMAGE"
        val KEY_GUARANTOR = "KEY_GUARANTOR"

        val DATABASE_NAME = "salamtakDb1"
        val forceUpdate = "forceUpdate"

        const val KEY_EMPLOYMENT_DATA = "KEY_EMPLOYMENT_DATA"
        const val KEY_EMPLOYMENT_IMAGES = "KEY_EMPLOYMENT_IMAGES"

        const val KEY_SUBCATEGORY_ID = "KEY_SUBCATEGORY_ID"
        const val KEY_CITY_ID = "KEY_CITY_ID"

        const val FINANCIAL_PROFILE_ID = "FINANCIAL_PROFILE_ID"
        const val KEY_SCHOOL_ID = "KEY_SCHOOL_ID"
        const val KEY_TYPE = "KEY_TYPE"
        const val KEY_STEP_2_NONE = "KEY_STEP_2_NONE"

        const val SHARED_PREFS_FILENAME = "adva_biometric_prefs"
        const val CIPHERTEXT_WRAPPER = "ciphertext_wrapper"
        var cartUID = ""
        private val blockCharacterSet = ".(),+-;/.#*"
        val filter =
            InputFilter { source, start, end, dest, dstart, dend ->
                if (source != null && blockCharacterSet.contains("" + source)) {
                    ""

                } else null
            }

//        const val KEY_EMPLOYMENT_DATA = "KEY_EMPLOYMENT_DATA"
//        const val KEY_EMPLOYMENT_DATA = "KEY_EMPLOYMENT_DATA"
//        const val KEY_EMPLOYMENT_DATA = "KEY_EMPLOYMENT_DATA"
//        const val KEY_EMPLOYMENT_DATA = "KEY_EMPLOYMENT_DATA"
//        const val KEY_EMPLOYMENT_DATA = "KEY_EMPLOYMENT_DATA"

        val HOME_VISITS_STATUS = mapOf(
            0 to "Pending",
            1 to "Processing",
            2 to "Waiting doctor confirmation",
            3 to "Waiting patient confirmation",
            4 to "Has problem",
            5 to "Done",
            6 to "Pending for cancel",
            7 to "Cancelled",
            8 to "Waiting for payment confirmation"
        )

//        1-5 + 9 -> in progress
//        7,8,10 -> canceled
//        6 done
//        val OPERATIONS_STATUS = mapOf(
//            0 to context.getString(R.string.pending),
//            1 to context.getString(R.string.processing),
//            2 to context.getString(R.string.processing), //"Financial reviewing",
//            3 to context.getString(R.string.processing),//"Financial approved",
//            4 to context.getString(R.string.processing),//"Medical approved",
//            5 to context.getString(R.string.processing),//"Has problem",
//            6 to context.getString(R.string.done),
//            7 to context.getString(R.string.cancelled),//context.getString(R.string.financial_rejected),
//            8 to context.getString(R.string.cancelled),//"Medical rejected",
//            9 to context.getString(R.string.processing),//"Pending for cancel",
//            10 to context.getString(R.string.cancelled)
//        )


        val PaymentMethod = mapOf(
            1 to context.getString(R.string.cash),
            2 to context.getString(R.string.card)
        )

        val CONTACT_TYPES = mapOf(
            1 to context.getString(R.string.phone),
            2 to context.getString(R.string.email_r),
            3 to context.getString(R.string.fax),
            4 to context.getString(R.string.social),
            5 to context.getString(R.string.website)
        )

        val CONTACT_TYPES_IMAGES =
            mapOf(
                1 to ContextCompat.getDrawable(context, R.drawable.ic_phone),
                2 to ContextCompat.getDrawable(context, R.drawable.ic_doctor),
                3 to ContextCompat.getDrawable(context, R.drawable.ic_phone),
                4 to ContextCompat.getDrawable(context, R.drawable.ic_social),
                5 to ContextCompat.getDrawable(context, R.drawable.ic_website)
            )

        val notificationTypes =
            mapOf(
                1 to context.getString(R.string.operations),
                2 to context.getString(R.string.home_visit),
                3 to context.getString(R.string.social),
                4 to context.getString(R.string.general)
            )

        val notificationTypesIcons =
            mapOf(
                1 to getDrawable(context, R.drawable.ic_health),
                2 to getDrawable(context, R.drawable.ic_home_visits),
                3 to getDrawable(context, R.drawable.ic_offer_notification), // offers
                4 to getDrawable(context, R.drawable.ic_adva_logo), // general
                5 to getDrawable(context, R.drawable.ic_education),
                6 to getDrawable(context, R.drawable.ic_insurance),
                7 to getDrawable(context, R.drawable.ic_wedding),
                8 to getDrawable(context, R.drawable.ic_finishing),
                9 to getDrawable(context, R.drawable.ic_car_service)
////                7 to ContextCompat.getDrawable(context, R.drawable.ic_adva_logo),
////                11 to ContextCompat.getDrawable(context, R.drawable.ic_adva_logo)
            )

        fun getDrawable(context: Context, drawableId: Int): Drawable {
            var drawable: Drawable?

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                drawable = VectorDrawableCompat.create(context.resources, drawableId, context.theme)
            } else {
                drawable = ContextCompat.getDrawable(context, drawableId)
            }

            return drawable!!
        }

        fun DisableDecimalWithMaxLength(length:Int): Array<InputFilter?> {
            val maxLength = length
            val FilterArray = arrayOfNulls<InputFilter>(2)
            FilterArray[0] = InputFilter.LengthFilter(maxLength)
            FilterArray[1] = filter
            return FilterArray
        }


        enum class AttachmentType(val id: Int) {
            EMPLOYMENT(1),
            PENSION(2),
            ASSETS_RENTAL(3),
            BUSINESS_OWNER(4),
            CLUB_MEMBERSHIP(5),
            BANK_CERTIFICATES(6),
            CREDIT_CARD(7),
            CAR_OWNER(8),
            VALUE_CUSTOMER(9)
        }

        val clubMembershipAttachments = listOf(
            FinancialTypeAttachments(5, 1, "Club membership Card front", null, 3),
            FinancialTypeAttachments(5, 2, "Club membership Card back", null, 3),
//            FinancialTypeAttachments(5, 3, "Guarantor ID front", null, 3),
//            FinancialTypeAttachments(5, 4, "Guranator ID back", null, 3)
        )
        val carOwnerAttachments = listOf(
            FinancialTypeAttachments(8, 1, "car license front", null, 3),
            FinancialTypeAttachments(8, 2, "car license back", null, 3),
//            FinancialTypeAttachments(8, 3, "Guarantor ID front", null, 3),
//            FinancialTypeAttachments(8, 4, "Guranator ID back", null, 3)
        )

    }


}