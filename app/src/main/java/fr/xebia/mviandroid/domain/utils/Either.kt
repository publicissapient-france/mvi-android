package fr.xebia.mviandroid.domain.utils

sealed class Either<out S : Any, out F : Exception> {

    data class Success<S : Any>(val result: S) : Either<S, Nothing>()
    data class Failure<F : Exception>(val exception: F) : Either<Nothing, F>()

    suspend fun either(onSuccess: suspend ((S) -> Unit), onFailure: suspend ((F) -> Unit)) {
        when (this) {
            is Success<S> -> onSuccess(result)
            is Failure<F> -> onFailure(exception)
        }
    }
}