package com.salamtak.app.utils

import android.app.Activity
import android.text.Html
import android.util.Patterns
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.salamtak.app.R
import org.junit.Assert
import java.util.*

class Validation {
    companion object {
        //Email Pattern
        private const val EMAIL_PATTERN = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")

        fun isValidString(string: String): Boolean {
            return string.trim { it <= ' ' }.isNotEmpty()
        }

        fun isValidEmail(email: String): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        var NATIONAL_ID_REG_EXP_PATTERN = "^[0-9]{14}$".toRegex()
        var EGYPT_PHONE_REG_EXP_PATTERN = "^01[0,1,2,5]{1}[0-9]{8}$".toRegex()

        fun isValidPhone(phone: String): Boolean {
            return EGYPT_PHONE_REG_EXP_PATTERN.matches(phone)
//            return phone.startsWith("01") && phone.length == 11
        }

        fun isValidIdNumber(idNumber: String): Boolean {
            return NATIONAL_ID_REG_EXP_PATTERN.matches(idNumber)
        }

        fun isValidPassword(charNum: Int, edt: EditText, activity: Activity): Boolean {
            val message: String
            return if (edt.text.toString().isEmpty()) {
                message = activity.getString(R.string.require_field)
                edt.error = Html.fromHtml("<fonts color='red'>$message</fonts>")
                false
            } else if (edt.text.toString().trim { it <= ' ' }.length < charNum) {
                message = activity.getString(R.string.NewPasswordLessThan4)
                edt.error = Html.fromHtml("<fonts color='red'>$message</fonts>")
                false
            } else true
        }

        fun isValidPassword(text: String, count: Int): Boolean {
            return text.length >= count
        }

        fun isValidPassword(
            charNum: Int,
            edt: EditText,
            activity: Activity,
            textInputLayout: TextInputLayout
        ): Boolean {
            val message: String
            return if (edt.text.toString().isEmpty()) {
                message = activity.getString(R.string.require_field)
                textInputLayout.error = message
                false
            } else if (edt.text.toString().trim { it <= ' ' }.length < charNum) {
                message = activity.getString(R.string.NewPasswordLessThan4)
                textInputLayout.error = message
                false
            } else {
                textInputLayout.error = null
                true
            }
        }

        fun ConfirmPassword(
            password: EditText,
            confirmPassword: EditText,
            message: String
        ): Boolean {
            var pStatus = false
            if (!confirmPassword.text.toString().isEmpty()
                && !password.text.toString().isEmpty()
            ) {
                if (password.text.toString().trim { it <= ' ' }
                    == confirmPassword.text.toString().trim { it <= ' ' }
                ) {
                    pStatus = true
                } else {
                    confirmPassword.error = Html
                        .fromHtml("<fonts color='red'>$message</fonts>")
                }
            }
            return pStatus
        }

        fun isValidBirthDate(
            selectedDate: Date?,
            message: String,
            view: TextView
        ): Boolean {
            val calendar = Calendar.getInstance()
            var date: Calendar? = null
            if (selectedDate != null) {
                date = Calendar.getInstance()
                date.time = selectedDate
            }
            if (date != null && date.time != null && (date.time.before(calendar.time)
                        || !date.time.after(calendar.time))
            ) {
                return true
            } else {
                view.error = Html
                    .fromHtml("<fonts color='red'>$message</fonts>")
            }
            return false
        }

        fun validateEmail(email: String): Boolean {
            return if (email.contains('@')) {
                Patterns.EMAIL_ADDRESS.matcher(email).matches()
            } else {
                email.isNotBlank()
            }
        }

        fun isValidUserName(text: String): Boolean {
            return text.length >= 3
        }

    }

}