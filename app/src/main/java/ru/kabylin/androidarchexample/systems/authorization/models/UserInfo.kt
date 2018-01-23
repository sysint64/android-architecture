package ru.kabylin.androidarchexample.systems.authorization.models

import android.support.annotation.StringRes
import ru.kabylin.androidarchexample.R
import java.util.*

/**
 * Внутреннее представление приложения информации о пользователе.
 */
data class UserInfo(
    val id: String = "",
    val phone: String = "",
    val firstName: String? = null,
    val lastName: String? = null,
    val patronymic: String? = null,
    val country: String? = null,
    val city: String? = null,
    val gender: Gender = Gender.NONE,
    val birthDate: Date? = null
) {
    val fullName: String = "$lastName $firstName $patronymic".trim()
}

enum class Gender(
    @StringRes val conciseStringRes: Int? = null,
    @StringRes val stringRes: Int? = null
) {
    NONE,
    MALE(R.string.concise_male, R.string.male),
    FEMALE(R.string.concise_female, R.string.female);
}
