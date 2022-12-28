package com.alhamoly.redditnews.utils

import android.util.Patterns
import android.widget.EditText
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Validator {

    fun validateET(mEditText: EditText, msg: String?=null): Boolean {
        return if (!mEditText.text.isNullOrEmpty()) {
            true
        } else {
            mEditText.error = msg ?: mEditText.hint
            false
        }

    }
//
//    fun validateOtp(otp: OtpTextView): Boolean {
//        return if (!otp.otp.isNullOrEmpty() && otp.otp?.length == 6) {
//            true
//        } else {
//            otp.showError()
//            false
//        }
//
//    }

    fun validatePasswordET(mEditText: EditText, msg: String?): Boolean {
        return if (!mEditText.text.isNullOrEmpty() && mEditText.text.toString().length >= 8) {
            true
        } else {
            mEditText.error = msg ?: mEditText.hint
            false
        }

    }

    fun validateVisaPasswordET(mEditText: EditText, msg: String?): Boolean {
        return if (!mEditText.text.isNullOrEmpty() && mEditText.text.toString().length == 3) {
            true
        } else {
            mEditText.error = msg ?: mEditText.hint
            false
        }

    }

    fun validatePhoneET(mEditText: EditText, msg: String?): Boolean {
        return if (!mEditText.text.isNullOrEmpty() && mEditText.text.toString().length >= 9) {
            true
        } else {
            mEditText.error = msg ?: mEditText.hint
            false
        }

    }

    fun validateEmailET(mEditText: EditText, msg: String?): Boolean {
        return if (!mEditText.text.isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(mEditText.text.toString())
                .matches()
        ) {
            true
        } else {
            mEditText.error = msg ?: mEditText.hint
            false
        }

    }

    fun validateMatchPassword(mEditText1: EditText, mEditText2: EditText, msg: String?): Boolean {
        return if (!mEditText1.text.isNullOrEmpty() && !mEditText2.text.isNullOrEmpty() && mEditText1.text.toString() == mEditText2.text.toString()) {
            true
        } else {
            mEditText1.error = msg ?: mEditText1.hint
            mEditText2.error = msg ?: mEditText2.hint
            false
        }

    }

    fun isValidDate(mEditText: EditText, msg: String?): Boolean {

        if (mEditText.text.isNullOrEmpty()) return false

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        simpleDateFormat.isLenient = false

        try {
            simpleDateFormat.parse(mEditText.text.toString().trim { it <= ' ' })
        } catch (pe: ParseException) {
            mEditText.error = msg ?: mEditText.hint
            return false
        }
        return true
    }

}