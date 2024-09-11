package lineage.vetal.server.game.game.validation

interface Error

class Result<out T, out E : Error> private constructor(
    val data: T? = null,
    val error: E? = null
) {
    companion object {
        fun <T, E : Error> success(value: T): Result<T, E> {
            return Result(data = value)
        }

        fun <T, E : Error> error(error: E): Result<T, E> {
            return Result(error = error)
        }
    }
}

inline fun <T, E : Error> Result<T, E>.onSuccess(block: (T) -> Unit): Result<T, E> {
    if (data != null) block.invoke(data)
    return this
}

inline fun <T, E : Error> Result<T, E>.onError(block: (E) -> Unit): Result<T, E> {
    if (error != null) block.invoke(error)
    return this
}

