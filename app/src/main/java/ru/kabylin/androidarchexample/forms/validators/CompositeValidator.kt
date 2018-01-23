package ru.kabylin.androidarchexample.forms.validators

class CompositeValidator(
    private val validators: List<Validator>
): Validator(null) {
    override fun isValid(value: Any?): Boolean {
        validators.forEach {
            if (!it.isValid(value))
                return false
        }

        return true
    }

    override fun getErrorText(value: Any?): String {
        validators.forEach {
            if (!it.isValid(value))
                return it.getErrorText(value)
        }

        return "Unspecified error"
    }
}
