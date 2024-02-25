package com.sn.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(
        val exception: Throwable? = null,
        val errorMessage: String? = "",
        val errorStatus: Boolean? = null,
        val errorResult: String? = ""
    ) : Result<Nothing>

    data object Loading : Result<Nothing>
}

fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this
        .map<T, Result<T>> {
            Result.Success(it)
        }
        .onStart { emit(Result.Loading) }
        .catch { emit(Result.Error(exception = it)) }
}
