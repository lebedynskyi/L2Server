package lineage.vetal.server.game.game

import lineage.vetal.server.game.game.ValidationResult.Error
import lineage.vetal.server.game.game.ValidationResult.Success

abstract class Validation

interface ValidationError

sealed interface ValidationResult<out T, out E : ValidationError> {
    data class Success<out T, out E : ValidationError>(val data: T) : ValidationResult<T, E>
    data class Error<out T, out E : ValidationError>(val error: E) : ValidationResult<T, E>
}

inline fun <T, E : ValidationError> ValidationResult<T, E>.onSuccess(block: (T) -> Unit): ValidationResult<T, E> {
    if (this is Success) block.invoke(data)
    return this
}

fun <T, E : ValidationError> ValidationResult<T, E>.onError(block: (E) -> Unit): ValidationResult<T, E> {
    if (this is Error) block.invoke(error)
    return this
}