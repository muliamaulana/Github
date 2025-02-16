package com.muliamaulana.github.data.source.remote.network

import com.muliamaulana.github.utils.ExceptionHandler
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by muliamaulana on 16/02/25.
 */
@Singleton
class SafeApiCall @Inject constructor(
    private val exceptionHandler: ExceptionHandler
) {

    suspend fun <T> execute(apiCall: suspend () -> Response<T>): ApiResponse<T> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                response.body()?.let {
                    return ApiResponse.Success(it)
                }
                return ApiResponse.Empty
            }

            // Parse the error response
            val error = parseErrorResponse(response.errorBody())
            ApiResponse.Error("${error.message} (Status: ${error.status})\nMore info: ${error.documentation_url}")

        } catch (e: Exception) {
            ApiResponse.Error(exceptionHandler.handler(e))
        }
    }
}
