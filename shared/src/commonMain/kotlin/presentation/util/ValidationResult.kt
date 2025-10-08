package presentation.util

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String = ""
)
