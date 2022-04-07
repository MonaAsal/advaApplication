package com.salamtak.app.utils

import android.graphics.Bitmap
import android.text.format.DateUtils
import android.util.Log
import com.salamtak.app.App
import com.salamtak.app.R
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


fun toDecimalNumberFormat(number: Double): String {
    return if (Locale.getDefault().language == Constants.ARABIC_LOCALE) {
        var otherSymbols = DecimalFormatSymbols(Locale.ENGLISH)
        otherSymbols.decimalSeparator = ','
        var formatter = DecimalFormat("###,###", otherSymbols)
        formatter.format(number)
    } else {
        val formatter = DecimalFormat("###,###")
        formatter.format(number)
    }
}

fun convertDateFormat(string: String): String {
    var strDate = ""
    try {
        val simpleDateFormat = SimpleDateFormat("d/M/yyyy", Locale.ENGLISH)
        val myDate = simpleDateFormat.parse(string)
        val afterconversion = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        strDate = afterconversion.format(myDate)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return strDate
}

fun getCurrentDate(): String {

    try {
        val c = Calendar.getInstance(Locale.ENGLISH).time
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        return df.format(c)

    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

fun isFutureDate(dateStr: String): Boolean {

    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
    val myDate = simpleDateFormat.parse(dateStr)

//    val c = GregorianCalendar()
//    c.add(Calendar.DATE, 1)
//    c[Calendar.HOUR_OF_DAY] = 0
//    c[Calendar.MINUTE] = 0
//    c[Calendar.SECOND] = 0
//    c[Calendar.MILLISECOND] = 0
//    val today = c.time

    val c = Calendar.getInstance()
    c.add(Calendar.DATE, 1)
    c[Calendar.HOUR_OF_DAY] = 0
    c[Calendar.MINUTE] = 0
    c[Calendar.SECOND] = 0
    c[Calendar.MILLISECOND] = 0
    val tomorrow = c.time

    if (myDate.before(tomorrow)) {
        System.err.println("Date specified [$myDate] is before today [$tomorrow]")
        return false
    } else {
        System.err.println("Date specified [$myDate] is NOT before today [$tomorrow]")
        return true
    }
}

fun convertDateFormat(date: String, formatFrom: String, formatTo: String): String {
    var strDate = ""
    try {
        val simpleDateFormat = SimpleDateFormat(formatFrom, Locale.ENGLISH)
        val myDate = simpleDateFormat.parse(date)
        val afterconversion = SimpleDateFormat(formatTo, Locale.ENGLISH)
        strDate = afterconversion.format(myDate)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return strDate
}

fun convertDateFormat(date: String, format: String): String {
    var strDate = ""
    try {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss", Locale.ENGLISH)
        val myDate = simpleDateFormat.parse(date)
        val afterconversion = SimpleDateFormat(format, Locale.ENGLISH)
        strDate = afterconversion.format(myDate)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return strDate
}

fun convertDateFormatTimeAgo(date: String, format: String): String {
    val simpleDateFormat =
        SimpleDateFormat(format)
    val date: Date = simpleDateFormat.parse(date)
    var niceDateStr = DateUtils.getRelativeTimeSpanString(
        date.time,
        Calendar.getInstance().timeInMillis,
        DateUtils.MINUTE_IN_MILLIS
    ) as String

    if (niceDateStr.startsWith("-")) {
        niceDateStr = niceDateStr.removePrefix("-") + " " + App.context.getString(R.string.ago)
    }

    if (niceDateStr.contains("minutes"))
        niceDateStr = niceDateStr.replace("minutes", "m")
    else if (niceDateStr.contains("hours"))
        niceDateStr = niceDateStr.replace("hours", "h")

    return niceDateStr

}

fun arabicNumberToEnglish(string: String): String {
    if (isProbablyArabic(string).not()) {
        return string
    } else {
        var english = StringBuilder()
        for (char in string) {
            when (char) {
                '٠' ->
                    english.append("0")
                '١' ->
                    english.append("1")
                '٢' ->
                    english.append("2")
                '٣' ->
                    english.append("3")
                '٤' ->
                    english.append("4")
                '٥' ->
                    english.append("5")
                '٦' ->
                    english.append("6")
                '٧' ->
                    english.append("7")
                '٨' ->
                    english.append("8")
                '٩' ->
                    english.append("9")
            }
        }

        return english.toString()
    }
}

fun isProbablyArabic(s: String): Boolean {
    var i = 0
    while (i < s.length) {
        val c = s.codePointAt(i)
        if (c in 0x0600..0x06E0) return true
        i += Character.charCount(c)
    }
    return false
}

fun scaleDown(
    realImage: Bitmap, maxImageSize: Float,
    filter: Boolean
): Bitmap {
    val ratio = Math.min(
        maxImageSize / realImage.width,
        maxImageSize / realImage.height
    )
    val width = Math.round(ratio * realImage.width)
    val height = Math.round(ratio * realImage.height)

    return Bitmap.createScaledBitmap(
        realImage, width,
        height, filter
    )
}
