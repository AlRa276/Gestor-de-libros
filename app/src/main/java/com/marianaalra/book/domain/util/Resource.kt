package com.marianaalra.book.domain.util

/**
 * Sealed class que encapsula estados de operaciones remotas/asincrónicas.
 * Permite manejar Success, Error y Loading de manera type-safe.
 */
sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val exception: Exception) : Resource<Nothing>()
    object Loading : Resource<Nothing>()

    /**
     * Ejecuta un bloque de código si el resultado es Success.
     */
    inline fun <R> map(block: (T) -> R): Resource<R> = when (this) {
        is Success -> Success(block(data))
        is Error -> Error(exception)
        is Loading -> Loading
    }

    /**
     * Retorna el dato si es Success, null en caso contrario.
     */
    fun getOrNull(): T? = (this as? Success)?.data

    /**
     * Retorna true si es Success.
     */
    fun isSuccess(): Boolean = this is Success

    /**
     * Retorna true si es Error.
     */
    fun isError(): Boolean = this is Error

    /**
     * Retorna true si es Loading.
     */
    fun isLoading(): Boolean = this is Loading
}