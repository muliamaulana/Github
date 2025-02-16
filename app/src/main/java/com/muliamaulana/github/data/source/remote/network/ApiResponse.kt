package com.muliamaulana.github.data.source.remote.network

import com.google.gson.Gson
import okhttp3.ResponseBody

/**
 * Created by muliamaulana on 14/02/25.
 */

sealed class ApiResponse<out R> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val errorMessage: String) : ApiResponse<Nothing>()
    object Empty : ApiResponse<Nothing>()
}

data class ApiError(
    val message: String,
    val documentation_url: String?,
    val status: Int
)

fun parseErrorResponse(errorBody: ResponseBody?): ApiError {
    return try {
        val gson = Gson()
        errorBody?.string()?.let {
            gson.fromJson(it, ApiError::class.java)
        } ?: ApiError("Unknown error", null, 0)
    } catch (e: Exception) {
        ApiError("Error parsing response", null, 0)
    }
}