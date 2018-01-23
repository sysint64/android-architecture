package ru.kabylin.androidarchexample.client.api

interface ApiValidationError : ApiError {
    /**
     * Ключи - имя поля, значение - ошика
     */
    val errors: Map<String, String>

    fun hasErrorForField(field: String): Boolean = errors.containsKey(field)

    fun errorMessageForField(field: String): String? = errors[field]
}
