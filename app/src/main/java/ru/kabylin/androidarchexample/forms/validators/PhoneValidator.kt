package ru.kabylin.androidarchexample.forms.validators

import android.content.res.Resources
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import ru.kabylin.androidarchexample.R

class PhoneValidator(resources: Resources? = null): Validator(resources) {
    override fun getErrorText(value: Any?): String =
        errorFromRes(R.string.phone_validation_error)

    override fun isValid(value: Any?): Boolean {
        val phoneUtil = PhoneNumberUtil.getInstance()
        val acceptableSymbols = "+0123456789"

        return when (value) {
            is CharSequence -> {
                value.forEach {
                    if (it !in acceptableSymbols)
                        return false
                }

                try {
                    val numberProto = phoneUtil.parse(value.toString(), null)
                    phoneUtil.isValidNumber(numberProto)
                } catch (e: NumberParseException) {
                    false
                }
            }
            else -> throw UnsupportedOperationException()
        }
    }
}
