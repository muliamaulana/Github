package com.muliamaulana.github.data.source.remote.network

/**
 * Created by muliamaulana on 14/02/25.
 */

sealed class ApiResponse<out R> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val errorMessage: String) : ApiResponse<Nothing>()
    object Empty : ApiResponse<Nothing>()
}