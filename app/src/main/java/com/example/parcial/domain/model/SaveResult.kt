package com.example.parcial.domain.model
import com.example.parcial.domain.validation.ValidationError
sealed interface SaveResult<out T> {
    data class Success<T>(val value: T) : SaveResult<T>
    data class Invalid(val errors: Set<ValidationError>) : SaveResult<Nothing>
    data class Failure(val message: String?) : SaveResult<Nothing>
}
