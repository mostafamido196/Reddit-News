package com.alhamoly.redditnews.utils

import android.annotation.SuppressLint
import android.util.Log
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CompositeDateValidator
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import okhttp3.internal.UTC
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "TimeUtils"

object TimeUtils {

    val currentYear: Int
        get() = Calendar.getInstance(Locale("en")).get(Calendar.YEAR)

    private val mCalendar = Calendar.getInstance()
    private val currentTime: Long = mCalendar.timeInMillis
    private fun currentTime(): Long {

        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val timeInMili = calendar.timeInMillis
        return timeInMili
    }

    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    private val fullDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
    private val timeOnlyFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)

    fun simpleDateFormat(calendar: Calendar): String = simpleDateFormat.format(calendar.time)
    fun fullDateFormat(calendar: Calendar): String = fullDateFormat.format(calendar.time)
    fun fullTimeFormat(): String = timeOnlyFormat.format(currentTime)
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

    @SuppressLint("ConstantLocale")
    private val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

    fun getDateOnly(calendar: Calendar): String = dateFormat.format(calendar.time)

    fun getTimeOnly(calendar: Calendar): String = timeFormat.format(calendar.time)

    @SuppressLint("ConstantLocale")
    private val simpleDateFullFormat = SimpleDateFormat("E,dd-MMM-yyyy", Locale.getDefault())

    private fun maxDate(amount: Int): Long {

        val a = Calendar.getInstance()
        a.add(Calendar.DATE, amount)

        return a.timeInMillis

    }

    private fun constraintsBuilder(plusAmount: Int): CalendarConstraints.Builder {

        val maxDate = maxDate(plusAmount)

        val constraintsBuilder = CalendarConstraints.Builder()
        val validators: ArrayList<CalendarConstraints.DateValidator> = ArrayList()
        validators.add(DateValidatorPointForward.from(currentTime))
        validators.add(DateValidatorPointBackward.before(maxDate))
        constraintsBuilder.setValidator(CompositeDateValidator.allOf(validators))

        return constraintsBuilder

    }

    @SuppressLint("SimpleDateFormat")
    fun String.dateFormat(): String {
        Log.e(TAG, "dateFormat: o $this")

        return try {

            val originalFormat: DateFormat =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH)
            val targetFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val date: Date = originalFormat.parse(this) as Date
            val formattedDate: String = targetFormat.format(date) // 20120821

            formattedDate
        } catch (e: Exception) {
            Log.e(TAG, "dateFormat: $e")
            this
        }

    }

    @SuppressLint("SimpleDateFormat")
    fun String.newsDateFormat(): String {
        Log.e(TAG, "dateFormat: o $this")

        return try {

            val originalFormat: DateFormat =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH)
            val targetFormat: DateFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
            val date: Date = originalFormat.parse(this) as Date
            val formattedDate: String = targetFormat.format(date) // 20120821

            formattedDate
        } catch (e: Exception) {
            Log.e(TAG, "dateFormat: $e")
            this
        }

    }

    fun String.subtract2Dates(): String {

        val sourceFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH)
        sourceFormat.timeZone = TimeZone.getTimeZone("UTC")
        val parsed = sourceFormat.parse(this)

        val tz = TimeZone.getDefault()
        val destFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH)
        destFormat.timeZone = tz

        val result = destFormat.parse(destFormat.format(parsed ?: Date(0)))
        val final = if (result != null) currentTime - result.time
        else 0


        val allSeconds = final / 1000
        val hours: Long = allSeconds / (60 * 60)
        val minutes = (allSeconds % (60 * 60)) / 60
        val seconds = (allSeconds % (60 * 60)) / 60

        val formattedDate = if (hours > 23) "${hours / 24} d" else "$hours h $minutes m $seconds s"

        Log.e(TAG, "subtract2Dates 0: $this")
        Log.e(TAG, "subtract2Dates 3: $formattedDate")

        return formattedDate
    }

    fun dateFormat(calendar: Calendar): String = simpleDateFullFormat.format(calendar.time)

    fun dateFormat(y: Int, m: Int, d: Int): String {

        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, y)
        cal.set(Calendar.MONTH, m)
        cal.set(Calendar.DAY_OF_MONTH, d)

        return ""

    }

}