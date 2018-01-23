package ru.kabylin.androidarchexample.client.api.impl

enum class ApiErrorCode(val code: Int) {
    PARSE_ERROR(code = 1),
    AUTHENTICATION_FAILED(code = 2),
    NOT_AUTHENTICATED(code = 3),
    PERMISSION_DENIED(code = 4),
    NOT_FOUND(code = 5),
    METHOD_NOT_ALLOWED(code = 6),
    NOT_ACCEPTABLE(code = 7),
    UNSUPPORTED_MEDIA_TYPE(code = 8),
    THROTTLED(code = 9),
    VALIDATION_ERROR(code = 10),

    // Разные коды
    EXTERNAL_API_ERROR(code = 11),
    CONFLICT(code = 12),
    UNKNOWN_ERROR(code = 999);

    companion object {
        private val map = ApiErrorCode.values().associateBy(ApiErrorCode::code)
        fun fromInt(type: Int) = map[type] ?: UNKNOWN_ERROR
    }
}
